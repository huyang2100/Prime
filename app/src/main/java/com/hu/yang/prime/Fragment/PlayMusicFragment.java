package com.hu.yang.prime.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/9/27.
 */

public class PlayMusicFragment extends Fragment {

    private static final String TAG = PlayMusicFragment.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private UpdateProgressTask updateProgressTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playmusic, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.best);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setMax(mediaPlayer.getDuration());
        updateProgressTask = new UpdateProgressTask();
        final Button btn = (Button) view.findViewById(R.id.btn);
        btn.setText("开始");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btn.setText("开始");
                } else {
                    if(seekBar.getProgress() == 0){
                        handler.post(updateProgressTask);
                    }
                    mediaPlayer.start();
                    btn.setText("暂停");
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btn.setText("开始");
                seekBar.setProgress(0);
                handler.removeCallbacks(updateProgressTask);
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i(TAG, "onBufferingUpdate: percent " + percent);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateProgressTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                handler.post(updateProgressTask);
            }
        });
    }

    public static Fragment newInstance() {
        return new PlayMusicFragment();
    }

    class UpdateProgressTask extends Thread{
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(this,50);
        }
    }
}
