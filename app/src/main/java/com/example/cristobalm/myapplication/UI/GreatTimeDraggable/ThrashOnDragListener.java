package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.MainActivity;

/**
 * Created by cristobalm on 3/7/17.
 * Describes behaviour for dropping items in thrash can
 */


public class ThrashOnDragListener implements View.OnDragListener {
    private MainActivity mainActivity;
    private ThrashCan thrashCan;
    public ThrashOnDragListener(MainActivity mainActivity, ThrashCan thrashCan){
        this.mainActivity = mainActivity;
        this.thrashCan = thrashCan;
    }

    public boolean onDrag(View v, DragEvent event){
        final int action  = event.getAction();
        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT) && mainActivity.current_state != MainStateGlobals.STATE_RUNNING;
            case DragEvent.ACTION_DRAG_ENTERED:
                thrashCan.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                thrashCan.setColorFilter(null);
                return true;
            case DragEvent.ACTION_DROP:
                thrashCan.setColorFilter(null);
                ClipData.Item item = event.getClipData().getItemAt(0);
                Intent intent = item.getIntent();
                int result = intent.getIntExtra(GTDragOnClickListener.SOURCE_INDEX, -1);
                //Toast.makeText(v.getContext(), "Source index is: " + result, Toast.LENGTH_SHORT).show();
                mainActivity.removeTimeField(result);
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                mainActivity.hideThrashCan();
                thrashCan.setColorFilter(null);
                return true;
            default:
                Log.e("Dragdrop-ex","Unknown action type received by OnDragListener");
                break;
        }
        return false;
    }
}