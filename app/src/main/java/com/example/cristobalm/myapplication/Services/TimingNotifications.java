package com.example.cristobalm.myapplication.Services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

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

    private Notification createNotification (Class target_activity_class, String text_notif){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_timer_black_24px)
                .setContentTitle("Great Timer")
                .setContentText(text_notif);

        Intent targetIntent = new Intent(context, target_activity_class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        return mBuilder.build();
    }

    public void sendNotification(int nID, Class target_activity_class, String text_notif){
        Notification notification = createNotification(target_activity_class, text_notif);
        notificationManager.notify(nID, notification);
    }

    public void stopAllNotifications(){
        notificationManager.cancelAll();
    }
    public void stopNotification(int nID){
        notificationManager.cancel(nID);
    }

}
