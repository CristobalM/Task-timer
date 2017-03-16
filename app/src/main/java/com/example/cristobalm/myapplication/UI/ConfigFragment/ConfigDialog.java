package com.example.cristobalm.myapplication.UI.ConfigFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.cristobalm.myapplication.R;

/**
 * Created by cristobalm on 3/14/17.
 */

public class ConfigDialog extends AlertDialog.Builder {
    ImageView allfinished;
    ImageView common;
    CheckBox checkBox;
    ConfigDialog(Context context, int theme){
        super(context, theme);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.config_dialog, null);
        allfinished = (ImageView) view.findViewById(R.id.music_finished);
        common = (ImageView) view.findViewById(R.id.music_task_common);
        checkBox = (CheckBox) view.findViewById(R.id.apply_to_all);


        setView(view);
    }

    public void setSoundFinishedListener(View.OnTouchListener onTouchListener){
        if(allfinished != null){
            allfinished.setOnTouchListener(onTouchListener);
        }

    }

    public void setSoundCommonListener(View.OnTouchListener onTouchListener){
        if(common != null){
            common.setOnTouchListener(onTouchListener);
        }
    }

    public ImageView getFinishedImageView(){
        return allfinished;
    }

    public ImageView getCommonImageView(){
        return common;
    }

    public boolean applyToAllChecked(){
        return checkBox != null && checkBox.isChecked();
    }


}
