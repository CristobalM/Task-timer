package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.GTDragOnClickListener;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.GTOnDragListener;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.DescriptionTouchEvent;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.GreatTimePicker.GreatTimePickerFragment;
import com.example.cristobalm.myapplication.UI.MusicFragment.MusicFragment;


/**
 * Created by cristobalm on 3/1/17.
 * Object which encapsulates views for a new item created with add button.
 */

public class Timefield {
    private Context context;
    private MainActivity main_activity;
    private int index;
    private int static_index;

    private TimeLinearLayout timeLinearLayout;

    private TimeContainer time_container;
    private TimeContainer temp_time_container;

    TLLRunnable tllRunnable;



    public Timefield getMe(){
        return this;
    }


    public class TLLRunnable implements  Runnable{
        TimeLinearLayout tll;
        ScrollView sv;
        TLLRunnable(TimeLinearLayout tll, ScrollView sv){
            this.tll = tll;
            this.sv = sv;
        }
        @Override
        public void run(){
            Log.d("TLLRUNABLE", "SCROLLING! getBottom:"+ tll.getBottom());
            //
            sv.scrollTo(0, tll.getTop() );
        }
    }

    public String getHint(){
        return "Task " + String.valueOf(index+1);
    }

    public void setTime(int milliseconds){
        time_container.setMilliseconds(milliseconds);
    }


    public class CountdownOnTouchListener implements View.OnTouchListener {
        int static_index;
        MainActivity mainActivity;
        public CountdownOnTouchListener(int static_index, MainActivity mainActivity){
            this.static_index = static_index;
            this.mainActivity = mainActivity;
        }
        @Override
        public boolean onTouch(View view, MotionEvent event){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(main_activity.getState() == MainStateGlobals.STATE_IDLE) {
                        main_activity.mService.setOnOpeningDialogFragment();
                        Intent keep_service_on = new Intent(context, TimingService.class);
                        keep_service_on.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.KEEP_ON);
                        context.startService(keep_service_on);
                        GreatTimePickerFragment greatTimePickerFragment = GreatTimePickerFragment.newInstance(static_index, mainActivity.mService);
                        greatTimePickerFragment.show(main_activity.getFragmentManager(), "timePicker");
                        timeLinearLayout.getTimeCountdownView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorCountdownBackgroundSelected));
                    }
                    break;
            }
            return true;
        }
    }

    Integer soundId;
    public void setSound(int id){
        this.soundId = id;
    }
    public int getSoundId(){
        return soundId;
    }


    public class OnMusicTouch implements View.OnTouchListener{
        public boolean onTouch(View v, MotionEvent e){
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(main_activity.getState() == MainStateGlobals.STATE_IDLE) {
                        main_activity.mService.setOnOpeningDialogFragment();
                        Intent keep_service_on = new Intent(context, TimingService.class);
                        keep_service_on.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.KEEP_ON);
                        context.startService(keep_service_on);

                        MusicFragment musicFragment = MusicFragment.newInstance(main_activity.mService, getMe());
                        musicFragment.show(main_activity.getFragmentManager(), "musicPicker");



                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    }

    Timefield(Context context, int index, MainActivity main_activity){
        this.context = context;
        this.main_activity = main_activity;
        setIndex(index);
        time_container = new TimeContainer(0);

        init();
        Log.d("Timefield", "constructor.. index:"+ index);
        this.timeLinearLayout.setHint(index+1);
        this.timeLinearLayout.setTime(0,0,0);
        startTimefieldView(main_activity);
    }

    public void setHint(int hint){
        this.timeLinearLayout.setHint(hint+1);
    }
    public  Timefield(Context context, int index, String custom_text, int milliseconds){
        this.context = context;
        setIndex(index);
        Log.d("Timefield", "secondconstructor.. index:"+index);

        time_container = new TimeContainer(milliseconds);
        init();
        this.timeLinearLayout.setHint(index+1);
        this.timeLinearLayout.setDescription(custom_text);
        this.timeLinearLayout.setTime(
                time_container.getHours(),
                time_container.getMinutes(),
                time_container.getSeconds());
        //timeLinearLayout.setMusicColor(InfoNameGlobals.getSound(getSoundId()));

    }

    void startTimefieldView(MainActivity activity){
        main_activity = activity;
        //main_activity.unique_index++;
        if(soundId == null) {
            setSound(main_activity.mService.getCommonSound());
        }



        timeLinearLayout.setDraggableClickListener(new GTDragOnClickListener(static_index, timeLinearLayout.getTimeDraggable(), main_activity));
        timeLinearLayout.setOnDragListener(new GTOnDragListener(timeLinearLayout, main_activity, this));

        tllRunnable = new TLLRunnable(timeLinearLayout, main_activity.getScrollView());

        timeLinearLayout.getTimeDescription().setOnTouchListener(new DescriptionTouchEvent(this));

        timeLinearLayout.setCountdownListener(new CountdownOnTouchListener(static_index, main_activity));
        timeLinearLayout.setMusicListener(new OnMusicTouch());
        updateMusicColor();
    }
    public void updateMusicColor(){
        timeLinearLayout.setMusicColor(InfoNameGlobals.getSColorById(getSoundId()));
    }

    public void focusInScroll(){
        timeLinearLayout.post(tllRunnable);
    }

    private void init(){
        static_index = getIndex();
        timeLinearLayout = new TimeLinearLayout(context);
        temp_time_container = new TimeContainer(time_container.getMilliseconds());
        timeLinearLayout.setPosition(index);

    }

    public void setIndex(int i){
        this.index = i;
        if(timeLinearLayout != null){
            timeLinearLayout.setPosition(index);
        }
    }

    public TimeLinearLayout getTimeLinearLayout(){
        return timeLinearLayout;
    }

    public String getCustomText(){
        return timeLinearLayout.getDescription();
    }

    LinearLayout getLayout(){
        return timeLinearLayout;
    }

    public int getIndex(){
        return index;
    }

    public int getMilliseconds(){
        return time_container.getMilliseconds();
    }
    public void blockInput(){
        timeLinearLayout.stopEditables();
    }
    public void enableInput(){
        timeLinearLayout.enableEditables();
    }

    public void setStaticIndex(int id){
        this.static_index = id;
    }
    public int getStatic_index(){
        return static_index;
    }

    public void restoreTime(){
        timeLinearLayout.setTime(time_container);
    }
}
