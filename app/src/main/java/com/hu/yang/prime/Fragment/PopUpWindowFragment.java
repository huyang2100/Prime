package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hu.yang.prime.R;

import java.util.ArrayList;

/**
 * Created by yanghu on 2017/8/19.
 */

public class PopUpWindowFragment extends Fragment {
    private ArrayList<FruitItem> fruits = new ArrayList<>();
    private FruitAdapter mFruitAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popupwindow,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FruitItem item1 = new FruitItem();
        item1.name = "Apple";
        ArrayList<String> versions1 = new ArrayList<>();
        versions1.add("版本1");
        versions1.add("版本2");
        versions1.add("版本3");
        item1.versionList = versions1;
        fruits.add(item1);

        FruitItem item2 = new FruitItem();
        item2.name = "Banana";
        ArrayList<String> versions2 = new ArrayList<>();
        versions2.add("版本1");
        versions2.add("版本2");
        item2.versionList = versions2;
        fruits.add(item2);

        FruitItem item3 = new FruitItem();
        item3.name = "Pear";
        ArrayList<String> versions3 = new ArrayList<>();
        versions3.add("版本1");
        item3.versionList = versions3;
        fruits.add(item3);

        FruitItem item4 = new FruitItem();
        item4.name = "Peach";
        ArrayList<String> versions4 = new ArrayList<>();
        versions4.add("版本1");
        versions4.add("版本2");
        item4.versionList = versions4;
        fruits.add(item4);

        FruitItem item5 = new FruitItem();
        item5.name = "Watermelon";
        ArrayList<String> versions5 = new ArrayList<>();
        versions5.add("版本1");
        versions5.add("版本2");
        versions5.add("版本3");
        versions5.add("版本4");
        versions5.add("版本5");
        item5.versionList = versions5;
        fruits.add(item5);

        ListView lv = (ListView) view.findViewById(R.id.lv);
        mFruitAdapter = new FruitAdapter();
        lv.setAdapter(mFruitAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupWindow pw = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                View v = View.inflate(getActivity(), R.layout.popupwindow_list, null);
                ListView lv = (ListView) v.findViewById(R.id.lv);
                ViewGroup.LayoutParams lm = lv.getLayoutParams();

                lm.width = 150;
                lv.setLayoutParams(lm);
                final ArrayList<String> itemList = fruits.get(position).versionList;
                lv.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1, itemList));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                        fruits.get(position).version = p;
                        mFruitAdapter.notifyDataSetChanged();
                    }
                });
                pw.setContentView(v);
                pw.setFocusable(true);
                pw.setBackgroundDrawable(getActivity().getResources().getDrawable(android.R.color.transparent));
                pw.setOutsideTouchable(true);
                pw.showAsDropDown(view,view.getWidth() + 10,-view.getHeight());
            }
        });
    }

    class FruitItem{
        public String name;
        public int version;
        public ArrayList<String> versionList;
    }

    class FruitAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return fruits.size();
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
            View v;
            if(convertView == null){
                v = View.inflate(getActivity(),R.layout.popupwindow,null);
            }else{
                v= convertView;
            }

            TextView tv = (TextView) v.findViewById(R.id.tv);
            FruitItem fruitItem = fruits.get(position);
            String item = fruitItem.name;
            tv.setText(item);

            TextView version = (TextView) v.findViewById(R.id.version);
            version.setText(fruitItem.versionList.get(fruitItem.version));
            return v;
        }
    }
}
