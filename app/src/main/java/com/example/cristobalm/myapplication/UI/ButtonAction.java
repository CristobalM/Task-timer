package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.cristobalm.myapplication.R;
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
    private ScrollView scrollView;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(scrollView != null) {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }
    };
    ButtonAction(String button_name, MainActivity caller_instance, ImageView button){
        this.button_name = button_name;
        this.main_activity = caller_instance;
        this.button = button;
        scrollView = main_activity.getScrollView();

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
                    button.getBackground().setColorFilter(ContextCompat.getColor(main_activity.getApplicationContext(), R.color.light_gray_a), PorterDuff.Mode.MULTIPLY);
                }
                break;
            case ButtonNameGlobals.BUTTON_REPEAT:
                if(main_activity.mService.getRepeatState()){
                    button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
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
            case ButtonNameGlobals.BUTTON_REPEAT:
                onClickButtonRepeat();
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

    private void stopUp(){
        //button.getBackground().clearColorFilter();
        button.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.light_gray_a), PorterDuff.Mode.MULTIPLY);

    }
    private void playUp(){
        if(main_activity.time_fields.size()>0) {
            button.getBackground().clearColorFilter();
        }
    }
    private void pauseUp(){
        button.getBackground().clearColorFilter();
    }
    private void addUp(){
        if(main_activity.getState() == MainStateGlobals.STATE_IDLE){
            button.getBackground().clearColorFilter();
        }
    }

    private void onClickButtonStop(){
        if(main_activity == null || main_activity.getState() == MainStateGlobals.STATE_IDLE || main_activity.getTime_fields().size() <= 0){
            return;
        }

        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        if(main_activity.mService!=null) {
            main_activity.mService.stopTimer();
        }
    }
    private void onClickButtonPlay(){
        if(main_activity != null){
            main_activity.playTimer();
        }
        if(main_activity.getState() == MainStateGlobals.STATE_RUNNING ||
                main_activity.getTime_fields().size() <= 0){
            return;
        }
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

    }
    private void onClickButtonPause(){
        if(main_activity == null || main_activity.mService == null){
            return;
        }
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        main_activity.pauseTimer();
        main_activity.getState();

    }
    private void onClickButtonAdd(){
        if(main_activity.getState() != MainStateGlobals.STATE_IDLE){
            return;
        }
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);


        ArrayList<Integer> time_fields = main_activity.getTime_fields();
        LinearLayout et_list = main_activity.getEt_list();


        Timefield time_f_single = new Timefield(context, time_fields.size(), main_activity);
        main_activity.addTimefield(time_f_single);

        int static_index = main_activity.map_timefields.size();

        main_activity.map_timefields.put(static_index,time_f_single);
        et_list.addView(time_f_single.getLayout());
        //scrollView.fullScroll(View.FOCUS_DOWN);
        //time_f_single.getLayout().requestFocus();
        scrollView.post(runnable);
        if(time_fields.size() > 0){
            main_activity.buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY)).getBackground().clearColorFilter();
        }

        if(main_activity != null && main_activity.mService != null){
            main_activity.mService.changeDone();
        }
    }
    private void onClickButtonRepeat(){
        //main_activity.reloadList();
        if(main_activity.mService == null){
            return;
        }
        boolean repeat = main_activity.mService.getRepeatState();
        if(repeat){
            button.getBackground().clearColorFilter();
        }else{
            button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        main_activity.mService.saveRepeatState(!repeat);
        Log.d("onClickButtonRepeat", "saved repeat state:"+!repeat);
        if(main_activity != null && main_activity.mService != null){
            main_activity.mService.changeDone();
        }
    }





}
