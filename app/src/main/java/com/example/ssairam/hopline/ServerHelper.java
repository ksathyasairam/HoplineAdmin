package com.example.ssairam.hopline;

import com.example.ssairam.hopline.vo.OrderVo;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class ServerHelper {


    private static final String BASE_REST_URL = "http://hopline.5fypzgp2y7.us-east-1.elasticbeanstalk.com/rest/";

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

    public static boolean markOrderCancel(Integer orderId, String cancelReason){
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.CANCELLED);
        ordrOrderStatusTo.setCancelReason(cancelReason);

        return updateOrderStatus(ordrOrderStatusTo);
    }

    public static boolean markOrderPreparing(Integer orderId){
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.PREPARING);

        return updateOrderStatus(ordrOrderStatusTo);
    }

    public static boolean markOrderReadyForPickup(Integer orderId) {
        OrderStatusTo ordrOrderStatusTo = new OrderStatusTo();
        ordrOrderStatusTo.setOrderId(orderId);
        ordrOrderStatusTo.setOrderStatus(OrderStates.READY_FOR_PICKUP);

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




}
