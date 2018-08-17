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

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission> {

	@Autowired
	private PermissionService permissionService;
	
	
	@Action(value ="permission_list")
	public String list() throws IOException {
		List<Permission> findAll = permissionService.findAll();
		String jsonString = JSONArray.toJSONString(findAll);
		resposeToFront(jsonString);
		return NONE;
	}
	
	//权限添加
	@Action(value="permission_save",results = {@Result(name="success",type="redirect",location="pages/system/permission.html")})
	public String save() {
		permissionService.save(model);
		return NONE;
	}
}
