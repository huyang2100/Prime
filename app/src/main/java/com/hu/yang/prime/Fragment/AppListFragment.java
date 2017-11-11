package com.hu.yang.prime.Fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by yanghu on 2017/10/10.
 */

public class AppListFragment extends ListFragment {

    private static final String TAG = AppListFragment.class.getSimpleName();
    private PackageManager pm;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pm = getActivity().getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        setListAdapter(new AppsAdapter(installedPackages));
    }

    public static Fragment newInstance() {
        return new AppListFragment();
    }

    class AppsAdapter extends BaseAdapter {
        private final List<PackageInfo> packageInfos;

        public AppsAdapter(List<PackageInfo> packageInfos) {
            this.packageInfos = packageInfos;
        }

        @Override
        public int getCount() {
            return packageInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PackageInfo packageInfo = packageInfos.get(position);

            if (convertView == null) {
                convertView = new TextView(getActivity());
                ((TextView) convertView).setTextSize(12);
                ((TextView) convertView).setPadding(10, 10, 10, 10);
                ((TextView) convertView).setTextColor(Color.BLACK);

            }

            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                ((TextView) convertView).setText(packageInfo.packageName);
            } else {
                ((TextView) convertView).setText(packageInfo.packageName + " (" + pm.getApplicationLabel(packageInfo.applicationInfo) + " " + packageInfo.versionName + "/" + packageInfo.versionCode + ")");
                Log.i(TAG, "packagename: " + packageInfo.packageName + " (" + pm.getApplicationLabel(packageInfo.applicationInfo) + " " + packageInfo.versionName + "/" + packageInfo.versionCode + ")");

                Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
                File primeDir = new File(Environment.getExternalStorageDirectory(), "prime/savedicon");
                if(!primeDir.exists()){
                    primeDir.mkdirs();
                }

                File file = new File(primeDir, packageInfo.packageName + ".png");
                if (!file.exists()) {
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    if(bitmap != null){
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return convertView;
        }
    }
}
