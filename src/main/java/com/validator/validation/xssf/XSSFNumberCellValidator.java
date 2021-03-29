package com.validator.validation.xssf;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.validator.validation.ICellValidator;

public abstract class XSSFNumberCellValidator implements ICellValidator{

	private FormulaEvaluator _formulaEvaluator;
	private String userMessage;
	
	public XSSFNumberCellValidator() {
		setUserMessage(this.getClass().getSimpleName() + " constraint not satisfied");
	}
	
	@Override
	public boolean _validate(Cell cell) {
		
		assert _formulaEvaluator != null;
		
		CellValue cellValue = _formulaEvaluator.evaluate(cell);
		Double value = cellValue.getNumberValue();
		boolean result = validate(value);
		
		return result;
	}
	
	@Override
	public void setFormulaEvaluator(FormulaEvaluator formulaEvaluator) {
		this._formulaEvaluator = formulaEvaluator;
	}
	
	public abstract boolean validate(Double value);
	
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
	@Override
	public String getUserMessage() {
		return this.userMessage;
	}
	
}
