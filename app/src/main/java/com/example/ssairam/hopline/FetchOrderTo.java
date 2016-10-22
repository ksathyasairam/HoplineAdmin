package com.example.ssairam.hopline;



import com.example.ssairam.hopline.vo.OrderVo;

import java.io.Serializable;
import java.util.List;


public class FetchOrderTo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 740088737221743039L;
	private Integer shopId;
	private List<String> orderStates;
	private List<OrderVo> orders;
	private boolean success;
	
	public List<String> getOrderStates() {
		return orderStates;
	}
	public void setOrderStates(List<String> orderStates) {
		this.orderStates = orderStates;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public List<OrderVo> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderVo> orders) {
		this.orders = orders;
	}
	
	
	

}
