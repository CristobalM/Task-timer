package com.example.cristobalm.myapplication;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/1/17.
 * Names for buttons specified in layout.activity_main.xml
 */


class ButtonName {
    static final String BUTTON_PLAY = "button_play";
    static final String BUTTON_STOP = "button_stop";
    static final String BUTTON_PAUSE = "button_pause";
    static final String BUTTON_ADD = "button_add";
    static ArrayList<String> getNamesList(){
        ArrayList<String> out = new ArrayList<>();
        out.add(BUTTON_PLAY);
        out.add(BUTTON_STOP);
        out.add(BUTTON_PAUSE);
        out.add(BUTTON_ADD);
        return out;
    }
}
