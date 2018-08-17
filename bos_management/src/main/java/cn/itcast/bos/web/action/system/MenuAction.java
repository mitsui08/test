package cn.itcast.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu>{

	@Autowired
	private MenuService menuService;

	@Action(value="menu_save",results = {@Result(name="success",type="redirect",location="pages/system/menu.html")})
	public String save() {
		
		System.out.println(model.getParentMenu());
		menuService.save(model);
		return SUCCESS;
	}
	

	@Action(value="menu_list")
	public String list() throws IOException {
		List<Menu> list =menuService.findAll();
		String jsonString = JSONArray.toJSONString(list);
		resposeToFront(jsonString);
		System.out.println(jsonString);
		return NONE;
	}
	
	
	
	@Action(value = "menu_showMenu")
	public String showmenu() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		return NONE;
	}
}
