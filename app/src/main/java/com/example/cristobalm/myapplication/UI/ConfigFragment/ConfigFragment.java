package com.example.cristobalm.myapplication.UI.ConfigFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.Globals.MainStateGlobals;
import com.example.cristobalm.myapplication.UI.ListFragment.ListDialog;
import com.example.cristobalm.myapplication.UI.ListFragment.ListFragment;
import com.example.cristobalm.myapplication.UI.ListFragment.ListItemInfo;
import com.example.cristobalm.myapplication.UI.MusicFragment.MusicFragment;

import java.util.ArrayList;

/**
 * Created by cristobalm on 3/14/17.
 */

public class ConfigFragment extends DialogFragment {
    TimingService timingService;

    public static ConfigFragment newInstance(TimingService timingService){
        ConfigFragment configFragment = new ConfigFragment();
        configFragment.setInfo(timingService);
        configFragment.setRetainInstance(true);

        return configFragment;
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
        final ConfigDialog configDialog = new ConfigDialog(getActivity(), R.style.MyDialogTheme);
        int color_finish = InfoNameGlobals.getSColorById(timingService.getFinishSound());
        int color_common = InfoNameGlobals.getSColorById(timingService.getCommonSound());
        configDialog.getFinishedImageView().setColorFilter(color_finish, PorterDuff.Mode.MULTIPLY);
        configDialog.getCommonImageView().setColorFilter(color_common, PorterDuff.Mode.MULTIPLY);

        configDialog.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timingService.processChange(configDialog.applyToAllChecked());
                if(timingService != null){
                    timingService.changeDone();
                }
            }
        });

        configDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        configDialog.setSoundFinishedListener(new ChangeMusicListener(InfoNameGlobals.SOUND_FINISHING, configDialog.getFinishedImageView()));
        configDialog.setSoundCommonListener(new ChangeMusicListener(InfoNameGlobals.SOUND_COMMON, configDialog.getCommonImageView()));


        AlertDialog to_return = configDialog.create();
        if(to_return != null && to_return.getWindow() != null) {
            to_return.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return to_return;

    }
    public void onCancel(DialogInterface dialog){
        timingService.setOffOpeningDialogFragment();

        super.onCancel(dialog);
    }

    public void setInfo(TimingService timingService){
        this.timingService = timingService;
    }

    public class ChangeMusicListener implements View.OnTouchListener{
        int which;
        ImageView imageView;
        ChangeMusicListener(int which, ImageView imageView){
            this.which = which;
            this.imageView = imageView;
        }

        @Override
        public boolean onTouch(View v, MotionEvent e){
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(timingService.getMainState() == MainStateGlobals.STATE_IDLE) {
                        MusicFragment musicFragment = MusicFragment.newInstance(timingService, which, imageView );
                        musicFragment.show(getFragmentManager(), "musicPicker");

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    }

}
