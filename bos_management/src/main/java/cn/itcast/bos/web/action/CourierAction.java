package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;
import cn.itcast.crm.domain.Customer;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

	private Courier courier = new Courier();

	public Courier getModel() {
		return courier;
	}

	private Integer page;
	private Integer rows;
	
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
	
	@Autowired
	private CourierService courierService;
	
	@Action(value = "courier_save", results = { @Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}
	
	/**
	 * 根据条件查询快递员
	 * 3个条件,1.courierNum员工编号,2.company,员工公司,3.员工类型
	 * @return
	 * @throws IOException
	 */
	@Action(value="courier_pageQuery")
	public String pageQuery() throws IOException {
		
		Pageable pageable = new PageRequest(page-1, rows);
		
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				
				List<Predicate> list = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(p1);
					
				}
				
				if(StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+courier.getCompany()+"%");
					list.add(p2);
				}
				 
				if(StringUtils.isNotBlank(courier.getType())) {
					Predicate p3 = cb.equal(root.get("type"), courier.getType());
					list.add(p3);
				}
				//TODO
			/*	Join<Standard, Courier> standardJoin = root.join("standard",JoinType.INNER);
				if(courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
					Predicate p4 = cb.like(standardJoin.get("name").as(String.class),"%"+courier.getStandard().getName()+"%");
					list.add(p4);
				}
				*/
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Page<Courier> pageData = courierService.findAll(specification, pageable);
		System.out.println(pageData);
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		JSONObject json = JSONObject.parseObject(JSON.toJSONString(map));
		System.out.println(json);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().println(json);
		return NONE;
	}
	
	/**
	 * 批量作废快递员
	 * @return
	 */
	@Action(value="courier_delBatch",results = { @Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String delBatch() {
	
		System.out.println(ids);
		if(StringUtils.isBlank(ids)) {
			//如果没有穿过来id,则提示错误信息, 不进行删除操作
			return NONE;
		}
		
		String[] idArray = ids.split(",");
		
		courierService.delBatch(idArray);
		
		return SUCCESS;
	}
	
	@Action(value="fastUpdate")
	public String fastUpdate() {
		courierService.fastUpdate(courier);
		System.out.println("111111111111");
		return NONE;
	}
	
	//查找没有关联的快递员
	@Action(value="courier_findnoassociation")
	public String findnoassociation() throws IOException {
		
		List<Courier> list = courierService.findnoassociation();
		String jsonString = JSONArray.toJSONString(list);
		
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(jsonString);
		return NONE;
	}
}
