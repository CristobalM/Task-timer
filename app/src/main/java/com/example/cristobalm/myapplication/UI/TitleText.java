package com.example.cristobalm.myapplication.UI;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.DragEvent;

import com.example.cristobalm.myapplication.R;

/**
 * Created by cristobalm on 3/18/17.
 */

public class TitleText extends AppCompatEditText {

    AttributeSet attrs;

    private float scale;


    public TitleText(Context context){
        super(context);
        init();
    }
    public TitleText(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.attrs = attrs;
        init();

    }
    public TitleText(Context context, AttributeSet attrs){
        super(context, attrs);
        this.attrs = attrs;
        init();
    }

    protected void init(){
        scale = getResources().getDisplayMetrics().density;
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);
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
