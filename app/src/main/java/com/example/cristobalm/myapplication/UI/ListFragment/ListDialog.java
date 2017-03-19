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
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.MainActivity;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/11/17.
 */

public class ListDialog extends AlertDialog.Builder {

    LinearLayout list;
    View view;
    public ListDialog(Context context, int theme, ArrayList<ListItemInfo> list_items ){
        super(context, theme);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lists_dialog, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(view.getMeasuredWidth()/2, view.getMeasuredHeight()));
        view.invalidate();
        //ArrayList<ListItemInfo> list_items = builtListItemInfoArrayList();
        list = (LinearLayout) view.findViewById(R.id.filenames_list);

        //list.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (list_items != null && list != null) {
            for (int i = 0; i < list_items.size(); i++) {
                ListItem auxListItem = list_items.get(i).getListItem();
                if (auxListItem.getParent() != null) {
                    ((ViewGroup) auxListItem.getParent()).removeView(auxListItem);
                }
                list.addView(auxListItem);
            }
            //list_items.get(list_items.size()-1).getListItem().setLast();
        }


        //this.list_items = timingService.builtListItemInfoArrayList();

        //setView(view);

    }
    public View getView(){
        return view;
    }

    public LinearLayout getList(){
        return list;
    }




}
