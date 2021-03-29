package com.validator.usermodel;

import com.validator.validation.xssf.XSSFNumberCellValidator;

public class SalaryValidator extends XSSFNumberCellValidator{

	@Override
	public boolean validate(Double value) {
		return value > 40000;
	}

}
