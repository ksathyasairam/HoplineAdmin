package com.example.ssairam.hopline.vo;

import java.util.List;

public class ProductGroupVo {

	private List<ProductVo> products;
	private String groupName;

	public List<ProductVo> getProducts() {
		return products;
	}

	public void setProducts(List<ProductVo> products) {
		this.products = products;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
