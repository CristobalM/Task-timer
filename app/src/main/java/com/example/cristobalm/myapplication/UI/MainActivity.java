package com.example.cristobalm.myapplication.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.UI.Globals.ButtonNameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.FilenameGlobals;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Storage.StateStorage;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.ThrashCan;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.ThrashOnDragListener;

import java.util.ArrayList;
import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {
    public static final String ET_LIST = "et_list";
    ArrayList<Button> buttons;
    ArrayList<Timefield> time_fields;
    Hashtable<Integer, Timefield> map_timefields;
    LinearLayout et_list;
    StateStorage stateStorage;
    boolean enabled_inputs;
    boolean mBound;
    TimingService mService;
    TimeCountdown current_countdown;

    public int current_state;
    private int current_index;

    ThrashCan thrashCan;

    public int unique_index;

    //public final float scale = getResources().getDisplayMetrics().density;



    public MainActivity getMyInstance(){
        return this;
    }
    public void setCurrentIndex(int index){
        current_index = index;
    }
    public int getCurrent_index(){
        return current_index;
    }

    public void moveTimefield(int static_dest, int static_source){
        Timefield source = map_timefields.get(static_source);
        Timefield dest = map_timefields.get(static_dest);
        if(static_source > -1 && time_fields != null && source != null && dest != null &&
                time_fields.size() > source.getIndex() &&
                time_fields.size() > dest.getIndex()
                ){
            time_fields.add(dest.getIndex(),
                    time_fields.remove(source.getIndex()));
            reloadList();
        }else{
            Toast.makeText(getApplicationContext(), "Some error occurred during dragging operation", Toast.LENGTH_LONG).show();
        }
    }
    public void removeTimeField(int static_which){
        Log.d("removeTimeField", "Trying to delete Timefield with static index: "+ static_which);
        Timefield target = map_timefields.get(static_which);
        if(static_which > -1 && target != null && time_fields.size()>0 && time_fields.size() > target.getIndex()) {
            time_fields.remove(target.getIndex());
            reloadList();
        }
        else{
            Log.e("removeTimeField",
                    "static_index " + static_which +
                            " not found. target==null?:"+ String.valueOf(target==null) +
                            " time_fields.size():"+ time_fields.size() +
                            " target.getIndex: " + String.valueOf((target != null ? target.getIndex() : "ES NULO"))
            );
        }
    }

    public void checkForCountdown(){
        if(mService != null){
            current_state = mService.getMainState();
            setCurrentIndex(mService.getCurrent_timer_index());
            if(current_state == MainStateGlobals.STATE_RUNNING){
                Timefield current_timefield = time_fields.get(getCurrent_index());
                startTimeCountDown(current_timefield, mService);
            }
        }
    }
    public void showThrashCan(){
        thrashCan.setVisibility(View.VISIBLE);
        thrashCan.invalidate();
    }
    public void hideThrashCan(){
        thrashCan.setVisibility(View.INVISIBLE);
        thrashCan.invalidate();
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimingService.LocalBinder binder = (TimingService.LocalBinder) service;
            mService = binder.getService();
            mService.setActivityInstance(getMyInstance());
            mBound = true;
            if(current_state != MainStateGlobals.STATE_RUNNING) {
                mService.setTimeList(time_fields);
            }
            checkForCountdown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void startTimeCountDown(Timefield timefield,
                                            TimingService _service){

        current_countdown.startNewCountDown(timefield, _service);
    }

    public Hashtable<Integer, Timefield> copyTimefieldList(ArrayList<Timefield> src){
        Hashtable<Integer, Timefield> hashtable = new Hashtable<Integer, Timefield>();
        for(int i = 0; i < src.size(); i++){
            hashtable.put(src.get(i).getIndex(), src.get(i));
        }
        return hashtable;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unique_index = 0;
        setContentView(R.layout.activity_main);

        stateStorage = new StateStorage(getApplicationContext(), FilenameGlobals.STORED_STATES);
        time_fields = stateStorage.getTimeFieldsList(StateGlobals.SAVE_STATE);
        map_timefields = copyTimefieldList(time_fields);

        int et_listID = getResources().getIdentifier(ET_LIST, "id", getPackageName());
        et_list = (LinearLayout) findViewById(et_listID);
        et_list.setGravity(Gravity.CENTER_HORIZONTAL);

        addListenerButtons();
        startTimeFieldsDisplay();
        enabled_inputs = true;

        thrashCan = (ThrashCan)findViewById(R.id.thrash_can);
        thrashCan.setOnDragListener(new ThrashOnDragListener(this, thrashCan));
        thrashCan.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(this, TimingService.class);
        current_countdown = new TimeCountdown();
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        super.onStop();

        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        saveState(StateGlobals.SAVE_STATE);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy(){
        saveState(StateGlobals.SAVE_STATE);
        super.onDestroy();
    }

    public void blockInputs(){
        for(int i = 0; i < time_fields.size(); i++){
            time_fields.get(i).blockInput();
        }
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_ADD)).setVisibility(View.INVISIBLE);
        enabled_inputs = false;

    }
    public void enableInputs(){
        for(int i = 0; i < time_fields.size(); i++){
            time_fields.get(i).enableInput();
        }
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_ADD)).setVisibility(View.VISIBLE);
        enabled_inputs = true;
    }
    public boolean isEnabled_inputs(){
        return enabled_inputs;
    }


    private void startTimeFieldsDisplay(){
        et_list.removeAllViews();
        for (int i = 0; i < time_fields.size(); i++) {
            time_fields.get(i).startTimefieldView(this);
            et_list.addView(time_fields.get(i).getLayout());
        }
    }

    public void addListenerButtons(){
        buttons = new ArrayList<>();
        ArrayList<String> buttons_names = ButtonNameGlobals.getNamesList();
        for(int i = 0; i < buttons_names.size(); i++ ) {
            int resID = getResources().getIdentifier(buttons_names.get(i), "id", getPackageName());
            Button button = (Button) findViewById(resID);
            buttons.add(button);
            ButtonAction b_act = new ButtonAction(buttons_names.get(i), this);
            button.setTag(b_act);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ButtonAction receivedButtonAction = (ButtonAction) v.getTag();
                    receivedButtonAction.setContext(getApplicationContext());
                    receivedButtonAction.run();
                }
            });
        }
    }

    public void reloadList(){
        et_list.removeAllViews();
        for (int i = 0; i < time_fields.size(); i++) {
            et_list.addView(time_fields.get(i).getLayout());
            time_fields.get(i).setIndex(i);
        }
    }

    public void saveState(int state){
        if(stateStorage != null && time_fields != null) {
            stateStorage.storeTimeFieldsList(time_fields, state);
            Log.d("saveState","Saving state #"+state);
        }
    }


    public LinearLayout getEt_list(){
        return et_list;
    }


    public ArrayList<Integer> getMillisecondsList(){
        ArrayList<Integer> out_list = new ArrayList<>();

        for(int i = 0; i < time_fields.size(); i++){
            out_list.add(time_fields.get(i).getMilliseconds());
        }

        return out_list;
    }

    public ArrayList<Timefield> getTime_fields(){
        return time_fields;
    }


}
