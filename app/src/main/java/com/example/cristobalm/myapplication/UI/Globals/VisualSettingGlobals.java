package com.example.cristobalm.myapplication.UI.Globals;

/**
 * Created by cristobalm on 3/1/17.
 * VisualSettingGlobals global integers which mean something to some code
 */

public class VisualSettingGlobals {
    public static final int ORIENTATION_UP = 1;
    public static final int ORIENTATION_DOWN = 0;


    public static int getPixels(int dps, float scale){
        return (int) (dps * scale + 0.5f);
    }
}
