package com.iceps.spring.shardingjdbc1.mapper;

import java.util.List;

import com.iceps.spring.shardingjdbc1.model.Order;

public interface OrderMapper {

	int insert(Order record);

	int updateByPrimaryKey(Order record);

	int deleteByPrimaryKey(String orderId);

	Order selectByPrimaryKey(String orderId);

	List<Order> selectAll();

	List<Order> selectAllOr();

	List<Order> selectAllOrderBy();

	List<Order> selectAllLike();

	List<Order> selectAllIn();

	List<Order> selectAllRange();

	List<Order> selectDistinct();

	List<Order> selectUnion();

	List<Order> selectByUserId(Integer userId);

}