package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import com.example.cristobalm.myapplication.UI.MainActivity;

/**
 * Created by cristobalm on 3/7/17.
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
                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT)) {

                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                thrashCan.setColorFilter(Color.RED);
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                thrashCan.setColorFilter(Color.BLACK);
                return true;
            case DragEvent.ACTION_DROP:
                thrashCan.setColorFilter(Color.BLACK);
                ClipData.Item item = event.getClipData().getItemAt(0);
                Intent intent = item.getIntent();
                int result = intent.getIntExtra(GTDragOnClickListener.SOURCE_INDEX, -1);
                Toast.makeText(v.getContext(), "Source index is: " + result, Toast.LENGTH_SHORT).show();
                mainActivity.removeTimeField(result);
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                mainActivity.hideThrashCan();
                thrashCan.setColorFilter(Color.BLACK);
                return true;
            default:
                Log.e("Dragdrop-ex","Unknown action type received by OnDragListener");
                break;
        }
        return false;
    }
}