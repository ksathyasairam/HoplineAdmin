package com.example.ssairam.hopline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.ssairam.hopline.vo.OrderProductAddonVo;
import com.example.ssairam.hopline.vo.OrderProductVo;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/10/16.
 */

public class Util {

    public static void synchronizedRemoveFromIncommingOrders(Integer deletedOrderId) {
        OrderVo deletedOrder = new OrderVo();
        deletedOrder.setIdorder(deletedOrderId);

        List<OrderVo> localOrderVoCopy = new ArrayList<OrderVo>(DataStore.getIncomingOrders());

        localOrderVoCopy.remove(deletedOrder);
        IncommingOrderBackgroudRefresh.setLocallyUpdated(true);
        DataStore.setIncomingOrders(localOrderVoCopy);
    }

    public static void synchronizedAddToIncommingOrders(OrderVo fullyInitializedOrder) {

        List<OrderVo> localOrderVoCopy = new ArrayList<OrderVo>(DataStore.getIncomingOrders());

        localOrderVoCopy.add(fullyInitializedOrder);
        IncommingOrderBackgroudRefresh.setLocallyUpdated(true);
        DataStore.setIncomingOrders(localOrderVoCopy);
    }


    private void removeOrderFromList(Integer deletedOrderId, List<OrderVo> orderList) {
        OrderVo deletedOrder = new OrderVo();
        deletedOrder.setIdorder(deletedOrderId);

        List<OrderVo> localOrderVoCopy = new ArrayList<OrderVo>(DataStore.getIncomingOrders());

        localOrderVoCopy.remove(deletedOrder);
        IncommingOrderBackgroudRefresh.setLocallyUpdated(true);
        DataStore.setIncomingOrders(localOrderVoCopy);
    }

    public static ProgressDialog createProgressDialog(Context context) {

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;

    }

    public static ProgressDialog showProgressDialog(Activity activity) {

        ProgressDialog dialog = new ProgressDialog(activity); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;

    }

    public static double calculatePrice(OrderProductVo orderProduct) {
        double addonPrice = 0;

        if (orderProduct.getOrderProductAddons() != null) {
            for (OrderProductAddonVo orderProductAddon : orderProduct.getOrderProductAddons()) {
                addonPrice += orderProductAddon.getAddOn().getPrice();
            }
        }

        return orderProduct.getCount() * (orderProduct.getProduct().getPrice().doubleValue() + addonPrice ) ;
    }

    public static CharSequence getAddonString(List<OrderProductAddonVo> orderProductAddons) {
        if (orderProductAddons == null || orderProductAddons.isEmpty()) return "";

        String result = "";
        for(OrderProductAddonVo orderProductAddonVo : orderProductAddons) {
            result += "," + orderProductAddonVo.getAddOn().getName();
        }

        return result.substring(1);
    }

    public static boolean printBill(OrderVo order) {
        return true;
    }
}
