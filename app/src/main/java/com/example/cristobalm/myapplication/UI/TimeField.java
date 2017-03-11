package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.GTDragOnClickListener;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.GTOnDragListener;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.GreatTimePicker.GreatTimePickerFragment;


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

    public void setTimeTemp(int milliseconds){
        temp_time_container.setMilliseconds(milliseconds);
    }
    public void setTime(TimeContainer t_container){
        time_container.setMilliseconds(t_container.getMilliseconds());
    }


    public class CountdownOnClickListener implements View.OnClickListener {
        TimeContainer timeContainer;
        TimeLinearLayout timeLinearLayout;
        Timefield timefield;
        public CountdownOnClickListener(TimeContainer timeContainer, TimeLinearLayout timeLinearLayout, Timefield timefield){
            this.timeContainer = timeContainer;
            this.timeLinearLayout = timeLinearLayout;
            this.timefield = timefield;
        }
        @Override
        public void onClick(View view){
            if(main_activity.getState() == MainStateGlobals.STATE_IDLE) {
                GreatTimePickerFragment greatTimePickerFragment = new GreatTimePickerFragment();
                greatTimePickerFragment.setInfo(timeLinearLayout, timeContainer, timefield, main_activity);
                greatTimePickerFragment.show(main_activity.getFragmentManager(), "timePicker");
                timeLinearLayout.getTimeCountdownView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorCountdownBackgroundSelected));
                ;
            }
        }
    }

    Timefield(Context context, int index, MainActivity main_activity){
        this.context = context;
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
    public Timefield(Context context, int index, String custom_text, int milliseconds){
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

    }

    void startTimefieldView(MainActivity activity){
        main_activity = activity;
        static_index = main_activity.unique_index;
        main_activity.unique_index++;

        timeLinearLayout.setDraggableClickListener(new GTDragOnClickListener(static_index, timeLinearLayout.getTimeDraggable(), main_activity));
        timeLinearLayout.setOnDragListener(new GTOnDragListener(timeLinearLayout, main_activity, this));


    }

    private void init(){
        timeLinearLayout = new TimeLinearLayout(context);
        timeLinearLayout.setCountdownListener(new CountdownOnClickListener(time_container, timeLinearLayout, this));
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
