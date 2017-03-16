package com.example.cristobalm.myapplication.UI.MusicFragment;

import android.graphics.PorterDuff;
import android.media.Image;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/15/17.
 */

public class SoundItem {
    int id;
    MediaPlayer mediaPlayer;
    TimingService timingService;
    ImageView soundimage;
    Timefield timefield;
    SelectMusicDialog selectMusicDialog;
    int which;
    public void setBackgroundColor(boolean on){
        if(on) {
            soundimage.setBackgroundColor(ContextCompat.getColor(timingService, R.color.itemNotificationBackgroundON));
        }
        else{
            soundimage.setBackgroundColor(0x00000000);
        }
    }
    SoundItem(int id, TimingService timingService, View view, Timefield timefield, SelectMusicDialog selectMusicDialog){
        this.id = id;
        this.timingService = timingService;
        this.timefield = timefield;
        this.selectMusicDialog = selectMusicDialog;
        int sound =  InfoNameGlobals.getSound(id);
        mediaPlayer = MediaPlayer.create(timingService.getApplicationContext(), sound);
        soundimage = (ImageView) InfoNameGlobals.getID(view, id);
        soundimage.setOnTouchListener(new SoundTouchListener());
        soundimage.setColorFilter(InfoNameGlobals.getSColorById(id), PorterDuff.Mode.MULTIPLY);
    }
    SoundItem(int id, TimingService timingService, View view, int which, SelectMusicDialog selectMusicDialog){
        this.id = id;
        this.timingService = timingService;
        this.which = which;
        this.selectMusicDialog = selectMusicDialog;
        int sound =  InfoNameGlobals.getSound(id);
        mediaPlayer = MediaPlayer.create(timingService.getApplicationContext(), sound);
        soundimage = (ImageView) InfoNameGlobals.getID(view, id);
        soundimage.setOnTouchListener(new SoundTouchListenerConfig());
        soundimage.setColorFilter(InfoNameGlobals.getSColorById(id), PorterDuff.Mode.MULTIPLY);
    }

    public class SoundTouchListener implements View.OnTouchListener{
        public boolean onTouch(View v, MotionEvent e){
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mediaPlayer.start();
                    timingService.loadMusic(id, timefield);
                    selectMusicDialog.updateBackground(id);

                    //timefield.setSound(id);
                    //timefield.getTimeLinearLayout().setMusicColor(InfoNameGlobals.getSColorById(id));
                    Log.e("SoundTouchListener",
                            "setting sound with id "+ id+
                            " and timefield with desc; "+ timefield.getCustomText());
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    }

    public class SoundTouchListenerConfig implements View.OnTouchListener{
        public boolean onTouch(View v, MotionEvent e){
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mediaPlayer.start();
                    timingService.setConfigMusic(which, id);
                    selectMusicDialog.updateBackground(id);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    }
}
