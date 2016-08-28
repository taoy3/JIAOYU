package com.cnst.wisdom.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.utills.ImageTools;
import com.cnst.wisdom.utills.SPUtills;
import com.cnst.wisdom.utills.UploadUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <设置头像页>
 * 显示较大的正方形的头像图片
 * 访问手机相册
 * 调用相机
 * 选择图片/拍照之后，进行图片截取
 * 保存正方形的图片到本地/服务器，在其他界面以圆形显示
 */
public class AvatarPickingActivity extends BaseNetActivity implements View.OnClickListener, UploadUtil.OnUploadProcessListener
{
    private TextView textView_head_text;
    private ImageView imageView_userhead;
    private ProgressBar progressBar;
    private TextView uploadImageResult;
    private ProgressDialog progressDialog;
    private TextView textView_takePic;

    //本地头像图片储存地址
    private String userheadLocation;
    //保存图片的名字：userId[时间毫秒值].png
    private String filename;
    private Uri imageUri;
    private String uploadUrl;
    private String userId;

    //头像图片的宽度 in px
    private static final int PIC_WIDTH = 256;
    //头像图片的高度 in px
    private static final int PIC_HEIGHT = 256;

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;
    private static final int SCALE = 5;//照片缩小比例

    private BaseApplication app = (BaseApplication)getApplication();
    private Login mlogin = app.getLogin();

    //去上传文件
    protected static final int TO_UPLOAD_FILE = 1;
    //上传文件响应
    protected static final int UPLOAD_FILE_DONE = 2;  //
    //选择文件
    public static final int TO_SELECT_PHOTO = 3;
    //上传初始化
    private static final int UPLOAD_INIT_PROCESS = 4;
    //上传中
    private static final int UPLOAD_IN_PROCESS = 5;

    //
    private boolean toRetry = false;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_avatar_picking);
        setViews();
        initUserhead();
        uploadUrl = SPUtills.get(getApplicationContext(), Constants.GET_SERVER, Constants.SERVER).toString()+Constants.UPLOAD_HEAD_IMG;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    private void setViews()
    {
        //隐藏头部右上角搜索按钮
        findViewById(R.id.head_back_action).setOnClickListener(this);
        findViewById(R.id.textView_takePic).setOnClickListener(this);

        textView_takePic = (TextView)findViewById(R.id.textView_takePic);
        textView_head_text = (TextView)findViewById(R.id.head_text);
        textView_head_text.setText("头像");

        imageView_userhead = (ImageView)findViewById(R.id.imageView_userhead_picking);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams params = imageView_userhead.getLayoutParams();
        params.height = dm.widthPixels;
        imageView_userhead.setLayoutParams(params);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        uploadImageResult = (TextView)findViewById(R.id.uploadImageResult);
        progressDialog = new ProgressDialog(this);

    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case TO_UPLOAD_FILE:
                    toUploadFile();
                    break;

                case UPLOAD_INIT_PROCESS:
                    progressBar.setMax(msg.arg1);
                    break;
                case UPLOAD_IN_PROCESS:
                    progressBar.setProgress(msg.arg1);
                    break;
                case UPLOAD_FILE_DONE:
                    String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+UploadUtil.getRequestTime()+"秒";
                    uploadImageResult.setText(result);
                    if(msg.arg1 != 1)
                    {
                        toRetry = true;
                    }else
                    {
                        toRetry=false;
                        SPUtills.put(getApplicationContext(), userId, filename);
                    }
                    modifyButton(toRetry);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    /**
     * <初始化头像>
     * 从个人中心页进入修改头像页之前会尝试下载新头像并保存在本地，此处显示本地的图片
     * 如果没有设置过头像，或者因网络原因没有下载到新头像，那么此处显示默认头像：APPLOGO
     * filename不包含后缀名.png
     */

    private void initUserhead()
    {

        userheadLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
        userId = mlogin.getData().getUserId();
        filename = SPUtills.get(getApplicationContext(), userId, "").toString();
        if(filename.equals(""))
        {
            imageView_userhead.setImageResource(R.drawable.applogo);
        }else
        {
            imageUri = Uri.parse(userheadLocation+"/"+filename+".png");
            try
            {
                imageView_userhead.setImageURI(imageUri);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * <点击事件>
     * 选择图片：弹出对话框，选择拍照或访问相册
     * 重试上传按钮：平时隐藏，在上传失败时显示。点击之后隐藏，并执行上传
     * 返回按钮：回到个人中心，以刷新图片
     */
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.textView_takePic:
                if(toRetry)
                {
                    handler.sendEmptyMessage(TO_UPLOAD_FILE);
                }else
                {
                    showPicturePicker();
                }
                break;
            case R.id.head_back_action:
                backToAccount();
                break;
            default:
                break;
        }
    }

    /**
     * <返回个人中心>
     * 直接finish()无法刷新头像
     */
    private void backToAccount()
    {
        Intent intent = new Intent(AvatarPickingActivity.this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch(requestCode)
            {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth()/SCALE, bitmap.getHeight()/SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
                    imageView_userhead.setImageBitmap(newBitmap);
                    //ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(),
                    //"/userhead.jpg");

                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try
                    {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if(photo != null)
                        {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth()/SCALE, photo.getHeight()/SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            imageView_userhead.setImageBitmap(smallBitmap);
                        }
                    }catch(FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                    break;

                case CROP:
                    Uri uri = null;
                    if(data != null)
                    {
                        uri = data.getData();
                        System.out.println("Data");
                    }else
                    {
                        System.out.println("File");
                        String fileName = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
                        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                    }
                    cropImage(uri, PIC_WIDTH, PIC_HEIGHT, CROP_PICTURE);
                    break;

                case CROP_PICTURE:
                    Bitmap photo = null;
                    Uri photoUri = data.getData();
                    if(photoUri != null)
                    {
                        photo = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                    if(photo == null)
                    {
                        Bundle extra = data.getExtras();
                        if(extra != null)
                        {
                            photo = (Bitmap)extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                    }
                    imageView_userhead.setImageBitmap(photo);
                    Date date = new Date();
                    long currentTime = date.getTime();
                    //头像图片文件名为userId与时间值拼接
                    filename = new String(userId+currentTime);
                    ImageTools.savePhotoToSDCard(photo, userheadLocation, filename);
                    handler.sendEmptyMessage(TO_UPLOAD_FILE);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 调用系统自带的图片剪裁程序
     */
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);
    }

    /**
     * <弹出对话框>
     * 询问访问手机相册或拍照，并调用相应的系统程序
     */
    public void showPicturePicker()
    {
        final boolean crop = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(AvatarPickingActivity.this);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "从手机相册选择"}, new DialogInterface.OnClickListener()
        {
            //类型码
            int REQUEST_CODE;

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch(which)
                {
                    case TAKE_PICTURE:
                        Uri imageUri = null;
                        String fileName = null;
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(crop)
                        {
                            REQUEST_CODE = CROP;
                            //删除上一次截图的临时文件
                            SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
                            ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(),
                                    sharedPreferences.getString("tempName", ""));

                            //保存本次截图临时文件名字
                            fileName = String.valueOf(System.currentTimeMillis())+".jpg";
                            Editor editor = sharedPreferences.edit();
                            editor.putString("tempName", fileName);
                            editor.commit();
                        }else
                        {
                            REQUEST_CODE = TAKE_PICTURE;
                            fileName = "image.jpg";
                        }
                        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, REQUEST_CODE);
                        break;
                    case CHOOSE_PICTURE:
                        //                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        //                        if(crop)
                        //                        {
                        //                            REQUEST_CODE = CROP;
                        //                        }else
                        //                        {
                        //                            REQUEST_CODE = CHOOSE_PICTURE;
                        //                        }
                        //                        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        //                        startActivityForResult(openAlbumIntent, REQUEST_CODE);
                        Intent openAlbumIntent = new Intent();
                        openAlbumIntent.setType("image/*");
                        openAlbumIntent.setAction(Intent.ACTION_PICK);
                        openAlbumIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if(crop)
                        {
                            REQUEST_CODE = CROP;
                        }else
                        {
                            REQUEST_CODE = CHOOSE_PICTURE;
                        }
                        startActivityForResult(openAlbumIntent, REQUEST_CODE);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * <上传完成>
     * 在上传完成时更新界面
     */
    @Override
    public void onUploadDone(int responseCode, String message)
    {
        progressDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    /**
     * <上传进度>
     * 在上传期间更新界面
     */
    @Override
    public void onUploadProcess(int uploadSize)
    {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg);
    }

    /**
     * 在上传就绪之前更新界面
     */
    @Override
    public void initUpload(int fileSize)
    {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg);
    }

    /**
     * <上传图片>
     * 参数：userId:从Login对象提取，图片文件:裁减完成并保存在本地的图片
     */

    private void toUploadFile()
    {
        uploadImageResult.setText("正在上传中...");
        progressDialog.setMessage("正在上传头像...");
        progressDialog.show();

        //设置监听器监听上传状态
        UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);

        Map<String,String> params = new HashMap<String,String>();
        params.put("userId", mlogin.getData().getUserId());
        uploadUtil.uploadFile(userheadLocation+"/"+filename+".png", "file", uploadUrl, params);
    }

    /**
     * <返回键>
     * 按下返回键：显示桌面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            backToAccount();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * <修改按钮文字和功能>
     *
     * @param action
     *         为true时，将按钮变为：上传失败，请点击重试，功能变为上传；为false时，按钮变为：选择图片，功能变为访问相机相册
     */
    public void modifyButton(boolean action)
    {
        if(action)
        {
            textView_takePic.setText("上传失败，请点击重试");
        }else
        {
            textView_takePic.setText("更换头像");

        }

    }

}
