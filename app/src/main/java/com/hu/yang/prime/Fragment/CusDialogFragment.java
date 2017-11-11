package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/8/16.
 */

public class CusDialogFragment extends DialogFragment {
    public static final int STYLE_ANDROID = 0;
    public static final int STYLE_IOS = 1;

    private int curStyle = STYLE_ANDROID;

    public void setShowStyle(int style){
        curStyle = style;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //背景透明
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //去除标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        int layout_dialog;
        if(curStyle == STYLE_ANDROID){
            layout_dialog = R.layout.fragment_android_dialog;
        }else{
            layout_dialog = R.layout.fragment_ios_dialog;
        }
        return inflater.inflate(layout_dialog,container,false);
    }
}
