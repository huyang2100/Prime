package com.hu.yang.prime.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hu.yang.prime.Fragment.ListViewBuildFragment;
import com.hu.yang.prime.R;
import com.hu.yang.prime.base.MyBaseAdapter;

import java.util.List;

/**
 * Created by yanghu on 2017/10/23.
 */

public class MyHolder extends BaseHolder<String> {
    private View view;
    private TextView tv;

    @Override
    public void initView(Context context) {
        view = View.inflate(context, R.layout.item_search_record, null);
        tv = (TextView) view.findViewById(R.id.tv_record);
    }

    @Override
    public View getRootView() {
        return view;
    }

    @Override
    public void refreshView(List<String> dataList, int position, MyBaseAdapter adapter) {

        String data = dataList.get(position);
        tv.setText(data);
    }

}
