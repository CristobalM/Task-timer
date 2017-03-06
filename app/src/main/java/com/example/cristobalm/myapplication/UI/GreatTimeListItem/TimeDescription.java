package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;

import com.example.cristobalm.myapplication.R;

/**
 * Created by cristobalm on 3/4/17.
 */

public class TimeDescription extends EditText {

    AttributeSet attrs;
    String text;

    int text_color;
    public TimeDescription(Context context){
        super(context);
        init();
    }
    public TimeDescription(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.attrs = attrs;
        init();

    }
    public TimeDescription(Context context, AttributeSet attrs){
        super(context, attrs);
        this.attrs = attrs;
        init();
    }

    protected void init(){
        text = "Description";
        text_color = Color.BLACK;

        if(attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TimeDescription);
            text = typedArray.getString(R.styleable.TimeDescription_android_text);
            text_color = typedArray.getColor(R.styleable.TimeDescription_android_textColor, Color.BLACK);
        }

    }

    @Override
    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int desiredHeight = 100;
        int height;

        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);
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

        setMeasuredDimension(widthSize/2, height);
    }
}

