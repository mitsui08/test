package cn.itcast.bos.web.action.system;

import java.io.IOException;
import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

	@Autowired
	private UserService userService;

	@Action(value = "user_login", results = { @Result(name = "success", type = "redirect", location = "index.html"),
			@Result(name = "login", type = "redirect", location = "login.html") })
	public String login() {

		Map<String, Object> map = new HashMap<>();
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());

		try {
			subject.login(token);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return LOGIN;
		}
	}

	@Action(value = "user_logout", results = { @Result(name = "success", type = "redirect", location = "login.html") })
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return SUCCESS;
	}

	@Action(value = "user_list")
	public String list() throws IOException {

		List<User> list = userService.findAll();
		String jsonString = JSONArray.toJSONString(list);
		resposeToFront(jsonString);
		return NONE;
	}

	private String[] roles;

	@Action(value="user_save")
	public String save() {
		userService.save(model, roles);
		return NONE;
	}

}
