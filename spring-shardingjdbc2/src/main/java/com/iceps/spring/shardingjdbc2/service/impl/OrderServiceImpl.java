package com.iceps.spring.shardingjdbc2.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.iceps.spring.shardingjdbc2.mapper.OrderMapper;
import com.iceps.spring.shardingjdbc2.model.Order;
import com.iceps.spring.shardingjdbc2.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	private final static List EMPTY_LIST = new ArrayList();

	@Autowired
	private OrderMapper orderMapper;

	@Override
	public int addOrder(Order order) {
		return orderMapper.insert(order);
	}

	@Override
	public List<Order> findAll(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Order> orders = orderMapper.selectAll();
		System.out.println("OrderServiceImpl.findAll.order"
				+ ((orders != null && orders.size() > 0) ? orders.get(0).getClass() : ""));
		for (Order u : orders)
			System.out.println("OrderServiceImpl.findAll.order" + u);
		return orders;
	}

	@Override
	public Order selOrder(String orderId) {
		return orderMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public List<Order> findAll() {
		return orderMapper.selectAll();
	}

	@Override
	public List<Order> selectByUserId(Integer userId) {
		try {
			return orderMapper.selectByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Order> selectAllOr() {
		try {
			return orderMapper.selectAllOr();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Order> selectAllOrderBy() {
		try {
			return orderMapper.selectAllOrderBy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Order> selectAllLike() {
		try {
			return orderMapper.selectAllLike();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Order> selectAllIn() {
		try {
			return orderMapper.selectAllIn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Order> selectAllRange() {
		try {
			return orderMapper.selectAllRange();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Order> selectDistinct() {
		try {
			return orderMapper.selectDistinct();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Order> selectUnion() {
		try {
			return orderMapper.selectUnion();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public int selectCountByUserId(Integer userId) {
		return orderMapper.selectCountByUserId(userId);
	}

	@Override
	public int selectMaxByUserId(Integer userId) {
		return orderMapper.selectMaxByUserId(userId);
	}

	@Override
	public int selectMinByUserId(Integer userId) {
		return orderMapper.selectMinByUserId(userId);
	}

	@Override
	public int selectSumByUserId(Integer userId) {
		return orderMapper.selectSumByUserId(userId);
	}

}
