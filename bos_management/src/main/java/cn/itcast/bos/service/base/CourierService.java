package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	public void save(Courier courier);
	
	public Page<Courier> findAll(Pageable pageable) ;
	
	public Page<Courier> findAll(Specification<Courier> spec,Pageable pageable);

	public void delBatch(String[] idArray);
	
	public void fastUpdate(Courier courier);

	public List<Courier> findnoassociation();
}
