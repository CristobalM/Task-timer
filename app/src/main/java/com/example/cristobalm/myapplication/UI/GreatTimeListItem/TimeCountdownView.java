package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

/**
 * Created by cristobalm on 3/4/17.
 */

public class TimeCountdownView extends AppCompatTextView {

    AttributeSet attrs;



    public TimeCountdownView(Context context){
        super(context);
        init();
    }
    public TimeCountdownView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.attrs = attrs;
        init();

    }
    public TimeCountdownView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.attrs = attrs;
        init();
    }


    protected void init(){
    }



    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        scale = getResources().getDisplayMetrics().density;
        int desiredHeight = VisualSettingGlobals.getPixels(50, scale);
        int desiredWidth =  VisualSettingGlobals.getPixels(150, scale);
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
