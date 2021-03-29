package com.validator.validation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public interface ICellValidator {

	boolean _validate(Cell cell);
	
	void setFormulaEvaluator(FormulaEvaluator formulaEvaluator);
	
	String getUserMessage();
	
}
