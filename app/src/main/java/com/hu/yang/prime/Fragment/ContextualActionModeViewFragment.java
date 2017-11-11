package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/9/6.
 */

public class ContextualActionModeViewFragment extends Fragment {

    private ActionMode.Callback actionModeCallback;
    private ActionMode actionMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floating_context_menu,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText("Contextual action mode view");

        actionModeCallback = new ActionMode.Callback(){

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.home,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_cloud:
                        Toast.makeText(getActivity(), "cloud", Toast.LENGTH_SHORT).show();
                        mode.finish();
                        return true;
                    case R.id.menu_search:
                        Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                        mode.finish();
                        return true;
                    case R.id.menu_setting:
                        Toast.makeText(getActivity(), "setting", Toast.LENGTH_SHORT).show();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
            }
        };

        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(actionMode != null){
                    return false;
                }

                actionMode = getActivity().startActionMode(actionModeCallback);
                v.setSelected(true);
                return true;
            }
        });
    }
}
