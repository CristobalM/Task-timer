package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by cristobalm on 3/7/17.
 */

public class GTDragShadowBuilder extends View.DragShadowBuilder {
    private static Drawable shadow;
    public GTDragShadowBuilder(View v){
        super(v);
        shadow = new ColorDrawable(Color.LTGRAY);
    }

    @Override
    public void onProvideShadowMetrics (Point size, Point touch){
        int width, height;

        width = getView().getWidth()/2;
        height = getView().getHeight()/2;

        shadow.setBounds(0, 0, width, height);
        size.set(width, height);
        touch.set(width/2, height/2);
    }

    @Override
    public void onDrawShadow(Canvas canvas){
        shadow.draw(canvas);
    }

}
