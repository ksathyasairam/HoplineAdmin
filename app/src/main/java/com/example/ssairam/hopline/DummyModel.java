package com.example.ssairam.hopline;

/**
 * Created by root on 20/10/16.
 */

public class DummyModel {
    private FetchOrderTo fetchOrder;
    private OrderStatusTo orderStatus;

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
}
