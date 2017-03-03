package com.example.cristobalm.myapplication.UI;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private EditText minutes_input;
    private EditText edit_text;

    private String pre_custom_text;
    private String pre_minutes;

    Timefield(Context context, int index, MainActivity main_activity){
        this.context = context;
        this.index = index;
        pre_custom_text = "";
        pre_minutes= "0";

        startTimefieldView(main_activity);

        Log.d("Timefield", "created new time field with index:"+String.valueOf(index));
    }
    public Timefield(Context context, int index, String custom_text, String minutes){
        this.context = context;
        this.index = index;
        pre_custom_text = custom_text;
        pre_minutes = minutes;
    }

    void startTimefieldView(MainActivity activity){
        this.main_activity = activity;
        initLayout();
        edit_text = createEditText(pre_custom_text);
        minutes_input = createMinutesInput(pre_minutes);
        Button del_button = createDelButton();
        Button moveup_button = createMoveUpButton();
        Button movedown_button = createMoveDownButton();
        TextView minutes_text = createMinutesText();

        horizontal_fields_options.addView(minutes_input);
        horizontal_fields_options.addView(minutes_text);
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
    private EditText createMinutesInput(String minutes){
        EditText timer = new EditText(context);
        timer.setLayoutParams(newLayoutParams(80, 70));
        timer.setTag("MinutesInput_EditText_" + String.valueOf(getListSize()));
        timer.setTextColor(Color.BLACK);
        timer.setInputType(InputType.TYPE_CLASS_NUMBER);
        timer.setText(minutes);
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
    private TextView createMinutesText(){
        TextView s_text = new TextView(context);
        s_text.setLayoutParams(newLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));
        s_text.setText(R.string.minutes_list_);
        s_text.setTextColor(Color.BLACK);
        return s_text;
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

    public int getMinutes(){
        return minutes_input != null && minutes_input.getText().length() > 0 ?  Integer.parseInt(minutes_input.getText().toString()) : 0;
    }

}
