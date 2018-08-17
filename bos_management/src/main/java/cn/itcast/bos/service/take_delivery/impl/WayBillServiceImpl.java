package cn.itcast.bos.service.take_delivery.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	/**
	 * saveOrUpdate
	 */
	public void save(WayBill model) {

		WayBill persistWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
		if (persistWayBill == null) {
			// 运单不存在保存操作

			wayBillRepository.save(model);
			// 保存的时候添加索引
			wayBillIndexRepository.save(model);
		} else {
			Integer id = persistWayBill.getId();
			try {
				BeanUtils.copyProperties(persistWayBill, model);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			model.setId(id);
			wayBillRepository.save(model);
			wayBillIndexRepository.save(model);
		}
	}

	@Override
	public Page<WayBill> pageQuery(WayBill wayBill, Pageable pageAble) {
		// 无条件的分页查询
		if (StringUtils.isBlank(wayBill.getWayBillNum()) && StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress()) && StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null)) {
			Page<WayBill> findAll = wayBillRepository.findAll(pageAble);
			return findAll;
		} else {
			// 条件查询
			BoolQueryBuilder query = new BoolQueryBuilder();
			// 向组合查询对象添加条件
			// must 条件必须成立 and
			// must not 条件必须不成立 not
			// should 条件可以成立 or
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				// 运单号精确查询,
				QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				//
				query.must(queryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				QueryBuilder queryBuilder = new WildcardQueryBuilder("sendAddress",
						"*" + wayBill.getSendAddress() + "*");
				// 发送地址,这个字段应该进行分词查询
				QueryBuilder queryStringBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress())
						.field("sendAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(queryBuilder);
				boolQueryBuilder.should(queryStringBuilder);
				// 两种关系取or的关系
				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
				QueryBuilder queryBuilder = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");

				// field指定对 指定的字段查询,
				// 默认多个词条切分取得是OR
				QueryBuilder queryStringBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress())
						.field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);
				BoolQueryBuilder bool = new BoolQueryBuilder();
				bool.must(queryBuilder);
				bool.must(queryStringBuilder);
				query.must(bool);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				query.must(queryBuilder);
			}
			if (wayBill.getSignStatus() != null) {
				QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				// must 相当于数据库的and
				query.must(queryBuilder);
			}
			SearchQuery searchQuery = new NativeSearchQuery(query);
			// 分页效果
			searchQuery.setPageable(pageAble);

			return wayBillIndexRepository.search(searchQuery);

		}

	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		// TODO Auto-generated method stub
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}

	@Override
	public List<WayBill> findWayBills(WayBill wayBill) {

		// 无条件的分页查询
		if (StringUtils.isBlank(wayBill.getWayBillNum()) && StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress()) && StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null)) {
			return wayBillRepository.findAll();
		} else {
			// 条件查询
			BoolQueryBuilder query = new BoolQueryBuilder();
			// 向组合查询对象添加条件
			// must 条件必须成立 and
			// must not 条件必须不成立 not
			// should 条件可以成立 or
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				// 运单号精确查询,
				QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				//
				query.must(queryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				QueryBuilder queryBuilder = new WildcardQueryBuilder("sendAddress",
						"*" + wayBill.getSendAddress() + "*");
				// 发送地址,这个字段应该进行分词查询
				QueryBuilder queryStringBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress())
						.field("sendAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(queryBuilder);
				boolQueryBuilder.should(queryStringBuilder);
				// 两种关系取or的关系
				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
				QueryBuilder queryBuilder = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");

				// field指定对 指定的字段查询,
				// 默认多个词条切分取得是OR
				QueryBuilder queryStringBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress())
						.field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);
				BoolQueryBuilder bool = new BoolQueryBuilder();
				bool.must(queryBuilder);
				bool.must(queryStringBuilder);
				query.must(bool);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				query.must(queryBuilder);
			}
			if (wayBill.getSignStatus() != null) {
				QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				// must 相当于数据库的and
				query.must(queryBuilder);
			}
			SearchQuery searchQuery = new NativeSearchQuery(query);
			// 分页效果
			PageRequest pageAble = new PageRequest(0, Integer.MAX_VALUE);
			searchQuery.setPageable(pageAble);
			return wayBillIndexRepository.search(searchQuery).getContent();

		}

	}
}
