package com.hu.yang.prime.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hu.yang.prime.Fragment.AppListFragment;
import com.hu.yang.prime.Fragment.AppbarLayout2Fragment;
import com.hu.yang.prime.Fragment.BasePageFragment;
import com.hu.yang.prime.Fragment.BasicTranstionFragment;
import com.hu.yang.prime.Fragment.BroadCastFragment;
import com.hu.yang.prime.Fragment.CanvasFragment;
import com.hu.yang.prime.Fragment.ContextualActionModeListFragment;
import com.hu.yang.prime.Fragment.ContextualActionModeViewFragment;
import com.hu.yang.prime.Fragment.DefaultLayoutAnimationsFragment;
import com.hu.yang.prime.Fragment.DownLoadFragment;
import com.hu.yang.prime.Fragment.ECGFragment;
import com.hu.yang.prime.Fragment.FloatingContextMenuFragment;
import com.hu.yang.prime.Fragment.FragmentPagerFragment;
import com.hu.yang.prime.Fragment.FragmentSwitchFragment;
import com.hu.yang.prime.Fragment.FragmentTextViewLengthFragment;
import com.hu.yang.prime.Fragment.FunctionsFragment;
import com.hu.yang.prime.Fragment.GridLayoutIndexCodeFragment;
import com.hu.yang.prime.Fragment.GridLayoutIndexFragment;
import com.hu.yang.prime.Fragment.GridLayoutLoginFragment;
import com.hu.yang.prime.Fragment.GridViewFragment;
import com.hu.yang.prime.Fragment.GridViewIndexCodeFragment;
import com.hu.yang.prime.Fragment.ItemDelFragment;
import com.hu.yang.prime.Fragment.ListViewBuildFragment;
import com.hu.yang.prime.Fragment.ListViewHeavyFragment;
import com.hu.yang.prime.Fragment.MyWebViewFragment;
import com.hu.yang.prime.Fragment.NotificationsFragment;
import com.hu.yang.prime.Fragment.OkHttpFragment;
import com.hu.yang.prime.Fragment.PDFListViewFragment;
import com.hu.yang.prime.Fragment.PDFViewFragment;
import com.hu.yang.prime.Fragment.PDFViewPagerFragment;
import com.hu.yang.prime.Fragment.PagerBarFragment;
import com.hu.yang.prime.Fragment.PathsFragment;
import com.hu.yang.prime.Fragment.PlayMusicFragment;
import com.hu.yang.prime.Fragment.PopUpDownFragment;
import com.hu.yang.prime.Fragment.PopUpWindowFragment;
import com.hu.yang.prime.Fragment.ProgressFragment;
import com.hu.yang.prime.Fragment.RecycleViewFragment;
import com.hu.yang.prime.Fragment.ShapeCrossFragment;
import com.hu.yang.prime.Fragment.StatusBarColor2Fragment;
import com.hu.yang.prime.Fragment.SwitchMenuActivityFragment;
import com.hu.yang.prime.Fragment.TestXmlFragment;
import com.hu.yang.prime.Fragment.ToolBarFragment;
import com.hu.yang.prime.Fragment.XListViewFragment;
import com.hu.yang.prime.Fragment.XScrollViewFragment;
import com.hu.yang.prime.R;
import com.hu.yang.prime.widget.XScrollView;

public class MainActivity extends AppCompatActivity implements FunctionsFragment.OnFunctionClickedLisenter{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new FunctionsFragment()).commit();
        }

    }

    @Override
    public void onFunctionClicked(String function) {
        if(FunctionsFragment.FUN_DIALOG.equals(function)){
            Intent intent = new Intent(this, DialogActivity.class);
            startActivity(intent);
        }else if(FunctionsFragment.FUN_TO_DO_LIST.equals(function)){
            Intent intent = new Intent(this, ToDoListActivity.class);
            startActivity(intent);
        }else if(FunctionsFragment.FUN_POPUPWINDOW.equals(function)){
            replaceFragment(new PopUpWindowFragment());
        }else if(FunctionsFragment.FUN_SHAPE_CROSS.equals(function)){
            replaceFragment(new ShapeCrossFragment());
        }else if(FunctionsFragment.FUN_CANVAS.equals(function)){
            replaceFragment(new CanvasFragment());
        }else if(FunctionsFragment.FUN_PATHS.equals(function)){
            replaceFragment(new PathsFragment());
        }else if(FunctionsFragment.FUN_PATH_ECG.equals(function)){
            replaceFragment(new ECGFragment());
        }else if(FunctionsFragment.FUN_GRIDVIEW.equals(function)){
            replaceFragment(new GridViewFragment());
        }else if(FunctionsFragment.FUN_BASIC_TRANSITION.equals(function)){
            replaceFragment(new BasicTranstionFragment());
        }else if(FunctionsFragment.FUN_SWITCH_MENU.equals(function)){
            replaceFragment(new SwitchMenuActivityFragment());
        }else if(FunctionsFragment.FUN_PROGRESS.equals(function)){
            replaceFragment(new ProgressFragment());
        }else if(FunctionsFragment.FUN_TOOLBAR.equals(function)){
            replaceFragment(new ToolBarFragment());
        }else if(FunctionsFragment.FUN_FLOATING_CONTEXT_MENU.equals(function)){
            replaceFragment(new FloatingContextMenuFragment());
        }else if(FunctionsFragment.FUN_CONTEXTUAL_ACTION_MODE_LIST.equals(function)){
            replaceFragment(new ContextualActionModeListFragment());
        }else if(FunctionsFragment.FUN_CONTEXTUAL_ACTION_MODE_VIEW.equals(function)){
            replaceFragment(new ContextualActionModeViewFragment());
        }else if(FunctionsFragment.FUN_LISTVIEW_HEAVY.equals(function)){
            replaceFragment(new ListViewHeavyFragment());
        }else if(FunctionsFragment.FUN_PDF_VIEW.equals(function)){
            replaceFragment(new PDFViewFragment());
        }else if(FunctionsFragment.FUN_PDF_VIEWPAGER.equals(function)){
            replaceFragment(new PDFViewPagerFragment());
        }else if(FunctionsFragment.FUN_PDF_LISTVIEW.equals(function)){
            replaceFragment(new PDFListViewFragment());
        }else if(FunctionsFragment.FUN_FRAGMENT_SWITCH.equals(function)){
            replaceFragment(new FragmentSwitchFragment());
        }else if(FunctionsFragment.FUN_FRAGMENT_TEXTVIEW_LENGTH.equals(function)){
            replaceFragment(new FragmentTextViewLengthFragment());
        }else if(FunctionsFragment.FUN_FRAGMENT_PLAYMUSIC.equals(function)){
            replaceFragment(new PlayMusicFragment());
        }else if(FunctionsFragment.FUN_LISTVIEW_APP.equals(function)){
            replaceFragment(new AppListFragment());
        }else if(FunctionsFragment.FUN_DEFAULT_LAYOUT_ANIMATIONS.equals(function)){
            replaceFragment(new DefaultLayoutAnimationsFragment());
        }else if(FunctionsFragment.FUN_GRIDLAYOUT_LOGIN.equals(function)){
            replaceFragment(new GridLayoutLoginFragment());
        }else if(FunctionsFragment.FUN_GRIDLAYOUT_INDEX_XML.equals(function)){
            replaceFragment(new GridLayoutIndexFragment());
        }else if(FunctionsFragment.FUN_GRIDLAYOUT_INDEX_CODE.equals(function)){
            replaceFragment(new GridLayoutIndexCodeFragment());
        }else if(FunctionsFragment.FUN_GRIDVIEW_INDEX.equals(function)){
            replaceFragment(new GridViewIndexCodeFragment());
        }else if(FunctionsFragment.FUN_FRAGMENT_PAGER.equals(function)){
            replaceFragment(new FragmentPagerFragment());
        }else if(FunctionsFragment.FUN_BASEPAGE.equals(function)){
            replaceFragment(new BasePageFragment());
        }else if(FunctionsFragment.FUN_LISTVIEW_BUILD.equals(function)){
            replaceFragment(new ListViewBuildFragment());
        }else if(FunctionsFragment.FUN_PAGER_BAR.equals(function)){
            replaceFragment(new PagerBarFragment());
        }else if(FunctionsFragment.FUN_PAGER_BAR.equals(function)){
            replaceFragment(new PagerBarFragment());
        }else if(FunctionsFragment.FUN_OKHTTP.equals(function)){
            replaceFragment(new OkHttpFragment());
        }else if(FunctionsFragment.FUN_WEBVIEW.equals(function)){
            replaceFragment(new MyWebViewFragment());
        }else if(FunctionsFragment.FUN_RECYCLEVIEW.equals(function)){
            replaceFragment(new RecycleViewFragment());
        }else if(FunctionsFragment.FUN_ITEM_DEL.equals(function)){
            replaceFragment(new ItemDelFragment());
        }else if(FunctionsFragment.FUN_APPBAR_LAYOUT.equals(function)){
            replaceFragment(new AppbarLayout2Fragment());
        }else if(FunctionsFragment.FUN_APPBAR_LAYOUT.equals(function)){
            replaceFragment(new AppbarLayout2Fragment());
        }else if(FunctionsFragment.FUN_NOTIFICATIONS.equals(function)){
            replaceFragment(new NotificationsFragment());
        }else if(FunctionsFragment.FUN_FLOW_TABS.equals(function)){
            SearchActivity.actionStart(this);
        }else if(FunctionsFragment.FUN_STATUS_BAR_COLOR.equals(function)){
            StatusBarColorActivity.actionStart(this);
        }else if(FunctionsFragment.FUN_TEST_XML.equals(function)){
            replaceFragment(new TestXmlFragment());
        }else if(FunctionsFragment.FUN_DOWNLOAD.equals(function)){
            replaceFragment(new DownLoadFragment());
        }else if(FunctionsFragment.FUN_POPUP_DOWN.equals(function)){
            replaceFragment(new PopUpDownFragment());
        }else if(FunctionsFragment.FUN_BROADCAST_RECEIVER.equals(function)){
            replaceFragment(new BroadCastFragment());
        }else if(FunctionsFragment.FUN_X_LISTVIEW.equals(function)){
            replaceFragment(new XListViewFragment());
        }else if(FunctionsFragment.FUN_X_SCROLLVIEW.equals(function)){
            replaceFragment(new XScrollViewFragment());
        }else if(FunctionsFragment.FUN_STATUS_BAR_COLOR2.equals(function)){
            replaceFragment(new StatusBarColor2Fragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,fragment).addToBackStack(null).commit();
    }
}
