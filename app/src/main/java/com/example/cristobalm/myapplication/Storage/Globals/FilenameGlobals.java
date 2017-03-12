package com.example.cristobalm.myapplication.Storage.Globals;

/**
 * Created by cristobalm on 3/3/17.
 * Self descriptive
 */

public class FilenameGlobals {
    public static final String STORED_STATES = "stored_states.json";
    public static final String CONFIG_STATES = "config_states.json";


    public static final String LIST_SAVE(int index){
        return "list_save_" + index + ".csv";
    }

    public static final String CSV_SEPARATOR = ",";
}
