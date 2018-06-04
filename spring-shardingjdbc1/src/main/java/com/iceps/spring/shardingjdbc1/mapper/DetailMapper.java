package com.iceps.spring.shardingjdbc1.mapper;

import java.util.List;
import java.util.Map;

import com.iceps.spring.shardingjdbc1.model.Detail;

public interface DetailMapper {

	Detail selectDetail(String orderId);
	
	List<Detail> selectAll();

	List<Detail> selectAllOr();
	
	List<Detail> selectAllOrderBy();
	
	List<Detail> selectAllLike();
	
	List<Detail> selectAllIn();
	
	List<Detail> selectAllRange();
	
	List<Detail> selectDistinct();
	
	List<Detail> selectUnion();

	List<Detail> selectByUserId(Integer userId);
	
	List<Detail> selectByCustId(Integer custId);
	
	List<Detail> selectByProdId(Integer prodId);
	
	int updateOrderByProdId(Map map);
	
}