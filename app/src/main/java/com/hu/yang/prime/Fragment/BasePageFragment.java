package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hu.yang.prime.widget.BasePage;

/**
 * Created by yanghu on 2017/10/23.
 */

public class BasePageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BasePage basePage = new BasePage(getContext()) {
            @Override
            public View getSuccessView() {
                TextView tv = new TextView(getActivity());
                tv.setText("BingGo!!!");
                tv.setTextSize(36);
                return tv;
            }
        };
        basePage.show(BasePage.STATE_SUCCESS);
        return basePage;
    }
}
