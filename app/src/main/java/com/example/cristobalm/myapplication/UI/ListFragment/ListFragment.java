package com.example.cristobalm.myapplication.UI.ListFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.MainActivity;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/11/17.
 */

public class ListFragment extends DialogFragment {
    TimingService timingService;
    public static ListFragment newInstance(TimingService timingService, ArrayList<ListItemInfo> list_items){
        ListFragment listFragment = new ListFragment();
        listFragment.setInfo(timingService, list_items);
        listFragment.setRetainInstance(true);

        return listFragment;
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //resetColors();
        timingService.loadFile(timingService.getCurrentIndexFile());
        timingService.reloadColor();
        ListDialog listDialog = new ListDialog(getActivity(), R.style.MyDialogTheme, listItemInfos);
        listDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timingService.setOffOpeningDialogFragment();
                //timingService.clearOtherBackground();
            }
        });
        listDialog.setPositiveButton(R.string.open,  new OkButtonListener(timingService));
        timingService.setFilesDialogList(listDialog.getList());

        //listDialog.setView(timingService.getViewForDialog(getActivity(), listItemInfos));

        AlertDialog to_return = listDialog.create();
        to_return.setTitle("Select file to open");
        return to_return;
    }
    public void onCancel(DialogInterface dialog){
        timingService.setOffOpeningDialogFragment();
        super.onCancel(dialog);
    }

    ArrayList<ListItemInfo> listItemInfos;
    public void setInfo(TimingService timingService, ArrayList<ListItemInfo> listItemInfos){
        this.timingService = timingService;
        this.listItemInfos = listItemInfos;
    }

    public class OkButtonListener implements DialogInterface.OnClickListener{
        TimingService aService;
        OkButtonListener(TimingService aService){
            this.aService = aService;
        }
        @Override
        public void onClick(DialogInterface dialogInterface, int which){
            aService.startFile();
        }
    }
}
