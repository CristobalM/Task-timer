package com.example.cristobalm.myapplication.UI.Listeners.Click;

import android.view.View;

import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/1/17.
 * Custom OnClickListener to delete field in ArrayList of Timefield
 */

public class TimeFieldDelOnClickListener implements View.OnClickListener {
    private Timefield tfield;
    private MainActivity main_activity;
    public TimeFieldDelOnClickListener(Timefield tfield, MainActivity main_activity){
        this.tfield = tfield;
        this.main_activity = main_activity;
    }
    @Override
    public void onClick(View v){
        main_activity.getTime_fields().remove(tfield.getIndex());
        main_activity.reloadList();
    }
}
