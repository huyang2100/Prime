package com.hu.yang.prime.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hu.yang.prime.Fragment.CusDialogFragment;
import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/8/17.
 */

public class DialogActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    public void androidDialog(View v){
        CusDialogFragment cusDialogFragment = new CusDialogFragment();
        cusDialogFragment.show(getSupportFragmentManager(),"androidDialog");
    }

    public void iosDialog(View v){
        CusDialogFragment cusDialogFragment = new CusDialogFragment();
        cusDialogFragment.setShowStyle(CusDialogFragment.STYLE_IOS);
        cusDialogFragment.show(getSupportFragmentManager(),"iosDialog");
    }
}
