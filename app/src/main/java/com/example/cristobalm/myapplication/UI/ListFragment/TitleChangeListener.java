package com.example.cristobalm.myapplication.UI.ListFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.example.cristobalm.myapplication.Services.TimingService;

/**
 * Created by cristobalm on 3/13/17.
 */

public class TitleChangeListener implements TextWatcher {
    TimingService timingService;
    public TitleChangeListener(TimingService timingService){
        this.timingService = timingService;
    }

    @Override
    public void afterTextChanged(Editable s){
        //Log.d("afterTextChanged", "s is:"+s.toString());
        timingService.setTitle(s.toString());
        timingService.changeDone();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after){

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count){

    }
}
