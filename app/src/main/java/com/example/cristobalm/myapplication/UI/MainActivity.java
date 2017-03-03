package com.example.cristobalm.myapplication.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.UI.Globals.ButtonNameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.FilenameGlobals;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Storage.StateStorage;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String ET_LIST = "et_list";
    ArrayList<Button> buttons;
    ArrayList<Timefield> time_fields;
    LinearLayout et_list;
    StateStorage stateStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateStorage = new StateStorage(getApplicationContext(), FilenameGlobals.STORED_STATES);
        time_fields = stateStorage.getTimeFieldsList(StateGlobals.SAVE_STATE);

        int et_listID = getResources().getIdentifier(ET_LIST, "id", getPackageName());
        et_list = (LinearLayout) findViewById(et_listID);
        et_list.setGravity(Gravity.CENTER_HORIZONTAL);

        addListenerButtons();
        startTimeFieldsDisplay();
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


    public ArrayList<Integer> getMinutesList(){
        ArrayList<Integer> out_list = new ArrayList<>();

        for(int i = 0; i < time_fields.size(); i++){
            out_list.add(time_fields.get(i).getMinutes());
        }

        return out_list;
    }

    public ArrayList<Timefield> getTime_fields(){
        return time_fields;
    }


}
