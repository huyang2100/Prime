package com.hu.yang.prime.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hu.yang.prime.R;

import java.util.ArrayList;

/**
 * Created by yanghu on 2017/9/14.
 */

public class ListViewHeavyFragment extends Fragment {
    private ArrayList<String> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listview_heavy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = (ListView) view.findViewById(R.id.lv);
        for (int i = 0; i < 10; i++) {
            dataList.add(String.valueOf(i));
        }
        HeavyAdapter heavyAdapter = new HeavyAdapter(dataList);
        lv.setAdapter(heavyAdapter);
    }

    class HeavyAdapter extends BaseAdapter {

        private final ArrayList<String> dataList;

        public HeavyAdapter(ArrayList<String> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            FrameLayout fl = new FrameLayout(getContext());

            TextView tv = new TextView(getContext());
            tv.setTextSize(48);
            tv.setTextColor(Color.BLUE);
            int padd = 120;
            tv.setPadding(padd, padd, padd, padd);
            fl.addView(tv);

            tv.setTag(i);

            ProgressBar pb = new ProgressBar(getContext());
            fl.addView(pb);

            new LoadTask(tv,pb).execute();

            return fl;
        }
    }

    class LoadTask extends AsyncTask<Void,Void,Void>{


        private final TextView tv;
        private final ProgressBar pb;

        public LoadTask(TextView tv, ProgressBar pb) {
            this.tv = tv;
            this.pb = pb;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pb.setVisibility(View.GONE);
            int i = (int) tv.getTag();
            tv.setText(dataList.get(i));
        }
    }
}
