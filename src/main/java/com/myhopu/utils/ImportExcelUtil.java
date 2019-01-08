package com.myhopu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

/**
 * Excel导出工具类
 * @author zero
 *
 */
public class ImportExcelUtil {

	//private static Logger logger  = Logger.getLogger(POIUtil.class);  
    private final static String xls = "xls";  
    private final static String xlsx = "xlsx";  
      
    /** 
     * 读入excel文件，解析后返回 
     * @param file 
     * @throws IOException  
     */  
    public static List<String[]> readExcel(MultipartFile file) throws IOException{  
        //检查文件  
        //checkFile(file);  
        //获得Workbook工作薄对象  
        Workbook workbook = getWorkBook(file);
    	
        //Workbook workbook = test();
        
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回  
        List<String[]> list = new ArrayList<String[]>();  
        if(workbook != null){  
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){  
                //获得当前sheet工作表  
                Sheet sheet = workbook.getSheetAt(sheetNum);  
                if(sheet == null){  
                    continue;  
                }  
                //获得当前sheet的开始行  
                int firstRowNum  = sheet.getFirstRowNum();  
                //获得当前sheet的结束行 
                int lastRowNum = sheet.getLastRowNum();  
                
//                System.out.println("开始行为："+firstRowNum);
//                System.out.println("结束行为："+lastRowNum);
                Row row;
                
                
                //循环所有行  
                for(int rowNum = firstRowNum;rowNum <= lastRowNum;rowNum++){  
                    //获得当前行  
                    row = sheet.getRow(rowNum);  
                    if(row == null){  
                        continue;  
                    }  
                    //获得当前行开始的当前单元格所在列
                    int firstCellNum = row.getFirstCellNum();  
//                    System.out.println("开始列:"+firstCellNum);
                    
                    //获得最后一行有效列的索引
                    int lastCellNum = row.getLastCellNum();  
//                    System.out.println("有效列数的索引是："+lastCellNum);

                    
                    //本行有效单元格的个数
                    int resultCellNum = lastCellNum - firstCellNum;
                    
                    String[] cells = new String[resultCellNum];//这个数组用来存放每一行的值  
//                    System.out.println("数组有："+cells.length+"列");
                    
                    //循环当前行的单元格
                    int cellNum = firstCellNum;//当前行第一个有效值单元格所在列数
                    for( int sarr = 0; sarr < resultCellNum;cellNum++){
                    	
                    	//取当前对应列对应行单元格中的值
                        Cell cell = row.getCell(cellNum);  
                        
                        //数组是从0开始赋值
                        //获取当前行当前列单元格的值
                        cells[sarr] = getCellValue(cell); 
                        sarr++;
                    }  
                    list.add(cells);  
                }  
            }  
            workbook.close();  
        }  
        return list;  
    }  
    public static void checkFile(MultipartFile file) throws IOException{  
        //判断文件是否存在  
        if(null == file){  
           // logger.error("文件不存在！");  
            throw new FileNotFoundException("文件不存在！");  
        }  
        //获得文件名  
        String fileName = file.getOriginalFilename();  
        //判断文件是否是excel文件  
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){  
           // logger.error(fileName + "不是excel文件");  
            throw new IOException(fileName + "不是excel文件");  
        }  
    }  
    public static Workbook getWorkBook(MultipartFile file) {  
        //获得文件名  
        String fileName = file.getOriginalFilename();  
        //创建Workbook工作薄对象，表示整个excel  
        Workbook workbook = null;  
        try {  
            //获取excel文件的io流  
            InputStream is = file.getInputStream();  
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象  
            if(fileName.endsWith(xls)){  
                //2003  
                workbook = new HSSFWorkbook(is);  
            }else if(fileName.endsWith(xlsx)){  
                //2007  
                workbook = new XSSFWorkbook(is);  
            }  
        } catch (IOException e) {  
          //  logger.info(e.getMessage());
        	e.printStackTrace();
        }  
        return workbook;  
    }  
    public static String getCellValue(Cell cell){  
        String cellValue = "";  
        if(cell == null){  
            return cellValue;  
        }  
        //把数字当成String来读，避免出现1读成1.0的情况  
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){  
            cell.setCellType(Cell.CELL_TYPE_STRING);  
        }  
        //判断数据的类型  
        switch (cell.getCellType()){  
            case Cell.CELL_TYPE_NUMERIC: //数字  
                cellValue = String.valueOf(cell.getNumericCellValue());  
                break;  
            case Cell.CELL_TYPE_STRING: //字符串  
                cellValue = String.valueOf(cell.getStringCellValue());  
                break;  
            case Cell.CELL_TYPE_BOOLEAN: //Boolean  
                cellValue = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case Cell.CELL_TYPE_FORMULA: //公式  
                cellValue = String.valueOf(cell.getCellFormula());  
                break;  
            case Cell.CELL_TYPE_BLANK: //空值   
                cellValue = "";  
                break;  
            case Cell.CELL_TYPE_ERROR: //故障  
                cellValue = "非法字符";  
                break;  
            default:  
                cellValue = "未知类型";  
                break;  
        }  
        return cellValue;  
    }  
    
    /**
     * 	测试方法
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
    	 
    	
    	List<String []> s = readExcel(null);;
		for(String [] s2 : s) {
			System.out.println(Arrays.asList(s2));
		}
		
    }
    
    
    public static Workbook test() {
    	  Workbook w = null;
    	  File f = new File("C:\\Users\\小钢炮-ST-PRO\\Desktop\\新建 XLS 工作表.xls");   
          try {
			InputStream in = new FileInputStream(f);
			
			 w = new HSSFWorkbook(in);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return w;   
    }
}
