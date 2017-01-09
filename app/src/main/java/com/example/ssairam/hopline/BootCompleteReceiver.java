package com.example.ssairam.hopline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by root on 9/1/17.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    static final String TAG = "BootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        M.log(TAG, "Received");
        IncommingOrderBackgroudRefresh.setAlarm(context);
    }
}