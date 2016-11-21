package com.example.ssairam.hopline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;

import java.io.IOException;

/**
 * Created by root on 19/11/16.
 */

public class PrinterHelper {

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

    public boolean canConnectToPrinter() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return !(bluetoothAdapter == null || !bluetoothAdapter.isEnabled() || bluetoothAdapter.isDiscovering());
    }

    public boolean connectToPrinter() {
        if (conn == null)
            conn = new AnalogicsThermalPrinter();

        try {
            conn.closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn.openBT("00:04:3E:93:41:13");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean print(String printerFormattedData) {
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
        try {
            return conn.printData(printerFormattedData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}




