package com.example.cristobalm.myapplication.Services;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/3/17.
 * Service Countdown, self descriptive
 */

class ServiceCountdown {
    private Timefield timefield;
    private CountDownTimer countDownTimer;
    private TimingService mService;
    private  int this_timer_num;

    private void createCountDown(){
        int milliseconds_remaining = mService.getMillisecondsRemaining()+5000;
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
                if(timefield != null && timefield.getTimeLinearLayout() != null) {
                    timefield.getTimeLinearLayout().setTime(to_record);
                }
                String _description = null;
                if(timefield != null){
                    _description = timefield.getCustomText();
                }
                if(mService != null && mService.getTimingNotifications() != null){
                    mService.getTimingNotifications().sendTimeNotification(
                            mService.getNotification_id(),
                            (_description == null ) ? "Error" : _description ,
                            to_record,
                            -1

                    );
                }
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
            if(this.timefield != null && this.timefield.getTimeLinearLayout() != null) {
                this.timefield.getTimeLinearLayout().setTime(this.timefield.getMilliseconds());
                if(this.timefield.getTimeLinearLayout().getTimeCountdownView() != null) {
                    this.timefield.getTimeLinearLayout().getTimeCountdownView().setBackgroundColor(ContextCompat.getColor(mService, R.color.colorCountdownBackground));
                    this.timefield.getTimeLinearLayout().getTimeCountdownView().invalidate();
                }
            }
            stopCountDown();
        }
        this.timefield = timefield;
        if(timefield.getTimeLinearLayout().getTimeCountdownView() != null) {
            this.timefield.getTimeLinearLayout().getTimeCountdownView().setBackgroundColor(Color.RED);

            timefield.getTimeLinearLayout().getTimeCountdownView().invalidate();
        }
        this.mService = mService;
        createCountDown();
        initCountDown();
    }
}
