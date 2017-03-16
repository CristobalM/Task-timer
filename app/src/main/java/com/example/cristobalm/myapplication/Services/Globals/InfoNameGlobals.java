package com.example.cristobalm.myapplication.Services.Globals;

import android.graphics.Color;
import android.view.View;

import com.example.cristobalm.myapplication.R;

/**
 * Created by cristobalm on 3/3/17.
 */

public class InfoNameGlobals {
    //Intent storage names
    public static final String REPEAT_TIME_LIST = "REPEAT_TIME_LIST";
    public static final String ACTION = "ACTION";

    // Action names
    public static final String START_TIMING = "START_TIMING";
    public static final String STOP_TIMING = "STOP_TIMING";
    public static final String PAUSE_TIMING = "PAUSE_TIMING";
    public static final String CONTINUE_TIMING = "CONTINUE_TIMING";
    public static final String KEEP_ON = "KEEP_ON";
    public static final String SHUTDOWN = "SHUTDOWN";



    // Sound notifications
    public static final int NOTIFICATION_ZERO = R.raw.notify0;
    public static final int NOTIFICATION_ONE = R.raw.notify1;
    public static final int NOTIFICATION_TWO = R.raw.notify2;
    public static final int NOTIFICATION_THREE = R.raw.notify3;
    public static final int NOTIFICATION_FOUR = R.raw.notify4;
    public static final int NOTIFICATION_FIVE = R.raw.notify5;
    public static final int NOTIFICATION_SIX = R.raw.notify6;


    public static final int _NOTIFICATION_ZERO = 0;
    public static final int _NOTIFICATION_ONE = 1;
    public static final int _NOTIFICATION_TWO = 2;
    public static final int _NOTIFICATION_THREE = 3;
    public static final int _NOTIFICATION_FOUR = 4;
    public static final int _NOTIFICATION_FIVE = 5;
    public static final int _NOTIFICATION_SIX = 6;


    public static final int getSound(int id){
        int [] arr = {
                NOTIFICATION_ZERO,
                NOTIFICATION_ONE,
                NOTIFICATION_TWO,
                NOTIFICATION_THREE,
                NOTIFICATION_FOUR,
                NOTIFICATION_FIVE,
                NOTIFICATION_SIX,
        };
        return arr[id];
    }
    public static final View getID(View v, int id){
        View [] arr = {
                v.findViewById(R.id.sound_one),
                v.findViewById(R.id.sound_two),
                v.findViewById(R.id.sound_three),
                v.findViewById(R.id.sound_four),
                v.findViewById(R.id.sound_five),
                v.findViewById(R.id.sound_six),
                v.findViewById(R.id.sound_seven),
        };
        return arr[id];
    }

    public static final int getSColorById(int id){
        int [] arr = {
                Color.BLACK,
                Color.BLUE,
                Color.RED,
                Color.GRAY,
                Color.GREEN,
                Color.CYAN,
                Color.YELLOW
        };
        return arr[id];
    }

    public static final int SOUND_FINISHING = 0;
    public static final int SOUND_COMMON = 1;


}
