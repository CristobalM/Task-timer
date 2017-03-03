package com.example.cristobalm.myapplication.UI.Listeners.Click;

import android.view.View.OnClickListener;
import android.view.View;

import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

import java.util.Collections;

/**
 * Created by cristobalm on 3/1/17.
 * Custom OnClickListener for moveup and movedown buttons
 */

public class TimeFieldUpDownOnClickListener implements OnClickListener {
    private Timefield tfield;
    private MainActivity main_activity;
    private int orientation;
    public TimeFieldUpDownOnClickListener(Timefield tfield, MainActivity main_activity, int orientation){
        this.tfield = tfield;
        this.main_activity = main_activity;
        this.orientation = orientation;
    }
    @Override
    public void onClick(View v){
        int index = tfield.getIndex();
        if(main_activity.getEt_list() != null && main_activity.getTime_fields() != null) {
            if (orientation == VisualSettingGlobals.ORIENTATION_UP && index > 0) {
                Collections.swap(main_activity.getTime_fields(), index - 1, index);
                main_activity.reloadList();
            }else if(orientation == VisualSettingGlobals.ORIENTATION_DOWN &&
                    index < main_activity.getTime_fields().size()-1){
                Collections.swap(main_activity.getTime_fields(), index, index+1);
                main_activity.reloadList();
            }
        }
    }

}
