package com.hu.yang.prime.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hu.yang.prime.R;
import com.hu.yang.prime.base.MyBaseAdapter;
import com.hu.yang.prime.viewholder.BaseHolder;
import com.hu.yang.prime.viewholder.MyHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanghu on 2017/10/23.
 */

public class ListViewBuildFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_listview_build,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add("It's item: " + i);
        }


//        ArrayList<PersonBean> dataList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            PersonBean personBean = new PersonBean();
//            personBean.setName("My name is: " + i);
//            personBean.setAge(i + 10);
//            dataList.add(personBean);
//        }


        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(new MyAdapter(getContext(), dataList));
    }

    public class MyAdapter extends MyBaseAdapter<String> {

        public MyAdapter(Context context, List<String> dataList) {
            super(context, dataList);
        }

        @Override
        public BaseHolder getHolder() {
            return new MyHolder();
        }

        @Override
        public void loadMore(final int position) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(500);
                    ArrayList<String> dataList = new ArrayList<>();
                    for (int i = position; i < position+20; i++) {
                        dataList.add("It's item: " + i);
                    }
                    loadMoreHolder.show(dataList);
                }
            }).start();
        }
    }


//    class MyAdapter extends MyBaseAdapter<PersonBean> {
//
//        public MyAdapter(Context context, List<PersonBean> dataList) {
//            super(context, dataList);
//        }
//
//        @Override
//        public BaseHolder getHolder() {
//            return new PersonHolder();
//        }
//
//        @Override
//        public void loadMore(final int position) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    SystemClock.sleep(1000);
//                    ArrayList<PersonBean> newList = new ArrayList<>();
//                    for (int i = position; i < position + 20; i++) {
//                        PersonBean personBean = new PersonBean();
//                        personBean.setName("My name is: " + i);
//                        personBean.setAge(i + 10);
//                        newList.add(personBean);
//                    }
//
//                    loadMoreHolder.show(newList);
//                }
//            }).start();
//        }
//    }
}
