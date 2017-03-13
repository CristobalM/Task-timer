package com.example.cristobalm.myapplication.UI.ListFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cristobalm.myapplication.R;
import com.example.cristobalm.myapplication.UI.Globals.VisualSettingGlobals;

import org.w3c.dom.Text;

/**
 * Created by cristobalm on 3/11/17.
 */

public class ListItem extends LinearLayout {
    Context context;
    AttributeSet attrs;

    ListName file_name;
    //ImageView edit_button;
    ImageView delete_button;
    float scale;

    public void setItemBackground(int color_src){
        int color;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            color = context.getResources().getColor(color_src, null);
        }
        else{
            color = context.getResources().getColor(color_src);
        }
        file_name.setBackgroundColor(color);
        file_name.invalidate();

    }

    public void setFile_name(String fileName){
        file_name.setText(fileName);
    }
    public String getFile_name(){
        return file_name.getText().toString();
    }


    public void setOpenerListener(OnTouchListener onTouchListener){
        file_name.setOnTouchListener(onTouchListener);
    }

    public ListItem(Context context){
        super(context);
        this.context = context;
        init();
    }
    public ListItem(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.context = context;
        this.attrs = attrs;
        init();
    }
    public ListItem(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    public void setHint(String hint){
        file_name.setHint(hint);
    }

    Drawable getDrawableCompat(int resourceID){
        Drawable drawable;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            drawable = getResources().getDrawable(resourceID , null);
        }
        else{
            drawable =  getResources().getDrawable(resourceID);
        }
        return drawable;
    }
    public void init(){
        scale =  getResources().getDisplayMetrics().density;
        file_name = new ListName(context);
        //edit_button = new ImageView(context);
        delete_button = new ImageView(context);

        int delete_button_width = VisualSettingGlobals.getPixels(30, scale);

        LayoutParams my_params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams file_name_params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //LayoutParams edit_button_params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams delete_button_params = new LayoutParams(delete_button_width, delete_button_width);

        Drawable delete_button_drawable =getDrawableCompat(R.drawable.ic_black_cross);

        //edit_button.setImageDrawable();
        delete_button.setImageDrawable(delete_button_drawable);

        //edit_button.setLayoutParams(edit_button_params);

        file_name.setPadding(VisualSettingGlobals.getPixels(10,scale),
                VisualSettingGlobals.getPixels(0, scale),
                VisualSettingGlobals.getPixels(10,scale),
                VisualSettingGlobals.getPixels(0,scale));
        file_name.setGravity(Gravity.CENTER_VERTICAL);

        delete_button_params.gravity = Gravity.CENTER;




        delete_button.setLayoutParams(delete_button_params);

        file_name.setLayoutParams(file_name_params);







        this.addView(file_name);
        //this.addView(edit_button);
        this.addView(delete_button);

        this.setLayoutParams(my_params);
        this.invalidate();
    }



}
