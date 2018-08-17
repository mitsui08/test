package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.service.system.MenuService;

@Service
@Transactional
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public List<Menu> findAll() {
		
		return menuRepository.findAll();
	}

	@Override
	public void save(Menu model) {
		//防止用户没有勾选父菜单
		if(model.getParentMenu() !=null && model.getParentMenu().getId() == 0) {
			model.setParentMenu(null);
		}
		//调用dao
		menuRepository.save(model);
	}

}
