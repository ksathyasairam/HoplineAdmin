package com.example.ssairam.hopline;

import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;

/**
 * Created by root on 20/10/16.
 */

public class DataStore {



    private static List<OrderVo> incomingOrders;
    private static List<OrderVo> preparingOrders;
    private static List<OrderVo> readyOrders;

    public static synchronized List<OrderVo> getIncomingOrders() {
        return incomingOrders;
    }

    public static synchronized void setIncomingOrders(List<OrderVo> incomingOrders) {
        DataStore.incomingOrders = incomingOrders;
    }

    public static synchronized List<OrderVo> getPreparingOrders() {
        return preparingOrders;
    }

    public static synchronized void setPreparingOrders(List<OrderVo> preparingOrders) {
        DataStore.preparingOrders = preparingOrders;
    }

    public static synchronized List<OrderVo> getReadyOrders() {
        return readyOrders;
    }

    public static synchronized void setReadyOrders(List<OrderVo> readyOrders) {
        DataStore.readyOrders = readyOrders;
    }


    public static synchronized void loadEverythingFromServer() throws  Exception{

        setIncomingOrders(ServerHelper.retrieveIncomingOrders());
        setPreparingOrders(ServerHelper.retrievePreparingOrders());
        setReadyOrders(ServerHelper.retrieveReadyOrders());
    }

    public static boolean isDataInilitised() {
        return incomingOrders != null && preparingOrders != null && readyOrders != null;
    }
}

