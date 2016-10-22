package com.example.ssairam.hopline;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.ssairam.hopline.vo.OrderVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/10/16.
 */

public class Util {



    public static void synchronizedRemoveOrderFromList(Integer deletedOrderId, List<OrderVo> orderVoList) {
        OrderVo deletedOrder = new OrderVo();
        deletedOrder.setIdorder(deletedOrderId);

        List<OrderVo> localConformationOrderCopy = new ArrayList<OrderVo>(orderVoList);

        localConformationOrderCopy.remove(deletedOrder);
        DataRefreshServcie.setLocallyUpdated(true);
        DataStore.setIncomingOrders(localConformationOrderCopy);
    }

    public static ProgressDialog createProgressDialog(Context context) {

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;

    }
}
