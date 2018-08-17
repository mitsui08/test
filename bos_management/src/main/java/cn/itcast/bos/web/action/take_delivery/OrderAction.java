package cn.itcast.bos.web.action.take_delivery;

import java.io.IOException;
import java.util.HashMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

	@Autowired
	private OrderService orderService;

	@Action(value = "order_findByOrderNum")
	public String findByOrderNum() throws IOException {

		HashMap<String, Object> map = new HashMap<>();
		Order order = order = orderService.findByOrderNum(model.getOrderNum());
		if (order != null) {
			map.put("flag", true);
			map.put("orderData", order);
		} else {
			map.put("flag", false);
			map.put("msg", "查找失败");
		}

		String jsonString = JSONObject.toJSONString(map);
		resposeToFront(jsonString);
		System.out.println(jsonString);
		return NONE;
	}

}
