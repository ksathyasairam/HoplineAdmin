package com.example.ssairam.hopline;

import android.content.Context;

import com.example.ssairam.hopline.adapters.OrderReadyAdatper;
import com.example.ssairam.hopline.vo.CategoryVo;
import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.ShopVo;
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

    private static List<OrderVo> retrieveOrdersFromServer(List<String> orderStates, Context context) throws Exception {

        FetchOrderTo fetchOrderTo = new FetchOrderTo();
        fetchOrderTo.setOrderStates(orderStates);
        fetchOrderTo.setShopId(MainPrefs.getShopId(context));

        DummyModel dm = new DummyModel();
        dm.setFetchOrder(fetchOrderTo);


        final String url = BASE_REST_URL + "fetchOrders";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        fetchOrderTo = restTemplate.postForObject(url,dm, FetchOrderTo.class);

        if (fetchOrderTo == null || fetchOrderTo.getOrders() == null) return new ArrayList<OrderVo>();

        return fetchOrderTo.getOrders();
    }

    public static ShopVo login(ShopVo shopVo) throws Exception {

        DummyModel dm = new DummyModel();
        dm.setShop(shopVo);


        final String url = BASE_REST_URL + "vendorLogin";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        shopVo = restTemplate.postForObject(url,dm, ShopVo.class);
        return shopVo;
    }

    public static List<OrderVo> retrieveOrderHistory(Context context) throws Exception {

        FetchOrderTo fetchOrderTo = new FetchOrderTo();
        fetchOrderTo.setShopId(MainPrefs.getShopId(context));

        DummyModel dm = new DummyModel();
        dm.setFetchOrder(fetchOrderTo);


        final String url = BASE_REST_URL + "fetchOrderHistory";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        fetchOrderTo = restTemplate.postForObject(url,dm, FetchOrderTo.class);

        if (fetchOrderTo == null || fetchOrderTo.getOrders() == null) return new ArrayList<OrderVo>();

        return fetchOrderTo.getOrders();
    }


    //TODO : test
    public static List<CategoryVo> retrieveMenu(Context context) throws Exception {

        DummyModel dm = new DummyModel();
        dm.setShopId(MainPrefs.getShopId(context));

        final String url = BASE_REST_URL + "retrieveMenu";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        CategoryVo[] categoryVos= restTemplate.postForObject(url,dm, CategoryVo[].class);
        if (categoryVos == null ) return new ArrayList<CategoryVo>();

        return Arrays.asList(categoryVos);
    }

    public static OrderVo createWalkInOrder(OrderVo order, Context context) throws Exception {

        DummyModel dm = new DummyModel();
        dm.setOrder(order);

        ShopVo shopVo = new ShopVo();
        shopVo.setIdshop(MainPrefs.getShopId(context));
        order.setShop(shopVo);

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

    public static boolean updateFirebaseId(String firebaseId, Context context){
        try {
            DummyModel dm = new DummyModel();
            dm.setFirebaseId(firebaseId);
            dm.setShopId(MainPrefs.getShopId(context));
            final String url = BASE_REST_URL + "updateFirebaseId";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Boolean success = restTemplate.postForObject(url, dm, Boolean.class);

            return success;
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

    public static List<OrderVo> retrieveIncomingOrders(Context context) throws  Exception{

        List<String> states = new ArrayList<>();
        states.add(OrderStates.OK_ORDER);
        states.add(OrderStates.BIG_ORDER_CALL);
        states.add(OrderStates.DEFAULTER_CALL);

        return retrieveOrdersFromServer(states, context);
    }

    public static List<OrderVo> retrievePreparingOrders(Context context) throws  Exception{

        List<String> states = new ArrayList<>();
        states.add(OrderStates.PREPARING);

        return retrieveOrdersFromServer(states, context);
    }

    public static List<OrderVo> retrieveReadyOrders(Context context) throws  Exception {

        List<String> states = new ArrayList<>();
        states.add(OrderStates.READY_FOR_PICKUP);

        return retrieveOrdersFromServer(states, context);
    }

    public static List<OrderVo> retrieveBigOrderPayOrders(Context context) throws  Exception {

        List<String> states = new ArrayList<>();
        states.add(OrderStates.BIG_ORDER_PAY);

        return retrieveOrdersFromServer(states, context);
    }


    public static boolean notifyUserPartialOrder(OrderVo orderVo) {
        OrderStatusTo orderStatusTo = new OrderStatusTo();
        orderStatusTo.setOrderId(orderVo.getIdorder());
        orderStatusTo.setAction("notifyUserPartialOrder");
        return updateOrderStatus(orderStatusTo);
    }
}
