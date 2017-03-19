package com.example.cristobalm.myapplication.UI.MusicFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.Services.Globals.InfoNameGlobals;
import com.example.cristobalm.myapplication.Services.TimingService;
import com.example.cristobalm.myapplication.UI.ConfigFragment.ConfigDialog;
import com.example.cristobalm.myapplication.UI.ConfigFragment.ConfigFragment;
import com.example.cristobalm.myapplication.UI.Timefield;

/**
 * Created by cristobalm on 3/15/17.
 */

public class MusicFragment extends DialogFragment {
    TimingService timingService;
    Timefield timefield;
    int which_config;
    public static MusicFragment newInstance(TimingService timingService, Timefield timefield){
        MusicFragment musicFragment = new MusicFragment();
        musicFragment.setInfo(timingService, timefield);
        musicFragment.setRetainInstance(true);

        return musicFragment;
    }
    public static MusicFragment newInstance(TimingService timingService, int which_config, ImageView to_show_img){
        MusicFragment musicFragment = new MusicFragment();
        musicFragment.setInfo(timingService, which_config, to_show_img);
        musicFragment.setRetainInstance(true);

        return musicFragment;
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

    SelectMusicDialog selectMusicDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(timefield != null) {
             selectMusicDialog = new SelectMusicDialog(
                    getActivity(),
                    R.style.MyDialogTheme,
                    timingService,
                    timefield);
        }else{
            selectMusicDialog = new SelectMusicDialog(
                    getActivity(),
                    R.style.MyDialogTheme,
                    timingService,
                    which_config
            );
        }

        selectMusicDialog.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(timefield != null) {
                    timingService.startLoadMusic();
                    timingService.loadMusic(0, null);
                    timefield.getTimeLinearLayout().resetMusicBackgroudColor();
                }else{
                    int color = -1;

                    if(which_config == InfoNameGlobals.SOUND_FINISHING &&
                            timingService.getTempFinishingSound() != null){
                        color = InfoNameGlobals.getSColorById(timingService.getTempFinishingSound());

                    }else if(which_config == InfoNameGlobals.SOUND_COMMON &&
                            timingService.getTempCommonSound() != null){
                        color = InfoNameGlobals.getSColorById(timingService.getTempCommonSound());
                    }
                    Log.e("onMusicFragment", "color:"+color+ " to_show_img==null?"+ (to_show_img==null) + ", which_config="+which_config);
                    if(color != -1) {
                        to_show_img.setColorFilter(
                                color
                                , PorterDuff.Mode.MULTIPLY);
                        to_show_img.invalidate();
                    }
                }
            }
        });

        selectMusicDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(timefield != null) {
                    timingService.loadMusic(0, null);
                    timefield.getTimeLinearLayout().resetMusicBackgroudColor();
                }
            }
        });


        AlertDialog to_return = selectMusicDialog.create();
        if(to_return != null && to_return.getWindow() != null) {
            to_return.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        return to_return;

    }
    public void onCancel(DialogInterface dialog){
        timingService.setOffOpeningDialogFragment();
        if(timefield != null){
            timefield.getTimeLinearLayout().resetMusicBackgroudColor();
        }

        super.onCancel(dialog);
    }

    public void setInfo(TimingService timingService, Timefield timefield){
        this.timingService = timingService;
        this.timefield = timefield;
    }
    ImageView to_show_img;
    public void setInfo(TimingService timingService, int which_config, ImageView to_show_img){
        this.timingService = timingService;
        this.which_config = which_config;
        this.to_show_img = to_show_img;
    }


}
