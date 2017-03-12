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
    public static ListFragment newInstance(TimingService timingService){
        ListFragment listFragment = new ListFragment();
        listFragment.setInfo(timingService);
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
        ArrayList<ListItemInfo> list_items = timingService.builtListItemInfoArrayList();

        if(list_items.size() <= 0) {
            ListItemInfo listItemInfoTest = new ListItemInfo(getActivity());
            listItemInfoTest.setFile_name("HOLA TE<st");
            list_items.add(listItemInfoTest);
        }


        ListDialog listDialog = new ListDialog(getActivity(), list_items, 0);
        AlertDialog to_return = listDialog.create();
        return to_return;
    }
    public void onCancel(DialogInterface dialog){
        timingService.setOffOpeningDialogFragment();
        super.onCancel(dialog);
    }

    public void setInfo(TimingService timingService){
        this.timingService = timingService;

    }
}
