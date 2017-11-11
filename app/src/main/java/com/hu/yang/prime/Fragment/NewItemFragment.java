package com.hu.yang.prime.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by yanghu on 2017/8/18.
 */

public class NewItemFragment extends Fragment {
    public interface OnNewItemAddedLisenter {
        void onNewItemAdded(String item);
    }

    private OnNewItemAddedLisenter onNewItemAddedLisenter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onNewItemAddedLisenter = (OnNewItemAddedLisenter) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.getClass().getSimpleName() + "must implements OnNewItemAddedLisenter");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final EditText et = new EditText(getActivity());
        et.setHint("New To Do Item");
        et.setSingleLine();
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String item = et.getText().toString();
                if(TextUtils.isEmpty(item)){
                   return true;
                }
                onNewItemAddedLisenter.onNewItemAdded(item);
                et.setText("");
                return true;
            }
        });
        return et;
    }
}
