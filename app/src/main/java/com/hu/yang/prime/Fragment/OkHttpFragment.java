package com.hu.yang.prime.Fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hu.yang.prime.application.PrimeApplication;
import com.hu.yang.prime.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by yanghu on 2017/10/24.
 */

public class OkHttpFragment extends Fragment {

    private TextView tv_info;
    private OkHttpClient okHttpClient;
    private String url = "https://publicobject.com/helloworld.txt";
    private Request request;
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_okhttp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_info = (TextView) view.findViewById(R.id.tv_info);
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(url)
                .build();
        view.findViewById(R.id.btn_get_sync).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            if (!response.isSuccessful()) {
                                return;
                            }

                            updateInfo("get sync: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        view.findViewById(R.id.btn_get_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        updateInfo("get async: " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        updateInfo("get async: " + response.body().string());
                    }
                });
            }
        });

        view.findViewById(R.id.btn_access_headers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = new Request.Builder()
                        .url(url)
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Accept", "application/vnd.github.v3+json")
                        .build();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            if (!response.isSuccessful()) {
                                return;
                            }
                            updateInfo("Server: " + response.header("Server") + "\r\n" + "Date: " + response.header("Date") + "\r\n" + "Vary: " + response.header("Vary"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        view.findViewById(R.id.btn_post_string).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postBody = ""
                        + "Releases\n"
                        + "--------\n"
                        + "\n"
                        + " * _1.0_ May 6, 2018\n"
                        + " * _1.1_ June 15, 2018\n"
                        + " * _1.2_ August 11, 2018\n";

                url = "https://api.github.com/markdown/raw";
                request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                        .build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            if (!response.isSuccessful()) {
                                return;
                            }
                            updateInfo(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        view.findViewById(R.id.btn_post_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = new RequestBody() {

                    @Nullable
                    @Override
                    public MediaType contentType() {
                        return MEDIA_TYPE_MARKDOWN;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {
                        sink.writeUtf8("Numbers\n");
                        sink.writeUtf8("-------\n");
                        for (int i = 2; i <= 997; i++) {
                            sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                        }
                    }

                    private String factor(int n) {
                        for (int i = 2; i < n; i++) {
                            int x = n / i;
                            if (x * i == n) return factor(x) + " × " + i;
                        }
                        return Integer.toString(n);
                    }
                };

                url = "https://api.github.com/markdown/raw";
                request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                sendRequestSync(request);
            }
        });

        view.findViewById(R.id.btn_post_afile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssetManager assetManager = getActivity().getAssets();
                File dir = new File(Environment.getExternalStorageDirectory(), "Prime");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir, "hello.md");
                try {
                    InputStream inputStream = assetManager.open("hello.md");
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                url = "https://api.github.com/markdown/raw";
                request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                        .build();
                sendRequestSync(request);
            }
        });
    }

    private void sendRequestSync(final Request request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        return;
                    }

                    updateInfo(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                    updateInfo(e.toString());
                }
            }
        }).start();
    }

    private void updateInfo(final String s) {
        PrimeApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                tv_info.setText(s);
            }
        });
    }
}
