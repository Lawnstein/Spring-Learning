package com.iceps.spring.shardingjdbc1.model;

public class Cust {
	private Integer custId;
	private String custName;

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	@Override
	public String toString() {
		return "Cust [custId=" + custId + ", custName=" + custName + "]";
	}

}