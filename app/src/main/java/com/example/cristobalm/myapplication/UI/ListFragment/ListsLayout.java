package com.example.cristobalm.myapplication.UI.ListFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by cristobalm on 3/12/17.
 */

public class ListsLayout extends RelativeLayout {
    Context context;
    AttributeSet attrs;

    ScrollView scrollView;
    LinearLayout linearLayout;

    public ListsLayout(Context context){
        super(context);
        this.context = context;
        init();
    }
    public ListsLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.context = context;
        this.attrs = attrs;
        init();
    }
    public ListsLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    public void init(){
        scrollView = new ScrollView(context);
        linearLayout = new LinearLayout(context);

        LinearLayout.LayoutParams scrollView_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams linearLayout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        scrollView.setLayoutParams(scrollView_params);
        linearLayout.setLayoutParams(linearLayout_params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        this.addView(scrollView);
    }

    public void addItemToList(ListItem listItem){
        linearLayout.addView(listItem);
    }
    public void removeItemFromList(ListItem listItem){
        linearLayout.removeView(listItem);
    }

}
