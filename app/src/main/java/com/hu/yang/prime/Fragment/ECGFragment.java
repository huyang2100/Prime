package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hu.yang.prime.widget.ECGView;

/**
 * Created by yanghu on 2017/8/28.
 */

public class ECGFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new ECGView(getActivity(),null);
    }
}
