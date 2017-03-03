package com.example.cristobalm.myapplication.ObjectContainer;

/**
 * Created by cristobalm on 3/3/17.
 * Time container hours minutes seconds
 */

public class Time {
    private int milliseconds;

    public Time(int hours, int minutes, int seconds){
        milliseconds = toMilliseconds(hours, minutes, seconds);
    }
    public Time(String hours, String minutes, String seconds){
        milliseconds = toMilliseconds(Integer.parseInt(hours),
                Integer.parseInt(minutes), Integer.parseInt(seconds));
    }

    private int toMilliseconds(int hours, int minutes, int seconds){
        return hours*60*60*1000 + minutes*60*1000 + seconds + 1000;
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
        return  milliseconds/1000;
    }
}
