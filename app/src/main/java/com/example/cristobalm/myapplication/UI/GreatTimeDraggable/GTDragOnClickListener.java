package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.MainActivity;

/**
 * Created by cristobalm on 3/7/17.
 */

public class GTDragOnClickListener implements View.OnTouchListener {
    public static final String SOURCE_INDEX = "SOURCE_INDEX";
    private int source_index;
    private View t_view;
    private MainActivity mainActivity;
    public GTDragOnClickListener(int source_index, View t_view, MainActivity mainActivity){
        this.source_index = source_index;
        this.t_view = t_view;
        this.mainActivity = mainActivity;
    }

    public boolean onTouch(View v, MotionEvent event){
        if(mainActivity.current_state == MainStateGlobals.STATE_RUNNING){
            return true;
        }
        Intent intent = new Intent();
        intent.putExtra(SOURCE_INDEX, source_index);
        ClipData.Item item = new ClipData.Item(intent);
        ClipData dragData = new ClipData(SOURCE_INDEX, new String[]{ClipDescription.MIMETYPE_TEXT_INTENT}, item);
        View.DragShadowBuilder myShadow = new GTDragShadowBuilder(t_view);
        mainActivity.showThrashCan();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            v.startDragAndDrop(dragData, myShadow, null, 0);
        }
        else{
            v.startDrag(dragData, myShadow, null, 0);
        }

        return true;
    }
}
