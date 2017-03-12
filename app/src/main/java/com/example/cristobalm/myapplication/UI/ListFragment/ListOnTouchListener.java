package com.example.cristobalm.myapplication.UI.ListFragment;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.MainActivity;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/11/17.
 */

public class ListOnTouchListener implements View.OnTouchListener {
    MainActivity mainActivity;
    public ListOnTouchListener(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mainActivity.getState() == MainStateGlobals.STATE_IDLE && mainActivity.mService != null) {
                    ListFragment listFragment = ListFragment.newInstance(mainActivity.mService);

                    /*
                    ArrayList<ListItemInfo> suL =  mainActivity.mService.builtListItemInfoArrayList();
                    if(suL.size() <= 0) {
                        ListItemInfo listItemInfoTest = new ListItemInfo(mainActivity);
                        listItemInfoTest.setFile_name("HOLA TE<st");
                        suL.add(listItemInfoTest);
                    }
                    */

                    Intent keep_service_on = new Intent(mainActivity, TimingService.class);
                    keep_service_on.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.KEEP_ON);
                    mainActivity.startService(keep_service_on);
                    listFragment.setInfo(mainActivity.mService);
                    listFragment.show(mainActivity.getFragmentManager(), "listItem");
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return false;
    }
}
