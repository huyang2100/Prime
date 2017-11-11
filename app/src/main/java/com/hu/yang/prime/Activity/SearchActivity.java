package com.hu.yang.prime.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hu.yang.prime.R;
import com.hu.yang.prime.db.SearchDB;
import com.hu.yang.prime.db.SearchDBHelper;
import com.hu.yang.prime.util.UIUtil;
import com.hu.yang.prime.widget.FlowLayout;

import java.util.ArrayList;

/**
 * Created by yanghu on 2017/11/4.
 */

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();
    String[] datas = {"园林课程", "园林景观设计", "北京园林学习", "园林景观设计", "绿化", "园艺", "绿化", "园林课程", "园林景观设计", "北京园林学习"};
    private ArrayList<String> recordList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private EditText etSearch;
    private SearchDB searchDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    private void initData() {
        searchDB = new SearchDB(this);
        recordList = searchDB.get();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new RecordAdapter();
        recyclerView.setAdapter(recordAdapter);
    }

    private void hideIM() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

    private void initView() {
        FrameLayout tabRoot = (FrameLayout) findViewById(R.id.tab_root);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        etSearch = (EditText) findViewById(R.id.et_search);
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                hideIM();
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String info = v.getText().toString();
                    if (!TextUtils.isEmpty(info)) {
                        searchInfo(info);
                    }
                }
                return true;
            }
        });
        FlowLayout floweLayout = new FlowLayout(this);
        floweLayout.setHorizontalSpacing(UIUtil.dip2px(this, 13));
        floweLayout.setVerticalSpacing(UIUtil.dip2px(this, 16));
        for (int i = 0; i < datas.length; i++) {
            TextView tv = new TextView(this);
            tv.setText(datas[i]);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchInfo(((TextView) v).getText().toString());
                }
            });
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv.setTextColor(getResources().getColor(R.color.tab_txt_color));
            tv.setGravity(Gravity.CENTER);
            int lr = UIUtil.dip2px(SearchActivity.this, 5);
            int tb = UIUtil.dip2px(SearchActivity.this, 5);
            tv.setPadding(lr, tb, lr, tb);
            floweLayout.addView(tv);
            tv.setBackgroundResource(R.drawable.tab_bg_sel);
        }
        tabRoot.addView(floweLayout);
    }

    private void searchInfo(String info) {
        hideIM();
        etSearch.setText(info);
        etSearch.setSelection(info.length());
        searchDB.add(info);
        recordList = searchDB.get();
        recordAdapter.notifyDataSetChanged();
    }

    class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
        public RecordAdapter() {
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tvTecord;
            private ImageView ivDel;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTecord = (TextView) itemView.findViewById(R.id.tv_record);
                ivDel = (ImageView) itemView.findViewById(R.id.iv_del);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_record, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            String info = recordList.get(position);
            holder.tvTecord.setText(info);
            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchDB.del(recordList.get(holder.getAdapterPosition()));
                    recordList.remove(holder.getAdapterPosition());
                    RecordAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchInfo(recordList.get(holder.getAdapterPosition()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return recordList.size();
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
}
