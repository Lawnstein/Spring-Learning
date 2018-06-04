package com.iceps.spring.shardingjdbc1.service;

import java.util.List;

import com.iceps.spring.shardingjdbc1.model.Product;

public interface ProductService {

    int addProduct(Product product);
    
    Product selProduct(Integer prodId);

    List<Product> findAll(int pageNum, int pageSize);
}
