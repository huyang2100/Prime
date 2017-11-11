package com.hu.yang.prime.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hu.yang.prime.Fragment.SwitchMenuFragment;
import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/8/30.
 */

public class SwitchMenuActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_menu);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.fl_root,new SwitchMenuFragment()).commit();
        }
    }

    public static void action(Context context) {
        Intent intent = new Intent(context, SwitchMenuActivity.class);
        context.startActivity(intent);
    }
}
