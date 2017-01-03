package com.example.ssairam.hopline.vo;

import java.util.Date;
import java.util.List;

public class OrderVo implements java.io.Serializable {

	private Integer idorder;
	private ShopVo shop;
	private UserVo user;
	private Integer customerOrderId;
	private String orderState;
	private String paidYn;
	private Date orderTime;
	private String cancelReason;
	private Integer totalItemCount;
	private Double totalPrice;
	private List<OrderProductVo> orderProducts;
	private String orderCreator;
	private String paymentMethod;
	private String paymentSettledYN;

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentSettledYN() {
		return paymentSettledYN;
	}

	public void setPaymentSettledYN(String paymentSettledYN) {
		this.paymentSettledYN = paymentSettledYN;
	}

	private Integer numUnitInProgressBar;
	private Integer currentProgress;
	private Integer orderCompleteTime;
	private Date settledDate;



	public Integer getCurrentProgress() {
		return currentProgress;
	}
	public void setCurrentProgress(Integer currentProgress) {
		this.currentProgress = currentProgress;
	}
	public Integer getIdorder() {
		return idorder;
	}
	public void setIdorder(Integer idorder) {
		this.idorder = idorder;
	}
	public ShopVo getShop() {
		return shop;
	}
	public void setShop(ShopVo shop) {
		this.shop = shop;
	}
	public UserVo getUser() {
		return user;
	}
	public void setUser(UserVo user) {
		this.user = user;
	}
	public Integer getCustomerOrderId() {
		return customerOrderId;
	}
	public void setCustomerOrderId(Integer customerOrderId) {
		this.customerOrderId = customerOrderId;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public String getPaidYn() {
		return paidYn;
	}
	public void setPaidYn(String paidYn) {
		this.paidYn = paidYn;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public Integer getTotalItemCount() {
		return totalItemCount;
	}
	public void setTotalItemCount(Integer totalItemCount) {
		this.totalItemCount = totalItemCount;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<OrderProductVo> getOrderProducts() {
		return orderProducts;
	}
	public void setOrderProducts(List<OrderProductVo> orderProducts) {
		this.orderProducts = orderProducts;
	}
	public String getOrderCreator() {
		return orderCreator;
	}
	public void setOrderCreator(String orderCreator) {
		this.orderCreator = orderCreator;
	}
	public Integer getNumUnitInProgressBar() {
		return numUnitInProgressBar;
	}
	public void setNumUnitInProgressBar(Integer numUnitInProgressBar) {
		this.numUnitInProgressBar = numUnitInProgressBar;
	}
	public Integer getOrderCompleteTime() {
		return orderCompleteTime;
	}
	public void setOrderCompleteTime(Integer orderCompleteTime) {
		this.orderCompleteTime = orderCompleteTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OrderVo orderVo = (OrderVo) o;

		return idorder != null ? idorder.equals(orderVo.idorder) : false;

	}

	public Date getSettledDate() {
		return settledDate;
	}

	public void setSettledDate(Date settledDate) {
		this.settledDate = settledDate;
	}
}
