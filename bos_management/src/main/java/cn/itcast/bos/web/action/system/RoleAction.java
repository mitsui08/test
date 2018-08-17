package cn.itcast.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {


	@Autowired
	private RoleService roleService;
	
	@Action(value="role_list")
	public String list() throws IOException {
		List<Role> list = roleService.findAll();
		String jsonString = JSONArray.toJSONString(list);
		resposeToFront(jsonString);
		return NONE;
	}

	private String[] permissionIds;
	private String menuIds;

	@Action(value="role_save")
	public String save() {
		roleService.save(model,permissionIds,menuIds);
		return NONE;
	}
	
	
	
	
	
	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
}
