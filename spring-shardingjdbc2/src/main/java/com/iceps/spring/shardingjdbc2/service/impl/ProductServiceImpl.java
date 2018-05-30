package com.iceps.spring.shardingjdbc2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.iceps.spring.shardingjdbc2.mapper.ProductMapper;
import com.iceps.spring.shardingjdbc2.model.Product;
import com.iceps.spring.shardingjdbc2.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;

	@Override
	public int addProduct(Product product) {
		return productMapper.insert(product);
	}

	@Override
	public List<Product> findAll(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Product> products = productMapper.selectAll();
		System.out.println("ProductServiceImpl.findAll.product"
				+ ((products != null && products.size() > 0) ? products.get(0).getClass() : ""));
		for (Product u : products)
			System.out.println("ProductServiceImpl.findAll.product" + u);
		return products;
	}

	@Override
	public Product selProduct(Integer prodId) {
		return productMapper.selectByPrimaryKey(prodId);
	}

}
