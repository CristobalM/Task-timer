package com.example.cristobalm.myapplication.UI.Globals;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/1/17.
 * Names for buttons specified in layout.activity_main.xml
 */


public class ButtonNameGlobals {
    public static final String BUTTON_PLAY = "button_play";
    public static final String BUTTON_STOP = "button_stop";
    public static final String BUTTON_PAUSE = "button_pause";
    public static final String BUTTON_ADD = "button_add";
    public static final String BUTTON_REPEAT = "button_repeat";
    public static ArrayList<String> getNamesList(){
        ArrayList<String> out = new ArrayList<>();
        out.add(BUTTON_PLAY);
        out.add(BUTTON_STOP);
        out.add(BUTTON_PAUSE);
        out.add(BUTTON_ADD);
        out.add(BUTTON_REPEAT);
        return out;
    }
    public static int getIndexByName(String name){
        if(name.equals(BUTTON_PLAY)){
            return 0;
        }
        else if(name.equals(BUTTON_STOP)){
            return 1;
        }
        else if(name.equals(BUTTON_PAUSE)){
            return 2;
        }
        else if(name.equals(BUTTON_ADD)){
            return 3;
        }
        else if(name.equals(BUTTON_REPEAT)){
            return 4;
        }
        else{
            return -1;
        }
    }


}
