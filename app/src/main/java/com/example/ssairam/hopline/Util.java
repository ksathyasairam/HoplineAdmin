package com.example.ssairam.hopline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.ssairam.hopline.vo.OrderVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/10/16.
 */

public class Util {



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
}
