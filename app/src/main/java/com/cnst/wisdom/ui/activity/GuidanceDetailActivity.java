package com.cnst.wisdom.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.cnst.wisdom.model.bean.CourseDetail;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.bean.RelativeMaterialBean;
import com.cnst.wisdom.model.bean.RelativeResourceBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;
import com.youku.service.download.DownloadInfo;
import com.youku.service.download.DownloadManager;
import com.youku.service.download.OnCreateDownloadListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GuidanceDetailActivity extends BaseNetActivity implements View.OnClickListener
{

    private ListView lvRelated;

    /**
     * 视频播放控件
     */
    private YoukuPlayerView playerView;

    private YoukuPlayer player;
    private YoukuBasePlayerManager playerManager;

    /**
     * 标识要播放视频是否来自本地
     */
    private boolean isFromlocal;

    /**
     * 当前播放视频id
     */
    private String vid;

    /**
     * 当前播放视频标题
     */
    private String videoTitle;

    /**
     * 正在下载视频信息
     */
    //    private DownloadInfo info;

    private static final int MSG_REFRESH_PROGRESS = 1;

    //    private Handler handler = new Handler()
    //    {
    //        @Override
    //        public void handleMessage(Message msg)
    //        {
    //            switch(msg.what)
    //            {
    //                case MSG_REFRESH_PROGRESS:
    //                    if(info.size != info.downloadedSize)
    //                    {
    //                        if(info.getState() == info.STATE_PAUSE)
    //                        {
    //                            btnDownload.setText("暂停");
    //                        }else if(info.getState() == info.STATE_DOWNLOADING)
    //                        {
    //                            info = downloadManager.getDownloadInfo(vid);
    //                            if(info != null)
    //                            {
    //                                btnDownload.setText((int)info.getProgress()+"%");
    //                                info.setState(info.STATE_DOWNLOADING);
    //                                handler.sendEmptyMessageDelayed(MSG_REFRESH_PROGRESS, 1000);
    //                            }
    //                        }
    //                    }else
    //                    {
    //                        btnDownload.setText("已下载");
    //                    }
    //                    break;
    //            }
    //        }
    //    };

    private Button btnDownload;
    private DownloadManager downloadManager;

    private VolleyManager volleyManager = VolleyManager.getInstance();

    /**
     * 相关素材列表
     */
    ArrayList<RelativeResourceBean.RelativeResource.ResourceDetail> relativeResList;
    ArrayList<RelativeMaterialBean.AboutMaterialBean> relativeMaterialResList;

    private String resourceType;
    private String resourceId;

    /**
     * 当前播放视频信息
     */
    private TextView tvSubject;
    private TextView tvTerm;
    private TextView tvCourse;
    private TextView tvIntroduction;
    private String mYoukuTitle;
    private BitmapUtils mBitmapUtils;
    private ImageButton mIbtnBack;
    private MaterialDetailAdapter mAdapter;
    private CourseDetail mCourseDetail;
    private TextView mTvWeek;
    private String mWeek;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_guidance_detail);
        if(mBitmapUtils == null)
        {
            mBitmapUtils = new BitmapUtils(this);
        }
        if(downloadManager == null)
        {
            downloadManager = DownloadManager.getInstance();
        }

        initView();
        playerManager = new YoukuBasePlayerManager(this)
        {
            @Override
            public void setPadHorizontalLayout()
            {

            }

            @Override
            public void onInitializationSuccess(YoukuPlayer player)
            {
                addPlugins();
                GuidanceDetailActivity.this.player = player;
                play();
                playerManager.btn_back.setOnClickListener(GuidanceDetailActivity.this);
            }

            @Override
            public void onFullscreenListener()
            {
                playerManager.btn_quality.setVisibility(View.VISIBLE);
                playerManager.playTitleTextView.setVisibility(View.VISIBLE);
                playerManager.btn_back.setVisibility(View.VISIBLE);
                mIbtnBack.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSmallscreenListener()
            {
                playerManager.btn_quality.setVisibility(View.GONE);
                playerManager.playTitleTextView.setVisibility(View.GONE);
                playerManager.btn_back.setVisibility(View.GONE);
                mIbtnBack.setVisibility(View.VISIBLE);
            }
        };
        playerManager.onCreate();

        /**
         * 获取上个页面传入参数
         */
        getIntentData(getIntent());

        getRelativeResList(resourceId);//resourceId //"40288bd75290c787015290c93f6d0001"
        //从资源页面跳转过来是添加标题
        if(!TextUtils.isEmpty(mYoukuTitle))
        {
            tvSubject.setText(mYoukuTitle);
        }
        /**
         * 默认视频
         */
        if(TextUtils.isEmpty(vid))
        {
            vid = "XOTI4ODEwMTYw";
            videoTitle = "默认视频";
        }
        initListener();
        playerView.initialize(playerManager);
    }

    private void initListener()
    {
        //播放相关视频
        lvRelated.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(isMaterial)
                {
                    RelativeMaterialBean.AboutMaterialBean materialBean = relativeMaterialResList.get(position);
                    tvSubject.setText(materialBean.title);
                    String videoId = materialBean.videoId;
                    if(videoId != null && videoId.equals(vid))
                    {
                        Toast.makeText(GuidanceDetailActivity.this, "该视频正在播放中", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        HashMap resPara = new HashMap();
                        resPara.put("resourceId", materialBean.resourceId);
                        resPara.put("dictCode", "shipin");
                        String url = Constants.MaterialRelative;
                        volleyManager.getString(url, resPara, "relativeRes", new NetResult<String>()
                        {
                            @Override
                            public void onFailure(VolleyError error)
                            {

                            }

                            @Override
                            public void onSucceed(String response)
                            {
                                Gson gson = new Gson();
                                RelativeMaterialBean relativeMaterialBean = null;
                                relativeMaterialBean = gson.fromJson(response, RelativeMaterialBean.class);
                                if(relativeMaterialBean != null)
                                {
                                    switch(relativeMaterialBean.code)
                                    {
                                        case Constants.STATUS_ARGUMENT_ERROR:
                                            break;
                                        case Constants.STATUS_DATA_NOTFOUND:
                                            break;
                                        case Constants.STATUS_ILLEGAL:
                                            break;
                                        case Constants.STATUS_SERVER_EXCEPTION:
                                            break;
                                        case Constants.STATUS_SUCCESS:
                                            if(relativeMaterialResList != null)
                                            {
                                                relativeMaterialResList = relativeMaterialBean.data.otherList;
                                                tvSubject.setText(relativeMaterialBean.data.title);
                                                tvIntroduction.setText(relativeMaterialBean.data.introduction);
                                                mAdapter.notifyDataSetChanged();

                                                Log.d("TAG", "sucess................");
                                            }else
                                            {
                                                Log.d("TAG", "failed................");
                                            }
                                            break;
                                        case Constants.STATUS_TIMEOUT:
                                            break;
                                    }
                                }

                            }
                        });
                        vid = videoId;
                        videoTitle = materialBean.title;
                        play();

                    }
                }else
                {
                    RelativeResourceBean.RelativeResource.ResourceDetail resourceDetail = relativeResList
                            .get(position);

                    tvSubject.setText(resourceDetail.getSubject());
                    tvTerm.setText(resourceDetail.getTerm());
                    tvCourse.setText(resourceDetail.getClassName());
                    tvIntroduction.setText(resourceDetail.getIntroduction());
                    String videoId = resourceDetail.getVideoId();

                    if(videoId != null && videoId.equals(vid))
                    {
                        Toast.makeText(GuidanceDetailActivity.this, "该视频正在播放中", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        vid = videoId;
                        videoTitle = resourceDetail.getTitle();
                        play();
                    }
                }
            }
        });


        com.youku.service.download.DownloadInfo youkuInfo = downloadManager.getDownloadInfo(vid);

        if(youkuInfo != null)
        {
            String stateText = "";
            int state = youkuInfo.getState();
            switch(state)
            {
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
            btnDownload.setText(stateText);
        }


    }

    protected void initView()
    {
        tvSubject = (TextView)findViewById(R.id.tvSubject);
        tvTerm = (TextView)findViewById(R.id.tvTerm);
        tvCourse = (TextView)findViewById(R.id.tvCourse);
        tvIntroduction = (TextView)findViewById(R.id.tvIntroduction);
        mTvWeek = (TextView)findViewById(R.id.tvWeek);
        lvRelated = (ListView)findViewById(R.id.related);

        mIbtnBack = (ImageButton)findViewById(R.id.ibtn_back);
        mIbtnBack.setOnClickListener(this);
        playerView = (YoukuPlayerView)findViewById(R.id.player_view);
        playerView.setSmallScreenLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        playerView.setFullScreenLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        btnDownload = (Button)findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.e("111111111111111111", event+"");
        return true;
    }

    protected void initData()
    {

    }

    /**
     * 通过上个页面传来的参数获取视频id
     *
     * @param intent
     */
    private boolean isMaterial = false;

    private void getIntentData(Intent intent)
    {
        if(intent != null)
        {
            isFromlocal = intent.getBooleanExtra("isFromLocal", false);
            mCourseDetail = intent.getParcelableExtra("course");
            if(mCourseDetail != null)
            {
                videoTitle = mCourseDetail.getTitle();
                resourceId = mCourseDetail.getResourceId();
                resourceType = mCourseDetail.getClassification();
                mWeek = mCourseDetail.getWeek();
                mTvWeek.setText(mWeek);
            }else
            {
                isMaterial = true;
                resourceId = intent.getStringExtra("resourceId");
                mYoukuTitle = intent.getStringExtra("title");
                resourceType = intent.getStringExtra("resourceType");
            }

            if(isFromlocal)
            {
                vid = intent.getStringExtra("video_id");
            }else
            {
                vid = intent.getStringExtra("vid");
            }
        }
    }

    /**
     * 播放视频
     */
    private void play()
    {
        if(isFromlocal)
        {
            player.playLocalVideo(vid);
            btnDownload.setText("已下载");
        }else
        {
            if(hasDownloaded(vid))
            {
                btnDownload.setText("已下载");
                player.playLocalVideo(vid);

            }else
            {
                player.playVideo(vid);
            }
        }
    }

    /**
     * 根据视频id判断该视频是否下载
     *
     * @param vid
     * @return
     */
    private boolean hasDownloaded(String vid)
    {
        ArrayList<DownloadInfo> downloadedList = DownloadManager.getInstance().getDownloadedList();
        for(DownloadInfo info : downloadedList)
        {
            if(info.videoid.equals(vid))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否在wifi状态下
     *
     * @return
     */
    private boolean isWifiConn()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isAvailable();
    }

    /**
     * 视频下载
     *
     * @param vid
     * @param title
     */
    private void download(final String vid, final String title)
    {


        DownloadInfo info = downloadManager.getDownloadInfo(vid);
        if(info != null)
        {
            int state = info.getState();
            if(state == DownloadInfo.STATE_DOWNLOADING || info.getState() == DownloadInfo.STATE_INIT || info
                    .getState() == DownloadInfo.STATE_EXCEPTION || info.getState() == DownloadInfo.STATE_WAITING)
            {
                downloadManager.pauseDownload(info.taskId);
                info.setState(DownloadInfo.STATE_PAUSE);
                btnDownload.setText("继续");
            }else if(state == DownloadInfo.STATE_PAUSE)
            {

                downloadManager.startDownload(info.taskId);
                info.setState(DownloadInfo.STATE_DOWNLOADING);
                btnDownload.setText("下载中");
            }
        }else
        {
            downloadManager.createDownload(vid, title, new OnCreateDownloadListener()
            {
                @Override
                public void onfinish(boolean isNeedRefresh)
                {

                }

                @Override
                public void onOneReady()
                {
                    btnDownload.setText("下载中");
                }
            });
            btnDownload.setText("准备下载");
        }
    }

    /**
     * 获取相关资源素材
     *
     * @param resId
     */
    private void getRelativeResList(String resId)
    {
        HashMap resPara = new HashMap();


        String url = "";
        //       是资源页的跳转过来
        if(isMaterial)
        {
            resPara.put("resourceId", resId);
            resPara.put("dictCode", "shipin");
            url = Constants.MaterialRelative;
        }else
        {// 不是资源页的跳转过来
            resPara.put("resourceId", resId);
            BaseApplication app = (BaseApplication)getApplication();
            Login login = app.getLogin();
            resPara.put("userId", login.getData().getUserId());
            url = Constants.GuidanceRelative;
        }
        volleyManager.getString(url, resPara, "relativeRes", new NetResult<String>()
        {
            @Override
            public void onFailure(VolleyError error)
            {

            }

            @Override
            public void onSucceed(String response)
            {
                Gson gson = new Gson();
                if(isMaterial) //       是资源页的跳转过来
                {
                    RelativeMaterialBean relativeMaterialBean = null;
                    relativeMaterialBean = gson.fromJson(response, RelativeMaterialBean.class);
                    if(relativeMaterialBean != null)
                    {
                        switch(relativeMaterialBean.code)
                        {
                            case Constants.STATUS_ARGUMENT_ERROR:
                                break;
                            case Constants.STATUS_DATA_NOTFOUND:
                                break;
                            case Constants.STATUS_ILLEGAL:
                                break;
                            case Constants.STATUS_SERVER_EXCEPTION:
                                break;
                            case Constants.STATUS_SUCCESS:
                                if(relativeMaterialResList == null)
                                {
                                    relativeMaterialResList = relativeMaterialBean.data.otherList;
                                    tvSubject.setText(relativeMaterialBean.data.title);
                                    //                                    tvTerm.setText(relativeResourceBean.getData().getTerm());
                                    //                                    tvCourse.setText(relativeResourceBean.getData().getClassName());
                                    tvIntroduction.setText(relativeMaterialBean.data.introduction);
                                    mAdapter = new MaterialDetailAdapter();
                                    lvRelated.setAdapter(mAdapter);
                                    Log.d("TAG", "sucess................");
                                }else
                                {
                                    Log.d("TAG", "failed................");
                                }
                                break;
                            case Constants.STATUS_TIMEOUT:
                                break;
                        }
                    }
                }else// 不是资源页的跳转过来
                {

                    RelativeResourceBean relativeResourceBean = null;
                    relativeResourceBean = gson.fromJson(response, RelativeResourceBean.class);
                    Log.d("TAG", response);
                    Log.d("TAG", relativeResourceBean.toString());
                    if(relativeResourceBean != null)
                    {
                        switch(relativeResourceBean.getCode())
                        {
                            case Constants.STATUS_ARGUMENT_ERROR:
                                break;
                            case Constants.STATUS_DATA_NOTFOUND:
                                break;
                            case Constants.STATUS_ILLEGAL:
                                break;
                            case Constants.STATUS_SERVER_EXCEPTION:
                                break;
                            case Constants.STATUS_SUCCESS:
                                if(relativeResList == null)
                                {
                                    relativeResList = relativeResourceBean.getData().getOtherList();
                                    ArrayList<RelativeResourceBean.RelativeResource.ResourceDetail> copyRelativeResList = new ArrayList<RelativeResourceBean.RelativeResource.ResourceDetail>();
                                    for(RelativeResourceBean.RelativeResource.ResourceDetail resourceDetail : relativeResList)
                                    {
                                        String txte = mCourseDetail.getTitle();
                                        String tite = resourceDetail.getTitle();
                                        Log.e("txtetxte", txte+"--------------------"+tite);
                                        if(txte.equals(tite))
                                        {
                                            copyRelativeResList.add(resourceDetail);
                                        }
                                    }

                                    relativeResList.removeAll(copyRelativeResList);

                                    tvSubject.setText(relativeResourceBean.getData().getSubject());


                                    tvTerm.setText(relativeResourceBean.getData().getTerm());
                                    tvCourse.setText(relativeResourceBean.getData().getClassName());
                                    tvIntroduction.setText(relativeResourceBean.getData().getIntroduction());
                                    GuidanceDetailAdapter adapter = new GuidanceDetailAdapter(
                                            GuidanceDetailActivity.this);
                                    lvRelated.setAdapter(adapter);
                                    Log.d("TAG", "sucess................");
                                }else
                                {
                                    Log.d("TAG", "failed................");
                                }
                                break;
                            case Constants.STATUS_TIMEOUT:
                                break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.btn_download:
                if(isMaterial)
                {

                    download(vid, mYoukuTitle);
                }else
                {
                    download(vid, videoTitle);
                }

                break;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        playerManager.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        playerManager.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        playerManager.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        playerManager.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        playerManager.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        playerManager.onBackPressed();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        playerManager.onLowMemory();
    }

    /**
     * 配置发生变化
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        playerManager.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onSearchRequested()
    {
        return playerManager.onSearchRequested();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean managerKeyDown = playerManager.onKeyDown(keyCode, event);
        if(playerManager.shouldCallSuperKeyDown())
        {
            return super.onKeyDown(keyCode, event);
        }else
        {
            return managerKeyDown;
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        getIntentData(intent);
        play();
    }

    /**
     * 相关素材listview适配器
     */
    private class MaterialDetailAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            if(relativeMaterialResList != null)
            {
                return relativeMaterialResList.size();
            }
            return 0;
        }

        @Override
        public RelativeMaterialBean.AboutMaterialBean getItem(int position)
        {
            return relativeMaterialResList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder = null;
            if(convertView == null)
            {
                viewHolder = new ViewHolder();
                convertView = View.inflate(GuidanceDetailActivity.this, R.layout.item_guidance_detail, null);
                viewHolder.ivItem = (ImageView)convertView.findViewById(R.id.iv_item);
                viewHolder.tvSubject = (TextView)convertView.findViewById(R.id.tvSubject);
                viewHolder.tvTerm = (TextView)convertView.findViewById(R.id.tvTerm);
                viewHolder.tvCourse = (TextView)convertView.findViewById(R.id.tvCourse);
                viewHolder.tvType = (TextView)convertView.findViewById(R.id.tvType);
                convertView.setTag(viewHolder);
            }else
            {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            RelativeMaterialBean.AboutMaterialBean item = getItem(position);
            if(item.isVideo)
            {
                mBitmapUtils.display(viewHolder.ivItem, item.thumbnailPath);
            }else
            {
                mBitmapUtils.display(viewHolder.ivItem, Constants.SERVER+item.thumbnailPath);
            }
            viewHolder.tvSubject.setText(item.title);
            viewHolder.tvType.setText(item.introduction);

            return convertView;
        }
    }

    /**
     * 相关课前指导listview适配器
     */
    private class GuidanceDetailAdapter extends BaseAdapter
    {

        private final LayoutInflater inflater;
        private Context mContext;

        public GuidanceDetailAdapter(Context context)
        {
            mContext = context;
            inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return relativeResList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return relativeResList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            RelativeResourceBean.RelativeResource.ResourceDetail resourceDetail = (RelativeResourceBean.RelativeResource.ResourceDetail)getItem(
                    position);
            ViewHolder viewHolder;
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.item_guidance_detail, null);
                viewHolder = new ViewHolder();
                viewHolder.ivItem = (ImageView)convertView.findViewById(R.id.iv_item);
                viewHolder.tvSubject = (TextView)convertView.findViewById(R.id.tvSubject);
                viewHolder.tvTerm = (TextView)convertView.findViewById(R.id.tvTerm);
                viewHolder.tvWeek = (TextView)convertView.findViewById(R.id.tvWeek);
                viewHolder.tvCourse = (TextView)convertView.findViewById(R.id.tvCourse);
                viewHolder.tvType = (TextView)convertView.findViewById(R.id.tvType);
                convertView.setTag(viewHolder);
            }else
            {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.tvSubject.setText(resourceDetail.getSubject());
            viewHolder.tvTerm.setText(resourceDetail.getTerm());
            viewHolder.tvCourse.setText(resourceDetail.getClassName());
            viewHolder.tvWeek.setText(resourceDetail.getWeek());
            viewHolder.tvType.setText("["+resourceDetail.getClassification()+"]");
            Log.d("图片地址: "+position, Constants.SERVER+resourceDetail.getThumbnailPath());
            Glide.with(mContext).load(resourceDetail.getThumbnailPath()).placeholder(R.drawable.ic_empty_page)
                    .error(R.drawable.ic_empty_page).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivItem);//bitmapTransform(new CropCircleTransformation(mContext))mContext
            return convertView;
        }
    }

    private static class ViewHolder
    {
        ImageView ivItem;
        TextView tvSubject;
        TextView tvTerm;
        TextView tvCourse;
        TextView tvType;
        TextView tvWeek;
    }
}
