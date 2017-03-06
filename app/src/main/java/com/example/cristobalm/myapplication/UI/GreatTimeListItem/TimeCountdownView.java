package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.cristobalm.myapplication.R;

/**
 * Created by cristobalm on 3/4/17.
 */

public class TimeCountdownView extends TextView {
    String text;
    int text_color;
    AttributeSet attrs;

    Paint paint_bar = new Paint();
    Path path_bar;

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
        text = "Description";
        text_color = Color.BLACK;
        if(attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TimeDescription);
            text = typedArray.getString(R.styleable.TimeDescription_android_text);
            text_color = typedArray.getColor(R.styleable.TimeDescription_android_textColor, Color.BLACK);
        }
        paint_bar.setARGB(250, 180, 180, 180);
        paint_bar.setStyle(Paint.Style.STROKE);
        paint_bar.setPathEffect(new DashPathEffect(new float[] {1, 5}, 0));
        path_bar = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas){
        float startx = 0;
        float starty = 10;
        float endy = getMeasuredHeight()-7;
        path_bar.moveTo(startx, starty);
        path_bar.lineTo(startx, endy);
        canvas.drawPath(path_bar, paint_bar);

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


        setMeasuredDimension(widthSize/4, height);
    }
}
