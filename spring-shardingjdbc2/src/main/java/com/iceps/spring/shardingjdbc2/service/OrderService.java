package com.iceps.spring.shardingjdbc2.service;

import com.iceps.spring.shardingjdbc2.model.Order;

import java.util.List;

public interface OrderService {

    int addOrder(Order order);
    
    Order selOrder(String orderId);

    List<Order> findAll();
    
    List<Order> findAll(int pageNum, int pageSize);

	List<Order> selectByUserId(Integer userId);
}
