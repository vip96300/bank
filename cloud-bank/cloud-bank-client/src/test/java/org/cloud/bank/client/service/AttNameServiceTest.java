package org.cloud.bank.client.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cloud.bank.client.BaseTest;
import org.cloud.bank.client.model.AttName;
import org.cloud.bank.client.model.AttValue;
import org.cloud.bank.client.model.Datum;
import org.cloud.bank.client.repository.AttNameRepository;
import org.cloud.bank.client.repository.AttValueRepository;
import org.cloud.bank.client.repository.DatumRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class AttNameServiceTest extends BaseTest{

	@Autowired
	private AttNameRepository attNameRepository;
	@Autowired
	private AttValueRepository attValueRepository;
	
	//@Test
	public void sort(){
		File file=new File("C:\\Users\\admin\\Desktop\\test.xlsx");
		Workbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(4);
		long menid=100;
		Row row = null;  
		int startReadLine = 0;
		for(int i=startReadLine; i<sheet.getLastRowNum(); i++) {  
			row = sheet.getRow(i);
			String name="";
			if(this.isMergedRegion(sheet, row.getRowNum(), 1)){
				name=this.getMergedRegionValue(sheet,  row.getRowNum(), 1);
			}else{
				name=row.getCell(1).getStringCellValue();
			}
			AttName attname=attNameRepository.findByMenidAndName(menid,name.trim());
			if(StringUtils.isEmpty(attname)){
				attname=new AttName();
			}
			AttValue attvalue=null;
			boolean isnull=false;
			for(int c=0;c<row.getLastCellNum();c++) {
				Cell cell=row.getCell(c);
				boolean isMerge = isMergedRegion(sheet, i,cell.getColumnIndex());  
				String value="";
				if(isMerge) {  
					value= getMergedRegionValue(sheet, row.getRowNum(),cell.getColumnIndex());  
				}else {
					row.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
					value=cell.getRichStringCellValue().getString();  
				}
				//如果是关联信息，会名称重复需要加上_0
				value=value.trim();
				if(c==0){//编号
					this.getValue(value);
				}
				if(c==1){//名称
					attname.setName(value);
				}
				if(c==2){//秒速
					attname.setDepict(value);
				}
				if(c==3){//长度
					int begin=value.lastIndexOf("(");
					int end=value.lastIndexOf(")");
					value=value.substring(begin+1, end);
					attname.setLength(Integer.valueOf(value));
				}
				if(c==4){//是否必须
					if(value.equals("是")){
						attname.setIsrequired(1);
					}else{
						attname.setIsrequired(0);
					}
				}
				if(c==5){//范围值
					if(!StringUtils.isEmpty(value)){
						attname.setIntype(1);
						attvalue=new AttValue();
						attvalue.setNamid(attname.getNamid());
						attvalue.setValue(String.valueOf(this.getValue(value)));
						attvalue.setIsdel(0);
						attvalue.setDepict(value);
						attvalue.setTime(System.currentTimeMillis());
						if(StringUtils.isEmpty(attname.getNamid())){
							isnull=true;
						}else{
							attValueRepository.save(attvalue);
						}
					}else{
						attname.setIntype(0);
					}
				}
				if(c==6){//是否译码
					//无操作
				}
				if(c==7){//补位规则
					if(StringUtils.isEmpty(value)){
						value="0";
					}
					attname.setCover(value);
				}
				if(c==8){//备注
					if(StringUtils.isEmpty(value)){
						value="0";
					}
					attname.setRemark(value);
				}
			}
			attname.setIsdel(0);
			attname.setDefvalue("0");
			attname.setIsexport(1);
			attname.setIsget(0);
			attname.setSort(Long.valueOf(i));
			attname.setMenid(menid);
			attname.setTime(System.currentTimeMillis());
			attNameRepository.save(attname);
			if(isnull){
				attvalue.setNamid(attname.getNamid());
				attValueRepository.save(attvalue);
			}
		}  
	}
	private int getValue(String v){
		String[] values=v.split("");
		int value=0;
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
		return value;
	}
	/**   
	* 获取合并单元格的值   
	* @param sheet   
	* @param row   
	* @param column   
	* @return   
	*/    
	public String getMergedRegionValue(Sheet sheet ,int row , int column){    
	    int sheetMergeCount = sheet.getNumMergedRegions();     
	    for(int i = 0 ; i < sheetMergeCount ; i++){    
	        CellRangeAddress ca = sheet.getMergedRegion(i);    
	        int firstColumn = ca.getFirstColumn();    
	        int lastColumn = ca.getLastColumn();    
	        int firstRow = ca.getFirstRow();    
	        int lastRow = ca.getLastRow();      
	        if(row >= firstRow && row <= lastRow){       
	            if(column >= firstColumn && column <= lastColumn){    
	                Row fRow = sheet.getRow(firstRow);    
	                Cell fCell = fRow.getCell(firstColumn);    
	                return fCell.getStringCellValue();    
	            }    
	        }    
	    }    
	        
	    return null ;    
	}    
	/**  
	* 判断指定的单元格是否是合并单元格  
	* @param sheet   
	* @param row 行下标  
	* @param column 列下标  
	* @return  
	*/  
	private boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}  
	/**  
	* 判断合并了行  
	* @param sheet  
	* @param row  
	* @param column  
	* @return  
	*/  
	private boolean isMergedRow(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row == firstRow && row == lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}
	
}
