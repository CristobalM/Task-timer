package com.example.cristobalm.myapplication.Storage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by cristobalm on 3/3/17.
 * File IO with a context
 */

final class FileHandling {
    private Context context;
    FileHandling(Context context){
        this.context = context;
    }
    void writeToFile(String filename, String data){
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    String readFromFile(String filename){
        String ret  = "";
        try{
            InputStream inputStream = context.openFileInput(filename);
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while((receiveString = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e){
            Log.e("Reading Data", "File not found: " + e.toString());
        }
        catch (IOException e){
            Log.e("Reading data", "Can not read file: " + e.toString());
        }
        return ret;
    }
    boolean fileExists(String filename){
        File file = context.getFileStreamPath(filename);
        return file.exists();
    }
}
