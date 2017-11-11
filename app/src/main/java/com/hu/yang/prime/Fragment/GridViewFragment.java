package com.hu.yang.prime.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hu.yang.prime.R;

import static android.content.ContentValues.TAG;

/**
 * Created by yanghu on 2017/8/29.
 */

public class GridViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gridview,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        final GridView gv = (GridView) view.findViewById(R.id.gv);
        final MyAdapter adapter = new MyAdapter(getActivity());
        gv.setAdapter(adapter);
        gv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gv.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                int numColumns = gv.getNumColumns();
                Log.i(TAG, "onGlobalLayout: num "+ numColumns +" space: "+ space);
                int width = (gv.getWidth() -space*(numColumns - 1))/ numColumns;
                Log.i(TAG, "onGlobalLayout: width: "+width);
                adapter.setItemHeight(width);
            }
        });
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyAdapter extends BaseAdapter{
        private Context mContext;
        private Integer[] mThumbIds = {
                R.drawable.sample_2, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7,
                R.drawable.sample_0, R.drawable.sample_1,
                R.drawable.sample_2, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7,
                R.drawable.sample_0, R.drawable.sample_1,
                R.drawable.sample_2, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7
        };
        private AbsListView.LayoutParams mParams;

        public MyAdapter(Context context) {
            mContext = context;
            mParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
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
            final ImageView iv;
            if (convertView == null) {
                iv = new ImageView(mContext);

                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                iv.setPadding(8,8,8,8);
//                iv.setBackgroundColor(Color.WHITE);
            } else {
                iv = (ImageView) convertView;
            }

            iv.setLayoutParams(mParams);

            iv.setImageResource(mThumbIds[position]);
            return iv;
        }

        public void setItemHeight(int width) {

            mParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,width);
            notifyDataSetChanged();
        }
    }
}
