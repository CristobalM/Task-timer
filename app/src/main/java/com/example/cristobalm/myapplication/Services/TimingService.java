package com.example.cristobalm.myapplication.Services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.FilenameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.Storage.StateStorage;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.ListFragment.ListDialog;
import com.example.cristobalm.myapplication.UI.ListFragment.ListItem;
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


    ArrayList<ListItemInfo> listItemInfoArrayList;

    //to stop or not to stop
    boolean openingDialogDragment = false;
    boolean waitingForScheduled = false;


    Integer current_index_file;



    int millis_editing = -1;

    int to_load_file;


    Integer to_load_music_id;
    Timefield to_load_music_timefield;

    public void loadMusic(int music_id, Timefield _timefield){
        to_load_music_id = music_id;
        to_load_music_timefield = _timefield;
    }
    public void startLoadMusic(){
        if(to_load_music_timefield != null && to_load_music_id != null){
            to_load_music_timefield
                    .setSound(to_load_music_id);
            to_load_music_timefield
                    .getTimeLinearLayout()
                    .setMusicColor(InfoNameGlobals.getSColorById(to_load_music_id));
        }
    }


    Integer tempFinishingSound = null;
    Integer tempCommonSound = null;
    public void setConfigMusic(int which, int idmusic){
        switch (which){
            case InfoNameGlobals.SOUND_FINISHING:
                tempFinishingSound = idmusic;
                break;
            case InfoNameGlobals.SOUND_COMMON:
                tempCommonSound = idmusic;
                break;
        }
    }
    public Integer getTempFinishingSound(){
        return tempFinishingSound;
    }

    public Integer getTempCommonSound(){
        return tempCommonSound;
    }
    public void processChange(boolean applyToAllChecked){
        if(tempFinishingSound != null){
            finishSound = tempFinishingSound;
            tempFinishingSound = null;
        }else if(tempCommonSound != null){
            commonSound = tempCommonSound;
            tempCommonSound = null;
            if(applyToAllChecked) {
                setAllSoundsToCommon();
            }
        }
    }
    public void setAllSoundsToCommon(){
        for(int i = 0; i < retrieveTimefields().size(); i++){
            Timefield _timefield = getTFAt(i);
            _timefield.setSound(getCommonSound());
            _timefield.updateMusicColor();
        }
    }


    public void saveRepeatState(boolean state){
        repeatState = state;
    }

    public void loadFile(int index_file){
        to_load_file = index_file;
    }
    public void startFile(){
        //clearOtherBackground();
        Log.e("startFile", "trying to open file with id "+ to_load_file + ", currentindexfile: " + getCurrentIndexFile());
        if(to_load_file == getCurrentIndexFile()){
            return;
        }
        totalSeconds = null;

        saveFile();
        current_index_file = to_load_file;
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

    public void setTitle(String title){
        getFileNamesMap().put(getCurrentIndexFile(), title);
        if(listItemInfoArrayList != null && listItemInfoArrayList.size() > getIDorder(getCurrentIndexFile())){
            listItemInfoArrayList.get(getIDorder(getCurrentIndexFile())).setFile_name(title);
        }
    }
    public String getTitle(){
        return getFileName(getCurrentIndexFile());
    }
    public String getTitleHint(){
        Log.e("getTitleHint", "current index file: " + getCurrentIndexFile());
        return "File "+(getIDorder(getCurrentIndexFile())+1) + " (Touch to edit)";
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


    ArrayList<Integer> fileListIDS;
    public ArrayList<Integer> getFileListIDS(){
        if(fileListIDS == null){
            fileListIDS = getConfigStates().getFileListIDS();
            if(fileListIDS == null){
                fileListIDS = new ArrayList<>();
            }
        }
        return fileListIDS;
    }
    Hashtable<Integer, String> fileNamesMap;
    public Hashtable<Integer, String> getFileNamesMap(){
        if(fileNamesMap == null){
            fileNamesMap = getConfigStates().getFileNamesMap(getFileListIDS());
        }
        return fileNamesMap;
    }

    public void reloadItemsInList(){
        getFilesDialogList().removeAllViewsInLayout();
        //getFilesDialogList().requestLayout();
        getFilesDialogList().removeAllViews();
        getFilesDialogList().invalidate();
        getFilesDialogList().postInvalidate();
        getFilesDialogList().requestLayout();
            getFilesDialogList().refreshDrawableState();
        getFilesDialogList().destroyDrawingCache();
        for(int i = 0; i < builtListItemInfoArrayList().size(); i++){
            ListItemInfo _lIF = builtListItemInfoArrayList().get(i);
            if(_lIF.getListItem().getParent() != null){
                ((ViewGroup) _lIF.getListItem().getParent()).removeView(_lIF.getListItem());
            }
            getFilesDialogList().addView(_lIF.getListItem());
        }
        getFilesDialogList().invalidate();
        //getFilesDialogList().postInvalidate();
    }

    public void reloadColor(){
        for(int i = 0; i < builtListItemInfoArrayList().size(); i++){
            ListItemInfo _lIF = builtListItemInfoArrayList().get(i);
            _lIF.setBackgroundColor(R.color.itemNotificationBackground);
        }
    }

    public void removeID(int id){
        if(id == getCurrentIndexFile()){
            Toast.makeText(main_activity, "Please open other file first", Toast.LENGTH_SHORT).show();
            return;
        }
        loadFile(getCurrentIndexFile());
        Log.e("removeID", "id:"+ id + " order:" + getIDorder(id)+ ", arrayinfosize:"+ builtListItemInfoArrayList().size());
        reloadOrder();
        getFilesDialogList().removeView(builtListItemInfoArrayList().get(getIDorder(id)).getListItem());
        //builtListItemInfoArrayList().remove(getIDorder(id));
        listItemInfoArrayList = null;

        getFileListIDS().remove(((int)getIDorder(id)));
        reloadOrder();
        getFileNamesMap().remove(id);
        deleteFile(id);
        reloadItemsInList();
        getFilesDialogList().invalidate();


        if(main_activity != null){
            main_activity.reloadCurrentTitle();
        }

    }

    ArrayList<Integer> bulk_delete;

    public void deleteFile(int id){
        if(bulk_delete == null){
            bulk_delete = new ArrayList<>();
        }
        bulk_delete.add(id);
    }
    public void bulkDelete(){
        if(bulk_delete != null){
            getConfigStates().deleteFiles(bulk_delete);
        }
    }

    public void reloadOrder(){
        String order_show_debug = "";
        for(int i = 0; i < getFileListIDS().size(); i++){
            setIDorder(getFileListIDS().get(i), i);
            order_show_debug += "id:"+getFileListIDS().get(i)+",order:"+i+";";
        }
        Log.e("reloadOrder", order_show_debug);
    }

    LinearLayout filesDialogList;

    public LinearLayout getFilesDialogList(){
        return filesDialogList;
    }

    public void setFilesDialogList(LinearLayout ll){
        filesDialogList = ll;
    }


    public ArrayList<ListItemInfo> builtListItemInfoArrayList (){
        if(listItemInfoArrayList == null) {
            ArrayList<Integer> f_list_ids = getFileListIDS();
            if(f_list_ids != null) {
                listItemInfoArrayList = new ArrayList<>(f_list_ids.size());
                for (int i = 0; i < f_list_ids.size(); i++) {
                    ListItemInfo listItemInfo = new ListItemInfo(this, this, f_list_ids.get(i));
                    String filename = getFileNamesMap().get(f_list_ids.get(i));
                    if(filename != null) {
                        listItemInfo.setFile_name(filename);
                    }
                    listItemInfo.setHint("File " + (i+1));

                    listItemInfoArrayList.add(listItemInfo);
                }
            }else{
                listItemInfoArrayList = new ArrayList<>();
            }
        }
        return listItemInfoArrayList;
    }
    Integer unique_id;
    public int getUniqueID(){
        if(unique_id == null){
            unique_id = getConfigStates().getUniqueID();
        }
        return unique_id;
    }
    public int nextUniqueID(){
        int out = getUniqueID();
        Log.d("nextUniqueID","out:"+out);
        unique_id++;
        return out;
    }

    Integer commonSound;
    public int getCommonSound(){
        if(commonSound == null){
            commonSound = getStateStorage().getCommonSound();
        }
        return commonSound;
    }
    Integer finishSound;
    public int getFinishSound(){
        if(finishSound == null){
            finishSound = getStateStorage().getFinishSound();
        }
        return finishSound;
    }

    public void saveAll(){
        saveFile();
        getConfigStates().saveAll(getFileNamesMap(),  getFileListIDS(), getUniqueID(), getCurrentIndexFile(), getRepeatState(), getCommonSound(), getFinishSound());
    }
    public void newFile(){
        saveFile();
        totalSeconds=null;
        current_index_file = nextUniqueID();

        Log.e("UNIQUE_IDIS", ":" + current_index_file);
        //getStateStorage().saveLastIndexFile(current_index_file);
        map_timefields = new Hashtable<>();
        time_fields = new ArrayList<>();
        saveFile();
        setIDorder(current_index_file, getFileListIDS().size());
        getFileListIDS().add(current_index_file);
        if(main_activity != null){
            main_activity.setTimefields(time_fields);
            main_activity.setMapTimeFields(map_timefields);
            main_activity.reloadList();
            if(main_activity.getTitle_list() != null){
                main_activity.getTitle_list().setHint(getTitleHint());
                main_activity.getTitle_list().setText("");
            }
            main_activity.reloadButtonStates();
            Toast.makeText(main_activity, "New file created", Toast.LENGTH_SHORT).show();
        }
        listItemInfoArrayList = null;



    }



    public void saveFile(){
        Log.d("saveFile", "saving data of index " + getCurrentIndexFile());
        getStateStorage().saveFileList(toStoreCurrentData(), getCurrentIndexFile());
    }

    public int getCurrentIndexFile(){
        if(current_index_file == null){
            Log.d("getCurrentIndexFile", "retrieving current index file");
            current_index_file = getConfigStates().getLastIndexFile();
            if(current_index_file < 0 || getIDorder(current_index_file) == null ||getFileListIDS().size() <= getIDorder(current_index_file)){
                current_index_file = getFileListIDS().size() > 0 ? getFileListIDS().get(0) : 0;
                if(getFileListIDS().size() == 0){
                    current_index_file = nextUniqueID();
                    getFileListIDS().add(current_index_file);
                }
                //getStateStorage().saveLastIndexFile(current_index_file);
                if(getFileNamesMap().get(current_index_file) == null){
                    getFileNamesMap().put(current_index_file, "");
                }
                reloadOrder();
            }
        }else{
            Log.d("getCurrentIndexFile", "NOT retrieving current index file");

        }
        return current_index_file;
    }

    Pair<Hashtable<Integer, Timefield>, ArrayList<Integer>> toStoreCurrentData(){
        return new Pair<>(retrieveMapTimefields(), retrieveTimefields());
    }

    Hashtable<Integer, Integer> id_order;

    public Hashtable<Integer, Integer> getOrderMap(){
        if(id_order == null){
            id_order = new Hashtable<>();
            for(int i = 0; i < getFileListIDS().size(); i++){
                id_order.put(getFileListIDS().get(i), i);
            }
        }
        return id_order;
    }

    public Integer getIDorder(int id){
        return getOrderMap().get(id);
    }
    public void setIDorder(int id, int order){
        getOrderMap().put(id, order);
    }

    public Hashtable<Integer, Timefield> retrieveMapTimefields(){
        if(map_timefields == null || time_fields == null) {
            int current_index = getCurrentIndexFile();
            Log.d("retrieveMapTimefields", "current index is "+ current_index);


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
        String fname = getFileNamesMap().get(index_file);
        return (fname != null) ? fname : out;
    }



    public boolean getRepeatState(){
        if(repeatState==null){
            repeatState = getConfigStates().getRepeatState();
        }
        return repeatState;
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
        else if(action.equals(InfoNameGlobals.SHUTDOWN)){
            saveAll();
        }
        return mStartMode;
    }
    @Override
    public void onDestroy(){
        if(timingNotifications != null) {
            timingNotifications.stopNotification(notification_id);
        }
        //stopTimer();
        saveAll();
        bulkDelete();
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

    public Timefield getTimefieldInOrder(int order){
        return retrieveMapTimefields().get(retrieveTimefields().get(order));
    }
    public int getSoundInOrder(int order){
        return getTimefieldInOrder(order).getSoundId();
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
                        "Finished all countdowns.. Repeating",  InfoNameGlobals.getSound(getSoundInOrder(retrieveTimefields().size()-1)), Notification.PRIORITY_HIGH);

                timerScheduling(-1);
            }else{
                timingNotifications.sendNotification(1,
                        MainActivity.class,
                        "Finished all countdowns", InfoNameGlobals.getSound(getFinishSound()), Notification.PRIORITY_HIGH);
                stopTimer();
                setOffWaitingForScheduled();

            }
        }
        else{

            // continue next iteration
            timingNotifications.sendNotification(0, MainActivity.class,
                    "Continuing on task #" + current_timer_index + ". " + getTFAt(current_timer_index).getCustomText() +
                            ". Total time: " + getTimeString(), InfoNameGlobals.getSound(getSoundInOrder((getCurrent_timer_index() -1))), Notification.PRIORITY_HIGH);
            timerScheduling(-1);
        }
    }

    private void timerScheduling(long from_pause_millis){
        long to_add;
        Timefield current_timefield = getTFAt(current_timer_index);
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
