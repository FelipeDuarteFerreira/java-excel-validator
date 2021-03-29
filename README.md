# java-excel-validator
A simple Java API for apache-poi excel sheet validation ( xssf/xlsx and hssf/xls )

## Backlog

- [x] Introduce log4j for simple logging
- [x] Create an alternative branch for POI 3.15 version ( Java < 8 )
- [x] XSSF Java instance supporting ( xlsx excel files )
- [x] Automatically formula resolving on validation module and its helper classes 
- [ ] HSSF Java instance supporting ( xls excel files )
- [ ] Complete log4j implementation for full API logging
- [ ] Complete Java 8+ support

#### Number/Numeric Validator creation code example

Java excel validator makes things simply, just extends our creator class and the job is done !

```java
import com.validator.validation.xssf.XSSFNumberCellValidator;

public class SalaryValidator extends XSSFNumberCellValidator{

	@Override
	public boolean validate(Double value) {
		return value > 40000;
	}

}
```

#### String/Text Validator creation code example

Another example for text validation purpose

```java
import com.validator.validation.xssf.XSSFStringCellValidator;

public class NameValidator extends XSSFStringCellValidator{

	@Override
	public boolean validate(String value) {
		return "John Doe".contentEquals(value);
	}

}
```



### Code example
```java
	try(XSSFWorkbook excel = new XSSFWorkbook(excelFile)) {
	
		XSSFExcelValidator excelValidator = new XSSFExcelValidator(excel);
		
		excelValidator.setLogLevel(Level.INFO);
		excelValidator.setActiveSheet("Plan1");
		
		SalaryValidator salaryValidator = new SalaryValidator();
		salaryValidator.setUserMessage("Salary must be greater than $ 40.000");
		
		excelValidator.addValidator("Salary", salaryValidator);
		excelValidator.validate();
		
		ValidationResult results = excelValidator.getResult();
		
		if(results.hasErrors()) {
			results.getResultErrors().forEach(System.out::println);
		}
	
	}
```