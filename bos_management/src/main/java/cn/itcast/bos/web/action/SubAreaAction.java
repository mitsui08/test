package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea> {

	private File file;

	public void setFile(File file) {
		this.file = file;
	}

	@Autowired
	private SubAreaService subAreaService;

	@Action(value = "subArea_batchImport")
	public String batchImport() throws FileNotFoundException, IOException {
		ArrayList<SubArea> list = new ArrayList<SubArea>();

		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 2.读取第一个sheet
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		// 3.读取sheet中每一行
		for (Row row : sheet) {

			//一行数据,对应一个区域对象
			if(row.getRowNum() == 0) {
				//第一行跳过.
				continue;
			}
			
			if(row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}   
			
			SubArea subArea = new SubArea();
			subArea.setId(row.getCell(0).getStringCellValue());
			subArea.setAssistKeyWords(row.getCell(8).getStringCellValue());
			subArea.setKeyWords(row.getCell(4).getStringCellValue());
			subArea.setStartNum(row.getCell(5).getStringCellValue());
			//根据表格中的省市区,查出area在赋值
			//subArea.setArea();
		}

		return NONE;
	}

}
