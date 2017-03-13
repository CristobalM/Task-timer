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
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.MainActivity;

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

    int millis;



    Button button_ok;

    public interface OnTimeSetListener{
        void onTimeSet(GreatTimePicker view,int millis);
        void updateMillis(int millis);
    }

    public GreatTimePickerDialog(Context context, int theme, OnTimeSetListener callback, int millis){
        super(context, theme);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCallback = callback;

        dateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.great_time_picker_dialog, null);

        greatTimePicker = (GreatTimePicker) view.findViewById(R.id.great_time_picker);

        this.setPositiveButton(R.string.ok, this);


        greatTimePicker.setCurrentHours(TimeContainer.getHours(millis));
        greatTimePicker.setCurrentMinutes(TimeContainer.getMinutes(millis));
        greatTimePicker.setCurrentSeconds(TimeContainer.getSeconds(millis));


        greatTimePicker.setOnTimeChangedListener(this);
        Log.d("GreatTimePickerDialog", "set GreatTimePickerDialog as listener");
        setView(view);



    }
    @Override
    public void onTimeChanged(GreatTimePicker view, int millis){
        //Log.d("OnTimeChanged","GreatTimePickerDialog: hours:"+hours+", minutes:"+minutes+", seconds:"+seconds);
        this.millis = millis;
        if(mCallback != null){
            mCallback.updateMillis(millis);
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which){
        Log.d("GTPickerDialog", "onClick, which:"+which);
        if(mCallback != null){
            greatTimePicker.clearFocus();
            mCallback.onTimeSet(greatTimePicker, greatTimePicker.getCurrentMillis());

        }
    }





}
