package com.example.ssairam.hopline;

/**
 * Created by root on 30/12/16.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.ssairam.hopline.activity_ui.MainActivity;
import com.example.ssairam.hopline.vo.OrderVo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        sendNotification("Order waiting for your conformation!");


        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        refreshIncommingOrder();

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 432 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificationsound);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_hopline)
                .setContentTitle("HOPLINE")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(432 /* ID of notification */, notificationBuilder.build());
    }


    private void refreshIncommingOrder() {
        new Thread() {
            public void run() {
                try {

                    List<OrderVo> offlineOrdersServerLog = MainPrefs.getOfflineOrdersForServerLog(getBaseContext());
                    if (offlineOrdersServerLog != null && !offlineOrdersServerLog.isEmpty()) {
                        boolean res = ServerHelper.logCompleteOfflineOrder(offlineOrdersServerLog);
                        if (res)
                            MainPrefs.saveOfflineOrderForServerLog(new ArrayList<OrderVo>(), getBaseContext());
                    }

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

                        DataStore.setIncomingOrders(serverBackup);
                        Intent intent = new Intent("newIncomingOrder");
                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                    }

                    M.log(TAG, "Refresh done, sleeeping for 10 secs");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

//    public void sendNotification() {
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_dot)
//                        .setContentTitle("New Order")
//                        .setContentText("Order waiting for your conformation!");
//// Creates an explicit intent for an Activity in your app
//        Intent resultIntent = new Intent(this, MainActivity.class);
//
//// The stack builder object will contain an artificial back stack for the
//// started Activity.
//// This ensures that navigating backward from the Activity leads out of
//// your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//// Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MainActivity.class);
//// Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
//
////        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificationsound);
////        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
//
//
//        mBuilder.setSound(alarmSound);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//// mId allows you to update the notification later on.
//        mNotificationManager.notify(2351, mBuilder.build());
//        M.log(TAG, "Notification sent");
//    }


}