package com.example.cristobalm.myapplication.UI.GreatTimePicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.cristobalm.myapplication.ObjectContainer.TimeContainer;
import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.GreatTimeListItem.TimeLinearLayout;
import com.example.cristobalm.myapplication.UI.MainActivity;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/5/17.
 */

public class GreatTimePickerFragment extends DialogFragment implements GreatTimePickerDialog.OnTimeSetListener {
    TimeLinearLayout timeLinearLayout;
    GreatTimePickerDialog greatTimePickerDialog;
    Timefield timefield;
    TimingService mService;
    public static GreatTimePickerFragment newInstance(int static_index, TimingService mService){

        GreatTimePickerFragment greatTimePickerFragment = new GreatTimePickerFragment();

        Bundle args = new Bundle();
        args.putInt("static_index", static_index);
        greatTimePickerFragment.setArguments(args);
        greatTimePickerFragment.setInfo(mService);
        greatTimePickerFragment.setRetainInstance(true);

        return greatTimePickerFragment;
    }

    public void setInfo(TimingService timingService){
        mService = timingService;
    }

    @Override
    public void updateMillis(int millis){
        if(mService != null){
            mService.setMillisEditing(millis);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart(){
        super.onStart();


    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int static_index = getArguments().getInt("static_index");
        int millis;

        if(mService != null) {
            timefield = mService.getTFwithID(static_index);
            timeLinearLayout = timefield.getTimeLinearLayout();
            millis = mService.getMillisEditing() > -1 ? mService.getMillisEditing() : timefield.getMilliseconds();
            Log.d("onCreateDialog","created dialog with service!");

        }
        else{
            millis = 0;
        }

        greatTimePickerDialog = new GreatTimePickerDialog(getActivity(), R.style.MyDialogTheme, this, millis);

        AlertDialog to_return = greatTimePickerDialog.create();
        if(to_return != null && to_return.getWindow() != null) {
            to_return.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
            //greatTimePickerDialog.setTitle("Select time");

        return to_return;
    }

    @Override
    public void onTimeSet(GreatTimePicker view, int millis){

        timeLinearLayout.setTime(millis);
        if(mService != null) {
            Log.d("onTimeSet", "calling addsecondsToTotal");
            mService.addSecondsToTotal((millis/1000 - timefield.getMilliseconds()/1000));
        }else{
            Log.d("onTimeSet", "mService is null!");
        }
        timefield.setTime(millis);
        mService.setMillisEditing(-1);
        mService.setOffOpeningDialogFragment();
        mService.changeDone();
    }
    public void onCancel(DialogInterface dialog){
        mService.setOffOpeningDialogFragment();
        super.onCancel(dialog);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        timeLinearLayout.getTimeCountdownView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorCountdownBackground));
    }

    @Override
    public void show(FragmentManager manager, String tag){
        super.show(manager, tag);
    }
}
