package com.iceps.spring.shardingjdbc2.mapper;

import com.iceps.spring.shardingjdbc2.model.Product;

import java.util.List;

public interface ProductMapper {

	int insert(Product record);

	int updateByPrimaryKey(Product record);

	int deleteByPrimaryKey(Integer productId);

	Product selectByPrimaryKey(Integer productId);

	List<Product> selectAll();
}