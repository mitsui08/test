package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class StandardAction extends BaseAction<Standard>{

	

	@Autowired
	private StandardService standardService;

	@Action(value = "standard_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/standard.html") })
	public String save() {
		standardService.save(model);
		return SUCCESS;
	}

	/**
	 * 分页查询列表
	 * @return
	 * @throws IOException
	 */
	@Action(value = "standard_pageQuery" )
	public String pageQuery() throws IOException {

		Pageable pageAble = new PageRequest(page-1, rows);
		Page<Standard> pageData = standardService.findAll(pageAble);
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		JSONObject json = JSONObject.parseObject(JSON.toJSONString(map));
		ServletActionContext.getResponse().getWriter().println(json);
		return NONE;
	}
	
	@Action(value = "standard_findAll" )
	public String findAll() {

		List<Standard> list = standardService.findAll();
		String jsonString = JSON.toJSONString(list);	
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	
}
