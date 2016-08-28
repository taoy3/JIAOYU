package com.cnst.wisdom.ui.materialpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseListAdapter;
import com.cnst.wisdom.download.DownloadInfo;
import com.cnst.wisdom.download.DownloadManager;
import com.cnst.wisdom.download.DownloadService;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.MaterialPagerBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.activity.GuidanceDetailActivity;
import com.cnst.wisdom.ui.activity.PictureActivity;
import com.cnst.wisdom.ui.view.RefreshListView;
import com.cnst.wisdom.utills.MD5Utils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.youku.service.download.DownloadListenerImpl;
import com.youku.service.download.OnCreateDownloadListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 资源页
 * <功能详细描述>
 *
 * @author tangbinfeng
 * @see [相关类/方法]
 * @since [产品/模板版本]
 */

public class MaterialPager
{
    private DownloadManager downloadManager;
    private BaseListAdapter<MaterialPagerBean.MaterialPager> mAdapter;//适配器测试
    private Activity mActivity;
    protected RefreshListView mListView;
    public View mRootView;
    private String mCode;//素材分类code
    private VolleyManager volleyManager = VolleyManager.getInstance();
    private ArrayList<MaterialPagerBean.MaterialPager> mMaterialPagerList;
    private ArrayList<MaterialPagerBean.MaterialPager> mMaterialMorePagerList;
    private String searchText;
    private com.youku.service.download.DownloadManager mYoukuDownloadManager;
    private com.youku.service.download.DownloadServiceManager mDownloadServiceManager;

    public MaterialPager(Activity activity, String code)
    {
        this.mActivity = activity;
        mRootView = getRootView();
        this.mCode = code;
    }

    /**
     * 获取视图
     *
     * @return
     */
    public View getRootView()
    {
        View view = View.inflate(mActivity, R.layout.fragment_music, null);
        mListView = (RefreshListView)view.findViewById(R.id.listView);
        return view;
    }

    /**
     * 初始化数据
     */
    public void initData()
    {
        if(mYoukuDownloadManager == null)
        {
            mYoukuDownloadManager = com.youku.service.download.DownloadManager.getInstance();
        }

        if(downloadManager == null)
        {
            downloadManager = DownloadService.getDownloadManager(mActivity);
            downloadManager.setMaxDownloadThread(5);

        }
        if(mDownloadServiceManager == null)
        {
            mDownloadServiceManager = com.youku.service.download.DownloadServiceManager.getInstance();


        }


        initDataFromService();
        initListener();
    }

    /**
     * 初始化监听器
     */
    private void initListener()
    {

        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener()
        {

            //当下拉刷新时调用
            @Override
            public void onRefresh()
            {
                //                Toast.makeText(mActivity,"下拉刷新啦",Toast.LENGTH_LONG).show();
                initDataFromService();
            }

            //当加载更多时调用
            @Override
            public void onLoadMore()
            {
                //   Toast.makeText(mActivity,"加载更多啦",Toast.LENGTH_LONG).show();
                mPageNum++;
                isMore = true;
                initDataFromService();
            }
        });
        //条目点击事件
        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                TextView tv_id = (TextView)view.findViewById(R.id.tv_id);
                TextView tv_img = (TextView)view.findViewById(R.id.tv_img);
                TextView tv_path = (TextView)view.findViewById(R.id.tv_path);
                TextView tv_item_music = (TextView)view.findViewById(R.id.tv_item_music);
                Button btn_item_music = (Button)view.findViewById(R.id.btn_item_music);
                String btnName = btn_item_music.getText().toString();

                if(mCode.equals("huiben") || mCode.equals("tupian"))
                {
                    String filePath = new String(tv_path.getText().toString());
                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                    if("下载完成".equals(btnName) || "打开".equals(btnName))
                    {
                        if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp".equals(fileName)|| ".png"
                                .equals(fileName))
                        {
                            Intent intent = new Intent(mActivity, PictureActivity.class);
                            intent.putExtra("resourceId", tv_id.getText().toString());
                            intent.putExtra("isOK", true);
                            intent.putExtra("filePath", tv_path.getText().toString());
                            intent.putExtra("pic_name", tv_item_music.getText().toString());
                            intent.putExtra("code", mCode);
                            mActivity.startActivity(intent);
                        }else
                        {
                            Toast.makeText(mActivity, "不是图片格式", Toast.LENGTH_SHORT).show();
                        }

                    }else
                    {
                        if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp".equals(fileName)|| ".png"
                                .equals(fileName))
                        {
                            Intent intent = new Intent(mActivity, PictureActivity.class);
                            intent.putExtra("filePath", tv_path.getText().toString());
                            intent.putExtra("pic_name", tv_item_music.getText().toString());
                            intent.putExtra("pic", tv_img.getText().toString());
                            intent.putExtra("resourceId", tv_id.getText().toString());
                            intent.putExtra("code", mCode);
                            mActivity.startActivity(intent);
                        }else
                        {
                            Toast.makeText(mActivity, "不是图片格式", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else if(mCode.equals("tushu"))
                {
                    if("下载完成".equals(btnName) || "打开".equals(btnName))
                    {
                        String filePath = new String(tv_path.getText().toString());
                        String fileName = filePath.substring(filePath.lastIndexOf("."));
                        if(".pdf".equals(fileName))
                        {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.parse("file://"+getTargetPath(tv_id.getText().toString(),
                                            tv_path.getText().toString())), "application/pdf");
                            mActivity.startActivity(intent);
                        }
                    }else
                    {
                        Toast.makeText(mActivity, "文件暂不支持在线查看", Toast.LENGTH_SHORT).show();
                    }
                }else if(mCode.equals("jiaoan"))
                {
                    if("下载完成".equals(btnName) || "打开".equals(btnName))
                    {
                        String filePath = new String(tv_path.getText().toString());
                        String fileName = filePath.substring(filePath.lastIndexOf("."));
                        if(".ppt".equals(fileName))
                        {
                            Intent intent = new Intent("android.intent.action.VIEW");

                            intent.addCategory("android.intent.category.DEFAULT");

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.setDataAndType(Uri.parse("file://"+getTargetPath(tv_id.getText().toString(),
                                            tv_path.getText().toString())), "application/vnd.ms-powerpoint");
                            mActivity.startActivity(intent);
                        }else if(".docx".equals(fileName))
                        {
                            Intent intent = new Intent("android.intent.action.VIEW");

                            intent.addCategory("android.intent.category.DEFAULT");

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.setDataAndType(Uri.parse("file://"+getTargetPath(tv_id.getText().toString(),
                                            tv_path.getText().toString())), "application/msword");
                            mActivity.startActivity(intent);
                        }

                    }else
                    {
                        Toast.makeText(mActivity, "文件暂不支持在线查看", Toast.LENGTH_SHORT).show();
                    }


                }else if(mCode.equals("yinyue"))
                {
                    String filePath = new String(tv_path.getText().toString());
                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                    if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma".equals(fileName))
                    {
                        if("下载完成".equals(btnName) || "打开".equals(btnName))
                        {
                            Intent it = new Intent(Intent.ACTION_VIEW);
                            it.setDataAndType(Uri.parse(
                                    "file://"+getTargetPath(tv_id.getText().toString(), tv_path.getText().toString())),
                                    "audio/MP3");

                            mActivity.startActivity(it);

                        }else
                        {
                            if(".wma".equals(fileName))
                            {
                                Toast.makeText(mActivity, "此文件不支持在线播放", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try
                            {
                                Intent it = new Intent(Intent.ACTION_VIEW);
                                it.setDataAndType(Uri.parse(Constants.SERVER+tv_path.getText().toString()),
                                        "audio/MP3");
                                mActivity.startActivity(it);
                            }catch(Exception e)
                            {
                                Toast.makeText(mActivity, "您手机型号不支持在线播放音乐", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else
                    {
                        Toast.makeText(mActivity, "不是音乐格式", Toast.LENGTH_SHORT).show();
                    }
                }else if(mCode.equals("jiemu") || mCode.equals("shipin"))
                {
                    Intent intent = new Intent(mActivity, GuidanceDetailActivity.class);
                    intent.putExtra("vid", tv_id.getText().toString());
                    intent.putExtra("title", tv_item_music.getText().toString());
                    intent.putExtra("resourceId", tv_path.getText().toString());
                    intent.putExtra("resourceType", mCode);
                    mActivity.startActivity(intent);
                }else
                {
                    if("下载完成".equals(btnName) || "打开".equals(btnName))
                    {
                        String filePath = new String(tv_path.getText().toString());
                        String fileName = filePath.substring(filePath.lastIndexOf("."));
                        if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp".equals(fileName) || ".png"
                                .equals(fileName))
                        {
                            Intent intent = new Intent(mActivity, PictureActivity.class);
                            intent.putExtra("filePath", tv_path.getText().toString());
                            intent.putExtra("pic_name", tv_item_music.getText().toString());
                            intent.putExtra("pic", tv_img.getText().toString());
                            intent.putExtra("resourceId", tv_id.getText().toString());
                            intent.putExtra("code", mCode);
                            mActivity.startActivity(intent);
                        }else if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma".equals(fileName))
                        {

                            if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma".equals(fileName))
                            {
                                if("下载完成".equals(btnName) || "打开".equals(btnName))
                                {
                                    Intent it = new Intent(Intent.ACTION_VIEW);
                                    it.setDataAndType(Uri.parse("file://"+getTargetPath(tv_id.getText().toString(),
                                            tv_path.getText().toString())), "audio/MP3");

                                    mActivity.startActivity(it);

                                }else
                                {
                                    if(".wma".equals(fileName))
                                    {
                                        Toast.makeText(mActivity, "此文件不支持在线播放", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    try
                                    {
                                        Intent it = new Intent(Intent.ACTION_VIEW);
                                        it.setDataAndType(Uri.parse(Constants.SERVER+tv_path.getText().toString()),
                                                "audio/MP3");
                                        mActivity.startActivity(it);
                                    }catch(Exception e)
                                    {
                                        Toast.makeText(mActivity, "您手机型号不支持在线播放音乐", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else
                            {
                                Toast.makeText(mActivity, "不是音乐格式", Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            Toast.makeText(mActivity, "不支持此格式", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
    }

    /**
     * 音乐广播接受者
     */
    class MusicBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    }

    /**
     * 从网络上获取数据
     */
    private int mPageNum = 1;
    private int mPageSize = 10;

    protected void initDataFromService()
    {
        //网络请求参数
        HashMap map = new HashMap();
        map.put("dictCode", mCode);
        map.put("pageNum", String.valueOf(mPageNum));
        map.put("pageSize", String.valueOf(mPageSize));
        volleyManager.getString(Constants.SERVER+Constants.QUERY_MATERIAL, map, "Material", new NetResult<String>()
        {
            @Override
            public void onFailure(VolleyError error)
            {
                mListView.onRefreshComplete(false);
            }

            @Override
            public void onSucceed(String response)
            {
                Gson gson = new Gson();
                MaterialPagerBean materialPagerBean = null;
                materialPagerBean = gson.fromJson(response, MaterialPagerBean.class);
                if(materialPagerBean != null)
                {
                    //                                    Log.e("Material", materialPagerBean.toString());
                    int code = materialPagerBean.code;
                    switch(code)
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


                            initServiceData(materialPagerBean);
                            mListView.onRefreshComplete(true);


                            break;
                        case Constants.STATUS_TIMEOUT:
                            break;
                    }

                }
            }
        });
    }

    /**
     * 初始化从网络获取的数据
     */
    private MaterialPagerAdapter mMaterialAdapter;
    private boolean isMore = false;

    protected void initServiceData(MaterialPagerBean materialPagerBean)
    {

        if(isMore && searchText == null)
        {
            mMaterialMorePagerList = materialPagerBean.data;
            if(mMaterialMorePagerList != null)
            {
                //                    以没有更多数据

                if(materialPagerBean.pageInfo.rowCount<=mMaterialPagerList.size())
                {
                    //                    Toast.makeText(mActivity, "没有更多数据", Toast.LENGTH_SHORT).show();
                }else
                {
                    mMaterialPagerList.addAll(mMaterialMorePagerList);
                    mMaterialAdapter.notifyDataSetChanged();
                }
            }
        }else

        {
            //                      第一次加载时
            mMaterialPagerList = materialPagerBean.data;
            if(mMaterialPagerList != null)
            {
                mMaterialAdapter = new MaterialPagerAdapter();
                mListView.setAdapter(mMaterialAdapter);
            }
        }
    }

    /**
     * 适配器
     */
    private HttpUtils mHttp;
    private HttpHandler mHandler;
    private BitmapUtils bitmapUtils;

    private void checkText(String text, TextView textView)
    {
        textView.setText(text);
        if(text != null && text.length()-searchText.length()>=0)
        {
            for(int i = 0; i<text.length()-searchText.length()+1; i++)
            {
                String sub = text.substring(i, searchText.length()+i);
                if(searchText.equals(sub))
                {
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(
                            mActivity.getResources().getColor(R.color.colorPrimary));
                    builder.setSpan(redSpan, i, searchText.length()+i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(builder);
                    break;
                }
            }
        }
    }

    public void setSearchText(String text)
    {
        this.searchText = text;
    }

    private DownloadListenerImpl mDownloadListener;//下载监听器

    class MaterialPagerAdapter extends BaseAdapter
    {


        public MaterialPagerAdapter()
        {
            bitmapUtils = new BitmapUtils(mActivity);

            mHttp = new HttpUtils();
        }

        @Override
        public int getCount()
        {
            return mMaterialPagerList.size();
        }

        @Override
        public MaterialPagerBean.MaterialPager getItem(int position)
        {
            return mMaterialPagerList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                if(mCode.equals("jiemu") || mCode.equals("shipin"))
                {
                    convertView = View.inflate(mActivity, R.layout.item_video, null);
                    holder.iv_item_music = (ImageView)convertView.findViewById(R.id.iv_item_music);
                    holder.tv_path = (TextView)convertView.findViewById(R.id.tv_path);
                    holder.tv_item_speed = (TextView)convertView.findViewById(R.id.tv_item_speed);
                    holder.tv_item_total = (TextView)convertView.findViewById(R.id.tv_item_total);
                    holder.tv_item_progress = (TextView)convertView.findViewById(R.id.tv_item_progress);
                    holder.tv_id = (TextView)convertView.findViewById(R.id.tv_id);
                    holder.tv_item_music = (TextView)convertView.findViewById(R.id.tv_item_music);
                    holder.btn_item_music = (Button)convertView.findViewById(R.id.btn_item_music);
                }else
                {
                    convertView = View.inflate(mActivity, R.layout.item_music, null);
                    holder.iv_item_music = (ImageView)convertView.findViewById(R.id.iv_item_music);
                    holder.tv_path = (TextView)convertView.findViewById(R.id.tv_path);
                    holder.tv_item_speed = (TextView)convertView.findViewById(R.id.tv_item_speed);
                    holder.tv_item_total = (TextView)convertView.findViewById(R.id.tv_item_total);
                    holder.tv_img = (TextView)convertView.findViewById(R.id.tv_img);
                    holder.tv_item_progress = (TextView)convertView.findViewById(R.id.tv_item_progress);
                    holder.tv_id = (TextView)convertView.findViewById(R.id.tv_id);
                    holder.tv_item_music = (TextView)convertView.findViewById(R.id.tv_item_music);
                    holder.btn_item_music = (Button)convertView.findViewById(R.id.btn_item_music);
                }
                convertView.setTag(holder);

            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            final MaterialPagerBean.MaterialPager item = getItem(position);
            //如果是教具视频
            if(mCode.equals("jiemu") || mCode.equals("shipin"))
            {
                holder.tv_id.setText(item.videoId);
                holder.tv_path.setText(item.resourceId);

            }else
            {
                holder.tv_id.setText(item.resourceId);
                holder.tv_path.setText(item.filePath);
                if(mCode.equals("huiben") || mCode.equals("tupian"))
                {
                    holder.tv_img.setText(item.thumbnailPath);
                }
            }
            //获取文件大小M单位
            if(item.fileSize == null)
            {
                item.fileSize = "0.1";
            }
            String fileSize = getFileSize(item.fileSize);
            if(searchText == null)
            {
                holder.tv_item_music.setText(item.title);
            }else
            {
                checkText(item.title, holder.tv_item_music);
            }

            holder.tv_item_total.setText(fileSize);


            if(Boolean.valueOf(item.isVideo) == true)
            {
                bitmapUtils.display(holder.iv_item_music, item.thumbnailPath);

            }else
            {

                Environment.getExternalStorageDirectory().getPath();
                bitmapUtils.display(holder.iv_item_music, Constants.SERVER+item.thumbnailPath);

            }

            /**
             * 设置下载按钮显示逻辑
             */
            final DownloadInfo info = downloadManager.getDownloadInfo(MD5Utils.Md5(Constants.SERVER+item.filePath));
            if(mCode.equals("jiemu") || mCode.equals("shipin"))
            {
                String stateText = "";
                com.youku.service.download.DownloadInfo youkuInfo = mYoukuDownloadManager
                        .getDownloadInfo(item.videoId);
                if(youkuInfo != null)
                {

                    int state = youkuInfo.getState();
                    switch(state)
                    {
                        case com.youku.service.download.DownloadInfo.STATE_DOWNLOADING:
                            stateText = "下载中";
                            break;
                        case com.youku.service.download.DownloadInfo.STATE_FINISH:
                            stateText = "打开";
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
                    holder.btn_item_music.setText(stateText);
                }
            }else
            {

                //如果状态是下载以完成
                if(info != null)
                {

                    Log.e("info.getState()", info.getState()+"");
                    if(info.getState() == HttpHandler.State.SUCCESS)
                    {
                        if(mCode.equals("huiben") || mCode.equals("yinyue"))
                        {
                            holder.btn_item_music.setText("打开");
                            holder.tv_item_speed.setText("");
                            holder.tv_item_progress.setVisibility(View.GONE);
                        }else
                        {
                            holder.btn_item_music.setText("打开");
                            holder.tv_item_speed.setText("");
                            holder.tv_item_progress.setVisibility(View.GONE);
                        }
                    }else if(info.getState() == HttpHandler.State.FAILURE)
                    {
                        holder.btn_item_music.setText("下载失败");
                    }else if(info.getState() == HttpHandler.State.CANCELLED)
                    {
                        holder.btn_item_music.setText("继续");
                        //                DownloadInfo info = downloadManager.getDownloadInfo(MD5Utils.Md5(item.filePath));
                    }else if(info.getState() == HttpHandler.State.WAITING)
                    {
                        holder.btn_item_music.setText("等待中");
                    }else if(info.getState() == HttpHandler.State.LOADING)
                    {
                        HttpHandler<File> handler = info.getHandler();
                        if(handler != null)
                        {
                            holder.btn_item_music.setText("下载中");
                            RequestCallBack callBack = handler.getRequestCallBack();
                            if(callBack instanceof DownloadManager.ManagerCallBack)
                            {
                                DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack)callBack;
                                //
                                managerCallBack.setBaseCallBack(new RequestCallBack<File>()
                                {
                                    private long startSpeed = 0;

                                    private long startSpeed2 = 0;

                                    @Override
                                    public void onStart()
                                    {
                                        holder.tv_item_progress.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoading(long total, long current, boolean isUploading)
                                    {
                                        if(holder.tv_item_progress.getVisibility() == View.GONE)
                                        {
                                            holder.tv_item_progress.setVisibility(View.VISIBLE);
                                        }
                                        long speed = current-startSpeed2;

                                        double progress = (double)current/1024/1024;
                                        String aa = progress+"";
                                        int i = aa.lastIndexOf(".");
                                        String file = aa.substring(0, i+2);
                                        holder.tv_item_progress.setText(file+"M/");
                                        holder.btn_item_music.setText(String.valueOf(current*100/total)+"%");
                                        holder.tv_item_speed.setText(speed/1024+"KB/s");
                                        startSpeed2 = current;

                                    }

                                    @Override
                                    public void onSuccess(ResponseInfo<File> responseInfo)
                                    {
                                        // super.onSuccess(responseInfo);
                                        holder.btn_item_music.setText("打开");
                                        holder.tv_item_speed.setText("");
                                        Toast.makeText(mActivity,
                                                "保存至"+"/com.cnst.wisdom/download/"+mCode+"/"+item.resourceId+getName(
                                                        item.filePath), Toast.LENGTH_LONG).show();
                                        mMaterialAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s)
                                    {
                                        Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                                        holder.btn_item_music.setText("下载失败");
                                        holder.tv_item_speed.setText("");
                                    }
                                });

                            }

                        }else
                        {
                            resumeDownload(info, holder);
                        }
                    }

                }
            }
            /**
             * 点击下载文件
             */
            holder.btn_item_music.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    if(mCode.equals("jiemu") || mCode.equals("shipin"))
                    {
                        com.youku.service.download.DownloadInfo info = mYoukuDownloadManager
                                .getDownloadInfo(item.videoId);
                        if(info != null)
                        {
                            int state = info.getState();
                            switch(state)
                            {
                                case com.youku.service.download.DownloadInfo.STATE_WAITING:
                                    holder.btn_item_music.setText("等待下载");
                                    break;
                                case com.youku.service.download.DownloadInfo.STATE_DOWNLOADING:
                                    mYoukuDownloadManager.pauseDownload(info.taskId);
                                    holder.btn_item_music.setText("继续");
                                    break;
                                case com.youku.service.download.DownloadInfo.STATE_PAUSE:
                                    mYoukuDownloadManager.startDownload(info.taskId);
                                    holder.btn_item_music.setText("下载中");
                                    break;
                            }


                        }else
                        {
                            mYoukuDownloadManager
                                    .createDownload(item.videoId, item.title, new OnCreateDownloadListener()
                                    {


                                        @Override
                                        public void onfinish(boolean isNeedRefresh)
                                        {
                                            com.youku.service.download.DownloadInfo info = mYoukuDownloadManager
                                                    .getDownloadInfo(item.videoId);
                                            if(info != null)
                                            {
                                                int state = info.getState();
                                                switch(state)
                                                {
                                                    case com.youku.service.download.DownloadInfo.STATE_DOWNLOADING:

                                                        break;
                                                    case com.youku.service.download.DownloadInfo.STATE_FINISH:
                                                        holder.btn_item_music.setText("打开");
                                                        break;
                                                    case com.youku.service.download.DownloadInfo.STATE_WAITING:

                                                        break;
                                                    case com.youku.service.download.DownloadInfo.STATE_EXCEPTION:

                                                        break;
                                                    case com.youku.service.download.DownloadInfo.STATE_PAUSE:

                                                        break;
                                                    case com.youku.service.download.DownloadInfo.STATE_INIT:

                                                        break;
                                                }

                                            }

                                        }

                                        @Override
                                        public void onOneReady()
                                        {
                                            //                                            com.youku.service.download.DownloadInfo info = mYoukuDownloadManager
                                            //                                                    .getDownloadInfo(item.videoId);
                                            //                                            HashMap<String,com.youku.service.download.DownloadInfo> hashMap = mYoukuDownloadManager
                                            //                                                    .getDownloadingData();
                                            //                                            if(hashMap.size() == 0)
                                            //                                            {
                                            holder.btn_item_music.setText("下载中");
                                            //                                            }else
                                            //                                            {
                                            //                                                holder.btn_item_music.setText("等待下载");
                                            //                                            }

                                            //                                            handler.sendEmptyMessageDelayed(MSG_REFRESH_PROGRESS, 1000);
                                        }
                                    });
                            holder.btn_item_music.setText("准备下载");
                        }
                    }else
                    {
                        if(info != null)
                        {
                            if(info.getState() == HttpHandler.State.SUCCESS)
                            {
                                if(mCode.equals("huiben") || mCode.equals("tupian"))
                                {
                                    String filePath = new String(item.filePath);
                                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                                    if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp".equals
                                            (fileName)|| ".png"
                                            .equals(fileName))
                                    {
                                        Intent intent = new Intent(mActivity, PictureActivity.class);
                                        intent.putExtra("resourceId", item.resourceId);
                                        intent.putExtra("filePath", item.filePath);
                                        intent.putExtra("pic_name", item.title);
                                        intent.putExtra("code", mCode);
                                        mActivity.startActivity(intent);
                                    }else
                                    {
                                        Toast.makeText(mActivity, "不是图片格式", Toast.LENGTH_SHORT).show();
                                    }

                                }else if(mCode.equals("tushu"))
                                {
                                    String filePath = new String(item.filePath);
                                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                                    if(".pdf".equals(fileName))
                                    {
                                        Intent intent = new Intent("android.intent.action.VIEW");
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setDataAndType(
                                                Uri.parse("file://"+getTargetPath(item.resourceId, item.filePath)),
                                                "application/pdf");
                                        mActivity.startActivity(intent);
                                    }

                                }else if(mCode.equals("jiaoan"))
                                {
                                    String filePath = new String(item.filePath);
                                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                                    if(".ppt".equals(fileName))
                                    {
                                        Intent intent = new Intent("android.intent.action.VIEW");
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setDataAndType(
                                                Uri.parse("file://"+getTargetPath(item.resourceId, item.filePath)),
                                                "application/vnd.ms-powerpoint");
                                        mActivity.startActivity(intent);
                                    }else if(".docx".equals(fileName))
                                    {
                                        Intent intent = new Intent("android.intent.action.VIEW");
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setDataAndType(
                                                Uri.parse("file://"+getTargetPath(item.resourceId, item.filePath)),
                                                "application/msword");
                                        mActivity.startActivity(intent);
                                    }
                                }else if(mCode.equals("yinyue"))
                                {
                                    String filePath = new String(item.filePath);
                                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                                    if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma".equals(fileName))
                                    {
                                        Intent it = new Intent(Intent.ACTION_VIEW);
                                        //                                it.setDataAndType(Uri.parse(Constants.SERVER+item.filePath),
                                        //                                        "audio/MP3");
                                        it.setDataAndType(
                                                Uri.parse("file://"+getTargetPath(item.resourceId, item.filePath)),
                                                "audio/MP3");
                                        mActivity.startActivity(it);

                                    }else
                                    {
                                        Toast.makeText(mActivity, "不是音频格式", Toast.LENGTH_SHORT).show();
                                    }

                                }else
                                {
                                    String filePath = new String(item.filePath);
                                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                                    if("下载完成".equals(holder.btn_item_music.getText().toString()) || "打开"
                                            .equals(holder.btn_item_music.getText().toString()))
                                    {
                                        if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma"
                                                .equals(fileName))
                                        {
                                            Intent it = new Intent(Intent.ACTION_VIEW);
                                            //                                it.setDataAndType(Uri.parse(Constants.SERVER+item.filePath),
                                            //                                        "audio/MP3");
                                            it.setDataAndType(
                                                    Uri.parse("file://"+getTargetPath(item.resourceId, item.filePath)),
                                                    "audio/MP3");
                                            mActivity.startActivity(it);
                                        }else if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp"
                                                .equals(fileName)|| ".png"
                                                .equals(fileName))
                                        {
                                            Intent intent = new Intent(mActivity, PictureActivity.class);
                                            intent.putExtra("resourceId", item.resourceId);
                                            intent.putExtra("filePath", item.filePath);
                                            intent.putExtra("pic_name", item.title);
                                            intent.putExtra("code", mCode);
                                            mActivity.startActivity(intent);
                                        }else
                                        {
                                            Toast.makeText(mActivity, "不支持此格式", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            }else if(info.getState() == HttpHandler.State.LOADING)
                            {//如果是正在下载点击应该暂停
                                HttpHandler handler = info.getHandler();
                                if(handler != null)
                                {
                                    handler.cancel();
                                    holder.btn_item_music.setText("继续");
                                    holder.tv_item_speed.setText("");
                                }
                            }else if(info.getState() == HttpHandler.State.CANCELLED)
                            {
                                //如果是暂停点击应该继续
                                resumeDownload(info, holder);

                            }

                        }else
                        {
                            download(item, holder.btn_item_music, holder.tv_item_progress, holder.tv_item_speed);
                        }
                    }
                }

            });


            return convertView;
        }


        //        private com.youku.service.download.DownloadInfo getDownloadInfoFromHashMap(HashMap<String,com.youku.service.download.DownloadInfo> hashMap, com.youku.service.download.DownloadInfo info)
        //        {
        //            Iterator iter = hashMap.entrySet().iterator();
        //            while(iter.hasNext())
        //            {
        //                Map.Entry<String,com.youku.service.download.DownloadInfo> entry = (Map.Entry)iter.next();
        //                String key = entry.getKey();
        //                if(key.equals(info.taskId))
        //                {
        //                    com.youku.service.download.DownloadInfo val = entry.getValue();
        //                    return val;
        //                }
        //
        //            }
        //            return null;
        //        }


        private void download(final MaterialPagerBean.MaterialPager item, final Button btn_item_music, final TextView tv_item_progress, final TextView tv_item_speed)
        {
            final String target = getTargetPath(item);
            Log.e("fsfsefsefgsef", target);
            final String url = Constants.SERVER+item.filePath;
            try
            {
                downloadManager.addNewDownload(url, item.title, target, mCode, item.thumbnailPath, item.resourceId,
                        item.filePath, true,
                        // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                        false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                        item.isVideo, new RequestCallBack<File>(1000)
                        {
                            private long startSpeed = 0;


                            @Override
                            public void onStart()
                            {
                                tv_item_progress.setVisibility(View.VISIBLE);
                                btn_item_music.setText("0%");


                            }

                            @Override
                            public void onLoading(long total, long current, boolean isUploading)
                            {
                                if(tv_item_progress.getVisibility() == View.GONE)
                                {
                                    tv_item_progress.setVisibility(View.VISIBLE);
                                }
                                long speed = current-startSpeed;

                                double progress = (double)current/1024/1024;
                                String aa = progress+"";
                                int i = aa.lastIndexOf(".");
                                String file = aa.substring(0, i+2);
                                tv_item_progress.setText(file+"M/");
                                if(total == 0)
                                {
                                    total = 1;
                                }
                                btn_item_music.setText(String.valueOf(current*100/total)+"%");
                                tv_item_speed.setText(speed/1024+"KB/s");
                                startSpeed = current;
                            }

                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo)
                            {
                                btn_item_music.setText("打开");
                                tv_item_speed.setText("");
                                Toast.makeText(mActivity,
                                        "保存至"+"/com.cnst.wisdom/download/"+mCode+"/"+item.resourceId+getName(
                                                item.filePath), Toast.LENGTH_LONG).show();

                                mMaterialAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(HttpException error, String msg)
                            {
                                Log.e("error!!!!!", error.toString()+"---------"+msg);
                                if(msg.equals("maybe the file has downloaded completely"))
                                {
                                    //已经下载
                                    Toast.makeText(mActivity, "已经完成", Toast.LENGTH_SHORT).show();
                                }else if("Not Found".equals(msg))
                                {//文件找不到
                                    showNoFoundDailog();
                                    btn_item_music.setText("重试");
                                    //删除数据库文件
                                    DownloadInfo info = downloadManager.getDownloadInfo(MD5Utils.Md5(url));

                                    try
                                    {
                                        downloadManager.removeDownload(info);
                                    }catch(DbException e)
                                    {
                                        e.printStackTrace();
                                    }

                                }else
                                {
                                    btn_item_music.setText("重试");
                                }
                                tv_item_speed.setText("");
                                //                                        downloadManager.stopDownload();
                                tv_item_progress.setVisibility(View.GONE);
                            }
                        });

            }catch(DbException e)
            {
                LogUtils.e(e.getMessage(), e);
            }
            mMaterialAdapter.notifyDataSetChanged();

        }

    }

    private String getFileSize(String fileSize)
    {
        String size = Double.valueOf(fileSize)/1024+"";
        int i = size.lastIndexOf(".");
        String file = size.substring(0, i+2);

        return file+"M";
    }

    /**
     * 显示文件找不到
     */
    private void showNoFoundDailog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("下载失败");
        builder.setMessage("文件不存在!");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }


    private void resumeDownload(final DownloadInfo info, final ViewHolder holder)
    {

        try
        {
            downloadManager.resumeDownload(info, new RequestCallBack<File>(1000)
            {

                private long startSpeed1 = 0;


                @Override
                public void onStart()
                {

                }

                @Override
                public void onLoading(long total, long current, boolean isUploading)
                {

                    if(holder.tv_item_progress.getVisibility() == View.GONE)
                    {
                        holder.tv_item_progress.setVisibility(View.VISIBLE);
                    }
                    long speed = current-startSpeed1;

                    double progress = (double)current/1024/1024;
                    String aa = progress+"";
                    int i = aa.lastIndexOf(".");
                    String file = aa.substring(0, i+2);
                    holder.tv_item_progress.setText(file+"M/");

                    holder.tv_item_speed.setText(speed/1024+"KB/s");
                    startSpeed1 = current;
                    holder.btn_item_music.setText(String.valueOf(current*100/total)+"%");
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo)
                {
                    holder.btn_item_music.setText("打开");
                    holder.tv_item_speed.setText("");
                    Toast.makeText(mActivity,
                            "保存至"+"/com.cnst.wisdom/download/"+mCode+"/"+info.getResourceId()+getName(
                                    info.getFilePath()), Toast.LENGTH_LONG).show();
                    mMaterialAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(HttpException error, String msg)
                {
                    Log.e("error!!!!!", error.toString()+"---------"+msg);
                    if(msg.equals("maybe the file has downloaded completely"))
                    {
                        //已经下载
                        Toast.makeText(mActivity, "已经完成", Toast.LENGTH_SHORT).show();
                    }else if("Not Found".equals(msg))
                    {//文件找不到
                        showNoFoundDailog();
                        holder.btn_item_music.setText("重试");
                        //删除数据库文件
                        try
                        {
                            downloadManager.removeDownload(info);
                        }catch(DbException e)
                        {
                            e.printStackTrace();
                        }

                    }else
                    {
                        holder.btn_item_music.setText("下载失败");
                    }

                }
            });
        }catch(DbException e)
        {
            e.printStackTrace();
        }
    }


    private String getTargetPath(MaterialPagerBean.MaterialPager item)
    {

        String filePath = new String(item.filePath);
        int index = filePath.lastIndexOf(".");
        String sdRoot = Environment.getExternalStorageDirectory().getPath();
        if(index == -1)
        {
            String target = sdRoot+"/com.cnst.wisdom/download/"+mCode+"/"+item.resourceId+"";
            return target;
        }else
        {
            String fileName = filePath.substring(index);
            String target1 = sdRoot+"/com.cnst.wisdom/download/"+mCode+"/"+item.resourceId+fileName;
            return target1;
        }


    }

    private String getName(String mFilePath)
    {
        String filePath = new String(mFilePath);
        String fileName = filePath.substring(filePath.lastIndexOf("."));

        return fileName;
    }

    private String getTargetPath(String resourceId, String mFilePath)
    {
        String filePath = new String(mFilePath);
        String fileName = filePath.substring(filePath.lastIndexOf("."));
        String sdRoot = Environment.getExternalStorageDirectory().getPath();
        String target = sdRoot+"/com.cnst.wisdom/download/"+mCode+"/"+resourceId+fileName;
        return target;
    }

    public static class ViewHolder
    {
        public TextView tv_item_total;
        public TextView tv_item_progress;
        public TextView tv_img;
        public TextView tv_item_speed;
        public ImageView iv_item_music;
        public TextView tv_item_music;
        public TextView tv_id;
        public TextView tv_path;
        public Button btn_item_music;
    }
}
