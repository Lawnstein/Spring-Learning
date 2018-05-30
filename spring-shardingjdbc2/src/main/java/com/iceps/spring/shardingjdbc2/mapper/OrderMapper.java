package com.iceps.spring.shardingjdbc2.mapper;

import com.iceps.spring.shardingjdbc2.model.Order;

import java.util.List;

public interface OrderMapper {

	int insert(Order record);

	int updateByPrimaryKey(Order record);

	int deleteByPrimaryKey(String orderId);

	Order selectByPrimaryKey(String orderId);

	List<Order> selectAll();

	List<Order> selectByUserId(Integer userId);
}