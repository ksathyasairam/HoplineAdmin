package com.example.ssairam.hopline.vo;

import java.math.BigDecimal;
import java.util.List;

public class ProductVo implements java.io.Serializable {

	private Integer productId;
	private String name;
	private String shortDesc;
	private String longDesc;
	private BigDecimal price;
	private String vegYn;
	private String stockYn;
	private List<AddOnVo> addOns;
	
	
	
	
	//For UI frontEnd
	private boolean isExpanded;
	private int quantity = 1;
	
	 
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getVegYn() {
		return vegYn;
	}
	public void setVegYn(String vegYn) {
		this.vegYn = vegYn;
	}
	public String getStockYn() {
		return stockYn;
	}
	public void setStockYn(String stockYn) {
		this.stockYn = stockYn;
	}
	public List<AddOnVo> getAddOns() {
		return addOns;
	}
	public void setAddOns(List<AddOnVo> addOns) {
		this.addOns = addOns;
	}
	public boolean isExpanded() {
		return isExpanded;
	}
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductVo productVo = (ProductVo) o;

		return productId.equals(productVo.productId);

	}

	@Override
	public int hashCode() {
		return productId.hashCode();
	}
}
