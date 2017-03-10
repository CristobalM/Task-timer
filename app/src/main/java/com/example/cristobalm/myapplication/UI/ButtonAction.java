package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.Globals.ButtonNameGlobals;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;

import java.util.ArrayList;


/**
 * Created by cristobalm on 2/28/17.
 * Behaviour for main_activity.xml buttons
 */

class ButtonAction {
    private String button_name;
    private Context context;
    private MainActivity main_activity;
    private ImageView button;
    ScrollView scrollView;
    ButtonAction(String button_name, MainActivity caller_instance, ImageView button){
        this.button_name = button_name;
        this.main_activity = caller_instance;
        this.button = button;
        scrollView = (ScrollView)  main_activity.findViewById(R.id.ScrollView);

        switch (button_name){
            case ButtonNameGlobals.BUTTON_PLAY:
                if(main_activity.getState() == MainStateGlobals.STATE_RUNNING){
                    button.setVisibility(View.INVISIBLE);
                }
                break;
            case ButtonNameGlobals.BUTTON_PAUSE:
                if(main_activity.getState() == MainStateGlobals.STATE_RUNNING){
                    button.setVisibility(View.VISIBLE);
                }
                break;
            case ButtonNameGlobals.BUTTON_ADD:
                if(main_activity.getState() != MainStateGlobals.STATE_IDLE){
                    button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
                break;
        }


    }

    void setContext(Context context){
        this.context = context;
    }
    void Run(){
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
    void actionUp(){
        if(context == null){
            return;
        }
        switch (button_name){
            case ButtonNameGlobals.BUTTON_STOP:
                stopUp();
                break;
            case ButtonNameGlobals.BUTTON_PLAY:
                playUp();
                break;
            case ButtonNameGlobals.BUTTON_PAUSE:
                pauseUp();
                break;
            case ButtonNameGlobals.BUTTON_ADD:
                addUp();
                break;
        }
    }

    public void stopUp(){
        button.getBackground().clearColorFilter();

    }
    public void playUp(){
        button.getBackground().clearColorFilter();
    }
    public void pauseUp(){
        button.getBackground().clearColorFilter();
    }
    public void addUp(){
        if(main_activity.getState() == MainStateGlobals.STATE_IDLE){
            button.getBackground().clearColorFilter();
        }
    }

    private void onClickButtonStop(){
        if(main_activity.getTime_fields().size() <= 0){
            return;
        }
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        main_activity.stopTimer();

        main_activity.getState();


    }
    private void onClickButtonPlay(){
        if(main_activity.getState() == MainStateGlobals.STATE_RUNNING || main_activity.getTime_fields().size() <= 0){
            return;
        }

        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        switch (main_activity.getState()){
            case MainStateGlobals.STATE_PAUSED:
                main_activity.mService.unPauseTimer();
                break;
            case MainStateGlobals.STATE_IDLE:
                ArrayList<Integer> millisecondsList = main_activity.getMillisecondsList();
                Intent intent = new Intent(context, TimingService.class);
                intent.putIntegerArrayListExtra(InfoNameGlobals.REPEAT_TIME_LIST, millisecondsList);
                intent.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.START_TIMING);
                context.startService(intent);
                main_activity.mService.startTimer();
        }

        main_activity.blockInputs();
        main_activity.buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PAUSE)).setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);
        main_activity.getState();
    }
    private void onClickButtonPause(){
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        main_activity.buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY)).setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);

        main_activity.pauseTimer();
        main_activity.getState();

    }
    private void onClickButtonAdd(){
        if(main_activity.getState() != MainStateGlobals.STATE_IDLE){
            return;
        }
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);


        ArrayList<Timefield> time_fields = main_activity.getTime_fields();
        LinearLayout et_list = main_activity.getEt_list();


        Timefield time_f_single = new Timefield(context, time_fields.size(), main_activity);
        time_fields.add(time_f_single);

        int static_index = main_activity.map_timefields.size();

        main_activity.map_timefields.put(static_index,time_f_single);
        et_list.addView(time_f_single.getLayout());
        scrollView.fullScroll(scrollView.FOCUS_DOWN);

    }




}
