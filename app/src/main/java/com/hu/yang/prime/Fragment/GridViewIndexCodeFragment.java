package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hu.yang.prime.R;

import java.util.Arrays;

/**
 * Created by yanghu on 2017/10/19.
 */

public class GridViewIndexCodeFragment extends Fragment {
    private static final int COLUMN_COUNT = 3;
    private String[] modes = {"新闻", "娱乐", "视频", "音乐", "电影", "艺术", "篮球", "足球", "科技", "未来"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gridview_index,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (modes.length % COLUMN_COUNT != 0) {
//            int newLength = ((modes.length / COLUMN_COUNT) + 1) * COLUMN_COUNT;
//            modes = Arrays.copyOf(modes, newLength);
//        }
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setNumColumns(COLUMN_COUNT);
        gridView.setHorizontalSpacing(1);
        gridView.setVerticalSpacing(1);
        ModeAdapter modeAdapter = new ModeAdapter();
        gridView.setAdapter(modeAdapter);
    }

    public static Fragment newInstance() {
        return new GridViewIndexCodeFragment();
    }

    class ModeAdapter extends BaseAdapter{

        private TextView tv;

        @Override
        public int getCount() {
            return modes.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getActivity(),R.layout.item_gridview_index,null);
                tv = (TextView) convertView.findViewById(R.id.tv);
                tv.setHeight(300);
            }

            String mode = modes[position];
            tv.setText(mode);
            return convertView;
        }
    }
}
