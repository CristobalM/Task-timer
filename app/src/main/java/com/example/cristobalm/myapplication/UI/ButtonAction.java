package com.example.cristobalm.myapplication.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.example.cristobalm.myapplication.UI.Globals.ButtonNameGlobals;
import com.example.cristobalm.myapplication.MyBroadcastReceiver;

import java.util.ArrayList;


/**
 * Created by cristobalm on 2/28/17.
 * Behaviour for main_activity.xml buttons
 */

class ButtonAction {
    private String button_name;
    private Context context;
    private MainActivity caller_instance;
    ButtonAction(String button_name, MainActivity caller_instance){
        this.button_name = button_name;
        this.caller_instance = caller_instance;
    }

    void setContext(Context context){
        this.context = context;
    }
    void run(){
        if(context == null){
            return;
        }
        switch (button_name){
            case ButtonNameGlobals.BUTTON_STOP:
                onClickButtonStop();
                break;
            case ButtonNameGlobals.BUTTON_PLAY:
                onClickButtonPlay();
                break;
            case ButtonNameGlobals.BUTTON_PAUSE:
                onClickButtonPause();
                break;
            case ButtonNameGlobals.BUTTON_ADD:
                onClickButtonAdd();
                break;
        }
    }
    private void onClickButtonStop(){
        //Toast.makeText(context, "Click on button " + button_name, Toast.LENGTH_SHORT).show();
    }
    private void onClickButtonPlay(){
        //Toast.makeText(context, "Click on button " + button_name, Toast.LENGTH_SHORT).show();
        ArrayList<Integer> minutes_list = caller_instance.getMinutesList();
        int firstval = 0;
        if(minutes_list.size()>0){
            firstval = minutes_list.get(0);
        }
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (firstval*1000 * 60), pendingIntent);
    }
    private void onClickButtonPause(){
        //Toast.makeText(context, "Click on button " + button_name, Toast.LENGTH_SHORT).show();

    }
    private void onClickButtonAdd(){
        //Toast.makeText(context, "Click on button " + button_name, Toast.LENGTH_SHORT).show();
        ArrayList<Timefield> time_fields = caller_instance.getTime_fields();
        LinearLayout et_list = caller_instance.getEt_list();


        Timefield time_f_single = new Timefield(context, time_fields.size(), caller_instance);


        time_fields.add(time_f_single);
        et_list.addView(time_f_single.getLayout());


        //Log.d("ButtonAction","Size of stuff is:" + String.valueOf(edit_texts.size()));
    }




}
