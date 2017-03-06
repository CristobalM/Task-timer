package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.Globals.ButtonNameGlobals;

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
        if(caller_instance.getTime_fields().size() <= 0){
            return;
        }
        Intent intent = new Intent(context, TimingService.class);
        caller_instance.mService.stopTimer();
        context.stopService(intent);
        if(!caller_instance.isEnabled_inputs()) {
            caller_instance.enableInputs();
        }
        if(caller_instance.current_countdown != null) {
            caller_instance.current_countdown.stopCountDown();
        }
    }
    private void onClickButtonPlay(){
        if(caller_instance.getTime_fields().size() <= 0){
            return;
        }

        ArrayList<Integer> millisecondsList = caller_instance.getMillisecondsList();
        Intent intent = new Intent(context, TimingService.class);
        intent.putIntegerArrayListExtra(InfoNameGlobals.REPEAT_TIME_LIST, millisecondsList);
        intent.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.START_TIMING);
        if(caller_instance.isEnabled_inputs()) {
            caller_instance.blockInputs();
        }
        context.startService(intent);
        caller_instance.mService.startTimer();

        //TextView target_view = (TextView) caller_instance.findViewById(R.id.time_show);
        //caller_instance.current_countdown = caller_instance.createTimeCountDown(target_view, caller_instance.mService.getMillisecondsRemaining());

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
