package com.example.cristobalm.myapplication.Services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.FilenameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.Storage.StateStorage;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.ListFragment.ListItemInfo;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Created by cristobalm on 3/2/17.
 * test
 */

public class TimingService extends Service {
    private int mStartMode;
    private int current_state = MainStateGlobals.STATE_IDLE;
    private ArrayList<Integer> time_fields;
    private int current_timer_index;
    AlarmManager alarmManager;
    TimingNotifications timingNotifications;
    private int notification_id;
    private long nextMillis = 0;
    private MainActivity main_activity;
    StateStorage stateStorage;
    StateStorage configStates;
    StateStorage listsData;
    ServiceCountdown serviceCountdown;

    Hashtable<Integer, Timefield> map_timefields;


    private Boolean repeatState;

    private long last_remaining_millis;

    private final IBinder mBinder = new LocalBinder();

    private Integer totalSeconds;

    ArrayList<String> fileListNames;

    ArrayList<ListItemInfo> listItemInfoArrayList;

    //to stop or not to stop
    boolean openingDialogDragment = false;
    boolean waitingForScheduled = false;


    Integer current_index_file;



    int millis_editing = -1;

    int to_load_file;

    public void loadFile(int index_file){
        to_load_file = index_file;
    }
    public void startFile(){
        if(to_load_file == getCurrentIndexFile()){
            return;
        }
        saveFile();
        current_index_file = to_load_file;
        getStateStorage().saveLastIndexFile(to_load_file);
        map_timefields = null;
        time_fields = null;
        map_timefields = retrieveMapTimefields();
        time_fields = retrieveTimefields();
        if(main_activity != null){
            main_activity.setTimefields(time_fields);
            main_activity.setMapTimeFields(map_timefields);
            //main_activity.reloadList();
            main_activity.startTimeFieldsDisplay();
            if(main_activity.getTitle_list() != null){
                main_activity.getTitle_list().setHint(getTitleHint());
                main_activity.getTitle_list().setText(getFileName(getCurrentIndexFile()));
            }

            main_activity.reloadButtonStates();

        }
    }

    public void clearOtherBackground(){
        if(listItemInfoArrayList != null && listItemInfoArrayList.size() > to_load_file){
            ListItemInfo listItemInfo = listItemInfoArrayList.get(to_load_file);
            listItemInfo.setBackgroundColor(R.color.colorDescriptionBackground);
        }
    }

    public void setTitle(String title){
        getFileListNames().set(getCurrentIndexFile(), title);
        if(listItemInfoArrayList != null && listItemInfoArrayList.size() > getCurrentIndexFile()){
            listItemInfoArrayList.get(getCurrentIndexFile()).setFile_name(title);
        }
    }
    public String getTitle(){
        return getFileName(getCurrentIndexFile());
    }
    public String getTitleHint(){
        return "File "+(getCurrentIndexFile()+1) + " (Touch to edit)";
    }

    public int getMillisEditing(){
        return millis_editing;
    }
    public void setMillisEditing(int _millis){
        millis_editing = _millis;
    }



    public Timefield getTFAt(int index){
        return retrieveMapTimefields().get(retrieveTimefields().get(index));
    }
    public Timefield getTFwithID(int static_id){
        return retrieveMapTimefields().get(static_id);
    }
    public void addTimefield(Timefield timefield){
        retrieveMapTimefields().put(retrieveTimefields().size(), timefield);
        retrieveTimefields().add(retrieveTimefields().size());
    }

    public long getLastRemainingMillis(){
        return last_remaining_millis;
    }
    public void setLastRemainingMillis(long _millis){
        last_remaining_millis = _millis;
    }


    public int getTotalSeconds(){
        if(totalSeconds == null){
            totalSeconds = 0;
            for(int i = 0; i < retrieveTimefields().size(); i++){
                totalSeconds += getTFAt(i).getMilliseconds()/1000;
            }
        }
        return totalSeconds;
    }
    public void addSecondsToTotal(int seconds){
        getTotalSeconds();
        totalSeconds += seconds;
        Log.d("addSecondsToTotal", "totalSeconds is = " + totalSeconds);
    }

    public StateStorage getStateStorage(){
        if(stateStorage == null) {
            stateStorage = new StateStorage(getApplicationContext(), FilenameGlobals.STORED_STATES);
        }
        return stateStorage;
    }
    public StateStorage getConfigStates(){
        if(configStates == null) {
            configStates = new StateStorage(getApplicationContext(), FilenameGlobals.CONFIG_STATES);
        }
        return configStates;
    }

    public StateStorage getListsData(){
        if(listsData == null){
            listsData = new StateStorage(getApplicationContext(), FilenameGlobals.CONFIG_STATES);
        }
        return listsData;
    }

    public ArrayList<String> getFileListNames(){
        if(fileListNames == null){
            fileListNames = getListsData().getFileListNames();
            if(fileListNames == null){
                fileListNames = new ArrayList<>();
                fileListNames.add("");
                saveFileListNames();
            }
        }
        return fileListNames;
    }

    public void saveFileListNames(){
        getListsData().saveFileListNames(fileListNames);
    }

    public ArrayList<ListItemInfo> builtListItemInfoArrayList (){
        if(listItemInfoArrayList == null) {
            ArrayList<String> f_list_names = getFileListNames();
            if(f_list_names != null) {
                listItemInfoArrayList = new ArrayList<>(f_list_names.size());
                for (int i = 0; i < f_list_names.size(); i++) {
                    ListItemInfo listItemInfo = new ListItemInfo(this, this, i);
                    listItemInfo.setFile_name(f_list_names.get(i));
                    listItemInfo.setHint("File " + (i+1));

                    listItemInfoArrayList.add(listItemInfo);
                }
            }else{
                listItemInfoArrayList = new ArrayList<>();
            }
        }
        return listItemInfoArrayList;
    }


    public void newFile(){
        saveFile();
        current_index_file = getFileListNames().size();
        getStateStorage().saveLastIndexFile(current_index_file);
        map_timefields = new Hashtable<>();
        time_fields = new ArrayList<>();
        saveFile();
        fileListNames.add("");
        if(main_activity != null){
            main_activity.setTimefields(time_fields);
            main_activity.setMapTimeFields(map_timefields);
            main_activity.reloadList();
            if(main_activity.getTitle_list() != null){
                main_activity.getTitle_list().setHint(getTitleHint());
                main_activity.getTitle_list().setText("");
            }
            main_activity.reloadButtonStates();

        }
        if(listItemInfoArrayList != null){
            ListItemInfo listItemInfo = new ListItemInfo(this, this, getCurrentIndexFile());
            listItemInfo.setFile_name("");
            listItemInfo.setHint("File " + (getCurrentIndexFile()+1));
            listItemInfoArrayList.add(listItemInfo);
        }

    }


    public void saveFile(){
        Log.d("saveFile", "saving data of index " + getCurrentIndexFile());
        getStateStorage().saveFileList(toStoreCurrentData(), getCurrentIndexFile());
    }

    public int getCurrentIndexFile(){
        if(current_index_file == null){
            Log.d("getCurrentIndexFile", "retrieving current index file");
            current_index_file = getStateStorage().getLastIndexFile();
            if(getFileListNames().size() <= current_index_file || current_index_file < 0){
                current_index_file = 0;
                getStateStorage().saveLastIndexFile(0);
                if(getFileListNames().size() == 0){
                    fileListNames.add("");
                    saveFileListNames();
                }
            }
        }else{
            Log.d("getCurrentIndexFile", "NOT retrieving current index file");

        }
        return current_index_file;
    }

    Pair<Hashtable<Integer, Timefield>, ArrayList<Integer>> toStoreCurrentData(){
        return new Pair<>(retrieveMapTimefields(), retrieveTimefields());
    }

    public Hashtable<Integer, Timefield> retrieveMapTimefields(){
        if(map_timefields == null || time_fields == null) {
            int current_index = getCurrentIndexFile();
            Log.d("retrieveMapTimefields", "current index is "+ current_index);
            if (current_index == -1) {
                getStateStorage().saveLastIndexFile(0);
                current_index = 0;
                Pair<Hashtable<Integer, Timefield>, ArrayList<Integer>> empty_stuff = new Pair<>(new Hashtable<Integer, Timefield>(), new ArrayList<Integer>());
                getStateStorage().saveFileList(empty_stuff, current_index);
            }

            Pair<Hashtable<Integer, Timefield>, Integer> pair = getStateStorage().getFileList(current_index);  //getStateStorage().getTimeFieldsList(StateGlobals.SAVE_STATE);
            map_timefields = pair.first;
            int init_size = pair.second;
            time_fields = new ArrayList<>(init_size);

            Log.d("retrieveMapTimefields", "init_size = " + init_size);

            for(int i = 0; i < init_size; i++){
                time_fields.add(i);
            }
        }
        return map_timefields;
    }
    public ArrayList<Integer> retrieveTimefields(){
        if(time_fields == null){
            retrieveMapTimefields();
        }
        return time_fields;
    }

    public String getFileName(int index_file){
        String out = "";
        if(getFileListNames().size() > index_file){
            out = getFileListNames().get(index_file);
        }
        return out;
    }



    public boolean getRepeatState(){
        if(repeatState==null){
            repeatState = getConfigStates().getRepeatState();
        }
        return repeatState;
    }
    public void saveRepeatState(boolean _repeatState){
        repeatState = _repeatState;
    }
    public void saveRepeatState(){
        getConfigStates().saveRepeatState(repeatState);
    }


    public int getNotification_id(){
        return notification_id;
    }

    public TimingNotifications getTimingNotifications(){
        return timingNotifications;
    }

    public void setActivityInstance(MainActivity mainActivity){
        this.main_activity = mainActivity;
    }

    public int getMainState(){
        return current_state;
    }
    public void setMainState(int state){
        current_state = state;
    }



    @Override
    public IBinder onBind(Intent intent){

        current_state = MainStateGlobals.STATE_IDLE;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(timingNotifications == null) {
            timingNotifications = new TimingNotifications(this);
        }
        if(serviceCountdown == null){
            serviceCountdown = new ServiceCountdown();
        }
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String action = intent.getStringExtra(InfoNameGlobals.ACTION);
        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        }

        if(action.equals(InfoNameGlobals.START_TIMING)) {
        }
        else if(action.equals(InfoNameGlobals.STOP_TIMING)){
        }
        else if(action.equals(InfoNameGlobals.PAUSE_TIMING)){

        }
        else if(action.equals(InfoNameGlobals.CONTINUE_TIMING)){
            Log.d("onStartCommand", "Action is CONTINUE_TIMING!!");
            continueTimer();
        }
        return mStartMode;
    }
    @Override
    public void onDestroy(){
        if(timingNotifications != null) {
            timingNotifications.stopNotification(notification_id);
        }
        //stopTimer();
        saveRepeatState();
        saveFileListNames();
        Log.d("onDestroy", "destroyed service!!!!!!!!");
        super.onDestroy();
    }

    public class LocalBinder extends Binder{
        public TimingService getService(){
            return TimingService.this;
        }
    }




    public void pauseTimer(){
        setLastRemainingMillis(getMillisecondsRemaining());
        Intent intent =  new Intent(this, TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        serviceCountdown.stopCountDown();
        setMainState(MainStateGlobals.STATE_PAUSED);
    }
    public void stopTimer(){
        Intent intent =  new Intent(this, TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        current_timer_index = 0;
        alarmManager.cancel(pendingIntent);
        serviceCountdown.stopCountDown();
        setMainState(MainStateGlobals.STATE_IDLE);
        if(timingNotifications != null) {
            timingNotifications.stopNotification(notification_id);
        }
        setMainState(MainStateGlobals.STATE_IDLE);
        if (main_activity != null) {
            main_activity.stopTimer();
        }

    }

    public void setOnOpeningDialogFragment(){
        openingDialogDragment = true;
    }
    public void setOffOpeningDialogFragment(){
        if(getMainState() == MainStateGlobals.STATE_IDLE){
            setOffWaitingForScheduled();
        }
        openingDialogDragment = false;
        if(!waitingForScheduled && !waitingForScheduled){
            stopSelf();
        }
    }
    public void setOnWaitingForScheduled(){
        waitingForScheduled = true;
    }
    public void setOffWaitingForScheduled(){
        if(main_activity == null){
            setOffOpeningDialogFragment();
        }
        waitingForScheduled = false;
        if(!waitingForScheduled && !waitingForScheduled){
            stopSelf();
        }
    }

    public void startTimer(){
        current_timer_index = 0;
        setMainState(MainStateGlobals.STATE_RUNNING);

        notification_id = 0;
        setOnWaitingForScheduled();

        //timingNotifications.sendNotification(1, MainActivity.class,
        //        "Continuing on iteration #"+current_timer_index+
        //                ". Total time: " + getTimeString(), -1, Notification.PRIORITY_HIGH);
        timerScheduling(-1);
    }

    public void unPauseTimer(){
        setMainState(MainStateGlobals.STATE_RUNNING);

        timerScheduling(getLastRemainingMillis());
    }


    private void continueTimer(){
        Log.d("continueTimer", "called continue timer! current_timer_index:" + current_timer_index);
        current_timer_index++;
        if(current_timer_index >= retrieveTimefields().size()){
            // stop timer
            current_timer_index = 0;
            if(repeatState){
                timingNotifications.sendNotification(1,
                        MainActivity.class,
                        "Finished all countdowns.. Repeating", InfoNameGlobals.NOTIFICATION_ONE, Notification.PRIORITY_HIGH);

                timerScheduling(-1);
            }else{
                timingNotifications.sendNotification(1,
                        MainActivity.class,
                        "Finished all countdowns", InfoNameGlobals.NOTIFICATION_ONE, Notification.PRIORITY_HIGH);
                stopTimer();
                setOffWaitingForScheduled();

            }
        }
        else{

            // continue next iteration
            timingNotifications.sendNotification(0, MainActivity.class,
                    "Continuing on task #" + current_timer_index + ". " + getTFAt(current_timer_index).getCustomText() +
                            ". Total time: " + getTimeString(), InfoNameGlobals.NOTIFICATION_ONE, Notification.PRIORITY_HIGH);
            timerScheduling(-1);
        }
    }

    private void timerScheduling(long from_pause_millis){
        long to_add;
        Timefield current_timefield= getTFAt(current_timer_index);
        if(from_pause_millis > -1){
            to_add = from_pause_millis;
        }
        else{
            to_add =current_timefield.getMilliseconds();
        }
        current_timefield.focusInScroll();
        Intent intent = new Intent(getApplicationContext(), TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        nextMillis = System.currentTimeMillis() + to_add;

        alarmManager.set(AlarmManager.RTC_WAKEUP, nextMillis, pendingIntent);
        serviceCountdown.startNewCountDown(getTFAt(current_timer_index), this);
    }

    private String getTimeString(){
        if(retrieveTimefields() != null && retrieveTimefields().size() > current_timer_index) {
            return TimeContainer.getTimeString(getTFAt(current_timer_index).getMilliseconds());
        }
        else{
            return "--:--:--";
        }
    }

    public int getMillisecondsRemaining(){
        long currentmilis = System.currentTimeMillis();
        if(nextMillis > currentmilis){
            return (int) (nextMillis - currentmilis);
        }else{
            return 0;
        }
    }

    public int getCurrent_timer_index(){
        return current_timer_index;
    }

}
