package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

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
    private MainActivity main_activity;
    ButtonAction(String button_name, MainActivity caller_instance){
        this.button_name = button_name;
        this.main_activity = caller_instance;
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
        if(main_activity.getTime_fields().size() <= 0){
            return;
        }
        Intent intent = new Intent(context, TimingService.class);
        main_activity.mService.stopTimer();
        context.stopService(intent);
        if(!main_activity.isEnabled_inputs()) {
            main_activity.enableInputs();
        }
        if(main_activity.current_countdown != null) {
            main_activity.current_countdown.stopCountDown();
        }
    }
    private void onClickButtonPlay(){
        if(main_activity.getTime_fields().size() <= 0){
            return;
        }

        ArrayList<Integer> millisecondsList = main_activity.getMillisecondsList();
        Intent intent = new Intent(context, TimingService.class);
        intent.putIntegerArrayListExtra(InfoNameGlobals.REPEAT_TIME_LIST, millisecondsList);
        intent.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.START_TIMING);
        if(main_activity.isEnabled_inputs()) {
            main_activity.blockInputs();
        }
        context.startService(intent);
        main_activity.mService.startTimer();
    }
    private void onClickButtonPause(){

    }
    private void onClickButtonAdd(){

        ArrayList<Timefield> time_fields = main_activity.getTime_fields();
        LinearLayout et_list = main_activity.getEt_list();


        Timefield time_f_single = new Timefield(context, time_fields.size(), main_activity);


        time_fields.add(time_f_single);
        et_list.addView(time_f_single.getLayout());
    }




}
