package com.iceps.spring.shardingjdbc2.mapper;

import com.iceps.spring.shardingjdbc2.model.Detail;

import java.util.List;
import java.util.Map;

public interface DetailMapper {

	Detail selectDetail(String orderId);
	
	List<Detail> selectAll();

	List<Detail> selectByUserId(Integer userId);
	
	List<Detail> selectByCustId(Integer custId);
	
	List<Detail> selectByProdId(Integer prodId);
	
	int updateOrderByProdId(Map map);
}