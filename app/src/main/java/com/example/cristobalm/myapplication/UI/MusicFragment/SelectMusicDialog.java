package com.example.cristobalm.myapplication.UI.MusicFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/15/17.
 */

public class SelectMusicDialog extends AlertDialog.Builder {
    ArrayList<SoundItem> soundItems;
    SelectMusicDialog(Context context, int theme, TimingService timingService, Timefield timefield){
        super(context, theme);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.selectsound_dialog, null);


        soundItems =  new ArrayList<>();

        for(int i = 0; i < 7; i++){
            soundItems.add(new SoundItem(i, timingService, view, timefield, this));
        }


        setView(view);
    }

    SelectMusicDialog(Context context, int theme, TimingService timingService, int which){
        super(context, theme);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.selectsound_dialog, null);


        soundItems =  new ArrayList<>();

        for(int i = 0; i < 7; i++){
            soundItems.add(new SoundItem(i, timingService, view, which, this));
        }


        setView(view);
    }


    public void updateBackground(int id){
        for(int i = 0; i < soundItems.size(); i++) {
            soundItems.get(i).setBackgroundColor(i == id);
        }
    }
}
