package com.example.cristobalm.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String ET_LIST = "et_list";
    ArrayList<Button> buttons;
    ArrayList<String> buttons_names;
    ArrayList<TimeFields> time_fields;
    LinearLayout et_list;

    public ArrayList<TimeFields> getTime_fields(){
        return time_fields;
    }
    public LinearLayout getEt_list(){
        return et_list;
    }

    public void reloadList(){
        et_list.removeAllViews();
        for (int i = 0; i < time_fields.size(); i++) {
            et_list.addView(time_fields.get(i).getLayout());
            time_fields.get(i).setIndex(i);
        }
    }

    public ArrayList<Integer> getMinutesList(){
        ArrayList<Integer> out_list = new ArrayList<>();

        for(int i = 0; i < time_fields.size(); i++){
            out_list.add(time_fields.get(i).getMinutes());
        }

        return out_list;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons = new ArrayList<>();
        buttons_names = ButtonName.getNamesList();
        time_fields = new ArrayList<>();

        int et_listID = getResources().getIdentifier(ET_LIST, "id", getPackageName());
        et_list = (LinearLayout) findViewById(et_listID);

        addListenerButtons(buttons_names);
    }

    public void addListenerButtons(ArrayList<String> b_names){
        for(int i = 0; i < b_names.size(); i++ ) {
            int resID = getResources().getIdentifier(b_names.get(i), "id", getPackageName());
            Button button = (Button) findViewById(resID);
            buttons.add(button);
            ButtonAction b_act = new ButtonAction(b_names.get(i), this);
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


}
