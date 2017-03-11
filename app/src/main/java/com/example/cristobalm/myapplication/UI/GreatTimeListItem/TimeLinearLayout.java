package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

/**
 * Created by cristobalm on 3/4/17.
 */

public class TimeLinearLayout extends LinearLayout {

    private Context context;
    private TimeDescription timeDescription;
    private TimeCountdownView timeCountdownView;
    private TimeDraggable timeDraggable;
    AttributeSet attrs;

    LinearLayout items_container;

    private ImageView imageView;
    boolean toggled;

    KeyListener descriptionKeyListener;

    private int position;

    private float scale;

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

    public float getScale(){
        return scale;
    }

    private void init(){
        scale=  getResources().getDisplayMetrics().density;
        items_container = new LinearLayout(context);
        timeDescription = new TimeDescription(context);
        timeCountdownView = new TimeCountdownView(context);
        timeDraggable = new TimeDraggable(context);
        imageView = new ImageView(context);

        Typeface retrieveFont = VisualSettingGlobals.FontCache.get(VisualSettingGlobals.TEXT_FONT, context);



        items_container.setOrientation(LinearLayout.HORIZONTAL);


        //timeDraggable.setId(R.id.time_draggable); // DON'T EVER DO THIS
        int draggable_width = VisualSettingGlobals.getPixels(50, scale);
        timeDraggable.setLayoutParams(new LayoutParams(draggable_width, LayoutParams.MATCH_PARENT));
        timeDraggable.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDraggable));


        //timeCountdownView.setId(R.id.time_countdown_view);// DON'T EVER DO THIS
        int countdown_width = VisualSettingGlobals.getPixels(100, scale);
        timeCountdownView.setLayoutParams(new LayoutParams(countdown_width, LayoutParams.MATCH_PARENT));
        timeCountdownView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorCountdownBackground));
        timeCountdownView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        timeCountdownView.setGravity(Gravity.CENTER);
        if(retrieveFont != null) {
            timeCountdownView.setTypeface(retrieveFont);
        }

        //timeDescription.setId(R.id.time_description);// DON'T EVER DO THIS
        LayoutParams description_params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        timeDescription.setLayoutParams(description_params);
        timeDescription.setBackgroundColor(ContextCompat.getColor (context, R.color.colorDescriptionBackground));
        timeDescription.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        timeDescription.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        timeDescription.setPadding(VisualSettingGlobals.getPixels(10,scale),
                VisualSettingGlobals.getPixels(0, scale),
                VisualSettingGlobals.getPixels(10,scale),
                VisualSettingGlobals.getPixels(0,scale));
        timeDescription.setGravity(Gravity.CENTER_VERTICAL);
        if(retrieveFont != null) {
            timeCountdownView.setTypeface(retrieveFont);
        }


        setTime(0,0,0);


        items_container.addView(timeDraggable);
        items_container.addView(timeDescription);
        items_container.addView(timeCountdownView);

        items_container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        //RelativeLayout.LayoutParams draggable_params = (RelativeLayout.LayoutParams) timeDraggable.getLayoutParams();
        //RelativeLayout.LayoutParams description_params = (RelativeLayout.LayoutParams) timeDescription.getLayoutParams();
        //RelativeLayout.LayoutParams countdown_params = (RelativeLayout.LayoutParams) timeCountdownView.getLayoutParams();

        //draggable_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //timeDraggable.setLayoutParams(draggable_params);

        //description_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //description_params.addRule(RelativeLayout.RIGHT_OF, R.id.time_draggable);
        //timeDescription.setLayoutParams(description_params);

        //countdown_params.addRule(RelativeLayout.RIGHT_OF, R.id.time_description);
        //timeCountdownView.setLayoutParams(countdown_params);



        this.addView(items_container);


        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //int top_margin = VisualSettingGlobals.getPixels(5, scale);
        int bottom_margin = VisualSettingGlobals.getPixels(1, scale);
        params.setMargins(0,0,0,bottom_margin);


        this.setLayoutParams(params);

        this.invalidate();

        imageView.setLayoutParams(new LayoutParams(200, 200));
        imageView.setBackgroundColor(Color.GRAY);
        imageView.setColorFilter(Color.RED);
        toggled = false;

        descriptionKeyListener = timeDescription.getKeyListener();

        position = -1;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCountdownListener(OnTouchListener listener){
        timeCountdownView.setOnTouchListener(listener);
    }

    public void setDescription(String description){
        Log.d("tll_setDescription","setting description: "+ description);
        timeDescription.setText(description);
    }
    public void setHint(int hint){
        timeDescription.setHint("Task " + String.valueOf(hint));
    }

    public void stopEditables(){
        timeDescription.setFocusable(false);
        timeCountdownView.setFocusable(false);
    }



    public void enableEditables(){
        timeDescription.setFocusableInTouchMode(true);
        timeDescription.setFocusable(true);

        timeCountdownView.setFocusableInTouchMode(true);
    }

    public void setDraggableClickListener(OnTouchListener onLongClickListener){
        timeDraggable.setOnTouchListener(onLongClickListener);
    }

    public TimeDraggable getTimeDraggable(){
        return timeDraggable;
    }
    public TimeDescription getTimeDescription(){
        return timeDescription;
    }
    public TimeCountdownView getTimeCountdownView(){
        return timeCountdownView;
    }

    public void changeBackgroundColor(int color){
        timeDescription.setBackgroundColor(color);
    }

    public void toggleDragInHover(){
        if(!toggled) {
            this.addView(imageView);
        }
        toggled = true;
    }
    public void toggleDragOutHover(){
        if(toggled) {
            this.removeView(imageView);
            toggled = false;
        }

    }
    public void restoreNotDragState(){
        if(toggled){
            this.removeView(imageView);
            toggled = false;
        }
        timeDescription.setFocusableInTouchMode(true);
    }

    public void setPosition(int pos){
        position = pos;
    }
    public int getPosition(){
        return position;
    }



}
