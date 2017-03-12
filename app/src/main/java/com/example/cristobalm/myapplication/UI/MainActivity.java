package com.example.cristobalm.myapplication.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.UI.Globals.ButtonNameGlobals;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.ThrashCan;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.ThrashOnDragListener;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.ListFragment.ListItem;
import com.example.cristobalm.myapplication.UI.ListFragment.ListOnTouchListener;
import com.example.cristobalm.myapplication.UI.ListFragment.ListsLayout;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {
    public static final String ET_LIST = "et_list";
    ArrayList<ImageView> buttons;
    ArrayList<Integer> time_fields;
    Hashtable<Integer, Timefield> map_timefields;
    LinearLayout et_list;
    boolean enabled_inputs;
    boolean mBound;
    public TimingService mService;

    public int current_state;
    private int current_index;

    ThrashCan thrashCan;

    public int unique_index;

    ScrollView scrollView;

    ImageView listsButton;

    ListsLayout listsLayout;

    public ListsLayout getListsLayout(){
        if(listsLayout == null){
            listsLayout = new ListsLayout(this);
        }
        return listsLayout;
    }







    public ScrollView getScrollView(){
        if(scrollView == null) {
         scrollView = (ScrollView) findViewById(R.id.ScrollView);
        }
        return scrollView;
    }



    public void addTimefield(Timefield timefield){
        if(mService != null){
            mService.addTimefield(timefield);
        }
    }




    public void pauseTimer(){
        if(mService == null){
            return;
        }
        mService.pauseTimer();
    }


    public void stopTimer(){

        if(!isEnabled_inputs()) {
            enableInputs();
        }

        ImageView button_play = buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY ));
        ImageView button_pause =  buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PAUSE));
        ImageView button_add =  buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_ADD));
        button_play.setVisibility(View.VISIBLE);
        button_pause.setVisibility(View.INVISIBLE);
        button_add.getBackground().clearColorFilter();

        reloadList();



        getState();
    }



    public MainActivity getMyInstance(){
        return this;
    }
    public void setCurrentIndex(int index){
        current_index = index;
    }
    public int getCurrent_index(){
        return current_index;
    }

    public Timefield getTimefieldByUniqueID(int unique_id){
        return map_timefields.get(unique_id);
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

        }else{
            Toast.makeText(getApplicationContext(), "Some error occurred during dragging operation", Toast.LENGTH_LONG).show();
        }
        reloadList();
    }
    public void removeTimeField(int static_which){
        Log.d("removeTimeField", "Trying to delete Timefield with static index: "+ static_which);
        Timefield target = map_timefields.get(static_which);
        if(static_which > -1 && target != null && time_fields.size()>0 && time_fields.size() > target.getIndex()) {
            time_fields.remove(target.getIndex());
        }
        else{
            Log.e("removeTimeField",
                    "static_index " + static_which +
                            " not found. target==null?:"+ String.valueOf(target==null) +
                            " time_fields.size():"+ time_fields.size() +
                            " target.getIndex: " + String.valueOf((target != null ? target.getIndex() : "ES NULO"))
            );
        }
        reloadList();
    }

    public int getState(){
        current_state = mService.getMainState();
        return current_state;
    }


    public void showThrashCan(){
        thrashCan.setVisibility(View.VISIBLE);
        thrashCan.invalidate();
    }
    public void hideThrashCan(){
        thrashCan.setVisibility(View.INVISIBLE);
        thrashCan.invalidate();
    }


    public void setTimefields(ArrayList<Integer> timefields){
        this.time_fields = timefields;
    }

    public Timefield getTFAt(int index){
        if(mService != null){
            return mService.getTFAt(index);
        }else{
            return null;
        }
    }
    public void setMapTimeFields(Hashtable<Integer, Timefield> ht){
        map_timefields = ht;
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            TimingService.LocalBinder binder = (TimingService.LocalBinder) service;
            mService = binder.getService();
            mService.setActivityInstance(getMyInstance());
            mBound = true;


            setMapTimeFields(mService.retrieveMapTimefields());
            setTimefields(mService.retrieveTimefields());

            startUI();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unique_index = 0;
        setContentView(R.layout.activity_main);


    }

    public void startUI(){
        int et_listID = getResources().getIdentifier(ET_LIST, "id", getPackageName());
        et_list = (LinearLayout) findViewById(et_listID);
        et_list.setGravity(Gravity.CENTER_HORIZONTAL);

        addListenerButtons();
        startTimeFieldsDisplay();
        enabled_inputs = true;

        thrashCan = (ThrashCan)findViewById(R.id.thrash_can);
        thrashCan.setOnDragListener(new ThrashOnDragListener(this, thrashCan));
        thrashCan.setVisibility(View.INVISIBLE);


        listsButton = (ImageView) findViewById(R.id.open_files);
        listsButton.setOnTouchListener(new ListOnTouchListener(this));


    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(this, TimingService.class);
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
            getTFAt(i).blockInput();
        }
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_ADD)).getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);  // setVisibility(View.INVISIBLE);
        enabled_inputs = false;

    }
    public void enableInputs(){
        for(int i = 0; i < time_fields.size(); i++){
            getTFAt(i).enableInput();
        }
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_ADD)).getBackground().clearColorFilter(); // .setVisibility(View.VISIBLE);
        enabled_inputs = true;
    }
    public boolean isEnabled_inputs(){
        return enabled_inputs;
    }


    private void startTimeFieldsDisplay(){
        et_list.removeAllViews();
        et_list.invalidate();
        for (int i = 0; i < time_fields.size(); i++) {
            getTFAt(i).startTimefieldView(this);
            LinearLayout tl = getTFAt(i).getLayout();
            if(tl.getParent() != null){
                ((ViewGroup) tl.getParent()).removeView(tl);
            }
            et_list.addView(getTFAt(i).getLayout());
        }
    }

    public class mainButtonListener implements View.OnTouchListener{
        ImageView button;
        ButtonAction receivedButtonAction;
        MainActivity mainActivity;
        mainButtonListener(ImageView button, ButtonAction receivedButtonAction, MainActivity mainActivity){
            this.button = button;
            this.receivedButtonAction = receivedButtonAction;
            this.mainActivity = mainActivity;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    receivedButtonAction.setContext(getApplicationContext());
                    receivedButtonAction.Run();

                    return true;
                case MotionEvent.ACTION_UP:
                    receivedButtonAction.actionUp();
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    //v.getBackground().clearColorFilter();
                    return true;
            }
            return false;
        }

    }


    public void addListenerButtons(){
        buttons = new ArrayList<>();
        ArrayList<String> buttons_names = ButtonNameGlobals.getNamesList();
        for(int i = 0; i < buttons_names.size(); i++ ) {
            Log.d("addListenerButtons", "trying to get button name: " + buttons_names.get(i) );
            int resID = getResources().getIdentifier(buttons_names.get(i), "id", getPackageName());
            ImageView button = (ImageView) findViewById(resID);
            buttons.add(button);
            ButtonAction b_act = new ButtonAction(buttons_names.get(i), this, button);
            button.setOnTouchListener(new mainButtonListener(button,b_act, this));
        }
    }

    public void reloadList(){
        et_list.removeAllViews();
        for (int i = 0; i < time_fields.size(); i++) {
            Timefield selected_timefield = getTFAt(i);
            ViewParent parent = selected_timefield.getLayout().getParent();
            if(parent != null){
                ((ViewGroup)parent).removeView(selected_timefield.getLayout());
            }
            et_list.addView(selected_timefield.getLayout());
            selected_timefield.setIndex(i);
            selected_timefield.restoreTime();
            //time_fields.get(i).getTimeLinearLayout().getTimeCountdownView().getBackground().clearColorFilter();
            selected_timefield.getTimeLinearLayout().getTimeCountdownView().setBackgroundColor(ContextCompat.getColor(mService, R.color.colorCountdownBackground));
            selected_timefield.getTimeLinearLayout().getTimeCountdownView().invalidate();
            selected_timefield.setHint(i);
            selected_timefield.enableInput();
            selected_timefield.getTimeLinearLayout().getTimeDescription().invalidate();

        }
        et_list.invalidate();
    }

    public void saveState(int state){
        if(mService != null && mService.getStateStorage() != null && time_fields != null) {
            mService.getStateStorage().storeTimeFieldsList(time_fields, map_timefields, state);
            Log.d("saveState","Saving state #"+state);
        }
    }


    public LinearLayout getEt_list(){
        return et_list;
    }


    public ArrayList<Integer> getMillisecondsList(){
        ArrayList<Integer> out_list = new ArrayList<>();

        for(int i = 0; i < time_fields.size(); i++){
            out_list.add(getTFAt(i).getMilliseconds());
        }

        return out_list;
    }

    public ArrayList<Integer> getTime_fields(){
        return time_fields;
    }


}
