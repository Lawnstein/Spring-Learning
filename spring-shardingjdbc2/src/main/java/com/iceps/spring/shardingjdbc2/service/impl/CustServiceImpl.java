package com.iceps.spring.shardingjdbc2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.iceps.spring.shardingjdbc2.mapper.CustMapper;
import com.iceps.spring.shardingjdbc2.model.Cust;
import com.iceps.spring.shardingjdbc2.service.CustService;

@Service
public class CustServiceImpl implements CustService {

	@Autowired
	private CustMapper custMapper;

	@Override
	public int addCust(Cust cust) {
		return custMapper.insert(cust);
	}

	@Override
	public List<Cust> findAll(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Cust> custs = custMapper.selectAll();
		System.out.println(
				"CustServiceImpl.findAll.cust" + ((custs != null && custs.size() > 0) ? custs.get(0).getClass() : ""));
		for (Cust u : custs)
			System.out.println("CustServiceImpl.findAll.cust" + u);
		return custs;
	}

	@Override
	public Cust selCust(Integer custId) {
		return custMapper.selectByPrimaryKey(custId);
	}

}
