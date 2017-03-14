package com.example.cristobalm.myapplication.UI.ListFragment;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

/**
 * Created by cristobalm on 3/12/17.
 */


public class ListName extends AppCompatTextView {

    AttributeSet attrs;

    private float scale;


    public ListName(Context context){
        super(context);
        init();
    }
    public ListName(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.attrs = attrs;
        init();

    }
    public ListName(Context context, AttributeSet attrs){
        super(context, attrs);
        this.attrs = attrs;
        init();
    }

    protected void init(){
        setFocusableInTouchMode(true);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        scale = getResources().getDisplayMetrics().density;
        int desiredHeight = VisualSettingGlobals.getPixels(50, scale) ;
        int height;

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

        setMeasuredDimension(widthSize - VisualSettingGlobals.getPixels(40,scale), height);
    }

}

