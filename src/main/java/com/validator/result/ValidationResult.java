package com.validator.result;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

	private List<ValidationError> errors;
	
	public ValidationResult() {
		this.errors = new ArrayList<>();
	}
	
	public List<ValidationError> getResultErrors(){
		return errors;
	}
	
	public boolean hasErrors() {
		return errors.size() > 0;
	}
	
	public void addError(Integer rowIndex, Integer columnIndex,
			String columnName, String userMessage) {
		
		ValidationError error = new ValidationError(columnName, rowIndex, columnIndex, userMessage);
		
		errors.add(error);
	}
	
	public static class ValidationError {
		
		private String columnName;
		private Integer rowIndex;
		private Integer columnIndex;
		private String userMessage;
		
		public ValidationError(String columnName, Integer rowIndex, 
				Integer columnIndex, String userMessage) {
			super();
			this.columnName = columnName;
			this.rowIndex = rowIndex;
			this.columnIndex = columnIndex;
			this.userMessage = userMessage;
		}
		
		public String getColumnName() {
			return columnName;
		}

		public Integer getRowIndex() {
			return rowIndex;
		}

		public Integer getColumnIndex() {
			return columnIndex;
		}

		public String getUserMessage() {
			return userMessage;
		}
		
		@Override
		public String toString() {
			return "Column '" + columnName + "'" + " at row " + rowIndex + " produced an error : " + userMessage; 
		}
		 
	}
	
}
