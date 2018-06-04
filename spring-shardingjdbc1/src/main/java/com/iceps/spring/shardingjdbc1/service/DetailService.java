package com.iceps.spring.shardingjdbc1.service;

import java.util.List;

import com.iceps.spring.shardingjdbc1.model.Detail;

public interface DetailService {

    Detail selectDetail(String orderId);
    
    List<Detail> selectAll();
    
    List<Detail> selectAll(int pageNum, int pageSize);

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

	int updateOrderByProdId(Integer prodId, String remark);
}
