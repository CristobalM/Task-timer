package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/7/17.
 */

public class ThrashCan extends ImageView {

    AttributeSet attrs;



    public ThrashCan(Context context){
        super(context);
        init();
    }
    public ThrashCan(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.attrs = attrs;
        init();

    }
    public ThrashCan(Context context, AttributeSet attrs){
        super(context, attrs);
        this.attrs = attrs;
        init();
    }

    protected void init(){
        //setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_black_24px, 0));
        Drawable drawable;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
           drawable = getResources().getDrawable(R.drawable.ic_delete_black_24px, null);
        }
        else{
            drawable =  getResources().getDrawable(R.drawable.ic_delete_black_24px);
        }
        setImageDrawable(drawable);
    }

    @Override
    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int desiredHeight = 100;
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

        setMeasuredDimension(widthSize*2/3, height);
    }




}
