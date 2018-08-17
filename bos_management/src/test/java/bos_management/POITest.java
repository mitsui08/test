package bos_management;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class POITest {

	public static String INputFile 
		= "E:\\BOS2.00\\bosv2.0_chapter03_批量数据导入导出、Apache POI\\资料\\资料\\新BOS项目资料_chapter03_04_区域测试数据\\区域导入测试数据.xls";
	public static String OUTPUT
	= "E:\\BOS2.00\\test.xls";
	
	//演示创建poi文档
	@Test
	public void test1() throws IOException {
		// 创建
		HSSFWorkbook workbook = new HSSFWorkbook();
		//
		HSSFSheet sheet = workbook.createSheet();
		
		//行
		HSSFRow row = sheet.createRow(0);
		
		FileOutputStream fos = new FileOutputStream(OUTPUT);
		
		workbook.write(fos);
		
	}
	
	
	//利用poi创建
	@Test
	public void test2() throws IOException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		sheet.createRow(0);
		
		//第一种是老版本的后缀
		FileOutputStream fos = new FileOutputStream("workbook.xls");
		wb.write(fos);
		fos.close();
		wb.close();
		//第二种是新版本的后缀
		Workbook xwb = new XSSFWorkbook();
		
		FileOutputStream fos2 = new FileOutputStream("workbook.xlsx");
		xwb.createSheet("这是sheet页的标题");
		xwb.createSheet("sheet2");
		xwb.createSheet("mitsui");
		xwb.write(fos2);
		fos.close();
		xwb.close();
	}
	
	//创建单元格
	@Test
	public void test3() {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFCreationHelper creationHelper = wb.getCreationHelper();
		
	}
}
