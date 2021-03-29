package com.validator.validation.xssf;

import org.apache.poi.ss.usermodel.Cell;

import com.validator.validation.ICellValidator;

public abstract class XSSFStringCellValidator implements ICellValidator{

	private String userMessage;
	
	@Override
	public boolean _validate(Cell cell) {

		String value = cell.getStringCellValue();
		boolean result = validate(value);
		
		return result;
	}
	
	
	public abstract boolean validate(String value);
	
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
	@Override
	public String getUserMessage() {
		return this.userMessage;
	}
	
}
