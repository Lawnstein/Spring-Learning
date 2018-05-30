package com.iceps.spring.shardingjdbc2.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.iceps.spring.shardingjdbc2.mapper.DetailMapper;
import com.iceps.spring.shardingjdbc2.model.Detail;
import com.iceps.spring.shardingjdbc2.service.DetailService;

@Service
public class DetailServiceImpl implements DetailService {

	@Autowired
	private DetailMapper detailMapper;

	@Override
	public Detail selectDetail(String orderId) {
		return detailMapper.selectDetail(orderId);
	}

	@Override
	public List<Detail> selectAll() {
		return detailMapper.selectAll();
	}

	@Override
	public List<Detail> selectAll(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return detailMapper.selectAll();
	}

	@Override
	public List<Detail> selectByUserId(Integer userId) {
		return detailMapper.selectByUserId(userId);
	}

	@Override
	public List<Detail> selectByCustId(Integer custId) {
		return detailMapper.selectByCustId(custId);
	}

	@Override
	public List<Detail> selectByProdId(Integer prodId) {
		return detailMapper.selectByProdId(prodId);
	}

	@Override
	public int updateOrderByProdId(Integer prodId, String remark) {
		Map m = new HashMap();
		m.put("prodId", prodId);
		m.put("remark", remark);
		return detailMapper.updateOrderByProdId(m);
	}

}
