package com.example.cristobalm.myapplication.UI.ConfigFragment;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.ListFragment.ListFragment;
import com.example.cristobalm.myapplication.UI.MainActivity;

/**
 * Created by cristobalm on 3/14/17.
 */

public class ConfigOnTouchListener implements View.OnTouchListener {
    MainActivity mainActivity;
    public ConfigOnTouchListener(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mainActivity.getState() == MainStateGlobals.STATE_IDLE && mainActivity.mService != null) {
                    ConfigFragment configFragment = ConfigFragment.newInstance(
                            mainActivity.mService);

                    Intent keep_service_on = new Intent(mainActivity, TimingService.class);
                    keep_service_on.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.KEEP_ON);
                    mainActivity.startService(keep_service_on);
                    //listFragment.setInfo(mainActivity.mService, );

                    configFragment.show(mainActivity.getFragmentManager(), "listItem");
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return false;
    }
}
