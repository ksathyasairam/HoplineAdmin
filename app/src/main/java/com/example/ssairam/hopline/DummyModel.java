package com.example.ssairam.hopline;

import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.Stock;

/**
 * Created by root on 20/10/16.
 */

public class DummyModel {
    private FetchOrderTo fetchOrder;
    private OrderStatusTo orderStatus;
    private OrderVo order;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    private Stock stock;

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
}
