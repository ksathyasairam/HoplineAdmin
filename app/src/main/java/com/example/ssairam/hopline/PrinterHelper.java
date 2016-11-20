package com.example.ssairam.hopline;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
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

    public boolean canConnectToPrinter(Activity activity) {
        Context context = activity.getBaseContext();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(context, "Bluetooth is NOT Enabled",
                    Toast.LENGTH_LONG).show();
            return false;
        }


        if (bluetoothAdapter.isDiscovering()) {
            Toast.makeText(
                    context,
                    "Bluetooth is currently in device discovery process.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return  true;
    }

    public boolean connectToPrinter() {
        try {
            conn.openBT("00:04:3E:93:41:13");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean print(String data) {
        Bluetooth_Printer_2inch_ThermalAPI printer = new Bluetooth_Printer_2inch_ThermalAPI();

        String printdata = "";
//        printdata = printer.font_Courier_10(data);
//        printdata += printer.font_Courier_19(data);
//        printdata += printer.font_Courier_20(data);
//        printdata += printer.font_Courier_24(data);
//        printdata += printer.font_Courier_25(data);
        printdata += printer.font_Courier_29(data);
//        printdata += printer.font_Courier_32(data);
//        printdata += printer.font_Courier_34(data);
//        printdata += printer.font_Courier_38(data);
//        printdata += printer.font_Courier_42(data);
//        printdata += printer.font_Courier_48(data);
//
        try {
            return conn.printData(printdata);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
