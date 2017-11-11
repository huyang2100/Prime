package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.ViewAnimator;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/8/30.
 */

public class SwitchMenuFragment extends Fragment {

    private ViewAnimator mViewAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_switch_menu,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rg);
        mViewAnimator = (ViewAnimator) view.findViewById(R.id.va);
        mViewAnimator.setDisplayedChild(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_china:
                        mViewAnimator.setDisplayedChild(0);
                        break;
                    case R.id.rb_usa:
                        mViewAnimator.setDisplayedChild(1);
                        break;
                    case R.id.rb_japan:
                        mViewAnimator.setDisplayedChild(2);
                        break;
                }
            }
        });
    }
}
