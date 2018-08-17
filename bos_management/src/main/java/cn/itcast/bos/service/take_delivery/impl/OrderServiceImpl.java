package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.WorkBillRepository;
import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.utils.UUIDUtils;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private WorkBillRepository WorkBillRepository;

	@Autowired
	private FixedAreaRepository fixedAreaRepository;

	public void saveOrder(Order order) {
		// 先设置订单编号
		String orderNum = UUIDUtils.getUUID();
		order.setOrderNum(orderNum);
		order.setOrderTime(new Date());
		Area sendArea = order.getSendArea();
		Area recArea = order.getRecArea();
		Area sendPresidentArea = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(),
				sendArea.getCity(), sendArea.getDistrict());

		Area recPresidentArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(), recArea.getDistrict());
		order.setSendArea(sendPresidentArea);
		order.setRecArea(recPresidentArea);
		// 自动分单
		// 精准匹配
		// crm,提供一个根据sendaddress查找定区的方法

		String fixedAreaId = WebClient.create(
				"http://localhost:9002/crm_management/services/customerService/findFixedAreaIdByAddress?address="
						+ order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		if (fixedAreaId != null && fixedAreaId != "") {
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
			if (iterator.hasNext()) {
				Courier courier = iterator.next();
				// 关联快递员,完成自动分单
				order.setCourier(courier);
				order.setOrderType("1");
				orderRepository.save(order);
				generateWorkBill(order);
				return;
			}
		}
		// 第二种分单方式
		if (fixedAreaId == null) {
			Area area = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(),
					sendArea.getDistrict());
			Set<SubArea> subareas = area.getSubareas();
			for (SubArea subArea : subareas) {
				if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
					Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
					if (iterator.hasNext()) {
						order.setCourier(iterator.next());
						order.setOrderType("1");
						orderRepository.save(order);
						generateWorkBill(order);
						return;
					}
				}
			}
		}
		// 自动分单失败
		order.setOrderType("2");
		orderRepository.save(order);
	}

	/**
	 * 生成工单,给快递员发送短信
	 */
	private void generateWorkBill(Order order) {
		WorkBill workBill = new WorkBill();
		workBill.setType("1");
		workBill.setPickstate(WorkBill.PICKSTATE_NEWSTATE);
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		WorkBillRepository.save(workBill);
		// 短信吗
		String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);
		// 发送短信
		System.out.println("短信吗:" + smsNumber);
		// 设置已通知
		workBill.setPickstate(WorkBill.PICKSTATE_ADVIED);
	}

	@Override
	public Order findByOrderNum(String orderNum) {
		return orderRepository.findByOrderNum(orderNum);
	}
}
