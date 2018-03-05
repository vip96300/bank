package org.cloud.bank.client.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.cloud.bank.client.BaseTest;
import org.cloud.bank.client.model.Bank;
import org.cloud.bank.client.repository.BankRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class BankServiceTest extends BaseTest {
	
	@Autowired
	private BankRepository bankRepository;
	/**
	 * 支行号
	 */
	//@Test
	public void AGNBRNO(){
		File file=new File("C:\\Users\\admin\\Desktop\\tmp002.xls");
		Workbook workbook=null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);
		for(int i=1; i<sheet.getLastRowNum(); i++) {  
			Row row = sheet.getRow(i);
			Bank bank=new Bank();
			String maxCode=bankRepository.findByCodeMax();
			bank.setCode(this.genCode(maxCode));
			bank.setBcode(row.getCell(2).getStringCellValue());
			bank.setWcode(row.getCell(1).getStringCellValue());
			bank.setName(row.getCell(3).getStringCellValue());
			bank.setIsdel(0);
			bankRepository.save(bank);
		}
	}
	/**
	 * 新编码生成器
	 * @param maxCode
	 * @return
	 */
	private String genCode(String maxCode){
		String code="";
		if(StringUtils.isEmpty(maxCode)){
			code="001";
		}else{
			int icode=Integer.parseInt(maxCode)+1;
			if(icode/1000<1){
				code=""+icode;
			}
			if(icode/100<1){
				code="0"+icode;
			}
			if(icode/10<1){
				code="00"+icode;
			}
		}
		return code;
	}
}
