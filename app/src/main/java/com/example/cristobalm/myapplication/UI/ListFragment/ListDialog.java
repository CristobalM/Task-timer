package com.example.cristobalm.myapplication.UI.ListFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.MainActivity;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/11/17.
 */

public class ListDialog extends AlertDialog.Builder {
    ArrayList<ListItemInfo> list_items;
    public ListDialog(Context context, ArrayList<ListItemInfo> list_items, int theme){
        super(context, theme);

        this.list_items = list_items;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.lists_dialog, null);

        LinearLayout list = (LinearLayout) view.findViewById(R.id.filenames_list);
        if(list_items != null && list != null) {
            for (int i = 0; i < list_items.size(); i++) {
                ListItem auxListItem = list_items.get(i).getListItem();
                if(auxListItem.getParent()!=null){
                    ((ViewGroup) auxListItem.getParent()).removeView(auxListItem);
                }
                list.addView(auxListItem);
            }
        }

        setView(view);
    }




}
