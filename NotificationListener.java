package com.daat.productivity;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

@SuppressLint("OverrideAbstract")
public class NotificationListener  extends NotificationListenerService {

    public static String TAG = NotificationListener.class.getSimpleName();

    @Override
    public void onNotificationPosted(StatusBarNotification sbm) {

        /**
         * This condition will define how you will identify your test is running
         * You can have an in memory variable regarding it, or persistant variable,
         * Or you can use Settings to store current state.
         * You can have your own approach
         */
        boolean condition = true; // say your test is running

        if(condition) {
            cancelAllNotifications(); //Cancel all the notifications . No Disturbance
        }

        //else
        // nothing.
    }
}