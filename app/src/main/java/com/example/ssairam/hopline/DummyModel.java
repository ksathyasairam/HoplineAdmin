package com.example.ssairam.hopline;

import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.ShopVo;
import com.example.ssairam.hopline.vo.Stock;

/**
 * Created by root on 20/10/16.
 */

public class DummyModel {
    private FetchOrderTo fetchOrder;
    private OrderStatusTo orderStatus;
    private Stock stock;
    private OrderVo order;
    private OfflineOrderLogTo offlineOrderLogTo;
    private ShopVo shop;
    private Integer shopId;
    private String firebaseId;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public OrderVo getOrder() {
        return order;
    }

    public void setOrder(OrderVo order) {
        this.order = order;
    }

    public OrderStatusTo getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusTo orderStatus) {
        this.orderStatus = orderStatus;
    }

    public FetchOrderTo getFetchOrder() {
        return fetchOrder;
    }

    public void setFetchOrder(FetchOrderTo fetchOrder) {
        this.fetchOrder = fetchOrder;
    }

    public OfflineOrderLogTo getOfflineOrderLogTo() {
        return offlineOrderLogTo;
    }

    public void setOfflineOrderLogTo(OfflineOrderLogTo offlineOrderLogTo) {
        this.offlineOrderLogTo = offlineOrderLogTo;
    }

    public ShopVo getShop() {
        return shop;
    }

    public void setShop(ShopVo shop) {
        this.shop = shop;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
}
