package cn.itcast.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierRepository courierRepository;

	public void save(Courier courier) {
		courierRepository.save(courier);
	}

	public void fastUpdate(Courier courier) {
		Courier oldC = courierRepository.findById(courier.getId());
		oldC.setName(courier.getName());
		oldC.setCompany(courier.getCompany());
		oldC.setCourierNum(courier.getCourierNum());

	}

	public Page<Courier> findAll(Pageable pageable) {
		Page<Courier> findAll = courierRepository.findAll(pageable);
		return findAll;
	}

	public Page<Courier> findAll(Specification<Courier> spec, Pageable pageable) {
		Page<Courier> findAll = courierRepository.findAll(spec, pageable);
		return findAll;
	}

	public void delBatch(String[] idArray) {

		for (String id : idArray) {
			Courier courier = courierRepository.findById(Integer.parseInt(id));
			courier.setDeltag('1');
		}
	}

	@Override
	public List<Courier> findnoassociation() {
		// TODO
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return p;
			}

		};

		return courierRepository.findAll(specification);
	}
}
