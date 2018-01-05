package com.hu.yang.prime.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hu.yang.prime.R;
import com.hu.yang.prime.util.UIUtil;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.UIUtils;

/**
 * Created by yanghu on 2018/1/3.
 */

public class SinaWeiboFragment extends Fragment implements WbShareCallback {
    private WbShareHandler shareHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareHandler = new WbShareHandler(getActivity());
        shareHandler.registerApp();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sina_weibo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.textObject = getTextObj();
                shareHandler.shareMessage(weiboMessage, false);
            }
        });
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "Hello World！";
        textObject.title = "Yang";
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    @Override
    public void onWbShareSuccess() {
        Toast.makeText(getActivity(), "成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWbShareCancel() {
        Toast.makeText(getActivity(), "取消！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWbShareFail() {
        Toast.makeText(getActivity(), "失败！", Toast.LENGTH_SHORT).show();
    }
}
