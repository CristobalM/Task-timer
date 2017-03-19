package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

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
    float scale;
    public GTOnDragListener(TimeLinearLayout timeLinearLayout, MainActivity mainActivity, Timefield timefield){
        this.timeLinearLayout = timeLinearLayout;
        this.mainActivity = mainActivity;
        this.timefield = timefield;
        //scale = mainActivity.getResources().getDisplayMetrics().density;

    }
    public int _px(int dp){
        return mainActivity._px(dp);
    }

    public boolean onDrag(View v, DragEvent event){
        final int action  = event.getAction();

        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT) && mainActivity.current_state != MainStateGlobals.STATE_RUNNING) {
                    v.invalidate();

                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                if(mainActivity.mService != null) {
                    int direction = event.getY() < timeLinearLayout.getMeasuredHeight()/2 ? TimeLinearLayout.UP_DIRECTION : TimeLinearLayout.DOWN_DIRECTION;

                    timeLinearLayout.toggleDragInHover(mainActivity.mService.getCurrentShadow(), direction);

                }

                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                if(mainActivity.mService != null) {
                    int direction = event.getY() < timeLinearLayout.getMeasuredHeight()/2 ? TimeLinearLayout.UP_DIRECTION : TimeLinearLayout.DOWN_DIRECTION;
                    if(timeLinearLayout.getDirection() > -1 && direction != timeLinearLayout.getDirection()){
                        timeLinearLayout.toggleDragOutHover();
                        timeLinearLayout.toggleDragInHover(mainActivity.mService.getCurrentShadow(), direction);
                    }
                }
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                timeLinearLayout.toggleDragOutHover();
                //mainActivity.reloadList();
                v.invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                Intent intent = item.getIntent();
                int result = intent.getIntExtra(GTDragOnClickListener.SOURCE_INDEX, -1);
                //Toast.makeText(v.getContext(), "Source index is: " + result, Toast.LENGTH_SHORT).show();
                timeLinearLayout.restoreNotDragState();
                v.invalidate();
                if(mainActivity.mService != null){
                    int moving_dir =
                            mainActivity.mService
                                    .retrieveMapTimefields()
                                    .get(mainActivity
                                            .mService
                                            .getCurrentMovingIndex())
                                    .getIndex() > timefield.getIndex() ?
                                    TimeLinearLayout.UP_DIRECTION : TimeLinearLayout.DOWN_DIRECTION;


                    if(event.getY() <= timeLinearLayout.getMeasuredHeight()/2){
                        switch (moving_dir) {
                            case TimeLinearLayout.UP_DIRECTION:
                                mainActivity.moveTimefield(timefield.getStatic_index(), result);
                                break;
                            case TimeLinearLayout.DOWN_DIRECTION:
                                Timefield temp_tf = mainActivity.mService.getTFAt(timefield.getIndex()-1);
                                mainActivity.moveTimefield(temp_tf.getStatic_index(), result);
                                break;
                        }
                    }else{
                        switch (moving_dir) {
                            case TimeLinearLayout.UP_DIRECTION:
                                Timefield temp_tf = mainActivity.mService.getTFAt(timefield.getIndex()+1);
                                mainActivity.moveTimefield(temp_tf.getStatic_index(), result);

                                break;
                            case TimeLinearLayout.DOWN_DIRECTION:
                                mainActivity.moveTimefield(timefield.getStatic_index(), result);

                                break;
                        }
                    }
                    mainActivity.reloadList();
                    mainActivity.mService.changeDone();
                }
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                //timeLinearLayout.changeBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorDescription));
                timeLinearLayout.restoreNotDragState();
                mainActivity.reloadList();
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
