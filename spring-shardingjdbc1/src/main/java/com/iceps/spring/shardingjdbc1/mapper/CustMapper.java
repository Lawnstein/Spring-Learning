package com.iceps.spring.shardingjdbc1.mapper;

import java.util.List;

import com.iceps.spring.shardingjdbc1.model.Cust;

public interface CustMapper {

	int insert(Cust record);

	int updateByPrimaryKey(Cust record);

	int deleteByPrimaryKey(Integer custId);

	Cust selectByPrimaryKey(Integer custId);

	List<Cust> selectAll();
	
}