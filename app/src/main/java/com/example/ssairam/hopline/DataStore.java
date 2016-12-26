package com.example.ssairam.hopline;

import android.content.Context;

import com.example.ssairam.hopline.vo.CategoryVo;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 20/10/16.
 */

public class DataStore {



    private static List<OrderVo> incomingOrders;
    private static List<OrderVo> preparingOrders;
    private static List<OrderVo> readyOrders;
    private static List<OrderVo> completeOfflineOrders = new ArrayList<OrderVo>();

    private static List<CategoryVo> menuCategories;

    public static synchronized List<CategoryVo> getMenuCategories(Context applicationContext) {
        return menuCategories == null ? MainPrefs.getMenuCategories(applicationContext) : menuCategories;
    }

    public static synchronized void setMenuCategories(List<CategoryVo> menuCategories, Context applicationContext) {
        DataStore.menuCategories = menuCategories;
        MainPrefs.saveMenuCategories(menuCategories,applicationContext);
    }

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


    public static synchronized void loadEverythingFromServer(Context applicationContext) throws  Exception{

        setIncomingOrders(ServerHelper.retrieveIncomingOrders(applicationContext));
        setPreparingOrders(ServerHelper.retrievePreparingOrders(applicationContext));
        setReadyOrders(ServerHelper.retrieveReadyOrders(applicationContext));
        setMenuCategories(ServerHelper.retrieveMenu(applicationContext),applicationContext);
    }

    public static boolean isDataInilitised() {
        return incomingOrders != null && preparingOrders != null && readyOrders != null;
    }

    public static List<OrderVo> getCompleteOfflineOrders(Context applicationContext) {
        if (completeOfflineOrders.isEmpty())
            completeOfflineOrders = MainPrefs.getOfflineOrders(applicationContext);

        return completeOfflineOrders;
    }

    public static void addOfflineOrder(OrderVo orderVo, Context applicationContext){
        completeOfflineOrders.add(orderVo);
        MainPrefs.saveOfflineOrders(completeOfflineOrders,applicationContext);
        addToOfflineOrderServerLog(orderVo, applicationContext);
    }

    private static void addToOfflineOrderServerLog(OrderVo orderVo, Context applicationContext) {

        List<OrderVo> ordersVos = MainPrefs.getOfflineOrdersForServerLog(applicationContext);
        ordersVos.add(orderVo);
        MainPrefs.saveOfflineOrderForServerLog(ordersVos,applicationContext);
    }

    public static void removeOfflineOrder(OrderVo orderVo, Context applicationContext){
        int foundInd = -1;
        for (int i=0 ; i < completeOfflineOrders.size() ; ++i){
            if (completeOfflineOrders.get(i).getCustomerOrderId().equals(orderVo.getCustomerOrderId())){
                foundInd = i;
                break;
            }
        }

        if (foundInd != -1) completeOfflineOrders.remove(foundInd);

        MainPrefs.saveOfflineOrders(completeOfflineOrders,applicationContext);
    }
}

