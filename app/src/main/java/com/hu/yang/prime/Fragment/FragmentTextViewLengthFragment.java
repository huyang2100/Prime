package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/9/25.
 */

public class FragmentTextViewLengthFragment extends Fragment {
    private String text = "我们知道,要使TextView单行显示,如果超出的话用“…”显示,只要设置android:ell";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_textview_length,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView tv = (TextView) view.findViewById(R.id.tv);
        final TextView tv_info = (TextView) view.findViewById(R.id.tv_info);
        tv.setText(text);
        tv.setText("Hello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello World");
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setSingleLine(true);
        tv.setSelected(true);
        tv.setFocusable(true);
        tv.setFocusableInTouchMode(true);
//        tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                tv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//                int widgetWidth = tv.getWidth();
//                float textWidth = tv.getPaint().measureText(tv.getText().toString());
//
//                if(textWidth > widgetWidth){
//
//                }else{
////                    tv_info.setText("no end");
//                }
//            }
//        });
    }
}
