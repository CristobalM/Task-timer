package com.example.cristobalm.myapplication.UI.ListFragment;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
                    timingService.loadFile(_index);
                    timingService.reloadColor();
                    listItem.setItemBackground(R.color.itemNotificationBackgroundON);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }

            return false;
        }
    }
    public class DeleteItemOnTouchListener implements View.OnTouchListener{
        TimingService timingService;
        int id;
        ListItem listItem;
        public DeleteItemOnTouchListener(TimingService timingService, int id, ListItem listItem){
            this.timingService = timingService;
            this.id = id;
            this.listItem = listItem;
        }
        @Override
        public boolean onTouch(View v, MotionEvent e){
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //timingService.getFilesDialogList().removeView(listItem);
                    if(timingService.getCurrentIndexFile() != id){
                        ((ViewGroup)listItem.getParent()).removeView(listItem);
                    }
                    timingService.removeID(id);
                    listItem.OnDeleteColor();
                    break;
                case MotionEvent.ACTION_UP:
                    listItem.OffDeleteColor();
                    break;
            }

            return true;
        }
    }

    public ListItemInfo(Context context, TimingService timingService, int index){
        listItem = new ListItem(context);
        this.timingService = timingService;
        this.index = index;
        listItem.setOpenerListener(new ListItemOnTouchListener(timingService, index));
        listItem.setItemBackground(R.color.itemNotificationBackground);
        listItem.setItemTextColor(R.color.itemNotificationText);
        listItem.setDeleteListener(new DeleteItemOnTouchListener(timingService, index, listItem));
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
