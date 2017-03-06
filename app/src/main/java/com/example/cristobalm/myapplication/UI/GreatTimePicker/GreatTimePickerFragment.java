package com.example.cristobalm.myapplication.UI.GreatTimePicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/5/17.
 */

public class GreatTimePickerFragment extends DialogFragment implements GreatTimePickerDialog.OnTimeSetListener {
    TimeContainer timeContainer;
    TimeLinearLayout timeLinearLayout;
    GreatTimePickerDialog greatTimePickerDialog;
    Timefield timefield;
    public void setInfo(TimeLinearLayout timeLinearLayout , TimeContainer timeContainer, Timefield timefield){
        this.timeLinearLayout = timeLinearLayout;
        this.timeContainer = timeContainer;
        this.timefield = timefield;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int hours = timeContainer.getHours();
        int minutes = timeContainer.getMinutes();
        int seconds = timeContainer.getSeconds();
        Log.d("GTPickerFragment", "hours"+hours+", minutes:"+minutes+", seconds:"+seconds);
        greatTimePickerDialog = new GreatTimePickerDialog(getActivity(), 0, this, timeContainer);
        greatTimePickerDialog.setTitle("Select time");
        AlertDialog alert = greatTimePickerDialog.create();

        return alert;
    }

    @Override
    public void onTimeSet(GreatTimePicker view, TimeContainer timeContainer){
        Log.d("onTimeSet", "setting time!");

        timeLinearLayout.setTime(timeContainer);
        timefield.setTime(timeContainer);
    }

    @Override
    public void show(FragmentManager manager, String tag){
        super.show(manager, tag);
    }
}
