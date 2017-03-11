package com.example.cristobalm.myapplication.Services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.FilenameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.Storage.StateStorage;
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
    private ArrayList<Timefield> time_fields;
    private int current_timer_index;
    AlarmManager alarmManager;
    TimingNotifications timingNotifications;
    private int notification_id;
    private long nextMillis = 0;
    private MainActivity main_activity;
    StateStorage stateStorage;
    StateStorage configStates;
    ServiceCountdown serviceCountdown;

    private Boolean repeatState;

    private long last_remaining_millis;

    private final IBinder mBinder = new LocalBinder();

    private Integer totalSeconds;

    public long getLastRemainingMillis(){
        return last_remaining_millis;
    }
    public void setLastRemainingMillis(long _millis){
        last_remaining_millis = _millis;
    }


    public int getTotalSeconds(){
        if(totalSeconds == null){
            totalSeconds = 0;
            for(int i = 0; i < time_fields.size(); i++){
                totalSeconds += time_fields.get(i).getMilliseconds()/1000;
            }
        }
        return totalSeconds;
    }
    public void addSecondsToTotal(int seconds){
        getTotalSeconds();
        totalSeconds += seconds;
    }

    public StateStorage getStateStorage(){
        if(stateStorage == null) {
            stateStorage = new StateStorage(getApplicationContext(), FilenameGlobals.STORED_STATES);
        }
        return stateStorage;
    }
    public StateStorage getConfigStates(){
        if(configStates == null) {
            configStates = new StateStorage(getApplicationContext(), FilenameGlobals.CONFIG_STATES);
        }
        return configStates;
    }

    public ArrayList<Timefield> retrieveTimefields(){
        if(time_fields == null) {
            time_fields = getStateStorage().getTimeFieldsList(StateGlobals.SAVE_STATE);
        }
        return time_fields;
    }

    public boolean getRepeatState(){
        if(repeatState==null){
            repeatState = getConfigStates().getRepeatState();
        }
        return repeatState;
    }
    public void saveRepeatState(boolean _repeatState){
        repeatState = _repeatState;
    }
    public void saveRepeatState(){
        getConfigStates().saveRepeatState(repeatState);
    }


    public int getNotification_id(){
        return notification_id;
    }

    public TimingNotifications getTimingNotifications(){
        return timingNotifications;
    }

    public void setActivityInstance(MainActivity mainActivity){
        this.main_activity = mainActivity;
    }

    public int getMainState(){
        return current_state;
    }
    public void setMainState(int state){
        current_state = state;
    }



    @Override
    public IBinder onBind(Intent intent){

        current_state = MainStateGlobals.STATE_IDLE;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(timingNotifications == null) {
            timingNotifications = new TimingNotifications(this);
        }
        if(serviceCountdown == null){
            serviceCountdown = new ServiceCountdown();
        }
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
        if(timingNotifications != null) {
            timingNotifications.stopNotification(notification_id);
        }
        //stopTimer();
        saveRepeatState();
        Log.d("onDestroy", "destroyed service!!!!!!!!");
        super.onDestroy();
    }

    public class LocalBinder extends Binder{
        public TimingService getService(){
            return TimingService.this;
        }
    }




    public void pauseTimer(){
        setLastRemainingMillis(getMillisecondsRemaining());
        Intent intent =  new Intent(this, TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        serviceCountdown.stopCountDown();
        setMainState(MainStateGlobals.STATE_PAUSED);
    }
    public void stopTimer(){
        Intent intent =  new Intent(this, TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        current_timer_index = 0;
        alarmManager.cancel(pendingIntent);
        serviceCountdown.stopCountDown();
        setMainState(MainStateGlobals.STATE_IDLE);
        if(timingNotifications != null) {
            timingNotifications.stopNotification(notification_id);
        }
        setMainState(MainStateGlobals.STATE_IDLE);
        if (main_activity != null) {
            main_activity.stopTimer();
        }
    }

    public void startTimer(){
        current_timer_index = 0;
        setMainState(MainStateGlobals.STATE_RUNNING);

        notification_id = 0;

        //timingNotifications.sendNotification(1, MainActivity.class,
        //        "Continuing on iteration #"+current_timer_index+
        //                ". Total time: " + getTimeString(), -1, Notification.PRIORITY_HIGH);
        timerScheduling(-1);
    }

    public void unPauseTimer(){
        setMainState(MainStateGlobals.STATE_RUNNING);

        timerScheduling(getLastRemainingMillis());
    }


    private void continueTimer(){
        Log.d("continueTimer", "called continue timer! current_timer_index:" + current_timer_index);
        current_timer_index++;
        if(current_timer_index >= time_fields.size()){
            // stop timer
            current_timer_index = 0;
            if(repeatState){
                timingNotifications.sendNotification(1,
                        MainActivity.class,
                        "Finished all countdowns.. Repeating", InfoNameGlobals.NOTIFICATION_ONE, Notification.PRIORITY_HIGH);

                timerScheduling(-1);
            }else{
                timingNotifications.sendNotification(1,
                        MainActivity.class,
                        "Finished all countdowns", InfoNameGlobals.NOTIFICATION_ONE, Notification.PRIORITY_HIGH);
                stopTimer();

            }
        }
        else{

            // continue next iteration
            timingNotifications.sendNotification(0, MainActivity.class,
                    "Continuing on task #" + current_timer_index + ". " + time_fields.get(current_timer_index).getCustomText() +
                            ". Total time: " + getTimeString(), InfoNameGlobals.NOTIFICATION_ONE, Notification.PRIORITY_HIGH);
            timerScheduling(-1);
        }
    }

    private void timerScheduling(long from_pause_millis){
        long to_add;
        if(from_pause_millis > -1){
            to_add = from_pause_millis;
        }
        else{
            to_add = time_fields.get(current_timer_index).getMilliseconds();
        }
        Intent intent = new Intent(getApplicationContext(), TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        nextMillis = System.currentTimeMillis() + to_add;

        alarmManager.set(AlarmManager.RTC_WAKEUP, nextMillis, pendingIntent);
        serviceCountdown.startNewCountDown(time_fields.get(current_timer_index), this);
    }

    private String getTimeString(){
        if(time_fields != null && time_fields.size() > current_timer_index) {
            return TimeContainer.getTimeString(time_fields.get(current_timer_index).getMilliseconds());
        }
        else{
            return "--:--:--";
        }
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
