package com.example.cristobalm.myapplication.UI.GreatTimePicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/5/17.
 */

public class GreatTimePickerFragment extends DialogFragment implements GreatTimePickerDialog.OnTimeSetListener {
    TimeContainer timeContainer;
    TimeLinearLayout timeLinearLayout;
    GreatTimePickerDialog greatTimePickerDialog;
    Timefield timefield;
    MainActivity mainActivity;
    public void setInfo(TimeLinearLayout timeLinearLayout , TimeContainer timeContainer, Timefield timefield, MainActivity mainActivity){
        this.timeLinearLayout = timeLinearLayout;
        this.timeContainer = timeContainer;
        this.timefield = timefield;
        this.mainActivity = mainActivity;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        greatTimePickerDialog = new GreatTimePickerDialog(getActivity(), 0, this, timeContainer, mainActivity);
        greatTimePickerDialog.setTitle("Select time");

        return greatTimePickerDialog.create();
    }

    @Override
    public void onTimeSet(GreatTimePicker view, TimeContainer timeContainer){

        timeLinearLayout.setTime(timeContainer);
        if(mainActivity != null && mainActivity.mService != null) {
            mainActivity.mService.addSecondsToTotal((timeContainer.getMilliseconds() - timefield.getMilliseconds())/1000);
        }
        timefield.setTime(timeContainer);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        timeLinearLayout.getTimeCountdownView().setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorCountdownBackground));
    }

    @Override
    public void show(FragmentManager manager, String tag){
        super.show(manager, tag);
    }
}
