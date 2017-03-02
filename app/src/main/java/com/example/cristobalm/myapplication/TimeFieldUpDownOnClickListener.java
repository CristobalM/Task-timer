package com.example.cristobalm.myapplication;

import android.view.View.OnClickListener;
import android.view.View;
import java.util.Collections;

/**
 * Created by cristobalm on 3/1/17.
 * Custom OnClickListener for moveup and movedown buttons
 */

class TimeFieldUpDownOnClickListener implements OnClickListener {
    private TimeFields tfield;
    private MainActivity main_activity;
    private int orientation;
    TimeFieldUpDownOnClickListener(TimeFields tfield, MainActivity main_activity, int orientation){
        this.tfield = tfield;
        this.main_activity = main_activity;
        this.orientation = orientation;
    }
    @Override
    public void onClick(View v){
        int index = tfield.getIndex();
        if(main_activity.getEt_list() != null && main_activity.getTime_fields() != null) {
            if (orientation == IntGlobals.ORIENTATION_UP && index > 0) {
                Collections.swap(main_activity.getTime_fields(), index - 1, index);
                main_activity.reloadList();
            }else if(orientation == IntGlobals.ORIENTATION_DOWN &&
                    index < main_activity.getTime_fields().size()-1){
                Collections.swap(main_activity.getTime_fields(), index, index+1);
                main_activity.reloadList();
            }
        }
    }

}
