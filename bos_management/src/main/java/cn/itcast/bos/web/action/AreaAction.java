package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area>{

	@Autowired
	private AreaService areaService;

	private File file;
	
	
	public void setFile(File file) {
		this.file = file;
	}


	@Action(value = "area_batchImport")
	public String batchImport() throws FileNotFoundException, IOException {
		List<Area> areas = new ArrayList<Area>();
		
		// 编写解析代码逻辑
		// 基于.xls 格式解析 HSSF
		// 1、 加载Excel文件对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		//2.读取第一个sheet
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		//3.读取sheet中每一行
		for (Row row : sheet) {
			//一行数据,对应一个区域对象
			if(row.getRowNum() == 0) {
				//第一行跳过.
				continue;
			}
			
			if(row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}   
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			//基于pinyinforj,生成城市编码和简码
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			
			province = province.substring(0, province.length()-1);
			
			city = city.substring(0, city.length()-1);
			district = district.substring(0, district.length()-1);
			
			//简码
			String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
			
			String shortCode = "";
			for (String string : headByString) {
				shortCode +=string;
			}
			
			area.setShortcode(shortCode);
			
			String citycode = PinYin4jUtils.hanziToPinyin(city,"");
			area.setCitycode(citycode);
			
			areas.add(area);
			
		}
		//调用业务层
		areaService.saveBatch(areas);
		return NONE;
	}
	
	
	@Action(value="area_pageQuery")
	public String pageQuery() throws IOException {
		//构造分页
		Pageable pageable = new PageRequest(page-1, rows);
		
		//构造条件查询对象
		Specification<Area> specification =new Specification<Area>() {

			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return null;
			}
		};
		//调用业务层完成查询
		Page<Area> pageData = areaService.findPageData(specification, pageable);
		
		String pageJson = pageJson(pageData);
		
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().print(pageJson);
		
		return NONE;
	}
	
	
	
	
	
	
	
	
}
