package com.hu.yang.prime.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hu.yang.prime.viewholder.BaseHolder;
import com.hu.yang.prime.viewholder.LoadMoreHolder;
import com.hu.yang.prime.viewholder.MyHolder;

import java.util.List;

/**
 * Created by yanghu on 2017/10/23.
 */

public abstract class MyBaseAdapter <T> extends BaseAdapter {
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_MORE = 1;
    private final Context context;
    public List<T> dataList;
    private BaseHolder holder;
    protected LoadMoreHolder loadMoreHolder;
    private boolean isFinsh;

    public MyBaseAdapter(Context context, List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if(isFinsh){
            return dataList.size();
        }else{
            return dataList.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(isFinsh){
            return ITEM_TYPE_NORMAL;
        }else{
            if (position == getCount() - 1) {
                return ITEM_TYPE_MORE;
            } else {
                return ITEM_TYPE_NORMAL;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (getItemViewType(position) == ITEM_TYPE_NORMAL) {
                holder = getHolder();
            } else if (getItemViewType(position) == ITEM_TYPE_MORE) {
                loadMoreHolder = new LoadMoreHolder();
                holder = loadMoreHolder;
            }
            holder.initView(context);
            convertView = holder.getRootView();
            convertView.setTag(holder);
        } else {
            holder = (BaseHolder) convertView.getTag();
        }

        holder.refreshView(dataList, position, this);
        return convertView;
    }

    public abstract BaseHolder getHolder();

    public void update(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setFinsh(boolean finsh) {
        isFinsh = finsh;
    }

    public abstract void loadMore(int position);
}
