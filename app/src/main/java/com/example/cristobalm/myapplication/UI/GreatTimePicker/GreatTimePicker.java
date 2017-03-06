package com.example.cristobalm.myapplication.UI.GreatTimePicker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;


/**
 * Created by cristobalm on 3/5/17.
 */

public class GreatTimePicker extends FrameLayout {
    public static final int HOURS_TAG = 0;
    public static final int MINUTES_TAG = 1;
    public static final int SECONDS_TAG = 2;

    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;

    private Integer currentHours = 0;
    private Integer currentMinutes = 0;
    private Integer currentSeconds = 0;

    private OnTimeChangedListener mOnTimeChangedListener;


    public interface OnTimeChangedListener{
        void onTimeChanged(GreatTimePicker view, int hours, int minutes, int seconds);
    }

    public class GreatOnValueChangeListener implements NumberPicker.OnValueChangeListener{
        GreatTimePicker targetPicker;
        int tag;
        public GreatOnValueChangeListener(GreatTimePicker targetPicker, int tag){
            this.targetPicker = targetPicker;
            this.tag = tag;
        }
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal){
            switch (tag){
                case HOURS_TAG:
                    targetPicker.setCurrentHours(newVal);
                    break;
                case MINUTES_TAG:
                    targetPicker.setCurrentMinutes(newVal);
                    break;
                case SECONDS_TAG:
                    targetPicker.setCurrentSeconds(newVal);
                    break;
            }
            Log.d("GTPicker-onvalchange","oldval:"+oldVal+ ", newval:"+newVal);
            onTimeChanged();
        }
    }

    private static final OnTimeChangedListener NO_OP_CHANGE_LISTENER = new OnTimeChangedListener() {
        @Override
        public void onTimeChanged(GreatTimePicker view, int hours, int minutes, int seconds) {
            Log.d("donothing","donothingilstener: hours"+hours+", minutes:"+minutes+", seconds:"+seconds);
        }
    };

    public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER = new NumberPicker.Formatter() {
        @Override
        public String format(int value) {
            return String.format("%02d", value);
        }
    };

    public GreatTimePicker(Context context){
        super(context);
        init();
    }
    public GreatTimePicker(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public GreatTimePicker(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }


    @Override
    public int getBaseline(){
        return hoursPicker.getBaseline();
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        hoursPicker.setEnabled(enabled);
        minutesPicker.setEnabled(enabled);
        secondsPicker.setEnabled(enabled);
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.great_time_picker_widget, this);

        hoursPicker = (NumberPicker) findViewById(R.id.hours_picker);
        minutesPicker = (NumberPicker) findViewById(R.id.minutes_picker);
        secondsPicker = (NumberPicker) findViewById(R.id.seconds_picker);

        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(23);
        hoursPicker.setFormatter(TWO_DIGIT_FORMATTER);
        hoursPicker.setOnValueChangedListener(new GreatOnValueChangeListener(this, HOURS_TAG));

        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setFormatter(TWO_DIGIT_FORMATTER);
        minutesPicker.setOnValueChangedListener(new GreatOnValueChangeListener(this, MINUTES_TAG));

        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setFormatter(TWO_DIGIT_FORMATTER);
        secondsPicker.setOnValueChangedListener(new GreatOnValueChangeListener(this, SECONDS_TAG));

        setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);

        setCurrentHours(0);
        setCurrentMinutes(0);
        setCurrentSeconds(0);
    }


    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener){
        mOnTimeChangedListener = onTimeChangedListener;
    }

    private void onTimeChanged(){
        Log.d("su", "PrivadoOntimechanged in el picker hours:"+getCurrentHours()+", minutes:"+getCurrentMinutes()+", seconds:"+getCurrentSeconds());
        mOnTimeChangedListener.onTimeChanged(this, getCurrentHours(), getCurrentMinutes(), getCurrentSeconds());
    }

    private void updateHoursDisplay(){
        hoursPicker.setValue(currentHours);
        onTimeChanged();
    }

    private void updateMinutesDisplay(){
        minutesPicker.setValue(currentMinutes);
        onTimeChanged();
    }
    private void updateSecondsDisplay(){
        secondsPicker.setValue(currentSeconds);
        onTimeChanged();
    }

    public void setCurrentHours(Integer currentHours){
        this.currentHours = currentHours;
        updateHoursDisplay();
    }
    public void setCurrentMinutes(Integer currentMinutes){
        this.currentMinutes = currentMinutes;
        updateMinutesDisplay();
    }
    public void setCurrentSeconds(Integer currentSeconds){
        this.currentSeconds = currentSeconds;
        updateSecondsDisplay();
    }

    public Integer getCurrentHours(){
        return currentHours;
    }
    public Integer getCurrentMinutes(){
        return currentMinutes;
    }
    public Integer getCurrentSeconds(){
        return currentSeconds;
    }
    public TimeContainer getTimeContainer(){
        return new TimeContainer(currentHours, currentMinutes, currentSeconds);
    }

}
