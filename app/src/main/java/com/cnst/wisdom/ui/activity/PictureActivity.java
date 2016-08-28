package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.download.DownloadService;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.ui.view.ImageControl;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * @author tangbinfeng.
 * @date 2016/2/24
 * @des [一句话描述]
 * @since [产品/模版版本]
 */

public class PictureActivity extends BaseNetActivity
{

    private RelativeLayout mHead_back_action;
    private TextView mHead_text;
    private String mCode;
    private ImageControl mIv_picture;
    private String mFilePath;
    private String mResourceId;
    private Button mHead_more_action;
    private String mPic_name;
    private com.cnst.wisdom.download.DownloadManager mDownloadManager;
    private String mPic;
    private ProgressBar mPb_loading;
    private float density;

    @Override
    protected void setLayout() {
        density = getResources().getDisplayMetrics().density;
        setContentView(R.layout.activity_picture);
    }


    protected void initView()
    {
        mHead_back_action = (RelativeLayout)findViewById(R.id.head_back_action);
        mHead_more_action = (Button)findViewById(R.id.head_more_action);
        mPb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        mHead_text = (TextView)findViewById(R.id.head_text);
        mIv_picture = (ImageControl)findViewById(R.id.iv_picture);

    }

    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        initData();
        initListener();
    }

    private String localCode;

    protected void initData()
    {
        if(mDownloadManager == null)
        {
            mDownloadManager = DownloadService.getDownloadManager(this);
        }


        Intent intent = getIntent();
        mResourceId = intent.getStringExtra("resourceId");

        mFilePath = intent.getStringExtra("filePath");
        mCode = intent.getStringExtra("code");
        localCode = mCode;
        mPic = intent.getStringExtra("pic");
        mPic_name = intent.getStringExtra("pic_name");
        boolean isOK = intent.getBooleanExtra("isOK", false);
        mHead_text.setText(mPic_name);
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        if("all".equals(mCode))
        {
            mCode = "tupian";
        }
        File picFile1 = new File(getTargetPath(mResourceId, mFilePath));
        if(picFile1.exists())
        {
            bitmapUtils
                    .display(mIv_picture, getTargetPath(mResourceId, mFilePath), new BitmapLoadCallBack<ImageControl>()
                    {
                        @Override
                        public void onLoadCompleted(ImageControl imageControl, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom)
                        {
                            Rect frame = new Rect();
                            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

                            int screenW = PictureActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                            int screenH = PictureActivity.this.getWindowManager().getDefaultDisplay()
                                    .getHeight()-(int)( 58*density );
                            if(bitmap != null)
                            {
                                mIv_picture.imageInit(bitmap, screenW, screenH, 0, new ImageControl.ICustomMethod()
                                {

                                    @Override
                                    public void customMethod(Boolean currentStatus)
                                    {
                                        // 当图片处于放大或缩小状态时，控制标题是否显示
                                        if(currentStatus)
                                        {

                                        }else
                                        {

                                        }
                                    }
                                });
                            }

                        }

                        @Override
                        public void onLoadFailed(ImageControl imageControl, String s, Drawable drawable)
                        {

                        }
                    });
            mPb_loading.setVisibility(View.GONE);
            mHead_more_action.setVisibility(View.GONE);
        }else
        {
            mCode = "all";
            if("all".equals(mCode))
            {
                mCode = "huiben";
            }
            File picFile = new File(getTargetPath(mResourceId, mFilePath));

            if(isOK || picFile.exists())
            {
                bitmapUtils.display(mIv_picture, getTargetPath(mResourceId, mFilePath),
                        new BitmapLoadCallBack<ImageControl>()
                        {
                            @Override
                            public void onLoadCompleted(ImageControl imageControl, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom)
                            {
                                Rect frame = new Rect();
                                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                                int statusBarHeight = frame.top;
                                int screenW = PictureActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                                int screenH = PictureActivity.this.getWindowManager().getDefaultDisplay()
                                        .getHeight()-(int)( 58*density );
                                if(bitmap != null)
                                {
                                    mIv_picture.imageInit(bitmap, screenW, screenH, 0, new ImageControl.ICustomMethod()
                                    {

                                        @Override
                                        public void customMethod(Boolean currentStatus)
                                        {
                                            // 当图片处于放大或缩小状态时，控制标题是否显示
                                            if(currentStatus)
                                            {

                                            }else
                                            {

                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onLoadFailed(ImageControl imageControl, String s, Drawable drawable)
                            {

                            }
                        });
                mPb_loading.setVisibility(View.GONE);
                mHead_more_action.setVisibility(View.GONE);
            }else
            {
                mHead_more_action.setVisibility(View.VISIBLE);
                bitmapUtils.display(mIv_picture, Constants.SERVER+mFilePath, new BitmapLoadCallBack<ImageView>()
                {
                    @Override
                    public void onLoadStarted(ImageView container, String uri, BitmapDisplayConfig config)
                    {
                        mPb_loading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom)
                    {
                        Rect frame = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                        int statusBarHeight = frame.top;
                        int screenW = PictureActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                        int screenH = PictureActivity.this.getWindowManager().getDefaultDisplay()
                                .getHeight()-(int)( 58*density );
                        if(bitmap != null)
                        {
                            mIv_picture.imageInit(bitmap, screenW, screenH, 0, new ImageControl.ICustomMethod()
                            {

                                @Override
                                public void customMethod(Boolean currentStatus)
                                {
                                    // 当图片处于放大或缩小状态时，控制标题是否显示
                                    if(currentStatus)
                                    {

                                    }else
                                    {

                                    }
                                }
                            });
                        }
                        mPb_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadFailed(ImageView imageView, String s, Drawable drawable)
                    {

                    }
                });

            }
        }

    }

    private void initListener()

    {
        final String target = getTargetPath(mResourceId, mFilePath);

        mHead_more_action.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    mDownloadManager.addNewDownload(Constants.SERVER+mFilePath, mPic_name, target, localCode, mPic,
                            mResourceId, mFilePath, true, true, "false", new RequestCallBack<File>()
                            {
                                @Override
                                public void onLoading(long total, long current, boolean isUploading)
                                {
                                    mHead_more_action.setText("下载中");
                                }

                                @Override
                                public void onSuccess(ResponseInfo<File> responseInfo)
                                {
                                    mHead_more_action.setText("下载成功");
                                    mHead_more_action.setEnabled(false);
                                }

                                @Override
                                public void onFailure(HttpException e, String s)
                                {
                                    mHead_more_action.setText("下载失败");
                                }
                            });
                }catch(DbException e)
                {
                    e.printStackTrace();
                }

            }
        });
        mHead_back_action.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private String getTargetPath(String resourceId, String mFilePath)
    {

        String filePath = new String(mFilePath);
        String fileName = filePath.substring(filePath.lastIndexOf("."));
        String sdRoot = Environment.getExternalStorageDirectory().getPath();
        String target = sdRoot+"/com.cnst.wisdom/download/"+mCode+"/"+resourceId+fileName;
        return target;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()&MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                mIv_picture.mouseDown(event);
                break;

            /**
             * 非第一个点按下
             */
            case MotionEvent.ACTION_POINTER_DOWN:

                mIv_picture.mousePointDown(event);

                break;
            case MotionEvent.ACTION_MOVE:
                mIv_picture.mouseMove(event);

                break;

            case MotionEvent.ACTION_UP:
                mIv_picture.mouseUp();
                break;

        }

        return super.onTouchEvent(event);
    }


}
