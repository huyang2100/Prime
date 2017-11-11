package com.hu.yang.prime.viewholder;

import android.content.Context;
import android.view.View;

import com.hu.yang.prime.base.MyBaseAdapter;

import java.util.List;

/**
 * Created by yanghu on 2017/10/23.
 */

public abstract class BaseHolder<T> {
    public abstract void initView(Context context);

    public abstract View getRootView();

    public abstract void refreshView(List<T> dataList, int position, MyBaseAdapter adapter);
}
