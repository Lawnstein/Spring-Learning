package com.iceps.spring.shardingjdbc2.service;

import com.iceps.spring.shardingjdbc2.model.Detail;

import java.util.List;

public interface DetailService {

    Detail selectDetail(String orderId);
    
    List<Detail> selectAll();
    
    List<Detail> selectAll(int pageNum, int pageSize);

	List<Detail> selectByUserId(Integer userId);
	
	List<Detail> selectByCustId(Integer custId);
	
	List<Detail> selectByProdId(Integer prodId);

	int updateOrderByProdId(Integer prodId, String remark);
}
