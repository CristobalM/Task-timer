package com.example.cristobalm.myapplication.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.UI.MainActivity;

/**
 * Created by cristobalm on 3/3/17.
 */

public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("TimeReceiver.onReceive", "called !!!");
        Intent next_iter = new Intent(context, TimingService.class);
        next_iter.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.CONTINUE_TIMING);
        context.startService(next_iter);
    }
}
