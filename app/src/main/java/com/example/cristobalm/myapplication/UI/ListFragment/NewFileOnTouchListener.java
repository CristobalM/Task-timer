package com.example.cristobalm.myapplication.UI.ListFragment;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.MainActivity;

/**
 * Created by cristobalm on 3/13/17.
 */

public class NewFileOnTouchListener implements View.OnTouchListener {
    MainActivity mainActivity;
    public NewFileOnTouchListener(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mainActivity.getState() == MainStateGlobals.STATE_IDLE && mainActivity.mService != null) {
                    mainActivity.mService.newFile();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return false;
    }
}
