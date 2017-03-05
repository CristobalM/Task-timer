package com.example.cristobalm.myapplication.Views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.TimeCountdown;

/**
 * Created by cristobalm on 3/4/17.
 */

public class TimeLinearLayout extends LinearLayout {

    Context context;
    TimeDescription timeDescription;
    TimeCountdownView timeCountdownView;
    TimeDraggable timeDraggable;
    AttributeSet attrs;
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

    private void init(){
        inflate(getContext(), R.layout.time_item, this);

        //LinearLayout time_item = (LinearLayout) findViewById(R.id.time_item);

    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();

        timeDescription = (TimeDescription) this.findViewById(R.id.time_description);
        timeCountdownView = (TimeCountdownView) this.findViewById(R.id.time_countdown_view);
        timeDraggable = (TimeDraggable) this.findViewById(R.id.time_draggable);

        timeDescription.setVisibility(VISIBLE);

        timeDescription.setBackgroundColor(Color.BLUE);
        timeCountdownView.setBackgroundColor(Color.GRAY);
        timeDraggable.setBackgroundResource(R.drawable.ic_drag_handle_black_24px);
    }

}
