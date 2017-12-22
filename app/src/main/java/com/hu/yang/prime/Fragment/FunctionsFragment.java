package com.hu.yang.prime.Fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by yanghu on 2017/8/16.
 */

public class FunctionsFragment extends ListFragment {
    public static final String FUN_STATUS_BAR_COLOR2 = "x_status_bar_color2";
    public static final String FUN_X_SCROLLVIEW = "x_scrollview";
    public static final String FUN_X_LISTVIEW = "x_listview";
    public static final String FUN_BROADCAST_RECEIVER = "broadcastreceiver";
    public static final String FUN_POPUP_DOWN = "popup_down";
    public static final String FUN_DOWNLOAD = "test_download";
    public static final String FUN_TEST_XML = "test_xml";
    public static final String FUN_STATUS_BAR_COLOR = "status_bar_color";
    public static final String FUN_FLOW_TABS = "flow_tabs";
    public static final String FUN_NOTIFICATIONS = "notifications";
    public static final String FUN_APPBAR_LAYOUT = "appbar_layout";
    public static final String FUN_ITEM_DEL = "item_del";
    public static final String FUN_RECYCLEVIEW = "recycleview";
    public static final String FUN_WEBVIEW = "webview";
    public static final String FUN_OKHTTP = "okhttp";
    public static final String FUN_PAGER_BAR = "pager_bar";
    public static final String FUN_LISTVIEW_BUILD = "listview_build";
    public static final String FUN_BASEPAGE = "basepage";
    public static final String FUN_FRAGMENT_PAGER = "gragment_pager";
    public static final String FUN_GRIDVIEW_INDEX = "gridview_index";
    public static final String FUN_GRIDLAYOUT_INDEX_CODE = "gridlayout_index_code";
    public static final String FUN_GRIDLAYOUT_INDEX_XML = "gridlayout_index_xml";
    public static final String FUN_GRIDLAYOUT_LOGIN = "gridlayout_login";
    public static final String FUN_DEFAULT_LAYOUT_ANIMATIONS = "default_layout_animations";
    public static final String FUN_LISTVIEW_APP = "listview_app";
    public static final String FUN_FRAGMENT_PLAYMUSIC = "fragment_playmusic";
    public static final String FUN_FRAGMENT_TEXTVIEW_LENGTH = "fragment_switch_textview_length";
    public static final String FUN_FRAGMENT_SWITCH = "fragment_switch";
    public static final String FUN_PDF_LISTVIEW = "pdf_listview";
    public static final String FUN_PDF_VIEWPAGER = "pdf_viewpager";
    public static final String FUN_PDF_VIEW = "pdf_view";
    public static final String FUN_LISTVIEW_HEAVY = "listview_heavy";
    public static final String FUN_FLOATING_CONTEXT_MENU = "floating_context_menu";
    public static final String FUN_CONTEXTUAL_ACTION_MODE_LIST = "contextual_action_mode_list";
    public static final String FUN_CONTEXTUAL_ACTION_MODE_VIEW = "contextual_action_mode_view";
    public static final String FUN_DIALOG = "dialog";
    public static final String FUN_TO_DO_LIST = "to_do_list";
    public static final String FUN_POPUPWINDOW = "popupwindow";
    public static final String FUN_SHAPE_CROSS = "shape_cross";
    public static final String FUN_CANVAS = "canvas";
    public static final String FUN_PATHS = "paths";
    public static final String FUN_PATH_ECG = "path_ecg";
    public static final String FUN_GRIDVIEW = "gridview";
    public static final String FUN_BASIC_TRANSITION = "basic_transtion";
    public static final String FUN_SWITCH_MENU = "switch_menu";
    public static final String FUN_PROGRESS = "progress";
    public static final String FUN_TOOLBAR = "toolbar";
    private String[] funcs = {
            FUN_STATUS_BAR_COLOR2,
            FUN_X_SCROLLVIEW,
            FUN_X_LISTVIEW,
            FUN_PDF_VIEWPAGER,
            FUN_BROADCAST_RECEIVER,
            FUN_POPUP_DOWN,
            FUN_DOWNLOAD,
            FUN_TEST_XML,
            FUN_STATUS_BAR_COLOR,
            FUN_FLOW_TABS,
            FUN_NOTIFICATIONS,
            FUN_APPBAR_LAYOUT,
            FUN_ITEM_DEL,
            FUN_RECYCLEVIEW,
            FUN_WEBVIEW,
            FUN_OKHTTP,
            FUN_PAGER_BAR,
            FUN_LISTVIEW_BUILD,
            FUN_BASEPAGE,
            FUN_FRAGMENT_PAGER,
            FUN_GRIDVIEW_INDEX,
            FUN_GRIDLAYOUT_INDEX_CODE,
            FUN_GRIDLAYOUT_INDEX_XML,
            FUN_GRIDLAYOUT_LOGIN,
            FUN_DEFAULT_LAYOUT_ANIMATIONS,
            FUN_LISTVIEW_APP,
            FUN_FRAGMENT_PLAYMUSIC,
            FUN_FRAGMENT_TEXTVIEW_LENGTH,
            FUN_PDF_LISTVIEW,
            FUN_PDF_VIEW,
            FUN_LISTVIEW_HEAVY,
            FUN_FLOATING_CONTEXT_MENU,
            FUN_CONTEXTUAL_ACTION_MODE_LIST,
            FUN_CONTEXTUAL_ACTION_MODE_VIEW,
            FUN_DIALOG,
            FUN_TO_DO_LIST,
            FUN_POPUPWINDOW,
            FUN_SHAPE_CROSS,
            FUN_CANVAS,
            FUN_PATHS,
            FUN_PATH_ECG,
            FUN_GRIDVIEW,
            FUN_BASIC_TRANSITION,
            FUN_SWITCH_MENU,
            FUN_PROGRESS,
            FUN_TOOLBAR,
    };

    private OnFunctionClickedLisenter onFunctionClickedLisenter;

    public interface OnFunctionClickedLisenter {
        void onFunctionClicked(String function);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onFunctionClickedLisenter = (OnFunctionClickedLisenter) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity + "must implements onFunctionClickedLisenter");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, funcs));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        onFunctionClickedLisenter.onFunctionClicked(funcs[position]);
    }
}
