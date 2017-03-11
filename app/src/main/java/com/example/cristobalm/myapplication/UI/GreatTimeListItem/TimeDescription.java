package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.ClipDescription;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

/**
 * Created by cristobalm on 3/4/17.
 * Edit text for each item
 */

public class TimeDescription extends EditText {

    AttributeSet attrs;

    private float scale;


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
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);
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

        setMeasuredDimension(widthSize - VisualSettingGlobals.getPixels(100,scale), height);
    }

    @Override
    public boolean onDragEvent(DragEvent event){
        switch (event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
            default:
                return super.onDragEvent(event);
        }
    }


}

