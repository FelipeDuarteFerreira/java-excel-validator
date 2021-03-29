package com.validator;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.validator.result.ValidationResult;
import com.validator.usermodel.SalaryValidator;


public class TestRunner {

	private static final String NOME_ARQUIVO_EXCEL = "funcionarios.xlsx";

	public static void main(String[] args) throws InvalidFormatException, IOException{
		
		File excelFile = getWorkingDirFile(NOME_ARQUIVO_EXCEL);
		
		try(XSSFWorkbook excel = new XSSFWorkbook(excelFile)) {
			
			XSSFExcelValidator excelValidator = new XSSFExcelValidator(excel);
			
			excelValidator.setLogLevel(Level.INFO);
			excelValidator.setActiveSheet("Plan1", 0);
			
			SalaryValidator salarioValidator = new SalaryValidator();
			salarioValidator.setUserMessage("Salary must be greater than $ 40.000");
			
			excelValidator.addValidator("Salary", salarioValidator);
			excelValidator.validate();
			
			ValidationResult results = excelValidator.getResult();
			
			if(results.hasErrors()) {
				results.getResultErrors().forEach(System.out::println);
			}
			
		}
		
	}
	
	
	public static File getWorkingDirFile(String filename) {
		return new File(System.getProperty("user.dir") + "/" + filename);
	}

}
