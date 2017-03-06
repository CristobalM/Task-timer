package com.example.cristobalm.myapplication.UI.GreatTimePicker;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;

import java.util.Calendar;


/**
 * Created by cristobalm on 3/5/17.
 */

public class GreatTimePickerDialog extends AlertDialog.Builder
        implements DialogInterface.OnClickListener, GreatTimePicker.OnTimeChangedListener{

    public static final String HOURS = "hours";
    public static final String MINUTES = "minutes";
    public static final String SECONDS = "seconds";


    private final GreatTimePicker greatTimePicker;
    private final OnTimeSetListener mCallback;
    private final java.text.DateFormat dateFormat;
    private final Calendar mCalendar;

    int initial_hours;
    int initial_minutes;
    int initial_seconds;

    private int hours;
    private int minutes;
    private int seconds;

    Button button_ok;

    public interface OnTimeSetListener{
        void onTimeSet(GreatTimePicker view,TimeContainer timeContainer);
    }

    public GreatTimePickerDialog(Context context, OnTimeSetListener callback, int hours, int minutes, int seconds){
        this(context, 0, callback, hours, minutes, seconds);
    }
    public GreatTimePickerDialog(Context context, int theme, OnTimeSetListener callback, TimeContainer timeContainer){
        this(context, 0, callback, timeContainer.getHours(), timeContainer.getMinutes(), timeContainer.getSeconds());
    }
    public GreatTimePickerDialog(Context context, int theme, OnTimeSetListener callback, int hours, int minutes, int seconds){
        super(context, theme);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCallback = callback;
        initial_hours = hours;
        initial_minutes = minutes;
        initial_seconds = seconds;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;

        dateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.great_time_picker_dialog, null);

        greatTimePicker = (GreatTimePicker) view.findViewById(R.id.great_time_picker);

        this.setPositiveButton(R.string.ok, this);


        greatTimePicker.setCurrentHours(initial_hours);
        greatTimePicker.setCurrentMinutes(initial_minutes);
        greatTimePicker.setCurrentSeconds(initial_seconds);
        Log.d("GTPickerDialog",
                "initial_hours:"+initial_hours+
                ", initial_minutes:"+initial_minutes+
                ", initial_seconds:"+initial_seconds);

        greatTimePicker.setOnTimeChangedListener(this);
        Log.d("GreatTimePickerDialog", "set GreatTimePickerDialog as listener");
        setView(view);



    }
    @Override
    public void onTimeChanged(GreatTimePicker view, int hours, int minutes, int seconds){
        Log.d("OnTimeChanged","GreatTimePickerDialog: hours:"+hours+", minutes:"+minutes+", seconds:"+seconds);
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void updateTime(int hours, int minutes, int seconds){
        greatTimePicker.setCurrentHours(hours);
        greatTimePicker.setCurrentMinutes(minutes);
        greatTimePicker.setCurrentSeconds(seconds);
    }





    public int getHours(){
        return hours;
    }
    public int getMinutes(){
        return minutes;
    }
    public int getSeconds(){
        return seconds;
    }


    @Override
    public void onClick(DialogInterface dialog, int which){
        Log.d("GTPickerDialog", "onClick, which:"+which);
        if(mCallback != null){
            greatTimePicker.clearFocus();
            mCallback.onTimeSet(greatTimePicker, greatTimePicker.getTimeContainer());
        }
    }





}
