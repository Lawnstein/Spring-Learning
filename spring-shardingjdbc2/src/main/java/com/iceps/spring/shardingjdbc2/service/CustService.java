package com.iceps.spring.shardingjdbc2.service;

import com.iceps.spring.shardingjdbc2.model.Cust;

import java.util.List;

public interface CustService {

    int addCust(Cust cust);
    
    Cust selCust(Integer custId);

    List<Cust> findAll(int pageNum, int pageSize);
    
}
