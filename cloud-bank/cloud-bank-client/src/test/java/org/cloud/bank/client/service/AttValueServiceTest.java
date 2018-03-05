package org.cloud.bank.client.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cloud.bank.client.BaseTest;
import org.cloud.bank.client.model.AttName;
import org.cloud.bank.client.model.AttValue;
import org.cloud.bank.client.repository.AttNameRepository;
import org.cloud.bank.client.repository.AttValueRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AttValueServiceTest extends BaseTest{

	@Autowired
	private AttNameRepository attNameRepository;
	@Autowired
	private AttValueRepository attValueRepository;
	
	//@Test
	public void splitValue(){
		List<AttValue> attValues=attValueRepository.findAll();
		for(AttValue attValue:attValues){
			attValue.setDepict(attValue.getValue());
			String[] values=attValue.getValue().split("");
			int value=0;
			String v="";
			try {
				value=Integer.valueOf(values[0]);
				v=values[0];
			} catch (Exception e) {
				
			}
			try {
				value=Integer.valueOf(values[0]+values[1]);
				v=values[0]+values[1];
			} catch (Exception e) {
			}
			try {
				value=Integer.valueOf(values[0]+values[1]+values[2]);
				v=values[0]+values[1]+values[2];
			} catch (Exception e) {
			}
			try {
				value=Integer.valueOf(values[0]+values[1]+values[2]+values[3]);
				v=values[0]+values[1]+values[2]+values[3];
			} catch (Exception e) {
			}
			try {
				value=Integer.valueOf(values[0]+values[1]+values[2]+values[3]+values[4]);
				v=values[0]+values[1]+values[2]+values[3]+values[4];
			} catch (Exception e) {
			}
			try {
				value=Integer.valueOf(values[0]+values[1]+values[2]+values[3]+values[4]+values[5]);
				v=values[0]+values[1]+values[2]+values[3]+values[4]+values[5];
			} catch (Exception e) {
			}
			try {
				value=Integer.valueOf(values[0]+values[1]+values[2]+values[3]+values[4]+values[5]+values[6]);
				v=values[0]+values[1]+values[2]+values[3]+values[4]+values[5]+values[6];
			} catch (Exception e) {
			}
			if(StringUtils.isEmpty(v)){
				v="0";
			}
			attValue.setValue(v);
			
		}
		attValueRepository.save(attValues);
	}
	/**
	 * 受理网点号导入与上传网点号与领卡网点号
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
		List<AttName> attnames=attNameRepository.findByNameAndIsdel("AGNBRNO",0);
		attnames.forEach(attname->{
			for(int i=1; i<sheet.getLastRowNum(); i++) {  
				Row row = sheet.getRow(i);
				AttValue attvalue=new AttValue();
				attvalue.setNamid(attname.getNamid());
				attvalue.setDepict(row.getCell(3).getStringCellValue());
				attvalue.setValue(row.getCell(1).getStringCellValue());
				attvalue.setIsdel(0);
				attvalue.setTime(System.currentTimeMillis());
				attValueRepository.save(attvalue);
			}
		});
		
	}
	/**
	 * 国籍导入
	 */
	//@Test
	public void PRIMNAT(){
		File file=new File("C:\\Users\\admin\\Desktop\\contry.xlsx");
		XSSFWorkbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<AttName> attnames=attNameRepository.findByNameAndIsdel("PRIMNAT",0);
		attnames.forEach(attname->{
			for(int i=0; i<sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				AttValue attvalue=new AttValue();
				attvalue.setNamid(attname.getNamid());
				attvalue.setDepict(row.getCell(1).getStringCellValue());
				attvalue.setValue(String.valueOf((int) row.getCell(0).getNumericCellValue()));
				attvalue.setIsdel(0);
				attvalue.setTime(System.currentTimeMillis());
				attValueRepository.save(attvalue);
			}
		});
		
	}
	
	/**
	 * 汽车品牌
	 */
	//@Test
	public void CARBRAND(){
		File file=new File("C:\\Users\\admin\\Desktop\\汽车品牌与款式规格.xlsx");
		XSSFWorkbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<AttName> attnames=attNameRepository.findByNameAndIsdel("CARBRAND",0);
		attnames.forEach(attname->{
			for(int i=0; i<sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				AttValue attvalue=new AttValue();
				attvalue.setNamid(attname.getNamid());
				attvalue.setDepict(row.getCell(0).getStringCellValue());
				attvalue.setValue(row.getCell(0).getStringCellValue());
				attvalue.setIsdel(0);
				attvalue.setTime(System.currentTimeMillis());
				attValueRepository.save(attvalue);
			}
		});
		
	}
	
	/**
	 * 汽车品牌-款式规格
	 */
	//@Test
	public void MODEL(){
		File file=new File("C:\\Users\\admin\\Desktop\\汽车品牌与款式规格.xlsx");
		XSSFWorkbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<AttName> attnames=attNameRepository.findByNameAndIsdel("MODEL",0);
		attnames.forEach(attname->{
			for(int i=0; i<sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				AttValue attvalue=new AttValue();
				attvalue.setNamid(attname.getNamid());
				attvalue.setDepict(row.getCell(1).getStringCellValue());
				attvalue.setFkvalue(row.getCell(0).getStringCellValue());
				attvalue.setValue(row.getCell(1).getStringCellValue());
				attvalue.setIsdel(0);
				attvalue.setTime(System.currentTimeMillis());
				attValueRepository.save(attvalue);
			}
		});
		
	}
	/**
	 * 关联客户信息条数
	 */
	//@Test
	public void RELCUSTNUM(){
		List<AttName> attnames=attNameRepository.findByNameAndIsdel("RELCUSTNUM",0);
		for(int i=0;i<10;i++){
			AttValue attvalue=new AttValue();
			attvalue.setNamid(attnames.get(0).getNamid());
			attvalue.setDepict(String.valueOf(i));
			attvalue.setValue(String.valueOf(i));
			attvalue.setIsdel(0);
			attvalue.setTime(System.currentTimeMillis());
			attValueRepository.save(attvalue);
		}
	}
}
