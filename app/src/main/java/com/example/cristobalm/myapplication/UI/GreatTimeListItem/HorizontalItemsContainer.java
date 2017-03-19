package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

/**
 * Created by cristobalm on 3/18/17.
 */

public class HorizontalItemsContainer extends LinearLayout {
    Context context;
    AttributeSet attrs;

    Path path;
    Paint paint;
    float scale;

    public HorizontalItemsContainer(Context context){
        super(context);
        this.context = context;
        init();
    }
    public HorizontalItemsContainer(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.context = context;
        this.attrs = attrs;
        init();
    }
    public HorizontalItemsContainer(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }
    public void init(){
        scale=  getResources().getDisplayMetrics().density;

        paint = new Paint();
        path = new Path();
        int color_bar = ContextCompat.getColor( context, R.color.upbar);


        paint.setColor(color_bar);
        paint.setStrokeWidth(VisualSettingGlobals.getPixels(5, scale));
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setTimeDescription(TimeDescription timeDescription){
        this.timeDescription = timeDescription;
    }
    TimeDescription timeDescription;

    @Override
    protected void dispatchDraw(Canvas canvas){

        super.dispatchDraw(canvas);
        path.moveTo(0,0);
        path.lineTo(0,getMeasuredHeight());
        path.lineTo(timeDescription.getMeasuredWidth(),getMeasuredHeight());
        //path.lineTo(timeDescription.getMeasuredWidth(), timeDraggable.getMeasuredHeight());
        canvas.drawPath(path, paint);
        //super.onDraw(canvas);
    }

}
