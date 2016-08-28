package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.bean.Userinfo;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.view.CircleImageView;
import com.cnst.wisdom.utills.ImageTools;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <个人中心>
 * 下载管理：转到管理下载任务页
 * 设置
 * ├修改头像：访问本地相册或调用相机
 * ├修改密码：需输入旧密码和新密码
 * └退出登录：清除自动登录信息，转到登录页
 */
public class AccountActivity extends BaseNetActivity implements View.OnClickListener
{

    private LinearLayout linearLayout_userinfo;
    private LinearLayout linearLayout_download;
    private LinearLayout linearLayout_setting;
    private CircleImageView imageView_userhead;
    private TextView textView_head_text;
    private TextView textView_username;
    private TextView textView_classpost;
    private ImageView imageView_gender;

    private String currentUrl;
    private String localPicName;
    private String picLocation;
    //转到个人信息页
    private static final int GO_USERINFO = 3;
    //转到头像设置页
    private static final int GO_AVATARPICKING = 404;
    //转到下载管理页
    private static final int GO_DOWNLOAD = 505;
    //转到设置页
    private static final int GO_SETTING = 606;

    Login mlogin = BaseApplication.getLogin();
    private VolleyManager mVolleyManager = VolleyManager.getInstance();
    private Userinfo mUserinfo;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_account);
    }

    @Override
    protected void initData() {
        setViews();
    }

    @Override
    protected void initView() {
        getUserinfo();
    }


    /**
     * <检查新头像>
     * 1)检查本地存储的头像图片地址与服务器返回来的地址是否一致，如果一致表示本地图片是最新版本，无需再次下载
     * 2)如果地址不一致表示有新图片，异步下载完成后刷新UI
     */
    private void checkNewPic()
    {
        //获取本地储存的图片文件名:userId时间值.png
        localPicName = SPUtills.get(getApplicationContext(), mlogin.getData().getUserId(), "").toString()+".png";
        picLocation = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+localPicName;

        String headImgUrl = mUserinfo.getData().getTeachInfo().getHeadImgUrl();

        //判断本地图片文件是否存在
        boolean picExists;
        try
        {
            File mfile = new File(picLocation);
            picExists = mfile.exists();
        }catch(Exception e)
        {
            picExists = false;
        }

        //判断是否设置过头像
        if(!( headImgUrl == null || headImgUrl.equals("") ))
        {
            //服务器地址 与 头像图片的路径 拼接 包含后缀名
            currentUrl = SPUtills.get(getApplicationContext(), Constants.GET_SERVER, Constants.SERVER).toString()+headImgUrl;

            //对比本地图片文件名与本地图片是否存在的检查结果，确认本地图片是否最新
            if(localPicName.equals(currentUrl.substring(currentUrl.lastIndexOf("/")+1)) && picExists)
            {
                setHeadPic();
            }else
            {
                //如果头像不是最新，或者本地图片不存在，就异步下载头像并更新UI
                localPicName = new String(currentUrl.substring(currentUrl.lastIndexOf("/")+1));
                localPicName = localPicName.substring(0, localPicName.lastIndexOf("."));
                new ImageTask().execute(currentUrl);
            }
        }else
        {
            //如果该帐号未设置头像，则在头像控件上显示默认图片:APPLOGO
            imageView_userhead.setImageResource(R.drawable.applogo);
        }


    }

    /**
     * <显示头像图片>
     * 将已经保存在本地的图片显示到头像控件内
     */

    private void setHeadPic()
    {
        try
        {
            Uri imageUri = Uri.parse(picLocation);
            imageView_userhead.setImageURI(imageUri);
            findViewById(R.id.progressBar_loading).setVisibility(View.GONE);
        }catch(Exception e)
        {
            e.printStackTrace();
            findViewById(R.id.progressBar_loading).setVisibility(View.VISIBLE);
        }

    }

    private void setViews()
    {
        //隐藏头部右上角搜索按钮
        findViewById(R.id.head_back_action).setOnClickListener(this);
        findViewById(R.id.imageView_userhead).setOnClickListener(this);

        textView_head_text = (TextView)findViewById(R.id.head_text);
        textView_head_text.setText("个人中心");

        textView_username = (TextView)findViewById(R.id.textView_username);
        imageView_gender = (ImageView)findViewById(R.id.imageView_gender);

        textView_classpost = (TextView)findViewById(R.id.textView_classpost);

        linearLayout_userinfo = (LinearLayout)findViewById(R.id.linearLayout_userinfo);
        linearLayout_userinfo.setOnClickListener(this);
        linearLayout_download = (LinearLayout)findViewById(R.id.linearLayout_download);
        linearLayout_download.setOnClickListener(this);
        linearLayout_setting = (LinearLayout)findViewById(R.id.linearLayout_setting);
        linearLayout_setting.setOnClickListener(this);

        imageView_userhead = (CircleImageView)findViewById(R.id.imageView_userhead);
        imageView_userhead.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.linearLayout_userinfo:
                switchActivity(GO_USERINFO);
                break;
            case R.id.linearLayout_download:
                switchActivity(GO_DOWNLOAD);
                break;
            case R.id.linearLayout_setting:
                switchActivity(GO_SETTING);
                break;
            case R.id.imageView_userhead:
                switchActivity(GO_AVATARPICKING);
                break;
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * <跳转其他界面>
     */
    private void switchActivity(int action)
    {
        Intent intent = new Intent();
        switch(action)
        {
            case GO_USERINFO:
                intent.setClass(AccountActivity.this, UserinfoActivity.class);
                break;
            case GO_AVATARPICKING:
                intent.setClass(AccountActivity.this, AvatarPickingActivity.class);
                break;
            case GO_DOWNLOAD:
                intent.setClass(AccountActivity.this, DownloadManager.class);
                break;
            case GO_SETTING:
                intent.setClass(AccountActivity.this, SettingActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
        if(action == GO_AVATARPICKING || action == GO_USERINFO)
        {
            finish();
        }
    }

    /**
     * <异步加载头像图片>
     * 在登录时获取头像图片地址
     * 下载完成时更新UI
     */

    public class ImageTask extends AsyncTask<String,Void,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... urls)
        {
            Bitmap bmp = null;
            String url = urls[0];
            SPUtills.put(getApplicationContext(), mlogin.getData().getUserId(), localPicName);
            if(url != null && !url.equals(""))
            {
                try
                {
                    if(URLUtil.isHttpUrl(url))
                    {
                        URL mUrl = new URL(url);
                        HttpURLConnection conn = (HttpURLConnection)mUrl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream stream = conn.getInputStream();
                        bmp = BitmapFactory.decodeStream(stream);
                        stream.close();
                    }else
                    {
                        bmp = BitmapFactory.decodeFile(url);
                    }
                }catch(Exception e)
                {
                    return null;
                }
            }
            ImageTools.savePhotoToSDCard(bmp, Environment.getExternalStorageDirectory().getAbsolutePath(), localPicName);
            picLocation = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+localPicName+".png";

            return bmp;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            setHeadPic();
        }

        protected void onPostExecute(Bitmap bmp)
        {
            if(bmp != null)
            {
                setHeadPic();
            }
        }
    }

    /**
     * 获取用户信息并显示
     * 用户名：在登陆时已经获取
     * 性别：0=男 图标♂ 1=女 图标♀ -1=未设置 无图标
     * 班级：可能有多个或者无
     * 岗位：一个或无 显示在班级之后
     */
    public void getUserinfo()
    {

        Map<String,String> mMap = new HashMap<String,String>();
        mMap.put("teacherId", mlogin.getData().getUserId());
        mVolleyManager
                .getString(SPUtills.get(getApplicationContext(), Constants.GET_SERVER, Constants.SERVER).toString()+Constants.QUERY_INFO,
                        mMap, "queryTeacherInfo_app.kq", new NetResult<String>()
                        {
                            @Override
                            public void onFailure(VolleyError error)
                            {

                            }

                            @Override
                            public void onSucceed(String response)
                            {
                                Gson gson = new Gson();
                                int code = Constants.STATUS_FAIL;
                                mUserinfo = gson.fromJson(response, Userinfo.class);

                                try
                                {
                                    code = Integer.parseInt(mUserinfo.getCode());
                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }

                                switch(code)
                                {
                                    case Constants.STATUS_SUCCESS:
                                        setInfo();
                                        checkNewPic();
                                        break;
                                    case Constants.STATUS_ARGUMENT_ERROR:
                                    case Constants.STATUS_DATA_NOTFOUND:
                                    case Constants.STATUS_FAIL:
                                    case Constants.STATUS_ILLEGAL:
                                    case Constants.STATUS_SERVER_EXCEPTION:
                                    case Constants.STATUS_TIMEOUT:
                                        break;
                                }
                            }
                        });
    }

    /**
     * 取得数据后更新UI
     * 登录接口返回的用户姓名因没有再次发送登录请求而导致可能不是最新数据，此处的姓名也是从用户信息查询接口返回。
     */
    public void setInfo()
    {

        textView_username.setText(mUserinfo.getData().getTeachInfo().getName());

        int gender = mUserinfo.getData().getTeachInfo().getGender();
        switch(gender)
        {
            case 0:
                imageView_gender.setImageResource(R.drawable.icon_male);
                break;
            case 1:
                imageView_gender.setImageResource(R.drawable.icon_female);
                break;
            default:
                imageView_gender.setVisibility(View.GONE);
                break;
        }

        String post = mUserinfo.getData().getTeachInfo().getStationName();

        String classText = "";
        List<Userinfo.DataEntity.ClassListEntity> list = mUserinfo.getData().getClassList();
        List<String> classNameList = new ArrayList<>();
        for(int i = 0; i<list.size(); i++)
        {
            classNameList.add(list.get(i).getId());
        }

        String tempClasses[] = mUserinfo.getData().getTeachInfo().getClassIds().split(",");
        for(int i = 0; i<tempClasses.length; i++)
        {
            if(classNameList.contains(tempClasses[i]))
            {
                if(classText.equals(""))
                {
                    classText = classText+list.get(classNameList.indexOf(tempClasses[i])).getName();
                }else
                {
                    classText = classText+"，"+list.get(classNameList.indexOf(tempClasses[i])).getName();
                }
            }
        }
        if(classText == null || classText.equals("null"))
        {
            classText = "";
        }else
        {
            classText += " ";
        }
        textView_classpost.setText(classText+post);
    }

}
