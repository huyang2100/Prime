package com.hu.yang.prime.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hu.yang.prime.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by yanghu on 2018/1/4.
 */

public class CropImageFragment extends Fragment implements View.OnClickListener {

    private static final int CODE_TAKE_PIC = 1;
    private static final int CODE_SEL_PIC = 2;
    private static final int CODE_CROP = 3;
    private ImageView iv;
    private Uri cropUri;
    private File cropFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crop_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.takephoto).setOnClickListener(this);
        view.findViewById(R.id.selimage).setOnClickListener(this);
        iv = (ImageView) view.findViewById(R.id.iv);

        cropFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.jpeg");
        cropUri = Uri.fromFile(cropFile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CODE_TAKE_PIC:
                    startImageZoom(cropUri);
                    break;
                case CODE_SEL_PIC:
                    if (data == null) {
                        return;
                    }

                    try {
                        InputStream is = getContext().getContentResolver().openInputStream(data.getData());
                        FileOutputStream fos = new FileOutputStream(cropFile);
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        fos.close();
                        startImageZoom(cropUri);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "选择图片出错", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_CROP:
                    Bitmap bitmap = BitmapFactory.decodeFile(cropFile.getAbsolutePath());
                    iv.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    /**
     * 通过Uri传递图像信息以供裁剪
     *
     * @param uri
     */
    private void startImageZoom(Uri uri) {
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //去除黑框
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        //设置最终图片的尺寸
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", false);
        //图片的输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //关闭面部识别
        intent.putExtra("noFaceDetection", false);
        //设置剪切的图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        startActivityForResult(intent, CODE_CROP);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.takephoto:
                //构建隐式Intent
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                //裁剪之后的数据是通过Intent返回
                intent.putExtra("return-data", false);
                // 指定存储路径，这样就可以保存原图了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
                //调用系统相机
                startActivityForResult(intent, CODE_TAKE_PIC);
                break;
            case R.id.selimage:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CODE_SEL_PIC);
                break;
        }
    }
}
