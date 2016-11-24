package com.example.ssairam.hopline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import java.io.*;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.example.ssairam.hopline.vo.OrderProductAddonVo;
import com.example.ssairam.hopline.vo.OrderProductVo;
import com.example.ssairam.hopline.vo.OrderVo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.pivotX;
import static android.R.attr.x;

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

        return orderProduct.getCount() * (orderProduct.getProduct().getPrice().doubleValue() + addonPrice);
    }

    public static CharSequence getAddonString(List<OrderProductAddonVo> orderProductAddons) {
        if (orderProductAddons == null || orderProductAddons.isEmpty()) return "";

        String result = "";
        for (OrderProductAddonVo orderProductAddonVo : orderProductAddons) {
            result += "," + orderProductAddonVo.getAddOn().getName();
        }

        return result.substring(1);
    }


    private static String formatPrice2Decimal(double price){
        return  new DecimalFormat("#.00", DecimalFormatSymbols.getInstance( Locale.ENGLISH )).format(price);
    }

    private static String getFormattedProductPrintString29(String quantityString, String productName, double price){

        String str =  quantityString + " " + productName;

        if (str.length() > 19)
            str = str.substring(0, 19);

        int numSpaces = 19 - str.length();

        String priceString = formatPrice2Decimal(price);


        String finalString = str + "                  ".substring(0, numSpaces) + "  " +    "            ".substring(0,8-priceString.length()) + priceString;

        return  finalString;

    }

    public static boolean printBill(OrderVo order, Activity activity) {

        M.log("Util","Enter printBill");

        Bluetooth_Printer_2inch_ThermalAPI printer = new Bluetooth_Printer_2inch_ThermalAPI();

        String printData = "";
        printData += printer.font_Courier_29("          Bistro 37") + printer.carriage_Return();
        printData += printer.font_Courier_29("          Shop No.11") + printer.carriage_Return();
        printData += printer.font_Courier_29("Godavari Complex,Arun Vihar") + printer.carriage_Return();
        printData += printer.font_Courier_29("       Sector 37, Noida") + printer.carriage_Return();
        printData += printer.font_Courier_29("   Ph. No. : 011 33105995") + printer.carriage_Return();
        printData += printer.font_Courier_29("   Tin No. : 29110326308") + printer.carriage_Return();
        printData += printer.font_Courier_29("-----------------------------");

        printData += printer.font_Courier_20("   Order No : " + order.getCustomerOrderId()) + printer.carriage_Return() + printer.carriage_Return();

        String date = new SimpleDateFormat("dd/MM/yyyy").format(order.getOrderTime());
        String time = new SimpleDateFormat("hh:mm a").format(order.getOrderTime());
        printData += printer.font_Courier_29(date + "         " + time) + printer.carriage_Return();

        printData += printer.font_Courier_29("-----------------------------");

        if (order.getOrderProducts() == null || order.getOrderProducts().isEmpty()) return  true;
        for (OrderProductVo orderProductVo : order.getOrderProducts()) {

            printData += printer.font_Courier_29(getFormattedProductPrintString29(orderProductVo.getCount()+"",
                    orderProductVo.getProduct().getName(), orderProductVo.getProduct().getPrice().doubleValue() * orderProductVo.getCount()));

            if (orderProductVo.getOrderProductAddons() == null) continue;
            for(OrderProductAddonVo addon : orderProductVo.getOrderProductAddons()){
                printData += printer.font_Courier_29(getFormattedProductPrintString29(orderProductVo.getCount()+"",
                        addon.getAddOn().getName(), addon.getAddOn().getPrice()));
            }

        }

        printData += printer.carriage_Return();
        printData += printer.font_Courier_29(getFormattedProductPrintString29(" ","Total", order.getTotalPrice()));
        printData += printer.font_Courier_29("-----------------------------");

        printData += printer.font_Courier_29("Thank you for dining with us ") + printer.carriage_Return();


        printData += printer.font_Courier_24("***Powered by Hopline***") + printer.carriage_Return();
        printData += printer.carriage_Return() + printer.carriage_Return() + printer.carriage_Return() +  printer.carriage_Return();

        if (PrinterHelper.get().print(printData)) {
            return true;
        } else {
        }


        M.log("Util","Retrying connection to printer");
        if (PrinterHelper.get().canConnectToPrinter()){
            M.log("Util","Printer can be connected");
            new PrinterConnector(activity).execute("");
            return PrinterHelper.get().print(printData);
        } else {
            M.log("Util","Printer cannot be connected");
            Toast.makeText(activity, "Printer connection FAILED! MAKE SURE BLUETOOTH IS TURNED ON AND CONNECTED TO PRINTER", Toast.LENGTH_LONG).show();
            return  false;
        }


    }
}
