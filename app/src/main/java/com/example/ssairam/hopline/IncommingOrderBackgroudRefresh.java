package com.example.ssairam.hopline;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.ssairam.hopline.activity_ui.MainActivity;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 20/10/16.
 */

public class IncommingOrderBackgroudRefresh extends Service {
    private static volatile boolean locallyUpdated;
    private static final String TAG = "IncommingOrderBackgroundRefresh";


    public static void setAlarm(Context context) {
        Intent intent = new Intent(context, IncommingOrderBackgroudRefresh.class);
        PendingIntent operation = PendingIntent.getService(context, 2423,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);


        AlarmManager am = ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
        am.cancel(operation);


        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 1000,
                AlarmManager.INTERVAL_HALF_HOUR, operation);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        M.log(TAG, "onCreate start");


        Intent notificationIntent = new Intent(this, IncommingOrderBackgroudRefresh.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Receiving Orders")
                .setSmallIcon(R.drawable.ic_hopline)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(562, notification);

        M.log(TAG, "onCreate end");
        startIncomingOrdersRefresh();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        M.log(TAG, "onStartComnand");
        return START_STICKY;
    }


    private void startIncomingOrdersRefresh() {


        new Thread() {
            public void run() {

                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        "MyWakelockTaggggg");
                wakeLock.acquire();


                M.log(TAG, "New thread started");
                while (true) {

                    try {
                        Thread.sleep(10000);

                        List<OrderVo> offlineOrdersServerLog = MainPrefs.getOfflineOrdersForServerLog(getBaseContext());
                        if (offlineOrdersServerLog != null && !offlineOrdersServerLog.isEmpty()) {
                            boolean res = ServerHelper.logCompleteOfflineOrder(offlineOrdersServerLog);
                            if (res)
                                MainPrefs.saveOfflineOrderForServerLog(new ArrayList<OrderVo>(), getBaseContext());
                        }

                        setLocallyUpdated(false);
                        if (DataStore.getIncomingOrders() == null) {
                            M.log(TAG, "Local data null!! going to refresh everyting");
                            DataStore.loadEverythingFromServer(getApplicationContext());
                        }

                        M.log(TAG, "Going to refresh Conformation List");
                        List<OrderVo> serverOrders = ServerHelper.retrieveIncomingOrders(getApplicationContext());

                        List<OrderVo> localOrders = DataStore.getIncomingOrders();


                        List<OrderVo> serverBackup = new ArrayList<OrderVo>(serverOrders);

                        serverOrders.removeAll(localOrders);

                        if (!serverOrders.isEmpty()) {
                            M.log(TAG, "New item added on server");

                            if (isLocallyUpdated()) continue;
                            DataStore.setIncomingOrders(serverBackup);
                            Intent intent = new Intent("newIncomingOrder");
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                            sendNotification();
                        }

                        M.log(TAG, "Refresh done, sleeeping for 10 secs");

                    } catch (Exception e) {
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

//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificationsound);
//        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;


        mBuilder.setSound(alarmSound);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(2351, mBuilder.build());
        M.log(TAG, "Notification sent");
    }


    @Override
    public void onDestroy() {
        M.log(TAG, "destroyed");
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        M.log(TAG, "low memory");
        super.onLowMemory();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        M.log(TAG, "unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        M.log(TAG, "rebind");
        super.onRebind(intent);
    }
}
