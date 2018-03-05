package org.cloud.bank.client.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cloud.bank.client.BaseTest;
import org.cloud.bank.client.model.AttName;
import org.cloud.bank.client.model.Datum;
import org.cloud.bank.client.repository.DatumRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DatumServiceTest extends BaseTest{

	@Autowired
	private DatumRepository datumRepository;
	/**
	 * 修改补位规则
	 */
	//@Test
	public void updateByCover(){
		File file=new File("C:\\Users\\admin\\Desktop\\test.xlsx");
		Workbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
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
			System.err.println(name);
			List<Datum> datums=datumRepository.findByMenidAndName(menid, name.trim()+"_0");
			System.err.println(datums.size());
			Cell cell=row.getCell(3);
			boolean isMerge = isMergedRegion(sheet, i,cell.getColumnIndex());  
			String value="";
			if(isMerge) {  
				value= getMergedRegionValue(sheet, row.getRowNum(),cell.getColumnIndex());  
			}else {
				row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
				value=cell.getRichStringCellValue().getString();  
			}
			for(Datum datum:datums){
				if(value.contains("X")){//字符串，右补空格
					datum.setCover(AttName.R);
				}else{
					//数字，左补0
					datum.setCover(AttName.L);
				}
			}
			datumRepository.save(datums);

		}  
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
