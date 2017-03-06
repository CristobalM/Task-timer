package com.example.cristobalm.myapplication.ObjectContainer;

import android.util.Log;

/**
 * Created by cristobalm on 3/3/17.
 * TimeContainer container hours minutes seconds
 */

public class TimeContainer {
    private int milliseconds;

    public TimeContainer(int milliseconds){
        this.milliseconds = milliseconds;
    }
    public TimeContainer(int hours, int minutes, int seconds){
        milliseconds = toMilliseconds(hours, minutes, seconds);
    }
    public TimeContainer(String hours, String minutes, String seconds){
        milliseconds = toMilliseconds(Integer.parseInt(hours),
                Integer.parseInt(minutes), Integer.parseInt(seconds));
    }
    public TimeContainer(String milliseconds){
        this.milliseconds = Integer.parseInt(milliseconds);
    }

    private int toMilliseconds(int hours, int minutes, int seconds){
        return hours*60*60*1000 + minutes*60*1000 + seconds*1000;
    }

    public void setMilliseconds(int milliseconds){
        this.milliseconds = milliseconds;
    }




    public int getHours(){
        return totalHours();
    }
    public int getMinutes(){
        return totalMinutes()%60;
    }
    public int getSeconds(){
        return totalSeconds()%60;
    }

    private int totalHours(){
        return totalMinutes()/60;
    }
    private int totalMinutes(){
        return totalSeconds()/60;
    }
    private int totalSeconds(){
        return  (int)(((double)milliseconds)/1000 + 0.5);
    }

    //TimeContainer time_container = new TimeContainer(times.get(current_timer_index));
    public int getMilliseconds(){
        return milliseconds;
    }
    public String getTimeString() {
        return String.format("%02d:%02d:%02d", getHours(), getMinutes(), getSeconds());
    }

    public static final int getHours(int _millis){
        return totalHours(_millis);
    }

    public static final int getMinutes(int _millis){
        return totalMinutes(_millis)%60;
    }

    public static final int getSeconds(int _millis){
        return totalSeconds(_millis)%60;
    }

    public static final int totalHours(int _millis){
        return totalMinutes(_millis)/60;
    }
    public static final int totalMinutes(int _millis){
        return totalSeconds(_millis)/60;
    }
    public static final int totalSeconds(int _millis){
        return (int)(((double)_millis)/1000 + 0.5);
    }

    public static final String getTimeString(int _millis){
        return String.format("%02d:%02d:%02d", getHours(_millis), getMinutes(_millis), getSeconds(_millis));
    }
}
