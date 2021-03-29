package com.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.validator.result.ValidationResult;
import com.validator.validation.ICellValidator;

public class XSSFExcelValidator {

	private TreeMap<Integer, String> _header;
	private Map<String, List<ICellValidator>> _mappedValidations;
	private Integer _headerRow;
	private XSSFWorkbook workbook;
	private XSSFSheet _activeSheet;
	private ValidationResult result;
	
	private final Logger log = LogManager.getLogger();
	
	public XSSFExcelValidator(XSSFWorkbook workbook) {
		this.workbook = workbook;
		this._headerRow = -1;
		setLogLevel(Level.INFO);
		result = new ValidationResult();
	}
	
	public XSSFExcelValidator(XSSFWorkbook workbook, int sheetIndex) {
		this(workbook);
		setActiveSheet(sheetIndex);
	}
	
	public XSSFExcelValidator(XSSFWorkbook workbook, String sheetName) {
		this(workbook);
		setActiveSheet(sheetName);
	}
	
	public void setActiveSheet(int index) {
		this._activeSheet = this.workbook.getSheetAt(index);
	}
	
	public void setActiveSheet(int sheetIndex, int headerIndex) {
		this._activeSheet = this.workbook.getSheetAt(sheetIndex);
		this._headerRow = headerIndex;
		this._initializeHeaders(headerIndex);
	}
	
	public void setActiveSheet(String name, int headerIndex) {
		this._activeSheet = this.workbook.getSheet(name);
		this._headerRow = headerIndex;
		this._initializeHeaders(headerIndex);
	}
	
	public void setActiveSheet(String name) {
		this._activeSheet = this.workbook.getSheet(name);
	}
	
	private void _initializeHeaders(int headerRowIndex) {
		this._header = new TreeMap<>();
		this._headerRow = headerRowIndex;
		this._mappedValidations = new HashedMap<String, List<ICellValidator>>();
		
		XSSFRow headerRow = this._activeSheet.getRow(headerRowIndex);
		Iterator<Cell> iterator = headerRow.iterator();
		StringBuilder columns = null;
		
		if(log.isDebugEnabled()) {
			columns = new StringBuilder();
		}
		
		while(iterator.hasNext()) {
			Cell headerCell = iterator.next();
			if(headerCell == null || headerCell.getCellType() != CellType.STRING) continue; // skipping if not an header column
			String columnName = headerCell.getStringCellValue();
			this._header.put(headerCell.getColumnIndex(), columnName);
			this._mappedValidations.put(columnName, new ArrayList<ICellValidator>());
			
			if(log.isDebugEnabled())
				columns.append(" " + "'" + columnName + "'");
		}
		
		log.printf(Level.DEBUG, "Columns found : [" + columns + " ]");
	}
	
	public void addValidator(int columnIndex, ICellValidator validator) {
		String columnName = this._header.get(columnIndex);
		if(columnName == null) return;
		List<ICellValidator> priorValidators = _mappedValidations.get(columnName);
		
		if(priorValidators == null)
			return;
		
		priorValidators.add(validator);
		this._mappedValidations.put(columnName, priorValidators);
	}
	
	public void addValidator(String columnName, ICellValidator validator) {
		List<ICellValidator> priorValidators = _mappedValidations.get(columnName);
		
		if(priorValidators == null)
			return;
		
		validator.setFormulaEvaluator(workbook.getCreationHelper().createFormulaEvaluator());
		priorValidators.add(validator);
		this._mappedValidations.put(columnName, priorValidators);
	}
	
	public void setLogLevel(Level level) {
		Configurator.setLevel(log.getName(), level);
	}
	
	public ValidationResult getResult() {
		return result;
	}
	
	public void validate() {
		Set<Integer> indexes = _header.keySet();
		log.info("Validating sheet...");
		for(int i = _headerRow+1; i < _activeSheet.getPhysicalNumberOfRows(); i++) {
			Row row = _activeSheet.getRow(i);
			for(Integer index : indexes) {
				Cell cell = row.getCell(index);
				String columnName = _header.get(index);
				List<ICellValidator> validatorsToApply = _mappedValidations.get(columnName);
				if(! validatorsToApply.isEmpty())
					for(ICellValidator validator : validatorsToApply) {
						boolean isCellValid = validator._validate(cell);
						if(! isCellValid) {
							log.debug("Column '" + columnName + "' at row " + cell.getRowIndex() + " failed validation : " + validator.getUserMessage());
							result.addError(cell.getRowIndex(), cell.getColumnIndex(), 
									columnName, validator.getUserMessage());
						}
					}	
			}
		}
		log.info("Validation completed");
	}
	
}
