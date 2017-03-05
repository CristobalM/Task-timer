package com.example.cristobalm.myapplication.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * Created by cristobalm on 3/4/17.
 */

public class PieChart extends EditText {
    public PieChart(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        int x = 80;
        int y = 80;
        int radius = 60;
        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawCircle(x, y, radius, paint);
        super.onDraw(canvas);
    }
}
