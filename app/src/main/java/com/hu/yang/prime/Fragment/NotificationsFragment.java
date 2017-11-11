package com.hu.yang.prime.Fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hu.yang.prime.Activity.PagerActivity;
import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/11/2.
 */

public class NotificationsFragment extends Fragment implements View.OnClickListener {
    private static final int NOTIFY_ID = 1;
    private NotificationManager nm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_notify).setOnClickListener(this);
        nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notify:
                sendNotify(0);
                break;
        }
    }

    private void sendNotify(int id) {
        Intent intent = new Intent(getActivity(), PagerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
        String title = "Hello notify!: " + id;
        String content = "I am a notification from Yang.";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());

        Notification notification = builder
                .setSmallIcon(R.drawable.ic_card)
                .setAutoCancel(true)
                .setColor(getContext().getResources().getColor(android.R.color.holo_green_dark))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(content)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(pendingIntent,false)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build();
        nm.notify(id, notification);
    }
}
