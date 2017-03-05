package com.example.cristobalm.myapplication.UI;

import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;

/**
 * Created by cristobalm on 3/3/17.
 */

public class TimeCountdown {
    TextView edit_remaining_time;
    CountDownTimer countDownTimer;
    int progress_millis;
    public TimeCountdown(){

    }
    public void setEditFields(TextView edit_remaining_time){
        this.edit_remaining_time = edit_remaining_time;
    }

    public void createCountDown(int milliseconds_remaining){
        progress_millis = milliseconds_remaining;
        countDownTimer = new CountDownTimer(milliseconds_remaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeContainer timeContainer = new TimeContainer((int) millisUntilFinished);
                edit_remaining_time.setText(String.valueOf(timeContainer.getTimeString()));
                progress_millis = (int) millisUntilFinished;
            }

            @Override
            public void onFinish() {
            }
        };
    }
    public void initCountDown(){
        if(countDownTimer != null){
            countDownTimer.start();
        }
    }
    public void resumeCountDown(){
        createCountDown(progress_millis);
    }
    public void stopCountDown(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }
    public void startNewCountDown(TextView edit_remaining_time_,
                                  int milliseconds_remaining){
        setEditFields(edit_remaining_time_);
        createCountDown(milliseconds_remaining);
        initCountDown();
    }


}
