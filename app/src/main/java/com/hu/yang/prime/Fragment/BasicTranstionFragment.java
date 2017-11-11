package com.hu.yang.prime.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.ViewAnimator;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/8/30.
 */

public class BasicTranstionFragment extends Fragment {

    private ViewAnimator mViewAnimator;
    private Scene mScene_1;
    private Scene mScene_2;
    private Scene mScene_3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_basic_transtion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewAnimator = (ViewAnimator) view.findViewById(R.id.va);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return;
        }
        final ViewGroup rootScene = (ViewGroup) view.findViewById(R.id.rootScene);
        mScene_1 = Scene.getSceneForLayout(rootScene, R.layout.scene_1, getActivity());
        mScene_2 = Scene.getSceneForLayout(rootScene, R.layout.scene_2, getActivity());
        mScene_3 = Scene.getSceneForLayout(rootScene, R.layout.scene_3, getActivity());

        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                            TransitionManager.go(mScene_1);
                        }
                        break;
                    case R.id.rb_2:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                            TransitionManager.go(mScene_2);
                        }
                        break;
                    case R.id.rb_3:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                         TransitionManager.go(mScene_3);
                        }
                        break;
                    case R.id.rb_4:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                            TransitionManager.beginDelayedTransition(rootScene);
                            View square = rootScene.findViewById(R.id.square);
                            ViewGroup.LayoutParams lp = square.getLayoutParams();
                            int size = getResources().getDimensionPixelSize(R.dimen.width);
                            lp.width = size;
                            lp.height = size;
                            square.setLayoutParams(lp);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.add(R.string.menu_title_two);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        if (title.equals(getString(R.string.menu_title_two))) {
            item.setTitle(R.string.menu_title_one);
            mViewAnimator.setDisplayedChild(1);
        } else {
            item.setTitle(R.string.menu_title_two);
            mViewAnimator.setDisplayedChild(0);

        }
        return super.onOptionsItemSelected(item);
    }
}
