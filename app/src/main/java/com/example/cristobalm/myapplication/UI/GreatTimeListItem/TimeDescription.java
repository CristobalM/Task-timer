package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.DragEvent;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

/**
 * Created by cristobalm on 3/4/17.
 * Edit text for each item
 */

public class TimeDescription extends AppCompatEditText {

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
        scale = getResources().getDisplayMetrics().density;
        setFocusableInTouchMode(true);
        int color_bar;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            color_bar = getResources().getColor(R.color.upbar, null);

        }
        else{
            color_bar = getResources().getColor(R.color.upbar);
        }


        setWillNotDraw(false);
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

