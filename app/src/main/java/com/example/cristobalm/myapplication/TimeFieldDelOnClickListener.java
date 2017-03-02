package com.example.cristobalm.myapplication;

import android.view.View;

/**
 * Created by cristobalm on 3/1/17.
 * Custom OnClickListener to delete field in ArrayList of TimeFields
 */

class TimeFieldDelOnClickListener implements View.OnClickListener {
    private TimeFields tfield;
    private MainActivity main_activity;
    TimeFieldDelOnClickListener(TimeFields tfield, MainActivity main_activity){
        this.tfield = tfield;
        this.main_activity = main_activity;
    }
    @Override
    public void onClick(View v){
        main_activity.getTime_fields().remove(tfield.index);
        main_activity.reloadList();
    }
}
