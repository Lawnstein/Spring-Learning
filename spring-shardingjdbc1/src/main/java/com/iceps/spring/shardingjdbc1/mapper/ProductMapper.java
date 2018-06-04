package com.iceps.spring.shardingjdbc1.mapper;

import java.util.List;

import com.iceps.spring.shardingjdbc1.model.Product;

public interface ProductMapper {

	int insert(Product record);

	int updateByPrimaryKey(Product record);

	int deleteByPrimaryKey(Integer productId);

	Product selectByPrimaryKey(Integer productId);

	List<Product> selectAll();
}