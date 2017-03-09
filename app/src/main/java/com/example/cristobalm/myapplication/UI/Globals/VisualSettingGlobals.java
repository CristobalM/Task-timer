package com.example.cristobalm.myapplication.UI.Globals;

import android.content.Context;
import android.graphics.Typeface;

import com.example.cristobalm.myapplication.UI.MainActivity;

import java.util.Hashtable;

/**
 * Created by cristobalm on 3/1/17.
 * VisualSettingGlobals global integers which mean something to some code
 */

public class VisualSettingGlobals {
    public static final int ORIENTATION_UP = 1;
    public static final int ORIENTATION_DOWN = 0;

    public static final String TEXT_FONT = "fonts/Open_Sans/OpenSans-Semibold.ttf";


    public static int getPixels(int dps, float scale){
        return (int) (dps * scale + 0.5f);
    }

    public static class FontCache {
        private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

        public static Typeface get(String name, Context context){
            Typeface tf = fontCache.get(name);
            if(tf == null){
                try{
                    tf = Typeface.createFromAsset(context.getAssets(), name);
                }
                catch(Exception e){
                    return null;
                }
                fontCache.put(name, tf);
            }
            return tf;
        }

    }


}
