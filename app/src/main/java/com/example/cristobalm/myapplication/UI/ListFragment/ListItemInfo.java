package com.example.cristobalm.myapplication.UI.ListFragment;

import android.content.Context;

/**
 * Created by cristobalm on 3/11/17.
 */

public class ListItemInfo {
    ListItem listItem;

    public ListItemInfo(Context context){
        listItem = new ListItem(context);
    }

    public String getFile_name(){
        return listItem.getFile_name();
    }

    public void setFile_name(String fileName){
        listItem.setFile_name(fileName);
    }


    public ListItem getListItem(){
        return listItem;
    }
}
