package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

/**
 * Created by cristobalm on 3/4/17.
 */

public class TimeDraggable extends AppCompatImageView {
    AttributeSet attrs;
    float scale;

    Path path;
    Paint paint;
    public TimeDraggable(Context context){
        super(context);
        init();
    }
    public TimeDraggable(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.attrs = attrs;
        init();
        int a;
    }
    public TimeDraggable(Context context, AttributeSet attrs){
        super(context, attrs);
        this.attrs = attrs;
        init();
    }


    protected void init(){
        scale = getResources().getDisplayMetrics().density;
        /*
        Drawable drawable;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            drawable = getResources().getDrawable(R.drawable.ic_draggable , null);
            drawable.setColorFilter(getResources().getColor(R.color.colorDraggable, null), PorterDuff.Mode.SRC_ATOP);

        }
        else{
            drawable =  getResources().getDrawable(R.drawable.ic_draggable);
            drawable.setColorFilter(getResources().getColor(R.color.colorDraggable), PorterDuff.Mode.SRC_ATOP);
        }
        setImageDrawable(drawable);
        */
        int color_bar = ContextCompat.getColor(getContext(), R.color.nicegray);

        paint = new Paint();
        path = new Path();

        paint.setColor(color_bar);
        paint.setStrokeWidth(VisualSettingGlobals.getPixels(1, scale));
        paint.setStyle(Paint.Style.STROKE);
        //paint.setARGB(255, 0, 0, 0);
        //paint.setPathEffect(new DashPathEffect(new float[]{2,20}, 0));

        setWillNotDraw(false);
    }

    @Override
    public void onDraw(Canvas canvas){
        path.moveTo(0,getMeasuredHeight());
        path.lineTo(0,0);
        path.lineTo(getMeasuredWidth(), 0);
        path.lineTo(getMeasuredWidth(), getMeasuredHeight());
        canvas.drawPath(path, paint);

        super.onDraw(canvas);
    }


}
