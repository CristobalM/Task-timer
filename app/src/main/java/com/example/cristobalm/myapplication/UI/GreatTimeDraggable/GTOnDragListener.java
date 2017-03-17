package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/7/17.
 */

public class GTOnDragListener implements View.OnDragListener {
    private TimeLinearLayout timeLinearLayout;
    private MainActivity mainActivity;
    private Timefield timefield;
    public GTOnDragListener(TimeLinearLayout timeLinearLayout, MainActivity mainActivity, Timefield timefield){
        this.timeLinearLayout = timeLinearLayout;
        this.mainActivity = mainActivity;
        this.timefield = timefield;
    }

    public boolean onDrag(View v, DragEvent event){
        final int action  = event.getAction();
        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT) && mainActivity.current_state != MainStateGlobals.STATE_RUNNING) {
                    //Log.d("ACTION_DRAG_STARTED", "I am " + timeLinearLayout.getDescription() + " receiving call!");
                    v.invalidate();
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                timeLinearLayout.toggleDragInHover();
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                timeLinearLayout.toggleDragOutHover();
                v.invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                Intent intent = item.getIntent();
                int result = intent.getIntExtra(GTDragOnClickListener.SOURCE_INDEX, -1);
                //Toast.makeText(v.getContext(), "Source index is: " + result, Toast.LENGTH_SHORT).show();
                timeLinearLayout.restoreNotDragState();
                mainActivity.moveTimefield(timefield.getStatic_index(), result);
                v.invalidate();
                if(mainActivity.mService != null){
                    mainActivity.mService.changeDone();
                }
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                //timeLinearLayout.changeBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorDescription));
                timeLinearLayout.restoreNotDragState();

                if(event.getResult()){
                    //Toast.makeText(v.getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(v.getContext(), "The drop didn't work", Toast.LENGTH_SHORT).show();
                }
                v.invalidate();
                return true;
            default:
                Log.e("Dragdrop-ex","Unknown action type received by OnDragListener");
                break;
        }
        return false;
    }
}
