package com.hu.yang.prime.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.hu.yang.prime.Fragment.ItemListFragment;
import com.hu.yang.prime.Fragment.NewItemFragment;
import com.hu.yang.prime.R;

import java.util.ArrayList;

/**
 * Created by yanghu on 2017/8/18.
 */

public class ToDoListActivity extends AppCompatActivity implements NewItemFragment.OnNewItemAddedLisenter{
    private ArrayList<String> itemList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        ItemListFragment itemListFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.frag_itemlist);
        mAdapter = new ArrayAdapter<>(this, R.layout.item_to_do_list, R.id.text, itemList);
        itemListFragment.setListAdapter(mAdapter);
    }

    @Override
    public void onNewItemAdded(String item) {
        itemList.add(0,item);
        mAdapter.notifyDataSetChanged();
    }
}
