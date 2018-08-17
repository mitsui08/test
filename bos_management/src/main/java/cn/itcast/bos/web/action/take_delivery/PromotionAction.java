package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
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
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.utils.UUIDUtils;
import cn.itcast.bos.web.action.common.BaseAction;


@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
@SuppressWarnings("unused")
public class PromotionAction extends BaseAction<Promotion>{

	
	private static final long serialVersionUID = -447229444564681071L;


	@Autowired
	private PromotionService promotionService;
	
	
	private File titleImgFile;
	private String titleImgFileFileName;
	
	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}
	@Action(value = "promotion_save", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		
		//1.先上传文件,
		String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		String randomName = UUIDUtils.getUUID();
		String finalName = randomName+ext;
		//拿到相对路径
		String savePath =ServletActionContext.getRequest().getContextPath()+"/upload/";
		
		FileUtils.copyFile(titleImgFile, new File(realPath+"/" + finalName));
		
		model.setTitleImg(savePath+finalName);
		
		promotionService.save(model);
		
		return SUCCESS;
	}
	
	@Action(value = "promotion_pageQuery")
	public String pageQuery() throws IOException {
		Pageable pageable = new PageRequest(page-1, rows);
		
		//序列化这个类
		Page<Promotion> pageQuer = promotionService.pageQuer(pageable);
		
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageQuer.getTotalElements());
		map.put("rows", pageQuer.getContent());
		
		//TODO 向前台返回时间是数值类型
		//endDate":1526313600000,"id":101,"startDate":1526227200000,
		String jsonString = JSON.toJSONString(map);
		System.out.println(jsonString);
		ServletActionContext.getResponse().getWriter().println(jsonString);
		return NONE;
	}
	
}
