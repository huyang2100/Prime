package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/9/6.
 */

public class ContextualActionModeListFragment extends ListFragment {
    private String[] conturies = {"China", "America", "Japana", "Canada", "English", "Germany", "Australia", "Korea", "Singapore", "Hong Kong", "Taiwan", "Malaysia", "Italy", "Brazil", "India"};

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, conturies));

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle(getListView().getCheckedItemCount() + "");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.home, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
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

            }
        });
    }
}
