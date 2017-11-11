package com.hu.yang.prime.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.hu.yang.prime.application.PrimeApplication;
import com.hu.yang.prime.R;
import com.hu.yang.prime.base.MyBaseAdapter;

import java.util.List;

/**
 * Created by yanghu on 2017/10/23.
 */

public class LoadMoreHolder extends BaseHolder {
    View rootView;
    private List dataList;
    private MyBaseAdapter adapter;
    private View loadingView;
    private View errView;
    private int position;
    private Context context;

    @Override
    public void initView(Context context) {
        this.context = context;
        rootView = View.inflate(context, R.layout.item_load_more, null);
        loadingView = rootView.findViewById(R.id.loadingView);
        errView = rootView.findViewById(R.id.errView);
        rootView.findViewById(R.id.btn_err_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null){
                    rootView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.VISIBLE);
                    errView.setVisibility(View.GONE);
                    adapter.loadMore(position);
                }
            }
        });
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void refreshView(List dataList, final int position, final MyBaseAdapter adapter) {
        this.dataList = dataList;
        this.adapter = adapter;
        this.position = position;
        rootView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.VISIBLE);
        errView.setVisibility(View.GONE);
        adapter.loadMore(position);
    }

    public  void show(final List newList) {
        PrimeApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                if(newList == null){
                    rootView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.GONE);
                    errView.setVisibility(View.VISIBLE);
                }else if(newList.size() == 0){
                    rootView.setVisibility(View.GONE);
                    adapter.setFinsh(true);
                    adapter.update(dataList);
                    Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
                }else{
                    rootView.setVisibility(View.GONE);
                    dataList.addAll(newList);
                    adapter.update(dataList);
                }
            }
        });
    }
}
