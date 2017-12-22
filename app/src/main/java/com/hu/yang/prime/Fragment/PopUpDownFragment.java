package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.PopupWindowCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.hu.yang.prime.R;
import com.hu.yang.prime.util.PopUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yanghu on 2017/11/19.
 */

public class PopUpDownFragment extends Fragment implements View.OnClickListener {

    private View ll_root;
    private View btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popup_down, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn = view.findViewById(R.id.btn_click);
        btn.setOnClickListener(this);
        ll_root = view.findViewById(R.id.ll_root);
    }

    @Override
    public void onClick(View v) {
        View view = View.inflate(getActivity(), R.layout.option, null);
        PopUtil.showBottom(getActivity(), view, ll_root);


        JSONObject json = null;
        try {
            json = new JSONObject("{\"redJsonList\":[\"{\\\"id\\\":1,\\\"num\\\":3}\",\"{\\\"id\\\":5,\\\"num\\\":2}\"]}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonObject = json.optJSONArray("redJsonList");
        String jsonString = json.toString();
        Log.e("=====返回数据", jsonString);
        if (jsonObject != null) {
            for (int i = 0; i < jsonObject.length(); i++) {
                String jsonstr = jsonObject.optString(i);
                jsonstr = jsonstr.substring(1, jsonstr.length() - 1);
                jsonstr = jsonstr.substring(5);
                int index = jsonstr.indexOf(",");
                String idstr = jsonstr.substring(0, index);
                int unread_id = Integer.parseInt(idstr);
                jsonstr = jsonstr.substring(index + 7);
                int unread_num = Integer.parseInt(jsonstr);
                Log.i("", "onClick: ");
//                if (unread_num > 0) {
//                    saveUnread(unread_id, unread_num);
//                }

            }
        }
    }
}
