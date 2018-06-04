package com.iceps.spring.shardingjdbc1.model;

import java.math.BigDecimal;

public class Order {
	private String orderId;

	private Integer custId;

	private Integer prodId;

	private Integer userId;

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

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", custId=" + custId + ", prodId=" + prodId + ", userId=" + userId
				+ ", amount=" + amount + ", remark=" + remark + ", shardingKey=" + shardingKey + "]";
	}

}