package com.iceps.spring.shardingjdbc2.mapper;

import com.iceps.spring.shardingjdbc2.model.Cust;

import java.util.List;

public interface CustMapper {

	int insert(Cust record);

	int updateByPrimaryKey(Cust record);

	int deleteByPrimaryKey(Integer custId);

	Cust selectByPrimaryKey(Integer custId);

	List<Cust> selectAll();
	
}