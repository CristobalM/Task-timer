package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/11/17.
 */

public class DescriptionTouchEvent implements View.OnTouchListener {
    private Timefield timefield;
    private MainActivity mainActivity;
    public DescriptionTouchEvent(Timefield timefield){
        this.timefield = timefield;
        this.mainActivity = mainActivity;
    }
    @Override
    public boolean onTouch(View v, MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                timefield.focusInScroll();
                break;
        }

        v.setFocusable(true);
        v.requestFocus();

        return false;
    }
}
