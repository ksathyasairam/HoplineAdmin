package com.example.ssairam.hopline;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.ssairam.hopline.vo.OrderVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 20/10/16.
 */

public class IncommingOrderBackgroudRefresh extends Service {
    private static volatile boolean locallyUpdated;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("servcie", "enterned onstart command");
        startIncomingOrdersRefresh();
        return START_STICKY;
    }


    private void startIncomingOrdersRefresh() {

        new Thread() {
            public void run() {


                while (true) {

                    try {
                        Thread.sleep(10000);

                        setLocallyUpdated(false);
                        if (DataStore.getIncomingOrders() == null) {
                            Log.d("servcie", "Local data null!! going to refresh everyting");
                            DataStore.loadEverythingFromServer();
                        }

                        Log.d("servcie", "Going to refresh Conformation List");
                        List<OrderVo> serverOrders = ServerHelper.retrieveIncomingOrders();

                        List<OrderVo> localOrders = DataStore.getIncomingOrders();


                        List<OrderVo> serverBackup = new ArrayList<OrderVo>(serverOrders);

                        serverOrders.removeAll(localOrders);

                        if (!serverOrders.isEmpty()) {
                            Log.d("servcie", "New item added on server");

                            if(isLocallyUpdated()) continue;
                            DataStore.setIncomingOrders(serverBackup);
                            Intent intent = new Intent("newIncomingOrder");
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                            sendNotification();
                        }

                        Log.d("servcie", "Refresh done, sleeeping for 10 secs");

                    }  catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }.start();
    }

    public static synchronized boolean isLocallyUpdated() {
        return locallyUpdated;
    }

    public static synchronized void setLocallyUpdated(boolean locallyUpdated) {
        IncommingOrderBackgroudRefresh.locallyUpdated = locallyUpdated;
    }





    public void sendNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_dot)
                        .setContentTitle("New Order")
                        .setContentText("Order waiting for your conformation!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(2351, mBuilder.build());
    }



















}
