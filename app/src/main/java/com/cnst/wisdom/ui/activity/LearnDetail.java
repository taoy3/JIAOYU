package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.DetailEvalueBean;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;
import com.youku.service.download.DownloadInfo;
import com.youku.service.download.DownloadManager;
import com.youku.service.download.OnCreateDownloadListener;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * <播放页详情>
 * <优酷播放器和评论展示>
 *
 * @author pengjingang.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class LearnDetail extends BaseNetActivity implements View.OnClickListener {
    private ImageButton mIbBack;
    private TextView mTvCoursename;
    private TextView mTvIntroduce;
    private Button mBtnDownload;
    private EditText mEtComment;
    private Button mBtnDeliver;
    private ListView mLvComment;
    private DetailEvalueBean.Data mData;
    private DownloadManager downloadManager;
    private EvalueAdapter mEvalueAdapter;
    /**
     * 优酷播放器
     */
    private YoukuPlayerView mPlayerView;
    private YoukuBasePlayerManager mPlayerManager;

    private YoukuPlayer player;
    private Boolean isFromlocal = false;
    private String mResourceId, mVideoId, title;


    /**
     * 正在下载视频信息
     */
    private DownloadInfo info;

    private static final int MSG_REFRESH_PROGRESS = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_PROGRESS:
                    if (info.size != info.downloadedSize) {
                        if (info.getState() == info.STATE_PAUSE) {
                            mBtnDownload.setText("暂停");
                        } else if (info.getState() == info.STATE_DOWNLOADING) {
                            info = downloadManager.getDownloadInfo(mVideoId);
                            if (info != null) {
                                mBtnDownload.setText((int) info.getProgress() + "%");
                                info.setState(info.STATE_DOWNLOADING);
                                handler.sendEmptyMessageDelayed(MSG_REFRESH_PROGRESS, 1000);
                            }
                        }
                    } else {
                        mBtnDownload.setText("已下载");
                    }
                    break;
            }
        }
    };

    @Override
    protected void setLayout() {

    }

    @Override
    protected void initData() {
//设置输入框悬浮在输入法上方
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //获取intent携带的数据
        mResourceId = getIntent().getStringExtra("resourceId");
        mVideoId = getIntent().getStringExtra("videoId");
        title = getIntent().getStringExtra("title");
        mEvalueAdapter = new EvalueAdapter();


        if (downloadManager == null) {
            downloadManager = DownloadManager.getInstance();
        }


        initPlayer();


        initListener();

        getDataFromServer();
    }


    private void getIntentData(Intent intent) {
        if (intent != null) {
            isFromlocal = intent.getBooleanExtra("isFromLocal", false);

            if (isFromlocal) {
                mVideoId = intent.getStringExtra("video_id");
            } else {
                mVideoId = intent.getStringExtra("vid");
            }
        }
    }


    private void initListener() {


        com.youku.service.download.DownloadInfo youkuInfo = downloadManager.getDownloadInfo(mVideoId);

        if (youkuInfo != null) {
            String stateText = "";
            int state = youkuInfo.getState();
            switch (state) {
                case com.youku.service.download.DownloadInfo.STATE_DOWNLOADING:
                    stateText = "下载中";
                    break;
                case com.youku.service.download.DownloadInfo.STATE_FINISH:
                    stateText = "下载完成";
                    break;
                case com.youku.service.download.DownloadInfo.STATE_WAITING:
                    stateText = "等待下载";
                    break;
                case com.youku.service.download.DownloadInfo.STATE_EXCEPTION:
                    stateText = "下载失败";
                    break;
                case com.youku.service.download.DownloadInfo.STATE_PAUSE:
                    stateText = "暂停";
                    break;
                case com.youku.service.download.DownloadInfo.STATE_INIT:
                    stateText = "准备下载";
                    break;
            }
            mBtnDownload.setText(stateText);
        }


    }

    @Override
    public void onBackPressed() { // android系统调用
        Log.e("sgh", "onBackPressed before super");
        super.onBackPressed();
        Log.e("sgh", "onBackPressed");
        mPlayerManager.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerManager.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerManager.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean managerKeyDown = mPlayerManager.onKeyDown(keyCode, event);
        if (mPlayerManager.shouldCallSuperKeyDown()) {
            return super.onKeyDown(keyCode, event);
        } else {
            return managerKeyDown;
        }

    }

    @Override
    public void onLowMemory() { // android系统调用
        super.onLowMemory();
        mPlayerManager.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerManager.onResume();
    }

    @Override
    public boolean onSearchRequested() { // android系统调用
        return mPlayerManager.onSearchRequested();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayerManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerManager.onStop();
    }

    private void initPlayer() {
        mPlayerManager = new YoukuBasePlayerManager(this) {

            @Override
            public void setPadHorizontalLayout() {

            }

            @Override
            public void onInitializationSuccess(YoukuPlayer player) {
                addPlugins();
                LearnDetail.this.player = player;
                play();
                mPlayerManager.btn_back.setOnClickListener(LearnDetail.this);

            }

            @Override
            public void onFullscreenListener() {
                mPlayerManager.btn_quality.setVisibility(View.VISIBLE);
                mPlayerManager.playTitleTextView.setVisibility(View.VISIBLE);

                mPlayerManager.btn_back.setVisibility(View.VISIBLE);
                mIbBack.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSmallscreenListener() {
                mPlayerManager.btn_quality.setVisibility(View.GONE);
                mPlayerManager.playTitleTextView.setVisibility(View.GONE);
                mPlayerManager.btn_back.setVisibility(View.INVISIBLE);
                mIbBack.setVisibility(View.VISIBLE);
            }
        };
        mPlayerManager.onCreate();
        if (TextUtils.isEmpty(mVideoId)) {
            mVideoId = "XOTI4ODEwMTYw";
        }
        mPlayerView.initialize(mPlayerManager);

    }

    private void play() {

        if (hasDownloaded(mVideoId)) {
            mBtnDownload.setText("已下载");
            player.playLocalVideo(mVideoId);

        } else {
            player.playVideo(mVideoId);
        }


    }

    private boolean hasDownloaded(String videoId) {

        ArrayList<DownloadInfo> downloadedList = com.youku.service.download.DownloadManager.getInstance().getDownloadedList();
        for (DownloadInfo info : downloadedList) {
            if (info.videoid.equals(videoId)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 填充评论数据
     */
    private void getDataFromServer() {


        HashMap map = new HashMap();
        map.put("resourceId", mResourceId);
        VolleyManager.getInstance()
                .postString(SPUtills.get(this, Constants.GET_SERVER, "").toString() + Constants.DETAIL_EVALUE, map, "Comment",
                        new NetResult<String>() {
                            @Override
                            public void onFailure(VolleyError error) {
                                Log.e("comment", "获取素材详情加评论列表失败");

                            }

                            @Override
                            public void onSucceed(String response) {

                                Log.e("comment------------", response);
                                Gson gson = new Gson();
                                DetailEvalueBean detailEvalueBean = gson.fromJson(response, DetailEvalueBean.class);
                                if (detailEvalueBean != null) {
                                    switch (detailEvalueBean.getCode()) {
                                        case Constants.STATUS_ARGUMENT_ERROR:
                                            break;
                                        case Constants.STATUS_DATA_NOTFOUND:
                                            break;
                                        case Constants.STATUS_ILLEGAL:
                                            break;
                                        case Constants.STATUS_SERVER_EXCEPTION:
                                            break;
                                        case Constants.STATUS_SUCCESS:
                                            mData = detailEvalueBean.getData();
                                            if (mData != null) {
                                                initDataFromService();
                                            }

                                    }

                                }
                            }
                        });
    }

    private void initDataFromService() {
        mTvCoursename.setText(mData.getTitle());
        mTvIntroduce.setText(mData.getIntroduction());
        mLvComment.setAdapter(mEvalueAdapter);
    }

    protected void initView() {
        setContentView(R.layout.activity_learndetail);
        mLvComment = (ListView) findViewById(R.id.lv_comment);


        mIbBack = (ImageButton) findViewById(R.id.ib_back);
        mTvCoursename = (TextView) findViewById(R.id.tv_coursename);
        mTvIntroduce = (TextView) findViewById(R.id.tv_introduce);
        mBtnDownload = (Button) findViewById(R.id.btn_download);
        mEtComment = (EditText) findViewById(R.id.et_comment);
        mBtnDeliver = (Button) findViewById(R.id.btn_deliver);
        mPlayerView = (YoukuPlayerView) findViewById(R.id.player_view);

        //设置全屏，非全屏
        mPlayerView.setSmallScreenLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mPlayerView.setFullScreenLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


        mBtnDeliver.setOnClickListener(this);
        mIbBack.setOnClickListener(this);
        mBtnDownload.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_deliver:
                deliverEvalue();
                break;
            case R.id.btn_download:
                //                doDownload();
                download(mVideoId, title);

                break;

            default:
                break;

        }

    }

    /**
     * 视频下载
     *
     * @param vid
     * @param title
     */
    private void download(final String vid, final String title) {


        DownloadInfo info = downloadManager.getDownloadInfo(vid);
        if (info != null) {
            int state = info.getState();
            if (state == DownloadInfo.STATE_DOWNLOADING || info.getState() == DownloadInfo.STATE_INIT || info
                    .getState() == DownloadInfo.STATE_EXCEPTION || info.getState() == DownloadInfo.STATE_WAITING) {
                downloadManager.pauseDownload(info.taskId);
                info.setState(DownloadInfo.STATE_PAUSE);
                mBtnDownload.setText("继续");
            } else if (state == DownloadInfo.STATE_PAUSE) {

                downloadManager.startDownload(info.taskId);
                info.setState(DownloadInfo.STATE_DOWNLOADING);
                mBtnDownload.setText("下载中");
            }
        } else {
            downloadManager.createDownload(vid, title, new OnCreateDownloadListener() {
                @Override
                public void onfinish(boolean isNeedRefresh) {

                }

                @Override
                public void onOneReady() {
                    mBtnDownload.setText("下载中");
                }
            });
        }
    }

    /**
     * 发表评论
     */
    private void deliverEvalue()

    {
        String content = mEtComment.getText().toString().trim();
        String teacherId = null;

        if (content.isEmpty()) {
            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
        } else {
            BaseApplication app = (BaseApplication) getApplication();
            Login login = app.getLogin();
            if (login != null) {
                teacherId = login.getData().getUserId();
            }
            //            String resourceId = getIntent().getStringExtra("resourceId");
            HashMap map = new HashMap();
            map.put("resourceId", mResourceId);
            map.put("teacherId", teacherId);
            map.put("content", content);
            VolleyManager.getInstance()
                    .postString(SPUtills.get(getApplicationContext(), Constants.GET_SERVER, "").toString() + Constants.ADD_EVALUE, map,
                            "DeliverEvalue", new NetResult<String>() {
                                @Override
                                public void onFailure(VolleyError error) {

                                }

                                @Override
                                public void onSucceed(String response) {
                                    Toast.makeText(LearnDetail.this, "评论成功", Toast.LENGTH_SHORT).show();
                                    getDataFromServer();
                                    mEvalueAdapter.notifyDataSetChanged();
                                    mEtComment.setText("");


                                }
                            });


        }

    }


    private class EvalueAdapter extends BaseAdapter {

        private DetailEvalueBean.Data.EvaluteList mEvaluteContent;

        @Override
        public int getCount() {
            return mData.getEvaluteList().size();
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
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(LearnDetail.this, R.layout.item_online_detail, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_item);
                holder.tv_username = (TextView) convertView.findViewById(R.id.username);
                holder.tv_content = (TextView) convertView.findViewById(R.id.content);
                holder.tv_time = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            mEvaluteContent = mData.getEvaluteList().get(position);

            holder.tv_username.setText(mEvaluteContent.getTeacherName());
            holder.tv_content.setText(mEvaluteContent.getContent());
            holder.tv_time.setText(mEvaluteContent.getCreateTime());
            //获取网络头像
            String imgUrl = SPUtills.get(getApplicationContext(), Constants.GET_SERVER, Constants.SERVER).toString() +
                    mEvaluteContent.getHeadImgUrl().trim();
            Glide.with(LearnDetail.this).load(imgUrl).placeholder(R.drawable.applogo).error(R.drawable
                    .applogo)
                    .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(LearnDetail.this)).into(holder.iv_icon);


            return convertView;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    }

    private class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_username;
        public TextView tv_content;
        public TextView tv_time;


    }
}
