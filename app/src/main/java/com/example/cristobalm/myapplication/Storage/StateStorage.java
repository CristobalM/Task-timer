package com.example.cristobalm.myapplication.Storage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.cristobalm.myapplication.Storage.Globals.FilenameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.UI.Timefield;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
    public StateStorage(Context context){
        this.context = context;
        fileHandling = new FileHandling(context);
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

    public void storeTimeFieldsList(ArrayList<Integer> time_fields, Hashtable<Integer, Timefield> map, int state){
        JSONArray list_building = new JSONArray();
        for(int i = 0; i < time_fields.size(); i++){
            JSONObject temp_pair = new JSONObject();
            try {
                temp_pair.put(CUSTOM_TEXT, map.get(time_fields.get(i)).getCustomText());
                temp_pair.put(MILLISECONDS, map.get(time_fields.get(i)).getMilliseconds());
                list_building.put(temp_pair);
                Log.d("storeTimeFieldsList", "storing field with name "+ map.get(time_fields.get(i)).getCustomText());
            }
            catch(JSONException e){
                Log.d("storeTimeFieldsList", "can not store element with custom text:" + map.get(time_fields.get(i)).getCustomText() );
            }
        }
        storeStateIntoJSONFile(TIME_FIELDS, list_building, state);
    }

    public Pair<Hashtable<Integer, Timefield>, Integer> getTimeFieldsList(int state){
        JSONArray receive_data = getStateList(TIME_FIELDS, state);
        //LinkedList<Timefield> time_fields = new LinkedList<>();
        Hashtable<Integer, Timefield> map_time_fields = new Hashtable<>();
        if(receive_data == null){
            Log.d("getTimeFieldsList", "receive_data is null!!");
            return null;
        }
        for(int i = 0; i < receive_data.length(); i++){
            try {
                JSONObject pair = receive_data.getJSONObject(i);
                Log.d("getTimeFieldsListLoop", "index:"+i+", string:"+pair.getString(CUSTOM_TEXT));
                Timefield tfield = new Timefield(context, i, pair.getString(CUSTOM_TEXT), Integer.parseInt(pair.getString(MILLISECONDS)));
                //time_fields.addLast(tfield);
                map_time_fields.put(i, tfield);

            }
            catch (JSONException e){
                Log.d("getTimeFieldsList", "can not read pair with index " + i);
            }
        }

        return new Pair<>(map_time_fields, receive_data.length()) ;
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

    public boolean getRepeatState(){
        String current_data = getFileJSONString();
        boolean out = false;
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(StateGlobals.REPEAT_STATE)){
                String state =  jsonObject.getString(StateGlobals.REPEAT_STATE);
                if(state.equals("true")){
                    out = true;
                }
            }
        }
        catch (JSONException e){
            Log.e("getRepeatState", "not found repeat state: " + e.getMessage() );
        }
        return out;
    }
    public void saveRepeatState(boolean state){
        String current_data = getFileJSONString();
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            jsonObject.put(StateGlobals.REPEAT_STATE, state);
            String storing_json_data = jsonObject.toString();
            fileHandling.writeToFile(filename, storing_json_data);
        }
        catch(JSONException e){
            Log.e("saveRepeatState", "not found repeat state: " + e.getMessage() );
        }
    }
    public void saveFileListNames(ArrayList<String> fileListNames){
        String current_data = getFileJSONString();
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            JSONArray jsonArray = new JSONArray(Arrays.asList(fileListNames));

            jsonObject.put(StateGlobals.LISTS_FILENAMES, jsonArray);
            String storing_json_data = jsonObject.toString();
            fileHandling.writeToFile(filename, storing_json_data);
        }
        catch(JSONException e){
            Log.e("saveFileListNames", "ERROR : " + e.getMessage() );
        }
    }
    public ArrayList<String> getFileListNames(){
        String current_data = getFileJSONString();
        ArrayList<String> fileListNames = null;
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(StateGlobals.LISTS_FILENAMES)){
                JSONArray list =  jsonObject.getJSONArray(StateGlobals.LISTS_FILENAMES);
                fileListNames = new ArrayList<>(list.length());
                for(int i = 0; i < list.length(); i++){
                    fileListNames.add(list.getString(i));
                }
            }
        }
        catch (JSONException e){
            Log.e("getFileListNames", ".. " + e.getMessage() );
        }
        return fileListNames;
    }

    public Pair<Hashtable<Integer, Timefield>, Integer> getFileList(int index_file){
        Pair<Hashtable<Integer, Timefield>, Integer> stored_data = null;
        CSVReader csvReader;
        try{
            csvReader = new CSVReader(new FileReader(FilenameGlobals.LIST_SAVE(index_file)));
            try {
                List<String[]> parsedCSV = csvReader.readAll();
                Hashtable<Integer, Timefield> map = new Hashtable<>();
                int counter = 0;
                for(String[] row : parsedCSV){
                    Timefield timefield = new Timefield(context, counter, row[0], Integer.parseInt(row[1]));
                    map.put(counter, timefield);
                    counter++;
                }
                stored_data = new Pair<>(map, counter);
            }
            catch (IOException e){
                Log.d("getFileList", "IOException reading csv!");
            }
        }
        catch (FileNotFoundException e){
            Log.d("getFileList", "File not found: "+ FilenameGlobals.LIST_SAVE(index_file));
        }
        return stored_data;
    }

    public void saveFileList(Pair<Hashtable<Integer, Timefield>, Integer> stored_data, int index_file){
        Hashtable<Integer, Timefield> map = stored_data.first;
        int items_quantity = stored_data.second;
        CSVWriter csvWriter;

        try {
            csvWriter = new CSVWriter(new FileWriter(FilenameGlobals.LIST_SAVE(index_file)), CSVWriter.DEFAULT_SEPARATOR);
            String [] entries = {CUSTOM_TEXT, MILLISECONDS};
            //csvWriter.writeNext(entries);
            ArrayList<String[]> list = new ArrayList<>();
            list.add(entries);
            for(int i = 0; i < items_quantity; i++){
                String [] item_csv = {map.get(i).getCustomText(), String.valueOf(map.get(i).getMilliseconds())};
                list.add(item_csv);
            }
            csvWriter.writeAll(list);
            csvWriter.close();
        }
        catch (IOException e){
            Log.e("saveFileList", "Error opening file: " + FilenameGlobals.LIST_SAVE(index_file));
        }

    }


}
