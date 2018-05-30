package com.iceps.spring.shardingjdbc2.model;

import java.math.BigDecimal;

public class Detail {
	private String orderId;

	private Integer custId;

	private String custName;

	private Integer prodId;

	private String prodName;

	private Integer userId;

	private String userName;

	private BigDecimal amount;

	private String remark;

	private Integer shardingKey;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
		this.shardingKey = Integer.valueOf(orderId.substring(orderId.length() - 2, orderId.length())) % 4;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getShardingKey() {
		return shardingKey;
	}

	public void setShardingKey(Integer shardingKey) {
		this.shardingKey = shardingKey;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "Detail [orderId=" + orderId + ", custId=" + custId + ", custName=" + custName + ", prodId=" + prodId
				+ ", prodName=" + prodName + ", userId=" + userId + ", userName=" + userName + ", amount=" + amount
				+ ", remark=" + remark + ", shardingKey=" + shardingKey + "]";
	}

}