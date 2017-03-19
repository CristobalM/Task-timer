package com.example.cristobalm.myapplication.UI.GreatTimeListItem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private ImageView timeMusic;
    AttributeSet attrs;

    HorizontalItemsContainer items_container;
    LinearLayout vertical_sidebar;

    private ImageView imageView;
    boolean toggled;

    KeyListener descriptionKeyListener;

    private int position;

    private float scale;


    LayoutParams imageViewParams;

    public void resetMusicBackgroudColor(){
        //timeMusic.getBackground().clearColorFilter();
        timeMusic.invalidate();
    }

    public void musicBackgroundColor(int color){
        //timeMusic.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        timeMusic.invalidate();
    }
    public void setMusicColor(int color){
        timeMusic.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        timeMusic.invalidate();
    }

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
    public int _px(int dp ){
        return VisualSettingGlobals.getPixels(dp, getScale());
    }
    private void init(){
        scale=  getResources().getDisplayMetrics().density;
        items_container = new HorizontalItemsContainer(context);
        vertical_sidebar = new LinearLayout(context);
        timeDescription = new TimeDescription(context);
        timeCountdownView = new TimeCountdownView(context);
        timeDraggable = new TimeDraggable(context);
        imageView = new ImageView(context);
        timeMusic = new ImageView(context);

        items_container.setTimeDescription(timeDescription);

        Typeface retrieveFont = VisualSettingGlobals.FontCache.get(VisualSettingGlobals.TEXT_FONT, context);



        items_container.setOrientation(LinearLayout.HORIZONTAL);


        //timeDraggable.setId(R.id.time_draggable); // DON'T EVER DO THIS
        int draggable_width = _px(30);
        timeDraggable.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, draggable_width));
        timeDraggable.setBackgroundColor(ContextCompat.getColor(context, R.color.upbar2));
        //timeDraggable.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_action_bar));
        //timeDraggable.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDraggable));


        //timeCountdownView.setId(R.id.time_countdown_view);// DON'T EVER DO THIS
        int countdown_width = VisualSettingGlobals.getPixels(120, scale);
        timeCountdownView.setLayoutParams(new LayoutParams(countdown_width, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        timeCountdownView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorCountdownBackground));
        timeCountdownView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        timeCountdownView.setGravity(Gravity.CENTER);
        timeCountdownView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        timeCountdownView.setPadding(_px(20),_px(10), _px(20), _px(10));
        if(retrieveFont != null) {
            timeCountdownView.setTypeface(retrieveFont);
        }

        //timeDescription.setId(R.id.time_description);// DON'T EVER DO THIS
        LayoutParams description_params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
        //description_params.leftMargin = _px(10);

        timeDescription.setLayoutParams(description_params);

        //timeDescription.setBackgroundColor(ContextCompat.getColor (context, R.color.colorDescriptionBackground));
        timeDescription.setBackgroundColor(ContextCompat.getColor (context, R.color.nicegray));
        //timeDescription.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        timeDescription.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        timeDescription.setHintTextColor(ContextCompat.getColor(context, R.color.light_gray_a));
        timeDescription.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        timeDescription.setSingleLine(false);
        timeDescription.setScrollContainer(true);
        timeDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        timeDescription.setPadding(_px(10),_px(0), _px(10), _px(20));
        timeDescription.setLines(3);
        timeDescription.setMaxLines(3);
        //timeDescription.setGravity(Gravity.CENTER_VERTICAL);
        if(retrieveFont != null) {
            timeCountdownView.setTypeface(retrieveFont);
        }

        Drawable music_drawable;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            music_drawable = getResources().getDrawable(R.drawable.ic_musical_note , null);
        }
        else{
            music_drawable =  getResources().getDrawable(R.drawable.ic_musical_note);
        }
        LayoutParams tmusic_params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
        //tmusic_params. = VisualSettingGlobals.getPixels(5,scale);
        tmusic_params.gravity = Gravity.CENTER;
        tmusic_params.topMargin = _px(10);
        timeMusic.setLayoutParams(tmusic_params);
        //timeMusic.setPadding(_px(7),0, _px(8), _px(3));
        timeMusic.setImageDrawable(music_drawable);
        //timeMusic.setBackgroundColor(ContextCompat.getColor (context, R.color.rednote));



        setTime(0,0,0);


        //items_container.addView(timeDraggable);
        //items_container.addView(timeCountdownView);
        //items_container.addView(timeMusic);

        vertical_sidebar.addView(timeCountdownView);
        vertical_sidebar.addView(timeMusic);
        vertical_sidebar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        vertical_sidebar.setOrientation(VERTICAL);
        vertical_sidebar.setBackgroundColor(ContextCompat.getColor(context, R.color.sidebar_vertical));

        items_container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        items_container.addView(timeDescription);
        items_container.addView(vertical_sidebar);

        this.setOrientation(VERTICAL);


        this.addView(timeDraggable);
        this.addView(items_container);


        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //int top_margin = VisualSettingGlobals.getPixels(5, scale);
        int bottom_margin = _px(5);
        params.setMargins(_px(20),0,_px(30),bottom_margin);


        this.setLayoutParams(params);

        this.invalidate();


        imageViewParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imageView.setLayoutParams(imageViewParams);
        //imageView.setBackgroundColor(Color.GRAY);

        imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray_a), PorterDuff.Mode.MULTIPLY);
        toggled = false;

        descriptionKeyListener = timeDescription.getKeyListener();

        position = -1;



/*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            setBackgroundColor(getResources().getColor(R.color.redBack, null));        }
        else{
            setBackgroundColor(getResources().getColor(R.color.redBack));        }
*/

        //paint.setARGB(255, 0, 0, 0);
        //paint.setPathEffect(new DashPathEffect(new float[]{2,20}, 0));
        //setWillNotDraw(false);
        //invalidate();
    }

    public void clearViews(){
        if(timeDraggable.getParent() != null){
            ((ViewGroup)timeDraggable.getParent()).removeView(timeDraggable);
        }

        if(items_container.getParent() != null){
            ((ViewGroup)items_container.getParent()).removeView(items_container);
        }
    }
    public void reAddViews(){
        this.addView(timeDraggable);
        this.addView(items_container);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCountdownListener(OnTouchListener listener){
        timeCountdownView.setOnTouchListener(listener);
    }
    public void setMusicListener(OnTouchListener listener){
        timeMusic.setOnTouchListener(listener);
    }

    public void setDescription(String description){
        Log.d("tll_setDescription","setting description: "+ description);
        timeDescription.setText(description);
    }
    public void setHint(int hint){
        timeDescription.setHint("Task " + String.valueOf(hint) + "");
    }
    public String getHint(){
        return timeDescription.getHint().toString();
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

    public static final int UP_DIRECTION = 1;
    public static final int DOWN_DIRECTION = 0;
    int current_dir=-1;
    public int getDirection(){
        return current_dir;
    }
    public void toggleDragInHover(Drawable the_shadow, int direction){
        current_dir = direction;
        if(!toggled) {
            imageView.setImageDrawable(the_shadow);
            if(direction == UP_DIRECTION) {
                clearViews();
                imageViewParams.bottomMargin = _px(15);
                imageView.setLayoutParams(imageViewParams);
                this.addView(imageView);

                reAddViews();
            }else if(direction == DOWN_DIRECTION){
                imageViewParams.topMargin = _px(15);
                imageView.setLayoutParams(imageViewParams);
                this.addView(imageView);
            }
        }
        toggled = true;
    }
    public void toggleDragOutHover(){
        if(toggled) {
            this.removeView(imageView);
            toggled = false;
            current_dir = -1;
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
