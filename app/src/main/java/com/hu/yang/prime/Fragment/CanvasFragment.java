package com.hu.yang.prime.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hu.yang.prime.R;
import com.hu.yang.prime.widget.FocusView;

/**
 * Created by yanghu on 2017/8/25.
 */

public class CanvasFragment extends Fragment {

    private View btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_canvas,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn = view.findViewById(R.id.btn_clear);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSharedPreferences(FocusView.class.getName(), Context.MODE_PRIVATE).edit().clear().commit();
                Toast.makeText(getActivity(), "sp clear!", Toast.LENGTH_SHORT).show();
            }
        });
        FocusView cv = (FocusView) view.findViewById(R.id.cv);
        cv.setFocusable(true);
        cv.setTargetView(view.findViewById(R.id.btn),R.drawable.ic_card,R.drawable.sample_0);

    }

    public static Fragment newInstance() {
        CanvasFragment canvasFragment = new CanvasFragment();
        return canvasFragment;
    }
}
