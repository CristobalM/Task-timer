package com.example.cristobalm.myapplication.UI.ListFragment;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.TimingService;

/**
 * Created by cristobalm on 3/11/17.
 */

public class ListItemInfo {
    ListItem listItem;
    TimingService timingService;
    int index;

    public class ListItemOnTouchListener implements View.OnTouchListener{
        TimingService timingService;
        int _index;
        ListItemOnTouchListener(TimingService timingService, int _index){
            this.timingService = timingService;
            this._index = _index;
        }
        @Override
        public boolean onTouch(View v, MotionEvent e){
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    timingService.clearOtherBackground();
                    timingService.loadFile(_index);
                    listItem.setItemBackground(R.color.colorCountdownBackground);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }

            return false;
        }
    }

    public ListItemInfo(Context context, TimingService timingService, int index){
        listItem = new ListItem(context);
        this.timingService = timingService;
        this.index = index;
        listItem.setOpenerListener(new ListItemOnTouchListener(timingService, index));
        listItem.setItemBackground(R.color.colorDescriptionBackground);
    }
    public void setBackgroundColor(int color){
        listItem.setItemBackground(color);
    }
    public String getFile_name(){
        return listItem.getFile_name();
    }

    public void setFile_name(String fileName){
        listItem.setFile_name(fileName);
    }
    public void setHint(String hint){
        listItem.setHint(hint);
    }



    public ListItem getListItem(){
        return listItem;
    }
}
