package com.iceps.spring.shardingjdbc1.service;

import java.util.List;

import com.iceps.spring.shardingjdbc1.model.Order;

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
}
