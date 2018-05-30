package com.iceps.spring.shardingjdbc2.service;

import com.iceps.spring.shardingjdbc2.model.Product;

import java.util.List;

public interface ProductService {

    int addProduct(Product product);
    
    Product selProduct(Integer prodId);

    List<Product> findAll(int pageNum, int pageSize);
}
