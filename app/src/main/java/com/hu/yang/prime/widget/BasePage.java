package com.hu.yang.prime.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/10/23.
 */

public abstract class BasePage extends FrameLayout {
    public static final int STATE_LOADING = 0;
    public static final int STATE_EMPTY = 1;
    public static final int STATE_ERR = 2;
    public static final int STATE_SUCCESS = 3;
    private int curState = STATE_LOADING;
    private View loadingView;
    private View emptyView;
    private View errView;
    private View succView;

    public BasePage(@NonNull Context context) {
        super(context);
        init();
    }

    public BasePage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loadingView = View.inflate(getContext(), R.layout.page_loading, null);
        addView(loadingView);

        emptyView = View.inflate(getContext(), R.layout.page_empty, null);
        addView(emptyView);

        errView = View.inflate(getContext(), R.layout.page_err, null);
        addView(errView);

        succView = getSuccessView();
        addView(succView);
    }

    public abstract View getSuccessView();

    public void show(int state){
        switch (state){
            case STATE_LOADING:
                loadingView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                errView.setVisibility(View.GONE);
                if(succView!=null){
                    succView.setVisibility(View.GONE);
                }
                break;
            case STATE_EMPTY:
                loadingView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                errView.setVisibility(View.GONE);
                if(succView!=null){
                    succView.setVisibility(View.GONE);
                }
                break;
            case STATE_ERR:
                loadingView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                errView.setVisibility(View.VISIBLE);
                if(succView!=null){
                    succView.setVisibility(View.GONE);
                }
                break;
            case STATE_SUCCESS:
                loadingView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                errView.setVisibility(View.GONE);
                if(succView!=null){
                    succView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
