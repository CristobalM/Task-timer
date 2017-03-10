package com.example.cristobalm.myapplication.Services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.MainActivity;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/3/17.
 */

public class TimingNotifications {
    Context context;
    NotificationManager notificationManager;
    public TimingNotifications(Context context){
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
    }

    private Notification createNotification (Class target_activity_class, String text_notif, int priority){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_clocklogo_more_contrast )
                .setContentTitle("Task Timing")
                .setContentText(text_notif)
                .setPriority(priority)
                //.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000);

        Intent targetIntent = new Intent(context, target_activity_class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        return mBuilder.build();
    }

    public void sendNotification(int nID, Class target_activity_class, String text_notif, int notification_sound, int priority){
        Notification notification = createNotification(target_activity_class, text_notif, priority);
        notification.defaults = Notification.DEFAULT_LIGHTS;

        if(notification_sound > -1) {
            notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + notification_sound);
        }
        notificationManager.notify(nID, notification);
    }

    public void stopAllNotifications(){
        notificationManager.cancelAll();
    }
    public void stopNotification(int nID){
        notificationManager.cancel(nID);
    }

    public void sendTimeNotification(int nID, String description, int millis, int notification_sound){
        String text = "On " + description +
                ", remaining time: " + TimeContainer.getTimeString(millis);
        sendNotification(nID, MainActivity.class, text, notification_sound, Notification.PRIORITY_DEFAULT);
    }


}
