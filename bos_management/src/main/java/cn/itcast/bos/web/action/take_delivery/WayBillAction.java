package cn.itcast.bos.web.action.take_delivery;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
@SuppressWarnings("unused")
public class WayBillAction extends BaseAction<WayBill> {

	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);
	@Autowired
	private WayBillService wayBillService;

	@Action(value = "waybill_save")
	public String save() throws IOException {
		Map<String, Object> result = new HashMap<String,Object>();
		
		Order order = model.getOrder();
		if(order != null &&(model.getOrder().getId() ==null)) {
			model.setOrder(null);
		}
		try {
			wayBillService.save(model);
			result.put("flag", true);
			result.put("msg", "添加成功");
			LOGGER.info("保存运单成功，运单号：" + model.getWayBillNum());
		} catch (Exception e) {
			result.put("flag", false);
			result.put("msg", "添加失败");
			LOGGER.error("保存运单失败，运单号：" + model.getWayBillNum(), e);
		}
		String json = JSONObject.toJSONString(result);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().println(json);
		return NONE;
	}
	
	
	@Action(value="wayBill_pageQuery")
	public String pageQuery() throws IOException {
		System.out.println(model.getSendAddress()+" " +model.getRecAddress());
		//第三个sort条件,是静态类
		PageRequest pageRequest = new PageRequest(page-1, rows,new Sort(new Sort.Order(Sort.Direction.DESC,"id")));                       
		Page<WayBill> pageQuery = wayBillService.pageQuery(model,pageRequest);
		String pageJson = pageJson(pageQuery);
		resposeToFront(pageJson);
		return NONE;
	}
	
	@Action(value="wayBill_findByWayBillNum")
	public String findByWayBillNum() throws IOException {
		//TODO 查找waybill,运单,然后返回数据,给页面回显
		HashMap<String, Object> map = new HashMap<>();
		WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
		if(wayBill != null) {
			map.put("flag", true);
			map.put("wayBillData", wayBill);
		}else {
			map.put("flag", false);
			map.put("msg", "查询失败");
		}
		String jsonString = JSONObject.toJSONString(map);
		resposeToFront(jsonString);
		return NONE;
	}
	
	

}



