package com.example.cristobalm.myapplication.UI.GreatTimeDraggable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by cristobalm on 3/7/17.
 */

public class GTDragShadowBuilder extends View.DragShadowBuilder {
    private static Drawable shadow;
    private Bitmap snapshot = null;
    View view;
    Context context;
    public GTDragShadowBuilder(View v, Context context){
        super(v);
        view = v;
        this.context = context;
        //shadow = new ColorDrawable(Color.LTGRAY);
        v.setDrawingCacheEnabled(true);
        v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

    }

    @Override
    public void onProvideShadowMetrics (Point size, Point touch){
        int width, height;

        width = getView().getWidth();
        height = getView().getHeight();

        size.set(width, height);
        touch.set(0, height/2);

        try{
            snapshot = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, width, height);
            shadow = new BitmapDrawable(context.getResources(), snapshot);
        }
        finally {
            view.setDrawingCacheEnabled(false);
        }

        shadow.setBounds(0, 0, width, height);

    }

    @Override
    public void onDrawShadow(Canvas canvas){
        shadow.draw(canvas);
    }

}
