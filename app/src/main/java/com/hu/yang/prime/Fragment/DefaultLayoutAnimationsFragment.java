package com.hu.yang.prime.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/10/17.
 */

public class DefaultLayoutAnimationsFragment extends Fragment {
    private int numButtons = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_default_layout_animations, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GridLayout gridContainer = (GridLayout) view.findViewById(R.id.gridContainer);

//        Button addButton = (Button) view.findViewById(R.id.addNewButton);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Button newButton = new Button(getActivity());
//                gridContainer.addView(newButton, (numButtons-1));
//                newButton.setText(String.valueOf(numButtons++));
//                newButton.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        gridContainer.removeView(v);
//                        numButtons--;
//                    }
//                });
////                gridContainer.addView(newButton, Math.min(1, gridContainer.getChildCount()));
//            }
//        });


        for(int i=0;i<10;i++){
            Button newButton = new Button(getActivity());
            gridContainer.addView(newButton, i);
            newButton.setText(String.valueOf(i));
            newButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    gridContainer.removeView(v);

                }
            });
        }
    }
}
