package com.iceps.spring.shardingjdbc1.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.iceps.spring.shardingjdbc1.mapper.DetailMapper;
import com.iceps.spring.shardingjdbc1.model.Detail;
import com.iceps.spring.shardingjdbc1.service.DetailService;

@Service
public class DetailServiceImpl implements DetailService {

	private final static List EMPTY_LIST = new ArrayList();

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

	@Override
	public List<Detail> selectAllOrderBy() {
		try {
			return detailMapper.selectAllOrderBy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Detail> selectAllLike() {
		try {
			return detailMapper.selectAllLike();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Detail> selectAllIn() {
		try {
			return detailMapper.selectAllIn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Detail> selectAllRange() {
		try {
			return detailMapper.selectAllRange();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Detail> selectDistinct() {
		try {
			return detailMapper.selectDistinct();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Detail> selectUnion() {
		try {
			return detailMapper.selectUnion();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

	@Override
	public List<Detail> selectAllOr() {
		try {
			return detailMapper.selectAllOr();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}

}
