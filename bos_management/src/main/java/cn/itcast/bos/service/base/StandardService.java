package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Standard;

public interface StandardService {

	public void save(Standard standard);
	
	Page<Standard> findAll(Pageable pageable);

	public List<Standard> findAll();
}
