package com.example.ssairam.hopline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;

import java.io.IOException;

/**
 * Created by root on 19/11/16.
 */

public class PrinterHelper {
    private static final boolean DISABLE_PRINTER = false;

    private static PrinterHelper printerHelper;

    AnalogicsThermalPrinter conn = new AnalogicsThermalPrinter();

    private PrinterHelper() {

    }

    public static PrinterHelper get() {
        if (printerHelper == null) {
            printerHelper = new PrinterHelper();
        }
        return printerHelper;
    }
//    PrinterHelper.get().print("        Bistro 37\n        Shop No.11\nGodavari Complex,Arun Vihar\n     Sector 37, Noida\n   Ph. No. : 011 33105995\n________________________\nQty   Item          Amounthello world");

    public void reFeed() {
        Bluetooth_Printer_2inch_ThermalAPI printer = new Bluetooth_Printer_2inch_ThermalAPI();

        String feeddata = "";
        feeddata = printer.variable_Size_Reverse_Line_Feed(150);

        conn.printData(feeddata);

    }

    public boolean isBluetoothOn() {
        if (DISABLE_PRINTER) return true;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return !(bluetoothAdapter == null || !bluetoothAdapter.isEnabled() || bluetoothAdapter.isDiscovering());
    }

    public boolean connectToPrinter() {
        if (DISABLE_PRINTER) return true;

        boolean result;
        if (conn == null)
            conn = new AnalogicsThermalPrinter();

        try {
            conn.closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn.openBT("00:04:3E:93:41:13");
            Log.d("tag","hogya connect");
            return true;
        } catch (Exception ex) {
            Log.d("tag","Exception aa gya");
            ex.printStackTrace();
            return false;
        }

//        return result;
    }


    public boolean print(String printerFormattedData) {
        if (DISABLE_PRINTER) return true;

//        Bluetooth_Printer_2inch_ThermalAPI printer = new Bluetooth_Printer_2inch_ThermalAPI();

//        String printdata = "";
//        printdata = printer.font_Courier_10(data);
//        printdata += printer.font_Courier_19(data);
//        printdata += printer.font_Courier_20(data);
//        printdata += printer.font_Courier_24(data);
//        printdata += printer.font_Courier_25(data);
//        printdata += printer.font_Courier_29(data);
//        printdata += printer.font_Courier_32(data);
//        printdata += printer.font_Courier_34(data);
//        printdata += printer.font_Courier_38(data);
//        printdata += printer.font_Courier_42(data);
//        printdata += printer.font_Courier_48(data);
//
        if (conn.printData(printerFormattedData)) {
            M.log("PrinterHelper", "print success");
            return  true;
        }


        if (connectToPrinter() && conn.printData(printerFormattedData)) {
            M.log("PrinterHelper", "print success");
            return  true;
        } else {
            M.log("PrinterHelper", "print failed!");
            return false;
        }





    }

}




