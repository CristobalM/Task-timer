package com.example.cristobalm.myapplication.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;

/**
 * Created by cristobalm on 3/14/17.
 */

public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())){
            Intent next_iter = new Intent(context, TimingService.class);
            next_iter.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.SHUTDOWN);
            context.startService(next_iter);
        }
    }
}
