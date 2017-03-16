package com.example.cristobalm.myapplication.Storage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.FilenameGlobals;
import com.example.cristobalm.myapplication.Storage.Globals.StateGlobals;
import com.example.cristobalm.myapplication.UI.Timefield;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
    private static String SOUNDID = "sound_id";
    private FileHandling fileHandling;
    private String filename;
    private boolean fileExists;


    public StateStorage(Context context, String filename){
        this.context = context;
        this.filename = filename;
        fileHandling = new FileHandling(context);
        fileExists = fileHandling.fileExists(filename);
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


    public int getUniqueID(){
        String current_data = getFileJSONString();
        Log.e("getUniqueID()", "current_data: "+ current_data);
        int out = 0;
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(StateGlobals.UNIQUE_ID)){
                String id =  jsonObject.getString(StateGlobals.UNIQUE_ID);
                out = Integer.parseInt(id);
            }
            /*else{
                out = 0;
                jsonObject.put(StateGlobals.UNIQUE_ID, out);
                String storing_json_data = jsonObject.toString();
                fileHandling.writeToFile(filename, storing_json_data);
            }*/
        }
        catch (JSONException e){
            Log.e("getUniqueID", ".. " + e.getMessage() );
        }

        return out;
    }

    public int getLastIndexFile(){
        String current_data = getFileJSONString();
        int out = -1;
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(StateGlobals.CURRENT_INDEX)){
                String index =  jsonObject.getString(StateGlobals.CURRENT_INDEX);
                out = Integer.parseInt(index);
            }
        }
        catch (JSONException e){
            Log.e("getLastIndexFile", ".. " + e.getMessage() );
        }
        Log.e("getLastIndexFile", "lastindex!:" +out);
        return out;
    }

    public int getCommonSound(){
        String current_data = getFileJSONString();
        Log.e("getCommonSound()", "current_data: "+ current_data);
        int out = InfoNameGlobals._NOTIFICATION_TWO;
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(StateGlobals.COMMON_SOUND)){
                String id =  jsonObject.getString(StateGlobals.COMMON_SOUND);
                out = Integer.parseInt(id);
            }

        }
        catch (JSONException e){
            Log.e("getUniqueID", ".. " + e.getMessage() );
        }

        return out;
    }
    public int getFinishSound(){
        String current_data = getFileJSONString();
        Log.e("getFinishSound()", "current_data: "+ current_data);

        int out = InfoNameGlobals._NOTIFICATION_ONE;
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(StateGlobals.FINISH_SOUND)){
                String id =  jsonObject.getString(StateGlobals.FINISH_SOUND);
                out = Integer.parseInt(id);
            }
        }
        catch (JSONException e){
            Log.e("getUniqueID", ".. " + e.getMessage() );
        }

        return out;
    }

    public void saveAll(Hashtable<Integer, String> hashtable,
                        ArrayList<Integer> ids,
                        int unique_id,
                        int current_index,
                        boolean repeat_state,
                        int common_sound,
                        int finish_sound){
        String current_data = getFileJSONString();
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            JSONObject _map = new JSONObject();
            for(int i = 0; i < ids.size(); i++){
                String name = hashtable.get(ids.get(i));
                if(name != null) {
                    String _key = String.valueOf(ids.get(i));
                    _map.put(_key, name);
                }
            }
            JSONArray jsonArray = new JSONArray(ids);

            jsonObject.put(StateGlobals.LIST_NAMES_MAP, _map);
            jsonObject.put(StateGlobals.LISTS_IDS, jsonArray);

            jsonObject.put(StateGlobals.UNIQUE_ID, (unique_id));
            jsonObject.put(StateGlobals.CURRENT_INDEX, (current_index));
            jsonObject.put(StateGlobals.REPEAT_STATE, repeat_state);

            jsonObject.put(StateGlobals.COMMON_SOUND, common_sound);
            jsonObject.put(StateGlobals.FINISH_SOUND, finish_sound);

            String storing_json_data = jsonObject.toString();
            Log.e("saveListAndMapFiles", "saveListAndMapFiles JSON!: "+ storing_json_data );
            fileHandling.writeToFile(filename, storing_json_data);


        }
        catch (JSONException e){
            Log.e("saveFileNamesMap", ".. " + e.getMessage() );
        }
    }

    public Hashtable<Integer, String> getFileNamesMap(ArrayList<Integer> ids){
        String current_data = getFileJSONString();
        Hashtable<Integer, String> hashtable = new Hashtable<>();
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(StateGlobals.LIST_NAMES_MAP)){
                JSONObject list_names_map =  jsonObject.getJSONObject(StateGlobals.LIST_NAMES_MAP);
                Log.e("getFileNamesMap", "list_names_map="+list_names_map.toString());
                for(int i = 0; i < ids.size(); i++){
                    String _key = String.valueOf(ids.get(i));
                    if(list_names_map.has(_key)){
                        hashtable.put(ids.get(i), list_names_map.getString(_key));
                    }
                }
            }
        }
        catch (JSONException e){
            Log.e("getFileNamesMap", ".. " + e.getMessage() );
        }
        return hashtable;
    }
    public ArrayList<Integer> getFileListIDS(){
        String current_data = getFileJSONString();
        Log.e("getFileListIDS","current_data:"+current_data);
        ArrayList<Integer> fileListIDS = null;
        try{
            JSONObject jsonObject = new JSONObject(current_data);
            if(jsonObject.has(StateGlobals.LISTS_IDS)){
                JSONArray list =  jsonObject.getJSONArray(StateGlobals.LISTS_IDS);
                Log.e("getFileListIDS", "list: " + list.toString());
                fileListIDS = new ArrayList<>(list.length());
                for(int i = 0; i < list.length(); i++){
                    fileListIDS.add(Integer.parseInt(list.getString(i)));
                }
            }else{
                Log.e("getFileListIDS", "NOT FOUND LIST!!!!!!: ");

            }
        }
        catch (JSONException e){
            Log.e("getFileListNames", ".. " + e.getMessage() );
        }
        return fileListIDS;
    }


    public Pair<Hashtable<Integer, Timefield>, Integer> getFileList(int index_file){
        Pair<Hashtable<Integer, Timefield>, Integer> stored_data = null;
        CSVReader csvReader;
        String filename = FilenameGlobals.LIST_SAVE(index_file);
        Log.d("getFileList", "reading file "+ filename);
        String file_string = fileHandling.readFromFile(filename);
        StringReader stringReader = new StringReader(file_string);
        Log.d("getFileList", "STRING="+ file_string );
        csvReader = new CSVReader(stringReader);
        try {
            List<String[]> parsedCSV = csvReader.readAll();
            Hashtable<Integer, Timefield> map = new Hashtable<>();
            int counter = 0;
            boolean first = true;
            for(String[] row : parsedCSV){
                Log.d("getFileList", "counter: "+ counter + ", name:" + row[0] + ", millis:" + row[1]);
                if(first){
                    first = false;
                }else {
                    Log.d("getFileList", " row[1] is " + row[1]);
                    int millis = row[1].length() > 0 ? Integer.parseInt(row[1]) : 0;
                    int soundID = row[2].length() > 0 ? Integer.parseInt(row[2]) : InfoNameGlobals._NOTIFICATION_TWO;
                    Timefield timefield = new Timefield(context, counter, row[0], millis);
                    timefield.setSound(soundID);
                    map.put(counter, timefield);
                    counter++;
                }
            }
            stored_data = new Pair<>(map, counter);

        }
        catch (IOException e){
            Log.e("getFileList", "IOEXCEPTION!!!!");
        }

        return stored_data;
    }

    public void saveFileList(Pair<Hashtable<Integer, Timefield>, ArrayList<Integer>> stored_data, int index_file){
        Hashtable<Integer, Timefield> map = stored_data.first;
        ArrayList<Integer> time_fields = stored_data.second;


        String filename = FilenameGlobals.LIST_SAVE(index_file);
        CSVWriter csvWriter;
        //File file = new File(FilenameGlobals.LIST_SAVE(index_file)).getAbsolutePath();
        StringWriter stringWriter = new StringWriter();
        Log.d("saveFileList", "writing to file: " + FilenameGlobals.LIST_SAVE(index_file) + ", items_quantity:"+time_fields.size());
        csvWriter = new CSVWriter(stringWriter);
        String [] entries = {CUSTOM_TEXT, MILLISECONDS, SOUNDID};
        ArrayList<String[]> list = new ArrayList<>();
        list.add(entries);
        for(int i = 0; i < time_fields.size(); i++){
            Timefield _timefield = map.get(time_fields.get(i));
            String [] item_csv = {
                    map.get(time_fields.get(i)).getCustomText(),
                    String.valueOf(_timefield.getMilliseconds()),
                    String.valueOf(_timefield.getSoundId())
            };
            list.add(item_csv);
        }
        //csvWriter.writeNext(entries);
        csvWriter.writeAll(list);
        try {
            csvWriter.close();
        }
        catch (IOException e){
            Log.e("saveFileList", "Error saving file: " + FilenameGlobals.LIST_SAVE(index_file));
        }
        String to_store_string = stringWriter.toString();
        fileHandling.writeToFile(filename, to_store_string);
    }

    public void deleteFiles(ArrayList<Integer> bulk_delete){
        for(int i = 0; i < bulk_delete.size(); i++) {
            String filename = FilenameGlobals.LIST_SAVE(bulk_delete.get(i));
            fileHandling.deleteFile(filename);
            Log.d("deleteFiles", "deleting file: " + filename);
        }
    }


}
