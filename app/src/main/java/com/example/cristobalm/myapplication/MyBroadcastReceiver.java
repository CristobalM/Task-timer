package com.example.cristobalm.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

/**
 * Created by cristobalm on 3/2/17.
 *
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent){
        mp = MediaPlayer.create(context, R.raw.surprise_on_a_spring);
        mp.start();
    }
}
