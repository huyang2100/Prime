package com.hu.yang.prime.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hu.yang.prime.base.MyBaseAdapter;
import com.hu.yang.prime.bean.PersonBean;

import java.util.List;

/**
 * Created by yanghu on 2017/10/23.
 */

public class PersonHolder extends BaseHolder<PersonBean> {

    private View view;
    private TextView textView;

    @Override
    public void initView(Context context) {
        view = View.inflate(context, android.R.layout.simple_list_item_1, null);
        textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public View getRootView() {
        return view;
    }

    @Override
    public void refreshView(List<PersonBean> dataList, int position, MyBaseAdapter adapter) {
        textView.setText(dataList.get(position).toString());
    }


}
