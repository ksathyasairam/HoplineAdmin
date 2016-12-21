package com.example.ssairam.hopline;

import com.example.ssairam.hopline.adapters.OrderReadyAdatper;
import com.example.ssairam.hopline.vo.CategoryVo;
import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.Stock;
import com.google.gson.Gson;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ServerHelper {

    public static final String BASE_URL = "http://demo.hopline.in/";
    public static final String BASE_REST_URL = BASE_URL + "rest/";

    private static List<OrderVo> retrieveOrdersFromServer(List<String> orderStates) throws Exception {

        FetchOrderTo fetchOrderTo = new FetchOrderTo();
        fetchOrderTo.setOrderStates(orderStates);
        fetchOrderTo.setShopId(1);

        DummyModel dm = new DummyModel();
        dm.setFetchOrder(fetchOrderTo);


        final String url = BASE_REST_URL + "fetchOrders";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        fetchOrderTo = restTemplate.postForObject(url,dm, FetchOrderTo.class);

        if (fetchOrderTo == null || fetchOrderTo.getOrders() == null) return new ArrayList<OrderVo>();

        return fetchOrderTo.getOrders();
    }

    public static List<OrderVo> retrieveOrderHistory() throws Exception {

        FetchOrderTo fetchOrderTo = new FetchOrderTo();
        fetchOrderTo.setShopId(1);

        DummyModel dm = new DummyModel();
        dm.setFetchOrder(fetchOrderTo);


        final String url = BASE_REST_URL + "fetchOrderHistory";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        fetchOrderTo = restTemplate.postForObject(url,dm, FetchOrderTo.class);

        if (fetchOrderTo == null || fetchOrderTo.getOrders() == null) return new ArrayList<OrderVo>();

        return fetchOrderTo.getOrders();
    }

    public static List<CategoryVo> retrieveMenu() throws Exception {

        DummyModel dm = new DummyModel();

        final String url = BASE_REST_URL + "retrieveMenu";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        CategoryVo[] categoryVos= restTemplate.postForObject(url,dm, CategoryVo[].class);
        if (categoryVos == null ) return new ArrayList<CategoryVo>();

        return Arrays.asList(categoryVos);
    }

    public static OrderVo createWalkInOrder(OrderVo order) throws Exception {

        DummyModel dm = new DummyModel();
        dm.setOrder(order);

        final String url = BASE_REST_URL + "createWalkinOrder";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        order= restTemplate.postForObject(url,dm, OrderVo.class);

        if (order == null || order.getIdorder() == null) throw new Exception();

        return order;
    }



    private static boolean updateOrderStatus(OrderStatusTo orderStatusTo){

        try {
            DummyModel dm = new DummyModel();
            dm.setOrderStatus(orderStatusTo);


            final String url = BASE_REST_URL + "udpateOrderStatus";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            orderStatusTo = restTemplate.postForObject(url, dm, OrderStatusTo.class);

            if (orderStatusTo == null) return false;

            return orderStatusTo.isSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean markOrderReadyForPickup(Integer orderId) {
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.READY_FOR_PICKUP);

        return updateOrderStatus(ordrOrderStatusTo);
    }

//    public static OrderStatusTo markItemsPrepared(OrderVo orderVo){
//
//        OrderStatusTo orderStatusTo = new OrderStatusTo();
//        orderStatusTo.setOrderId(orderVo.getIdorder());
//        orderStatusTo.setOrderProductVoList(orderVo.getOrderProducts());
//        orderStatusTo.setAction("markItemsPrepared");
//
//        try {
//            DummyModel dm = new DummyModel();
//            dm.setOrderStatus(orderStatusTo);
//
//
//            final String url = BASE_REST_URL + "udpateOrderStatus";
//            RestTemplate restTemplate = new RestTemplate();
//            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//            orderStatusTo = restTemplate.postForObject(url, dm, OrderStatusTo.class);
//
//            return orderStatusTo;
//        }catch (Exception e) {
//            e.printStackTrace();
//            return orderStatusTo;
//        }
//    }

    public static boolean updateStock(Stock stock){
        try {

            DummyModel dm = new DummyModel();
            dm.setStock(stock);

            final String url = BASE_REST_URL + "updateStock";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            stock = restTemplate.postForObject(url, dm, Stock.class);

            if (stock == null) return false;

            return stock.isSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean logCompleteOfflineOrder(List<OrderVo> orderVos){

        OfflineOrderLogTo offlineOrderLogTo = new OfflineOrderLogTo();
        offlineOrderLogTo.setOrdersJson(new Gson().toJson(orderVos));

        try {

            DummyModel dm = new DummyModel();
            dm.setOfflineOrderLogTo(offlineOrderLogTo);

            final String url = BASE_REST_URL + "createOfflineOrderLog";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            offlineOrderLogTo = restTemplate.postForObject(url, dm, OfflineOrderLogTo.class);

            if (offlineOrderLogTo == null) return false;

            return offlineOrderLogTo.isSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean markOrderCancel(Integer orderId, String cancelReason){
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.CANCELLED);
        ordrOrderStatusTo.setCancelReason(cancelReason);

        return updateOrderStatus(ordrOrderStatusTo);
    }

    public static boolean markOrderPreparing(Integer orderId, Integer orderCompletionTime){
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.PREPARING);
//        ordrOrderStatusTo.setUpdateOrderTime(true);
        ordrOrderStatusTo.setOrderCompletionTime(orderCompletionTime);
        return updateOrderStatus(ordrOrderStatusTo);
    }

    public static boolean markOrderPreparingAndPaidAndUdpateDate(Integer orderId,Integer orderCompletionTime){
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.PREPARING);
        ordrOrderStatusTo.setUpdateOrderTime(true);
        ordrOrderStatusTo.setPaidYN("Y");
        ordrOrderStatusTo.setOrderCompletionTime(orderCompletionTime);

        return updateOrderStatus(ordrOrderStatusTo);
    }



    public static boolean markOrderUnpicked(Integer orderId){
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.UNPICKED);

        return updateOrderStatus(ordrOrderStatusTo);
    }

    public static boolean markOrderCompleted(Integer orderId) {
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.COMPLETED);
        ordrOrderStatusTo.setPaidYN("Y");

        return updateOrderStatus(ordrOrderStatusTo);
    }

    public static List<OrderVo> retrieveIncomingOrders() throws  Exception{

        List<String> states = new ArrayList<>();
        states.add(OrderStates.OK_ORDER);
        states.add(OrderStates.BIG_ORDER_CALL);
        states.add(OrderStates.DEFAULTER_CALL);

        return retrieveOrdersFromServer(states);
    }

    public static List<OrderVo> retrievePreparingOrders() throws  Exception{

        List<String> states = new ArrayList<>();
        states.add(OrderStates.PREPARING);

        return retrieveOrdersFromServer(states);
    }

    public static List<OrderVo> retrieveReadyOrders() throws  Exception {

        List<String> states = new ArrayList<>();
        states.add(OrderStates.READY_FOR_PICKUP);

        return retrieveOrdersFromServer(states);
    }

    public static List<OrderVo> retrieveBigOrderPayOrders() throws  Exception {

        List<String> states = new ArrayList<>();
        states.add(OrderStates.BIG_ORDER_PAY);

        return retrieveOrdersFromServer(states);
    }


    public static boolean notifyUserPartialOrder(OrderVo orderVo) {
        OrderStatusTo orderStatusTo = new OrderStatusTo();
        orderStatusTo.setOrderId(orderVo.getIdorder());
        orderStatusTo.setAction("notifyUserPartialOrder");
        return updateOrderStatus(orderStatusTo);
    }
}
