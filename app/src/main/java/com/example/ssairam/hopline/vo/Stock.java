package com.example.ssairam.hopline.vo;

import java.io.Serializable;
import java.util.List;

public class Stock implements Serializable{
	
	private Integer productId;
	private String stockYN;
	private boolean success;
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getStockYN() {
		return stockYN;
	}
	public void setStockYN(String stockYN) {
		this.stockYN = stockYN;
	}
	

}
