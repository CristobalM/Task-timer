package com.example.cristobalm.myapplication.Services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/2/17.
 * test
 */

public class TimingService extends Service {
    private int mStartMode;
    private int current_state = MainStateGlobals.STATE_IDLE;
    private ArrayList<Integer> times;
    private ArrayList<Timefield> time_fields;
    private int current_timer_index;
    AlarmManager alarmManager;
    TimingNotifications timingNotifications;
    private int notification_id;
    private long nextMillis = 0;
    private MainActivity main_activity;

    private final IBinder mBinder = new LocalBinder();

    public void setActivityInstance(MainActivity mainActivity){
        this.main_activity = mainActivity;
    }

    public int getMainState(){
        return current_state;
    }
    public void setMainState(int state){
        current_state = state;
    }

    public void setTimeList(ArrayList<Timefield> times){
        this.time_fields = times;
    }
    private void reloadTimes(){
        times = new ArrayList<>();
        for(int i = 0; i < time_fields.size(); i++){
            times.add(time_fields.get(i).getMilliseconds());
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        current_state = MainStateGlobals.STATE_IDLE;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String action = intent.getStringExtra(InfoNameGlobals.ACTION);
        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        }

        if(action.equals(InfoNameGlobals.START_TIMING)) {
        }
        else if(action.equals(InfoNameGlobals.STOP_TIMING)){
        }
        else if(action.equals(InfoNameGlobals.PAUSE_TIMING)){

        }
        else if(action.equals(InfoNameGlobals.CONTINUE_TIMING)){
            Log.d("onStartCommand", "Action is CONTINUE_TIMING!!");
            continueTimer();
        }
        return mStartMode;
    }
    @Override
    public void onDestroy(){
        //timingNotifications.stopNotification(notification_id);
        //stopTimer();
        super.onDestroy();
    }

    public class LocalBinder extends Binder{
        public TimingService getService(){
            return TimingService.this;
        }
    }

    public void stopTimer(){
        Intent intent =  new Intent(this, TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        current_timer_index = 0;
        alarmManager.cancel(pendingIntent);
        setMainState(MainStateGlobals.STATE_IDLE);
    }

    public void startTimer(){
        reloadTimes();
        current_timer_index = 0;
        setMainState(MainStateGlobals.STATE_RUNNING);

        //notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        timingNotifications = new TimingNotifications(this);
        notification_id = 0;
        timingNotifications.sendNotification(notification_id, MainActivity.class,
                "Continuing on iteration #"+current_timer_index+
                        ". Total time: " + getTimeString());
        timerScheduling();
    }

    public void pauseTimer(){
        setMainState(MainStateGlobals.STATE_PAUSED);
    }
    public void unPauseTimer(){
        setMainState(MainStateGlobals.STATE_RUNNING);
    }


    private void continueTimer(){
        Log.d("continueTimer", "called continue timer! current_timer_index:" + current_timer_index);
        current_timer_index++;
        if(current_timer_index >= times.size()){
            // stop timer
            timingNotifications.sendNotification(notification_id, MainActivity.class, "Finished all " + current_timer_index + " timing iterations");
            current_timer_index = 0;
            stopTimer();

        }else {

            // continue next iteration
            timingNotifications.sendNotification(notification_id, MainActivity.class,
                    "Continuing on iteration #" + current_timer_index +
                            ". Total time: " + getTimeString());
            timerScheduling();
        }
    }

    private void timerScheduling(){
        Log.d("timerScheduling", "called timerScheduling!, time to do:"+times.get(current_timer_index));
        Intent intent = new Intent(getApplicationContext(), TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        nextMillis = System.currentTimeMillis() + (long) times.get(current_timer_index);
        if(main_activity != null){
            main_activity.checkForCountdown();
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextMillis, pendingIntent);
    }

    private String getTimeString(){
        if(times != null && times.size() > current_timer_index) {
            TimeContainer time_container = new TimeContainer(times.get(current_timer_index));
            return time_container.getTimeString();
        }
        else{
            return "--:--:--";
        }
    }
    public TimeContainer getTimeContainer(){
        return new TimeContainer(times.get(current_timer_index));
    }
    public int getMillisecondsRemaining(){
        long currentmilis = System.currentTimeMillis();
        if(nextMillis > currentmilis){
            return (int) (nextMillis - currentmilis);
        }else{
            return 0;
        }
    }

    public int getCurrent_timer_index(){
        return current_timer_index;
    }

}
