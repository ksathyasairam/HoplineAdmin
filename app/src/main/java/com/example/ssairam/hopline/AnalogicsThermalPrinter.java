//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.ssairam.hopline;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@SuppressLint({"NewApi"})
public class AnalogicsThermalPrinter {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer = null;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    public AnalogicsThermalPrinter() {
    }

    public void openBT(String address) throws IOException {
        this.findBT(address);

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            this.mmSocket = this.mmDevice.createRfcommSocketToServiceRecord(uuid);
            this.mmSocket.connect();
            this.mmOutputStream = this.mmSocket.getOutputStream();
            this.mmInputStream = this.mmSocket.getInputStream();
            this.beginListenForData();

    }

    public void findBT(String address) {
        try {
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(!this.mBluetoothAdapter.isEnabled()) {
                this.mBluetoothAdapter.enable();
            }

            Set e = this.mBluetoothAdapter.getBondedDevices();
            BluetoothDevice device1;
            if(e.size() > 0) {
                Iterator var4 = e.iterator();

                while(var4.hasNext()) {
                    device1 = (BluetoothDevice)var4.next();
                    if(device1.getAddress().equals(address)) {
                        this.mmDevice = device1;
                        break;
                    }
                }
            }

            device1 = null;
            device1 = this.mBluetoothAdapter.getRemoteDevice(address);
            this.mmDevice = device1;
            this.mmDevice.createBond();
        } catch (NullPointerException var5) {
            var5.printStackTrace();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void closeBT() throws IOException {
        try {
            this.mmOutputStream.close();
            this.mmInputStream.close();
            this.mmSocket.close();
        } catch (NullPointerException var2) {
            var2.printStackTrace();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    void beginListenForData() {
        try {
            final Handler e = new Handler();
            boolean delimiter = true;
            this.stopWorker = false;
            this.readBufferPosition = 0;
            this.readBuffer = new byte[1024];
            this.workerThread = new Thread(new Runnable() {
                public void run() {
                    while(!Thread.currentThread().isInterrupted() && !AnalogicsThermalPrinter.this.stopWorker) {
                        try {
                            int ex = AnalogicsThermalPrinter.this.mmInputStream.available();
                            if(ex > 0) {
                                byte[] packetBytes = new byte[ex];
                                AnalogicsThermalPrinter.this.mmInputStream.read(packetBytes);

                                for(int i = 0; i < ex; ++i) {
                                    byte b = packetBytes[i];
                                    if(b == 10) {
                                        byte[] encodedBytes = new byte[AnalogicsThermalPrinter.this.readBufferPosition];
                                        System.arraycopy(AnalogicsThermalPrinter.this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        new String(encodedBytes, "US-ASCII");
                                        AnalogicsThermalPrinter.this.readBufferPosition = 0;
                                        e.post(new Runnable() {
                                            public void run() {
                                            }
                                        });
                                    } else {
                                        AnalogicsThermalPrinter.this.readBuffer[AnalogicsThermalPrinter.this.readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException var7) {
                            AnalogicsThermalPrinter.this.stopWorker = true;
                        }
                    }

                }
            });
            this.workerThread.start();
        } catch (NullPointerException var3) {
            var3.printStackTrace();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public boolean printData(byte[] msg) {
        boolean flag = false;

        try {
            this.mmOutputStream.write(msg);
            flag = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return flag;
    }

    public boolean printData(String msg){
        try {
            this.mmOutputStream.write(msg.getBytes());
            this.mmOutputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
