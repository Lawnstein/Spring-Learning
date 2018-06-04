package com.iceps.spring.shardingjdbc2.mapper;

import java.util.List;

import com.iceps.spring.shardingjdbc2.model.Order;

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

	int selectCountByUserId(Integer userId);

	int selectMaxByUserId(Integer userId);

	int selectMinByUserId(Integer userId);

	int selectSumByUserId(Integer userId);
	
}