package com.iceps.spring.shardingjdbc1.service;

import java.util.List;

import com.iceps.spring.shardingjdbc1.model.Cust;

public interface CustService {

    int addCust(Cust cust);
    
    Cust selCust(Integer custId);

    List<Cust> findAll(int pageNum, int pageSize);
    
}
