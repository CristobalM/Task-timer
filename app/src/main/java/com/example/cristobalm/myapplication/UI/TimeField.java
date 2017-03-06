package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.GreatTimePicker.GreatTimePickerFragment;


/**
 * Created by cristobalm on 3/1/17.
 * Object which encapsulates views for a new item created with add button.
 */

public class Timefield {
    private Context context;
    private int index;
    private MainActivity main_activity;


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
            Log.d("CountdownOn..", "Clicked countdown textview!");
            GreatTimePickerFragment greatTimePickerFragment = new GreatTimePickerFragment();
            greatTimePickerFragment.setInfo(timeLinearLayout, timeContainer, timefield);
            greatTimePickerFragment.show(main_activity.getFragmentManager(), "timePicker");
        }
    }

    Timefield(Context context, int index, MainActivity main_activity){
        this.context = context;
        this.index = index;
        time_container = new TimeContainer(0);

        init();
        timeLinearLayout.setDescription("Task " + String.valueOf(index+1));
        timeLinearLayout.setTime(0,0,0);
        startTimefieldView(main_activity);


        Log.d("Timefield", "created new time field with index:"+String.valueOf(index));
    }
    public Timefield(Context context, int index, String custom_text, int milliseconds){
        Log.d("Timefield", "milliseconds:"+milliseconds);
        this.context = context;
        this.index = index;
        time_container = new TimeContainer(milliseconds);
        init();
        timeLinearLayout.setDescription(custom_text);
        timeLinearLayout.setTime(
                time_container.getHours(),
                time_container.getMinutes(),
                time_container.getSeconds());

    }

    void startTimefieldView(MainActivity activity){
        main_activity = activity;

    }

    private void init(){
        timeLinearLayout = new TimeLinearLayout(context);
        timeLinearLayout.setCountdownListener(new CountdownOnClickListener(time_container, timeLinearLayout, this));
        temp_time_container = new TimeContainer(time_container.getMilliseconds());

    }


    void setIndex(int i){
        this.index = i;
    }

    public TimeLinearLayout getTimeLinearLayout(){
        return timeLinearLayout;
    }

    public String getCustomText(){
        return timeLinearLayout.getDescription();
    }
    public int getIndex(){
        return index;
    }
    LinearLayout getLayout(){
        return timeLinearLayout;
    }

    public int getHours(){
        return time_container.getHours();
    }
    public int getMinutes(){
        return time_container.getMinutes();
    }
    public int getSeconds(){
        return time_container.getSeconds();
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
}
