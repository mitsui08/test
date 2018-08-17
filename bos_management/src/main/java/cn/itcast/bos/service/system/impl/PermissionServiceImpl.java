package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService{

	@Autowired
	private PermissionRepository permissionRepository;

	public List<Permission> findByUser(User user) {
		// TODO Auto-generated method stub
		if("admin".equals(user.getUsername())) {
			return permissionRepository.findAll();
		}else {
			return permissionRepository.findByUser(user.getId());
		}
		
	}
	
	public List<Permission> findAll() {
		
		List<Permission> findAll = permissionRepository.findAll();
		return findAll;
	}

	@Override
	public void save(Permission model) {
	
		permissionRepository.save(model);
	}

}
