package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;

/**
 * Created by cristobalm on 3/4/17.
 */

public class TimeLinearLayout extends LinearLayout {

    Context context;
    TimeDescription timeDescription;
    TimeCountdownView timeCountdownView;
    TimeDraggable timeDraggable;
    AttributeSet attrs;



    public void setTime(int hours, int minutes, int seconds){
        TimeContainer timeContainer = new TimeContainer(hours, minutes, seconds);
        timeCountdownView.setText(timeContainer.getTimeString());
    }
    public void setTime(TimeContainer timeContainer){
        timeCountdownView.setText(timeContainer.getTimeString());
    }
    public void setTime(int milliseconds){
        TimeContainer timeContainer = new TimeContainer(milliseconds);
        timeCountdownView.setText(timeContainer.getTimeString());
    }

    public String getDescription(){
        return timeDescription == null ? "" : timeDescription.getText().toString();
    }

    public TimeLinearLayout(Context context){
        super(context);
        this.context = context;
        init();
    }
    public TimeLinearLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.context = context;
        this.attrs = attrs;
        init();
    }
    public TimeLinearLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }
    private int getPixels(int dp){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)(dp*scale + 0.5f);
    }

    private void init(){
        timeDescription = new TimeDescription(context);
        timeCountdownView = new TimeCountdownView(context);
        timeDraggable = new TimeDraggable(context);
        setOrientation(HORIZONTAL);
        int a;
        timeDescription.setId(R.id.time_description);
        timeDescription.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        timeDescription.setBackgroundColor(ContextCompat.getColor (context, R.color.colorDescription));
        timeDescription.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        timeDescription.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        timeDescription.setPadding(getPixels(10), getPixels(0), getPixels(10), getPixels(0));
        timeDescription.setText(R.string.task);

        timeCountdownView.setId(R.id.time_countdown_view);
        timeCountdownView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        timeCountdownView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorCountDown));
        timeCountdownView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        timeCountdownView.setGravity(Gravity.CENTER);
        setTime(0,0,0);

        timeDraggable.setId(R.id.time_draggable);
        timeDraggable.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        timeDraggable.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlack));


        this.addView(timeDescription);
        this.addView(timeCountdownView);
        this.addView(timeDraggable);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCountdownListener(OnClickListener listener){
        timeCountdownView.setOnClickListener(listener);
    }

    public void setDescription(String description){
        timeDescription.setText(description);
    }

    public void stopEditables(){
        timeDescription.setFocusable(false);
        timeCountdownView.setFocusable(false);
    }

    public void enableEditables(){
        timeDescription.setFocusableInTouchMode(true);
        timeCountdownView.setFocusableInTouchMode(true);
    }


}
