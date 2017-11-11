package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hu.yang.prime.R;

import java.util.ArrayList;

/**
 * Created by yanghu on 2017/10/30.
 */

public class RecycleViewFragment extends Fragment {
    private ArrayList<String> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recylceview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 0; i < 20; i++) {
            datas.add("item: " + i);
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyAdapter(datas));
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private final ArrayList<String> datas;

        public MyAdapter(ArrayList<String> datas) {
            this.datas = datas;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_recycleview, parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final String info = datas.get(position);
            holder.textView.setText(info);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(holder.getLayoutPosition());
                    MyAdapter.this.notifyItemRemoved(holder.getLayoutPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
