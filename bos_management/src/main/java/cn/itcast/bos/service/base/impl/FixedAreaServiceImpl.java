package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService{

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	
	@Autowired
	private CourierRepository courierRepository;
	
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	@Override
	public void save(FixedArea fixedArea) {
		
		fixedAreaRepository.save(fixedArea);
	}
	

	public Page<FixedArea> pageQuery(Specification<FixedArea> spec ,Pageable pageable){
		Page findAll = fixedAreaRepository.findAll(spec, pageable);
		return findAll;
	}


	@Override
	public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId) {
		FixedArea fixedarea = fixedAreaRepository.findOne(model.getId());
		Courier c = courierRepository.findOne(courierId);
		TakeTime tt = takeTimeRepository.findOne(takeTimeId);
		c.setTakeTime(tt);
		fixedarea.getCouriers().add(c);
	}
}
