package com.iceps.spring.shardingjdbc2.service;

import com.iceps.spring.shardingjdbc2.model.Order;

import java.util.List;

public interface OrderService {

    int addOrder(Order order);
    
    Order selOrder(String orderId);

    List<Order> findAll();
    
    List<Order> findAll(int pageNum, int pageSize);

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
