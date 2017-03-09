package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

/**
 * Created by cristobalm on 3/4/17.
 */

public class TimeDraggable extends View {
    AttributeSet attrs;
    float scale;
    public TimeDraggable(Context context){
        super(context);
        init();
    }
    public TimeDraggable(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.attrs = attrs;
        init();
    }
    public TimeDraggable(Context context, AttributeSet attrs){
        super(context, attrs);
        this.attrs = attrs;
        init();
    }


    protected void init(){
        //scale = getResources().getDisplayMetrics().density;
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        scale = getResources().getDisplayMetrics().density;
        int desiredHeight = VisualSettingGlobals.getPixels(50, scale);
        int desiredWidth = desiredHeight;
        int height;
        int width;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }
        else if(heightMode ==  MeasureSpec.AT_MOST){
            height = Math.min(desiredHeight, heightSize);
        }
        else{
            height = desiredHeight;
        }

        if(widthMode == MeasureSpec.EXACTLY){
            width = desiredWidth;
        }
        else if(widthMode ==  MeasureSpec.AT_MOST){
            width = Math.min(desiredWidth, widthSize);
        }
        else{
            width = desiredWidth;
        }

        setMeasuredDimension(width, height);
    }
    */
}
