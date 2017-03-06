package com.example.cristobalm.myapplication.UI;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;

/**
 * Created by cristobalm on 3/3/17.
 */

class TimeCountdown {
    Timefield timefield;
    CountDownTimer countDownTimer;
    TimingService mService;
    int progress_millis;
    int this_timer_num;

    private void setEditFields(Timefield timefield){
        this.timefield = timefield;
    }

    private void createCountDown(){
        int milliseconds_remaining = mService.getMillisecondsRemaining()+5000;
        progress_millis = milliseconds_remaining;
        this_timer_num = mService.getCurrent_timer_index();
        countDownTimer = new CountDownTimer(milliseconds_remaining, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                int now_timer_num = mService.getCurrent_timer_index();
                if(now_timer_num != this_timer_num){
                    return;
                }
                int to_record = mService.getMillisecondsRemaining();
                Log.d("onTick", "to_record:"+to_record);
                timefield.getTimeLinearLayout().setTime(to_record);
                progress_millis = to_record;
            }

            @Override
            public void onFinish() {
            }
        };
    }
    private void initCountDown(){
        if(countDownTimer != null){
            countDownTimer.start();
        }
    }
    void stopCountDown(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }
    void startNewCountDown(Timefield timefield, TimingService mService){
        if(countDownTimer != null){
            if(this.timefield != null) {
                this.timefield.getTimeLinearLayout().setTime(this.timefield.getMilliseconds());
                //this.timefield.setTimeTemp(this.timefield.getMilliseconds());
            }
            stopCountDown();
        }
        setEditFields(timefield);
        this.mService = mService;
        createCountDown();
        initCountDown();
    }
}
