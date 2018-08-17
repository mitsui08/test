package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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
import com.alibaba.fastjson.JSONObject;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {

	@Autowired
	private FixedAreaService fixedAreaService;

	@Action(value = "fixedArea_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String save() {

		fixedAreaService.save(model);

		return "success";
	}

	@Action(value = "fixedArea_pageQuery")
	public String area_pageQuery() {

		Pageable pageable = new PageRequest(page - 1, rows);
		Specification<FixedArea> spec = new Specification<FixedArea>() {

			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return null;
			}
		};

		Page<FixedArea> page = fixedAreaService.pageQuery(spec, pageable);

		// 分页的json
		String pageJson = pageJson(page);

		ServletActionContext.getResponse().setCharacterEncoding("utf-8");

		try {
			ServletActionContext.getResponse().getWriter().print(pageJson);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return NONE;
	}

	// 查询所有未关联定区的客户
	@Action("fixedArea_findNoAssociationCustomers")
	public String findNoAssociationCustomers() throws IOException {

		@SuppressWarnings("unchecked")
		List<Customer> list = (List<Customer>) WebClient
				.create("http://localhost:9002/crm_management/services/customerService/noassociationcustomers")
				.accept(MediaType.APPLICATION_XML).getCollection(Customer.class);

		String jsonString = JSON.toJSONString(list);
		System.out.println("未关联定区的客户" + jsonString);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().println(jsonString);
		return NONE;
	}

	// 查询所有关联当前选中定区的客户列表
	@Action("fixedArea_findHasAssociationFixedAreaCustomers")
	public String findHasAssociationFixedAreaCustomers() throws IOException {

		@SuppressWarnings("unchecked")
		List<Customer> list = (List<Customer>) WebClient
				.create("http://localhost:9002/crm_management/services/customerService/associationfixedareacustomers/"
						+ model.getId().toString())
				.accept(MediaType.APPLICATION_XML).getCollection(Customer.class);

		String jsonString = JSON.toJSONString(list);
		ServletActionContext.getResponse().setContentType("application/json;charset=utf-8");
		ServletActionContext.getResponse().getWriter().println(jsonString);

		return NONE;
	}

	// 属性驱动
	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	// 关联客户到定区
	@Action("fixedArea_associationCustomersToFixedArea")
	public String associationCustomersToFixedArea() {
		System.out.println(customerIds);
		String customerIdStr = StringUtils.join(customerIds, ",");
		// .有定区id,
		WebClient.create("http://localhost:9002/crm_management/services/customerService"
				+ "/associationcustomerstofixedarea?customerIdStr=" + customerIdStr + "&fixedAreaId=" + model.getId())
				.put(null);

		return NONE;
	}

	//属性驱动,定区关联取派员的时候用
	private Integer courierId;
	private Integer takeTimeId;
	
	
	public Integer getTakeTimeId() {
		return takeTimeId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	/**
	 * 定区关联取派员
	 */
	@Action(value = "fixedArea_associationCourierToFixedArea", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String associationCourierToFixedArea() {

		
		fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
		
		
		
		return "success";
	}
}
