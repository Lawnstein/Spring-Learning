package com.iceps.spring.shardingjdbc2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.iceps.spring.shardingjdbc2.mapper.OrderMapper;
import com.iceps.spring.shardingjdbc2.model.Order;
import com.iceps.spring.shardingjdbc2.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

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
		return orderMapper.selectByUserId(userId);
	}

}
