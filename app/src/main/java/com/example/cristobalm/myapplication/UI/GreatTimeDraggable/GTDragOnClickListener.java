package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.MainActivity;

/**
 * Created by cristobalm on 3/7/17.
 */

public class GTDragOnClickListener implements View.OnTouchListener {
    public static final String SOURCE_INDEX = "SOURCE_INDEX";
    private int source_index;
    private TimeLinearLayout t_view;
    private MainActivity mainActivity;
    public GTDragOnClickListener(int source_index, TimeLinearLayout t_view, MainActivity mainActivity){
        this.source_index = source_index;
        this.t_view = t_view;
        this.mainActivity = mainActivity;
    }

    public  boolean onTouch(View v, MotionEvent event){
        if(mainActivity.current_state != MainStateGlobals.STATE_IDLE){
            return true;
        }
        Intent intent = new Intent();
        intent.putExtra(SOURCE_INDEX, source_index);
        ClipData.Item item = new ClipData.Item(intent);
        ClipData dragData = new ClipData(SOURCE_INDEX, new String[]{ClipDescription.MIMETYPE_TEXT_INTENT}, item);
        GTDragShadowBuilder myShadow_b = new GTDragShadowBuilder(t_view, mainActivity.getApplicationContext());
        if(mainActivity.mService != null){
            mainActivity.mService.setCurrentShadowBuilder(myShadow_b);
        }
        View.DragShadowBuilder myShadow = myShadow_b;
        mainActivity.showThrashCan();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            v.startDragAndDrop(dragData, myShadow, null, 0);
        }
        else{
            v.startDrag(dragData, myShadow, null, 0);
        }
        if(t_view.getParent() != null) {
            ((ViewGroup)t_view.getParent() ).removeView(t_view);
        }
        mainActivity.mService.setCurrentMovingIndex(source_index);

        return true;
    }
}
