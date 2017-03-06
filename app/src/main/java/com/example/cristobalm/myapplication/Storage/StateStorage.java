package com.example.cristobalm.myapplication.Storage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.cristobalm.myapplication.UI.Timefield;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/2/17.
 * Store JSON timefield list
 */

public final class StateStorage {
    private Context context;
    private static String TIME_FIELDS = "time_fields";
    private static String CUSTOM_TEXT = "custom_text";
    private static String MILLISECONDS = "milliseconds";
    private FileHandling fileHandling;
    private String filename;
    private boolean fileExists;

    public StateStorage(Context context, String filename){
        this.context = context;
        this.filename = filename;
        fileHandling = new FileHandling(context);
        fileExists = fileHandling.fileExists(filename);
    }

    private void storeStateIntoJSONFile(String keyname, Object data, int state){
        String current_data = getFileJSONString();
        JSONObject jsonObject;
        try{
            jsonObject = new JSONObject(current_data);
            jsonObject.put(keyname + String.valueOf(state), data);
            String storing_json_data = jsonObject.toString();
            fileHandling.writeToFile(filename, storing_json_data);
        }
        catch (JSONException e){
            Log.e("storeStateIntoJSONFile", "Can not parse json data");
        }
    }
    public void storeTimeFieldsList(ArrayList<Timefield> time_fields, int state){
        JSONArray list_building = new JSONArray();
        for(int i = 0; i < time_fields.size(); i++){
            JSONObject temp_pair = new JSONObject();
            try {
                temp_pair.put(CUSTOM_TEXT, time_fields.get(i).getCustomText());
                temp_pair.put(MILLISECONDS, time_fields.get(i).getMilliseconds());
                list_building.put(temp_pair);
            }
            catch(JSONException e){
                Log.d("storeTimeFieldsList", "can not store element with custom text:" + time_fields.get(i).getCustomText() );
            }
        }
        storeStateIntoJSONFile(TIME_FIELDS, list_building, state);
    }

    public ArrayList<Timefield> getTimeFieldsList(int state){
        JSONArray receive_data = getStateList(TIME_FIELDS, state);
        ArrayList<Timefield> time_fields = new ArrayList<>();
        if(receive_data == null){
            Log.d("getTimeFieldsList", "receive_data is null!!");
            return time_fields;
        }
        for(int i = 0; i < receive_data.length(); i++){
            try {
                JSONObject pair = receive_data.getJSONObject(i);
                Timefield tfield = new Timefield(context, i, pair.getString(CUSTOM_TEXT), Integer.parseInt(pair.getString(MILLISECONDS)));
                time_fields.add(tfield);
            }
            catch (JSONException e){
                Log.d("getTimeFieldsList", "can not read pair with index " + i);
            }
        }

        return time_fields;
    }
    private String getFileJSONString(){ // Correct with commments
        String current_data;

        if(fileExists) {
            current_data = fileHandling.readFromFile(filename);
        }else{
            current_data = "{}";
        }
        if(current_data.equals("")){
            current_data = "{}";
        }
        return current_data;
    }
    private JSONArray getStateList(String keyname, int state){
        String current_data = getFileJSONString();
        JSONArray out_data = null;
        String key_to_fetch = keyname + String.valueOf(state);
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(key_to_fetch)) {
                out_data = jsonObject.getJSONArray(key_to_fetch);
            }
            else{
              Log.d("getStateList", "keyname " + key_to_fetch + " not found");
            }
        }
        catch (JSONException e){
            Log.e("getStateList", "Can not parse json data");
        }
        return out_data;
    }
}
