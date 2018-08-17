package cn.itcast.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;

public class BosRelam extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService; 

	// 授权
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		System.out.println("shiro 授权管理");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		
		Subject subject = SecurityUtils.getSubject();
		
		//从subject中获取当前
		User user = (User) subject.getPrincipal();
		//调用业务层,查询权限
		List<Role> list =roleService.findByUser(user);
		
		for (Role role : list) {
			authorizationInfo.addRole(role.getKeyword());
		}
		
		List<Permission> permissions =permissionService.findByUser(user);
		//
		for (Permission permission : permissions) {
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		return authorizationInfo;
	}

	// 认证
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("shiro认证");
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		if(user == null) {
			return null;
		}else {
			// 用户名存在
			// 当返回用户密码时，securityManager安全管理器，自动比较返回密码和用户输入密码是否一致
			// 如果密码一致 登录成功， 如果密码不一致 报密码错误异常
			return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
		}
	}

}
