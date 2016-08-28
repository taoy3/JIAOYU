package com.taoy3.freight.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.UserBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends BaseActivity {
    private CircleImageView headView;
    private static final int PHOTO_REQUEST_TAKE_PHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private File photoFile;
    private File tempFile = new File(Config.photoPath, "temp.png");
    private UserBean bean = CacheDataConstant.userBean;

    private TextView userNameTv;
    private TextView passwordTv;
    private TextView userTypeTv;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_user_info);
    }
    @Override
    protected void initView(TextView leftView,TextView titleView,TextView rightView) {
        headView = (CircleImageView) findViewById(R.id.head_view);
        headView.setOnClickListener(this);
        userNameTv = (TextView) findViewById(R.id.user_name);
        userTypeTv = (TextView) findViewById(R.id.user_type);
        passwordTv = (TextView) findViewById(R.id.change_password);
        passwordTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        userNameTv.setText(CacheDataConstant.userBean.getUsername());
        userTypeTv.setText(CacheDataConstant.userBean.getType()+"");
        photoFile = new File(Config.photoPath, bean.getUsername() + ".png");
        if (photoFile.exists()) {
            headView.setImageURI(Uri.fromFile(photoFile));
        }else {
            try {
                Config.photoPath.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.head_view:
                showDialog();
                break;
            case R.id.change_password:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKE_PHOTO://当选择拍照时调用
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;
            case PHOTO_REQUEST_GALLERY://当选择从本地获取图片时
                //做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null)
                startPhotoZoom(data.getData(), 150);
                break;
            case PHOTO_REQUEST_CUT://返回的结果
                if (data != null)
                    setPicToView(data);
                break;
            default:
                break;
        }
    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //将进行剪裁后的图片显示到UI界面上
    @SuppressWarnings("deprecation")
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            headView.setImageBitmap(photo);
            saveBitmapFile(photo);
        }
    }
    //Bitmap对象保存味图片文件
    private void saveBitmapFile(Bitmap bitmap){
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(photoFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //提示对话框方法
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("头像设置")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKE_PHOTO);
                    }
                })
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }
}
