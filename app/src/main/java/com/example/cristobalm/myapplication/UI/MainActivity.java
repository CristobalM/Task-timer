package com.example.cristobalm.myapplication.UI;

import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.ConfigFragment.ConfigOnTouchListener;
import com.example.cristobalm.myapplication.UI.Globals.ButtonNameGlobals;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.ThrashCan;
import com.example.cristobalm.myapplication.UI.GreatTimeDraggable.ThrashOnDragListener;
import com.example.cristobalm.myapplication.UI.ListFragment.ListOnTouchListener;
import com.example.cristobalm.myapplication.UI.ListFragment.ListsLayout;
import com.example.cristobalm.myapplication.UI.ListFragment.NewFileOnTouchListener;
import com.example.cristobalm.myapplication.UI.ListFragment.TitleChangeListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;


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

    ImageView newFileButton;
    ImageView listsButton;
    ImageView configButton;

    ListsLayout listsLayout;

    TextView title_list;


    public boolean isViewVisible(View _view){
        Rect scrollBounds = new Rect();
        scrollView.getDrawingRect(scrollBounds);

        float top = _view.getY();
        float bottom = top+ _view.getHeight();
        if(scrollBounds.top < top && scrollBounds.bottom > bottom){
            return true;
        }else{
            return false;
        }
    }

    public class scrollRunnable implements  Runnable{
        AtomicBoolean _is_done;
        int direction;
        ScrollView _sview;
        scrollRunnable(AtomicBoolean _is_done, ScrollView _sview, int direction){
            this._is_done = _is_done;
            this.direction = direction;
            this._sview = _sview;
        }
        public void run() {
            if(! _is_done.get()){
                if(mService != null) {
                    View child;
                    if(direction == 1) {
                        child = mService.getTFAt(mService.retrieveTimefields().size() - 1).getTimeLinearLayout();
                        is_done_s_top.set(true);

                    }else{
                        child = mService.getTFAt(0).getTimeLinearLayout();
                        is_done_s_bottom.set(true);
                    }
                    if(!isViewVisible(child)) {
                        Log.e("scrollrunable", "trying to scroll!!!");
                        _sview.smoothScrollBy(0, _px(5) * direction);
                        didScrollingStop(_is_done, _sview, direction);

                    }else{
                        _is_done.set(true);
                    }
                }

            }
        }
    }

    public class CheckIfFinishedScrolling implements Runnable{
        AtomicBoolean _is_done;
        int direction;
        ScrollView _sview;
        CheckIfFinishedScrolling(AtomicBoolean _is_done, ScrollView _sview, int direction){
            this._is_done = _is_done;
            this.direction = direction;
            this._sview = _sview;
        }
        @Override
        public void run() {
            int new_pos = getScrollView().getScrollY();
            if(_last_scroll_ != null && new_pos == _last_scroll_){
                //done
                did_scrolling_stop = true;
                timeOutScroll(_is_done, _sview, direction);
            }
            else{
                _last_scroll_ = new_pos;
                didScrollingStop(_is_done, _sview, direction);
            }
        }
    }

    Integer _last_scroll_;
    Boolean  did_scrolling_stop;
    public synchronized void didScrollingStop(AtomicBoolean _is_done, ScrollView _sview, int direction){
        if(getScrollView() == null){
            return;
        }
        new android.os.Handler().postDelayed(
                new CheckIfFinishedScrolling(_is_done, _sview, direction),
                5);
    }





    public void timeOutScroll(AtomicBoolean _is_done, ScrollView _sview, int direction){
        new android.os.Handler().postDelayed(
                new scrollRunnable(_is_done, _sview, direction),
                5);
    }

    public class ScrollerListener implements View.OnDragListener{
        int direction;
        AtomicBoolean _is_done;
        ScrollerListener(int direction, AtomicBoolean _is_done){
            this.direction = direction;
            this._is_done = _is_done;
        }
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT) && current_state != MainStateGlobals.STATE_RUNNING) {
                        _is_done.set(false);
                        v.invalidate();
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    _is_done.set(false);
                    timeOutScroll(_is_done, getScrollView(), direction);
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DROP:
                    _is_done.set(true);
                    reloadList();
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    _is_done.set(true);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    _is_done.set(true);
                    reloadList();
                    return true;
            }
            _is_done.set(true);
            return false;
        }
    }
    ImageView schecker_top;
    ImageView schecker_bottom;
    AtomicBoolean is_done_s_top;
    AtomicBoolean is_done_s_bottom;
    public void startScrollCheckers(){
        is_done_s_bottom = new AtomicBoolean();
        is_done_s_top = new AtomicBoolean();
        is_done_s_bottom.set(true);
        is_done_s_top.set(true);
        schecker_top = (ImageView) findViewById(R.id.top_scroller);
        schecker_bottom = (ImageView) findViewById(R.id.bottom_scroller);
        schecker_top.setOnDragListener(new ScrollerListener(-1, is_done_s_top));
        schecker_bottom.setOnDragListener(new ScrollerListener(1, is_done_s_bottom));
    }

    public ListsLayout getListsLayout(){
        if(listsLayout == null){
            listsLayout = new ListsLayout(this);
        }
        return listsLayout;
    }

    public TextView getTitle_list(){
        return title_list;
    }


    public int _px(int dp){
        return VisualSettingGlobals.getPixels(dp, getResources().getDisplayMetrics().density);
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



    public void playTimer(){
        if(getState() == MainStateGlobals.STATE_RUNNING ||
                getTime_fields().size() <= 0){
            return;
        }

        if(mService.getTotalSeconds() <= 0){
            Toast.makeText(this, R.string.total_time_greater_than_zero, Toast.LENGTH_SHORT).show();
            return;
        }


        switch (getState()){
            case MainStateGlobals.STATE_PAUSED:
                mService.unPauseTimer();
                break;
            case MainStateGlobals.STATE_IDLE:
                ArrayList<Integer> millisecondsList = getMillisecondsList();
                Intent intent = new Intent(getApplicationContext(), TimingService.class);
                intent.putIntegerArrayListExtra(InfoNameGlobals.REPEAT_TIME_LIST, millisecondsList);
                intent.putExtra(InfoNameGlobals.ACTION, InfoNameGlobals.START_TIMING);
                startService(intent);
                mService.startTimer();
        }

        blockInputs();
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PAUSE)).setVisibility(View.VISIBLE);
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY)).setVisibility(View.INVISIBLE);
        getState();
        //main_activity.saveState();
        if(mService != null){
            mService.changeForce();
        }
    }

    public void pauseTimer(){
        if(mService == null){
            return;
        }
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PAUSE)).setVisibility(View.INVISIBLE);
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY)).setVisibility(View.VISIBLE);

        mService.pauseTimer();
    }


    public void stopTimer(){

        if(!isEnabled_inputs()) {
            enableInputs();
        }

        ImageView button_play = buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY ));
        ImageView button_pause =  buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PAUSE));
        ImageView button_add =  buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_ADD));
        ImageView button_stop =  buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_STOP));
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
            reloadList();
        }else{
            Toast.makeText(getApplicationContext(), R.string.dragging_error, Toast.LENGTH_LONG).show();
        }
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


        newFileButton = (ImageView) findViewById(R.id.new_file);
        newFileButton.setOnTouchListener(new NewFileOnTouchListener(this));

        listsButton = (ImageView) findViewById(R.id.open_files);
        listsButton.setOnTouchListener(new ListOnTouchListener(this));


        configButton = (ImageView) findViewById(R.id.open_config);
        configButton.setOnTouchListener(new ConfigOnTouchListener(this));

        reloadButtonStates();
        reloadCurrentTitle();
        startScrollCheckers();

    }

    public void reloadCurrentTitle(){
        title_list = (TextView) findViewById(R.id.title_file);
        title_list.setText(mService.getTitle());
        title_list.addTextChangedListener(new TitleChangeListener(mService));
        title_list.setHint(mService.getTitleHint());
    }

    public void reloadButtonStates(){
        if(getState() == MainStateGlobals.STATE_IDLE) {
            buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_STOP)).getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.light_gray_a), PorterDuff.Mode.MULTIPLY);
        }else{
            buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_STOP)).getBackground().clearColorFilter();

        }
        if(time_fields.size() <= 0){
            buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY)).getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.light_gray_a), PorterDuff.Mode.MULTIPLY);
        }else{
            buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY)).getBackground().clearColorFilter();
        }
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
        if(mService != null) {
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy(){
        saveState();


        super.onDestroy();
    }
    @Override
    protected void onPause(){
        saveState();
        super.onPause();
    }

    public void blockInputs(){
        for(int i = 0; i < time_fields.size(); i++){
            getTFAt(i).blockInput();
        }
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_ADD)).getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.light_gray_a), PorterDuff.Mode.MULTIPLY);  // setVisibility(View.INVISIBLE);
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_STOP)).getBackground().clearColorFilter();  // setVisibility(View.INVISIBLE);
        enabled_inputs = false;

    }
    public void enableInputs(){
        for(int i = 0; i < time_fields.size(); i++){
            getTFAt(i).enableInput();
        }
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_ADD)).getBackground().clearColorFilter(); // .setVisibility(View.VISIBLE);
        buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_STOP)).getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.light_gray_a), PorterDuff.Mode.MULTIPLY);

        enabled_inputs = true;
    }
    public boolean isEnabled_inputs(){
        return enabled_inputs;
    }


    public void startTimeFieldsDisplay(){
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

    public synchronized void reloadList(){
        et_list.removeAllViews();
        if(time_fields.size() <= 0) {
            buttons.get(ButtonNameGlobals.getIndexByName(ButtonNameGlobals.BUTTON_PLAY)).getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.light_gray_a), PorterDuff.Mode.MULTIPLY);
        }
        for (int i = 0; i < time_fields.size(); i++) {
            Timefield selected_timefield = getTFAt(i);
            ViewParent parent = selected_timefield.getLayout().getParent();
            if(parent != null){
                ((ViewGroup)parent).removeView(selected_timefield.getLayout());
            }
            selected_timefield.setIndex(i);
            selected_timefield.restoreTime();
            //time_fields.get(i).getTimeLinearLayout().getTimeCountdownView().getBackground().clearColorFilter();
            selected_timefield.getTimeLinearLayout().getTimeCountdownView().setBackgroundColor(ContextCompat.getColor(mService, R.color.colorCountdownBackground));
            selected_timefield.getTimeLinearLayout().getTimeCountdownView().invalidate();
            selected_timefield.setHint(i);
            selected_timefield.enableInput();
            selected_timefield.getTimeLinearLayout().getTimeDescription().invalidate();
            et_list.addView(selected_timefield.getLayout());
        }
        et_list.invalidate();
    }

    public void saveState(){
        /*
        if(mService != null && mService.getStateStorage() != null && time_fields != null) {
            mService.getStateStorage().storeTimeFieldsList(time_fields, map_timefields, state);
            Log.d("saveState","Saving state #"+state);
        }
        */
        if(mService != null){
            //mService.saveFile();
            mService.changeForce();
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
