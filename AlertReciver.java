package com.daat.productivity;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DashboardActivity.removeDnd(context);
        DashboardActivity.alarmOn = false;
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification("Alert","Alarm");
        notificationHelper.getManager().notify(1,nb.build());
    }
}
