package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	public List<Role> findByUser(User user) {
		if("admin".equals(user.getUsername())) {
			return roleRepository.findAll();
		}else {
			return roleRepository.findByUser(user.getId());
		}
		
	}

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public void save(Role role, String[] permissionIds, String menuIds) {
		
		if(permissionIds != null) {
			for (String string : permissionIds) {
				//根据id查permiss,然后add
				Permission permission = permissionRepository.findOne(Integer.parseInt(string));
				role.getPermissions().add(permission);
			}
		}
		
		if(menuIds !=null) {
			String[] ids = menuIds.split(",");
			for (String string : ids) {
				Menu findOne = menuRepository.findOne(Integer.parseInt(string));
				role.getMenus().add(findOne);
			}
		}
		roleRepository.save(role);
	}

}
