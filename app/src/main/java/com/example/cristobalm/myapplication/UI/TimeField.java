package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;
import com.example.cristobalm.myapplication.UI.Listeners.Click.TimeFieldDelOnClickListener;
import com.example.cristobalm.myapplication.UI.Listeners.Click.TimeFieldUpDownOnClickListener;
import com.example.cristobalm.myapplication.R;


/**
 * Created by cristobalm on 3/1/17.
 * Object which encapsulates views for a new item created with add button.
 */

public class Timefield {
    private LinearLayout horizontal_fields_options;
    private LinearLayout vertical_positioning;
    private Context context;
    private int index;
    private MainActivity main_activity;

    private EditText hours_input;
    private EditText minutes_input;
    private EditText seconds_input;
    private EditText edit_text;

    private String pre_custom_text;
    private String pre_hours;
    private String pre_minutes;
    private String pre_seconds;
    private TimeContainer time_container;

    private Button del_button;
    private Button moveup_button;
    private Button movedown_button;

    Timefield(Context context, int index, MainActivity main_activity){
        this.context = context;
        this.index = index;
        pre_custom_text = "";
        pre_hours= "00";
        pre_minutes= "00";
        pre_seconds= "00";

        startTimefieldView(main_activity);

        Log.d("Timefield", "created new time field with index:"+String.valueOf(index));
    }
    public Timefield(Context context, int index, String custom_text, int milliseconds){
        this.context = context;
        this.index = index;
        pre_custom_text = custom_text;
        time_container = new TimeContainer(milliseconds);
        pre_hours = String.valueOf(time_container.getHours());
        pre_minutes = String.valueOf(time_container.getMinutes());
        pre_seconds = String.valueOf(time_container.getSeconds());
    }

    void startTimefieldView(MainActivity activity){
        this.main_activity = activity;
        initLayout();
        edit_text = createEditText(pre_custom_text);
        hours_input = createTimeInput(pre_hours);
        minutes_input = createTimeInput(pre_minutes);
        seconds_input = createTimeInput(pre_seconds);
        del_button = createDelButton();
        moveup_button = createMoveUpButton();
        movedown_button = createMoveDownButton();

        horizontal_fields_options.addView(hours_input);
        horizontal_fields_options.addView(minutes_input);
        horizontal_fields_options.addView(seconds_input);
        horizontal_fields_options.addView(edit_text);
        horizontal_fields_options.addView(del_button);
        horizontal_fields_options.addView(moveup_button);
        horizontal_fields_options.addView(movedown_button);

        vertical_positioning.addView(horizontal_fields_options);
    }


    private void initLayout(){
        horizontal_fields_options = createDefaultLinearLayout();
        horizontal_fields_options.setOrientation(LinearLayout.HORIZONTAL);
        vertical_positioning = createDefaultLinearLayout();
        vertical_positioning.setOrientation(LinearLayout.VERTICAL);

    }

    private LinearLayout.LayoutParams newLayoutParams(int width, int height){
        return new LinearLayout.LayoutParams(width, height);
    }

    private LinearLayout createDefaultLinearLayout(){
        LinearLayout default_llayout = new LinearLayout(context);
        default_llayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        return default_llayout;
    }
    private EditText createEditText(String text){
        EditText edit_text = new EditText(context);
        edit_text.setLayoutParams(newLayoutParams(300, LinearLayout.LayoutParams.MATCH_PARENT));
        edit_text.setTag("EditText_"+ String.valueOf(getListSize()));
        edit_text.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        edit_text.setTextColor(Color.BLACK);
        edit_text.setText(text);
        return edit_text;
    }
    private EditText createTimeInput(String text){
        EditText timer = new EditText(context);
        timer.setLayoutParams(newLayoutParams(80, 70));
        timer.setTag("TimeInput_EditText_" + String.valueOf(getListSize()));
        timer.setTextColor(Color.BLACK);
        timer.setInputType(InputType.TYPE_CLASS_NUMBER);
        timer.setText(text);
        timer.setCursorVisible(false);
        return timer;
    }
    private Button createDelButton(){
        Button delbutton = new Button(context);
        delbutton.setLayoutParams(newLayoutParams(70,70));
        delbutton.setBackgroundResource(R.drawable.ic_delete_black_24px);
        delbutton.setOnClickListener(new TimeFieldDelOnClickListener(this, main_activity));
        return delbutton;
    }
    private Button createMoveUpButton(){
        Button moveup_button = new Button(context);
        moveup_button.setLayoutParams(newLayoutParams(70,70));
        moveup_button.setOnClickListener(new TimeFieldUpDownOnClickListener(this, main_activity, VisualSettingGlobals.ORIENTATION_UP));
        moveup_button.setBackgroundResource(R.drawable.ic_arrow_upward_black_24px);
        return moveup_button;
    }
    private Button createMoveDownButton(){
        Button movedown_button = new Button(context);
        movedown_button.setLayoutParams(newLayoutParams(70,70));
        movedown_button.setOnClickListener(new TimeFieldUpDownOnClickListener(this, main_activity, VisualSettingGlobals.ORIENTATION_DOWN));
        movedown_button.setBackgroundResource(R.drawable.ic_arrow_downward_black_24px);
        return movedown_button;
    }

    void setIndex(int i){
        this.index = i;
    }

    public String getCustomText(){
        return edit_text != null ? edit_text.getText().toString() : "";
    }
    public int getIndex(){
        return index;
    }
    LinearLayout getLayout(){
        return vertical_positioning;
    }
    private int getListSize(){
        return main_activity.getTime_fields().size();
    }

    public int getHours(){
        return hours_input != null && hours_input.getText().length() > 0 ?  Integer.parseInt(hours_input.getText().toString()) : 0;
    }
    public int getMinutes(){
        return minutes_input != null && minutes_input.getText().length() > 0 ?  Integer.parseInt(minutes_input.getText().toString()) : 0;
    }
    public int getSeconds(){
        return seconds_input != null && seconds_input.getText().length() > 0 ?  Integer.parseInt(seconds_input.getText().toString()) : 0;
    }
    public int getMilliseconds(){
        return getHours()*60*60*1000 + getMinutes()*60*1000 + getSeconds()*1000;
    }
    public void blockInput(){
        edit_text.setFocusable(false);
        hours_input.setFocusable(false);
        minutes_input.setFocusable(false);
        seconds_input.setFocusable(false);
        horizontal_fields_options.removeView(del_button);
        horizontal_fields_options.removeView(moveup_button);
        horizontal_fields_options.removeView(movedown_button);
    }
    public void enableInput(){
        edit_text.setFocusableInTouchMode(true);
        hours_input.setFocusableInTouchMode(true);
        minutes_input.setFocusableInTouchMode(true);
        seconds_input.setFocusableInTouchMode(true);

        horizontal_fields_options.addView(del_button);
        horizontal_fields_options.addView(moveup_button);
        horizontal_fields_options.addView(movedown_button);
    }
}
