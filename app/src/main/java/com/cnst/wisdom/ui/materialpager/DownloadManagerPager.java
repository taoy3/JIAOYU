package com.cnst.wisdom.ui.materialpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseListAdapter;
import com.cnst.wisdom.download.DownloadInfo;
import com.cnst.wisdom.download.DownloadManager;
import com.cnst.wisdom.download.DownloadService;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.MaterialPagerBean;
import com.cnst.wisdom.ui.activity.GuidanceDetailActivity;
import com.cnst.wisdom.ui.activity.PictureActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author tangbinfeng.
 * @date 2016/2/22
 * @des [一句话描述]
 * @since [产品/模版版本]
 */


public class DownloadManagerPager
{
    private BaseListAdapter<MaterialPagerBean.MaterialPager> mAdapter;//适配器测试
    private Activity mActivity;
    private ListView mListView;
    public View mRootView;
    private String mCode;//素材分类code
    private DownloadManager downloadManager;
    private List<DownloadInfo> mDownloadInfoList;//下载数据集合
    private DownloadListAdapter mAdapter1;
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {


        }
    };

    public DownloadManagerPager(Activity activity, String code)
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
        View view = View.inflate(mActivity, R.layout.fragment_download, null);
        mListView = (ListView)view.findViewById(R.id.listView);
        return view;
    }

    /**
     * 初始化数据
     */

    public void initData()
    {
        initUtilsData();
        initDataFromLocation();
        initListener();
    }

    /**
     * 长按条目显示删除
     */

    private void initListener()
    {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {

                final TextView tv_id = (TextView)view.findViewById(R.id.tv_id);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("确认删除");
                builder.setMessage("确定要删除此条下载记录");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(mCode.equals("jiemu") || mCode.equals("shipin") || ( mCode
                                .equals("all") && position<downloadingList_show.size()
                        ))
                        {
                            com.youku.service.download.DownloadInfo info = youkuDownloadManager
                                    .getDownloadInfo(tv_id.getText().toString());
                            if(info != null)
                            {
                                //                                deleteDownloading
                                if(info.getState() == com.youku.service.download.DownloadInfo.STATE_DOWNLOADING || info
                                        .getState() == com.youku.service.download.DownloadInfo.STATE_PAUSE || info
                                        .getState() == com.youku.service.download.DownloadInfo.STATE_WAITING || info
                                        .getState() == com.youku.service.download.DownloadInfo.STATE_INIT || info
                                        .getState() == com.youku.service.download.DownloadInfo.STATE_CANCEL)
                                {
                                    youkuDownloadManager.deleteDownloading(info.taskId);
                                }
                                boolean isDel = youkuDownloadManager.deleteDownloaded(info);
                                if(isDel)
                                {
                                    Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
                                    downloadingList_show.remove(position);
                                }else
                                {
                                    Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
                                }

                                mAdapter1.notifyDataSetChanged();

                            }

                        }else
                        {
                            try
                            {
                                DownloadInfo downloadInfo1 = null;
                                if(mCode.equals("all") && downloadingList_show.size() != 0)
                                {
                                    downloadInfo1 = downloadManager
                                            .getDownloadInfo(position-downloadingList_show.size());
                                }else
                                {
                                    downloadInfo1 = downloadManager.getDownloadInfo(position);
                                }

                                File file = new File(downloadInfo1.getFileSavePath());
                                if(file.exists())
                                {
                                    boolean b = file.delete();
                                    if(b)
                                    {
                                        Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                Log.e("DownloadInfo", downloadInfo1+"");
                                if(downloadInfo1 != null)
                                {
                                    downloadManager.removeDownload(downloadInfo1);

                                    mAdapter1.notifyDataSetChanged();
                                }


                            }catch(DbException e)
                            {

                                Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
                return true;
            }

        });

        //条目点击事件
        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView tv_id = (TextView)view.findViewById(R.id.tv_id);
                TextView tv_path = (TextView)view.findViewById(R.id.tv_path);
                TextView tv_code = (TextView)view.findViewById(R.id.tv_code);
                TextView tv_item_music = (TextView)view.findViewById(R.id.tv_item_music);
                Button btn_item_music = (Button)view.findViewById(R.id.btn_item_music);
                String btnName = btn_item_music.getText().toString();
                if(mCode.equals("all"))
                {
                    if(downloadingList_show.size() != 0 && position<downloadingList_show.size())
                    {
                        com.youku.service.download.DownloadInfo youkuInfo = youkuDownloadManager
                                .getDownloadInfo(tv_id.getText().toString());
                        if(youkuInfo != null)
                        {
                            Intent intent = new Intent(mActivity, GuidanceDetailActivity.class);
                            intent.putExtra("vid", tv_id.getText().toString());
                            intent.putExtra("title", tv_item_music.getText().toString());
                            intent.putExtra("resourceId", tv_path.getText().toString());
                            intent.putExtra("resourceType", "shipin");
                            mActivity.startActivity(intent);
                        }
                    }else
                    {
                        String filePath = tv_path.getText().toString();
                        String fileName = filePath.substring(filePath.lastIndexOf("."));
                        if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp".equals(fileName)|| ".png"
                                .equals(fileName))
                        {
                            Intent intent = new Intent(mActivity, PictureActivity.class);
                            intent.putExtra("resourceId", tv_id.getText().toString());
                            intent.putExtra("filePath", tv_path.getText().toString());
                            intent.putExtra("pic_name", tv_item_music.getText().toString());
                            intent.putExtra("code", tv_code.getText().toString());
                            mActivity.startActivity(intent);
                        }else if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma".equals(fileName))
                        {
                            if("下载完成".equals(btnName) || "打开".equals(btnName))
                            {
                                DownloadInfo downloadInfo1 = downloadManager
                                        .getDownloadInfo(position-downloadingList_show.size());
                                Intent it = new Intent(Intent.ACTION_VIEW);
                                it.setType("audio/MP3");
                                it.setDataAndType(Uri.parse("file://"+downloadInfo1.getFileSavePath()), "audio/MP3");
                                Log.e("downloadInfo1", downloadInfo1.getFileSavePath());
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
                        }else if(".ppt".equals(fileName) && "打开".equals(btnName))
                        {
                            DownloadInfo downloadInfo1 = downloadManager
                                    .getDownloadInfo(position-downloadingList_show.size());
                            Intent intent = new Intent("android.intent.action.VIEW");

                            intent.addCategory("android.intent.category.DEFAULT");

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.setDataAndType(Uri.parse("file://"+downloadInfo1.getFileSavePath()),
                                    "application/vnd.ms-powerpoint");
                            mActivity.startActivity(intent);
                        }else if(".docx".equals(fileName) && "打开".equals(btnName))
                        {
                            DownloadInfo downloadInfo1 = downloadManager
                                    .getDownloadInfo(position-downloadingList_show.size());
                            Intent intent = new Intent("android.intent.action.VIEW");

                            intent.addCategory("android.intent.category.DEFAULT");

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.setDataAndType(Uri.parse("file://"+downloadInfo1.getFileSavePath()),
                                    "application/msword");
                            mActivity.startActivity(intent);
                        }else if(".pdf".equals(fileName) && "打开".equals(btnName))
                        {
                            DownloadInfo downloadInfo1 = downloadManager
                                    .getDownloadInfo(position-downloadingList_show.size());
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.parse("file://"+downloadInfo1.getFileSavePath()),
                                    "application/pdf");
                            mActivity.startActivity(intent);
                        }
                    }
                }else if(mCode.equals("huiben") || mCode.equals("tupian"))
                {

                    if("下载完成".equals(btnName) || "打开".equals(btnName))
                    {
                        Intent intent = new Intent(mActivity, PictureActivity.class);
                        intent.putExtra("resourceId", tv_id.getText().toString());
                        intent.putExtra("filePath", tv_path.getText().toString());
                        intent.putExtra("pic_name", tv_item_music.getText().toString());
                        intent.putExtra("code", mCode);
                        mActivity.startActivity(intent);
                    }
                }else if(mCode.equals("yinyue"))
                {
                    String filePath = tv_path.getText().toString();
                    String fileName = filePath.substring(filePath.lastIndexOf("."));
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
                            it.setDataAndType(Uri.parse(Constants.SERVER+tv_path.getText().toString()), "audio/MP3");
                            mActivity.startActivity(it);
                        }catch(Exception e)
                        {
                            Toast.makeText(mActivity, "您手机型号不支持在线播放音乐", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(mCode.equals("tushu"))
                {
                    String filePath = tv_path.getText().toString();
                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                    if("下载完成".equals(btnName) || "打开".equals(btnName))
                    {
                        if(".pdf".equals(fileName) && "打开".equals(btnName))
                        {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.parse(
                                    "file://"+getTargetPath(tv_id.getText().toString(), tv_path.getText().toString())),
                                    "application/pdf");
                            mActivity.startActivity(intent);
                        }
                    }
                }else if(mCode.equals("jiaoan"))
                {
                    String filePath = tv_path.getText().toString();
                    String fileName = filePath.substring(filePath.lastIndexOf("."));
                    if("下载完成".equals(btnName) || "打开".equals(btnName))
                    {
                        if(".ppt".equals(fileName))
                        {
                            Intent intent = new Intent("android.intent.action.VIEW");

                            intent.addCategory("android.intent.category.DEFAULT");

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.setDataAndType(Uri.parse(
                                    "file://"+getTargetPath(tv_id.getText().toString(), tv_path.getText().toString())),
                                    "application/vnd.ms-powerpoint");
                            mActivity.startActivity(intent);
                        }else if(".docx".equals(fileName))
                        {
                            Intent intent = new Intent("android.intent.action.VIEW");

                            intent.addCategory("android.intent.category.DEFAULT");

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.setDataAndType(Uri.parse(
                                    "file://"+getTargetPath(tv_id.getText().toString(), tv_path.getText().toString())),
                                    "application/msword");
                            mActivity.startActivity(intent);
                        }
                    }

                }else if(mCode.equals("jiemu") || mCode.equals("shipin"))
                {
                    com.youku.service.download.DownloadInfo youkuInfo = youkuDownloadManager
                            .getDownloadInfo(tv_id.getText().toString());
                    if(youkuInfo != null)
                    {
                        Intent intent = new Intent(mActivity, GuidanceDetailActivity.class);
                        intent.putExtra("vid", tv_id.getText().toString());
                        intent.putExtra("title", tv_item_music.getText().toString());
                        intent.putExtra("resourceId", tv_path.getText().toString());
                        intent.putExtra("resourceType", "shipin");
                        mActivity.startActivity(intent);
                    }
                }else
                {
                    if("下载完成".equals(btnName) || "打开".equals(btnName))
                    {
                        String filePath = tv_path.getText().toString();
                        String fileName = filePath.substring(filePath.lastIndexOf("."));
                        if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp".equals(fileName)|| ".png"
                                .equals(fileName))
                        {
                            Intent intent = new Intent(mActivity, PictureActivity.class);
                            intent.putExtra("resourceId", tv_id.getText().toString());
                            intent.putExtra("filePath", tv_path.getText().toString());
                            intent.putExtra("pic_name", tv_item_music.getText().toString());
                            intent.putExtra("code", mCode);
                            mActivity.startActivity(intent);
                        }else if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma".equals(fileName))
                        {
                            if("下载完成".equals(btnName) || "打开".equals(btnName))
                            {
                                DownloadInfo downloadInfo1 = downloadManager
                                        .getDownloadInfo(position-downloadingList_show.size());
                                Intent it = new Intent(Intent.ACTION_VIEW);
                                it.setDataAndType(Uri.parse("file://"+downloadInfo1.getFileSavePath()), "audio/MP3");
                                Log.e("downloadInfo1", downloadInfo1.getFileSavePath());
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
                            Toast.makeText(mActivity, "不支持此格式", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
    }


    private void initUtilsData()
    {
        if(mCode.equals("jiemu") || mCode.equals("shipin") || mCode.equals("all"))
        {
            if(youkuDownloadManager == null)
            {
                youkuDownloadManager = com.youku.service.download.DownloadManager.getInstance();

                if(downloadingList_show.size() == 0)
                {
                    Iterator itering = youkuDownloadManager.getDownloadingData().entrySet().iterator();
                    while(itering.hasNext())
                    {
                        java.util.Map.Entry entry = (java.util.Map.Entry)itering.next();
                        //视频信息实体类用DownloadInfo表示
                        com.youku.service.download.DownloadInfo info = (com.youku.service.download.DownloadInfo)entry
                                .getValue();
                        downloadingList_show.add(info);
                    }

                    Iterator itered = youkuDownloadManager.getDownloadedData().entrySet().iterator();

                    while(itered.hasNext())
                    {
                        java.util.Map.Entry entry = (java.util.Map.Entry)itered.next();
                        //视频信息实体类用DownloadInfo表示
                        com.youku.service.download.DownloadInfo info = (com.youku.service.download.DownloadInfo)entry
                                .getValue();
                        downloadingList_show.add(info);
                    }

                }
            }

        }
        //初始化数据库工具类
        if(db == null)
        {

            db = DbUtils.create(mActivity);

        }

        try
        {
            mDownloadInfoList = db.findAll(Selector.from(DownloadInfo.class));
        }catch(DbException e)
        {
            LogUtils.e(e.getMessage(), e);
        }
        if(mDownloadInfoList == null)
        {
            mDownloadInfoList = new ArrayList<DownloadInfo>();
        }

        if(downloadManager == null)
        {
            downloadManager = DownloadService.getDownloadManager(mActivity);
        }
    }

    private DbUtils db;

    private void initDataFromLocation()
    {
        mAdapter1 = new DownloadListAdapter();
        mListView.setAdapter(mAdapter1);
    }


    /**
     * 适配器
     */
    private com.youku.service.download.DownloadManager youkuDownloadManager;
    private DownloadListAdapter mDownloadListAdapter;
    private ArrayList<com.youku.service.download.DownloadInfo> downloadingList_show = new ArrayList<com.youku.service.download.DownloadInfo>();

    class DownloadListAdapter extends BaseAdapter
    {
        BitmapUtils bitmapUtils;

        public DownloadListAdapter()
        {

            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount()
        {

            if(downloadManager == null)
            {
                return 0;
            }
            if("all".equals(mCode))
            {

                return downloadManager.getDownloadInfoListCount()+downloadingList_show.size();
            }else if(mCode.equals("jiemu") || mCode.equals("shipin"))
            {
                return downloadingList_show.size();
            }else
            {
                List<DownloadInfo> list = downloadManager.getCodeDownloadList(mCode);
                return list.size();
            }


        }

        @Override
        public Object getItem(int position)
        {

            if("all".equals(mCode))
            {
                if(downloadingList_show.size() != 0)
                {
                    if(position<downloadingList_show.size())
                    {
                        return downloadingList_show.get(position);
                    }else
                    {
                        return downloadManager.getDownloadInfo(position-downloadingList_show.size());
                    }

                }else
                {
                    return downloadManager.getDownloadInfo(position);
                }
            }else if(mCode.equals("jiemu") || mCode.equals("shipin"))
            {
                return downloadingList_show.get(position);
            }else
            {
                List<DownloadInfo> list = downloadManager.getCodeDownloadList(mCode);
                return list.get(position);
            }

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
                    holder.tv_item_total = (TextView)convertView.findViewById(R.id.tv_item_total);
                    holder.tv_item_progress = (TextView)convertView.findViewById(R.id.tv_item_progress);
                    holder.tv_id = (TextView)convertView.findViewById(R.id.tv_id);
                    holder.underline_gray = convertView.findViewById(R.id.underline_gray);
                    holder.tv_item_music = (TextView)convertView.findViewById(R.id.tv_item_music);
                    holder.btn_item_music = (Button)convertView.findViewById(R.id.btn_item_music);
                }else
                {
                    convertView = View.inflate(mActivity, R.layout.item_music, null);
                    holder.iv_item_music = (ImageView)convertView.findViewById(R.id.iv_item_music);
                    holder.tv_path = (TextView)convertView.findViewById(R.id.tv_path);
                    holder.tv_code = (TextView)convertView.findViewById(R.id.tv_code);
                    holder.underline_gray = convertView.findViewById(R.id.underline_gray);
                    holder.tv_item_total = (TextView)convertView.findViewById(R.id.tv_item_total);
                    holder.tv_id = (TextView)convertView.findViewById(R.id.tv_id);
                    holder.tv_item_music = (TextView)convertView.findViewById(R.id.tv_item_music);
                    holder.tv_item_progress = (TextView)convertView.findViewById(R.id.tv_item_progress);
                    holder.btn_item_music = (Button)convertView.findViewById(R.id.btn_item_music);
                }
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            //隐藏下划线
            holder.underline_gray.setVisibility(View.INVISIBLE);
            if(mCode.equals("jiemu") || mCode.equals("shipin") || ( mCode
                    .equals("all") && position<downloadingList_show.size()
            ))
            {

                final com.youku.service.download.DownloadInfo info = (com.youku.service.download.DownloadInfo)getItem(
                        position);
                holder.tv_id.setText(info.videoid);
                holder.tv_path.setText("");
                holder.tv_item_music.setText(info.title);

                //显示视频文件大小
                long size = info.size;
                double progress = (double)size/1024/1024;
                String aa = progress+"";
                int i = aa.lastIndexOf(".");
                String file = aa.substring(0, i+2);
                holder.tv_item_total.setText(file+"M");

                //显示按钮文字
                String stateText = "";
                final com.youku.service.download.DownloadInfo youkuInfo = youkuDownloadManager
                        .getDownloadInfo(info.videoid);

                if(youkuInfo != null)
                {
                    String savePath = youkuInfo.savePath;
                    int index = savePath.lastIndexOf("videocache");
                    String path = savePath.substring(0, index+11);
                    bitmapUtils.display(holder.iv_item_music, path+youkuInfo.videoid+"/1.png");
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
                    Log.e("youkuInfoyoukuInfo", youkuInfo.savePath);
                }
                holder.btn_item_music.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        com.youku.service.download.DownloadInfo youkuInfo = youkuDownloadManager
                                .getDownloadInfo(info.videoid);
                        if(youkuInfo != null)
                        {
                            int state = youkuInfo.getState();
                            switch(state)
                            {
                                case com.youku.service.download.DownloadInfo.STATE_DOWNLOADING:
                                    youkuDownloadManager.pauseDownload(youkuInfo.taskId);
                                    holder.btn_item_music.setText("继续");
                                    break;
                                case com.youku.service.download.DownloadInfo.STATE_PAUSE:
                                    youkuDownloadManager.startDownload(youkuInfo.taskId);
                                    holder.btn_item_music.setText("下载中");
                                    Button bb = (Button)v;
                                    bb.setText("下载中");
                                    break;
                            }


                        }
                    }
                });


            }else
            {
                final DownloadInfo downloadInfo = (DownloadInfo)getItem(position);

                holder.tv_item_music.setText(downloadInfo.getFileName());
                //加载图片
                if(Boolean.valueOf(downloadInfo.getIsVideo()) == true)
                {
                    bitmapUtils.display(holder.iv_item_music, downloadInfo.getThumbnailPath());

                }else
                {
                    bitmapUtils.display(holder.iv_item_music, Constants.SERVER+downloadInfo.getThumbnailPath());
                }
                holder.tv_path.setText(downloadInfo.getFilePath());
                //设置id
                holder.tv_id.setText(downloadInfo.getResourceId());
                holder.tv_code.setText(downloadInfo.getCode());
                //处理下载显示问题
                long size = downloadInfo.getFileLength();
                double progress = (double)size/1024/1024;
                String aa = progress+"";
                int i = aa.lastIndexOf(".");
                String file = aa.substring(0, i+2);
                holder.tv_item_total.setText(file+"M");

                if(downloadInfo.getState() == HttpHandler.State.SUCCESS)
                {
                    holder.tv_item_progress.setVisibility(View.GONE);
                    holder.btn_item_music.setText("打开");
                }else if(downloadInfo.getState() == HttpHandler.State.FAILURE)
                {
                    holder.btn_item_music.setText("下载失败");
                }else if(downloadInfo.getState() == HttpHandler.State.WAITING)
                {
                    holder.btn_item_music.setText("等待中");
                }else if(downloadInfo.getState() == HttpHandler.State.LOADING)
                {
                    holder.btn_item_music.setText("下载中");
                    HttpHandler<File> handler = downloadInfo.getHandler();
                    if(handler != null)
                    {
                        RequestCallBack callBack = handler.getRequestCallBack();
                        if(callBack instanceof DownloadManager.ManagerCallBack)
                        {
                            DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack)callBack;
                            //
                            managerCallBack.setBaseCallBack(new RequestCallBack<File>()
                            {
                                @Override
                                public void onStart()
                                {
                                    super.onStart();
                                }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading)
                                {
                                    if(holder.tv_item_progress.getVisibility() == View.GONE)
                                    {
                                        holder.tv_item_progress.setVisibility(View.VISIBLE);
                                    }
                                    //设置进度显示
                                    double progress = (double)current/1024/1024;
                                    String aa = progress+"";
                                    int i = aa.lastIndexOf(".");
                                    String file = aa.substring(0, i+2);
                                    holder.tv_item_progress.setText(file+"M/");

                                    holder.btn_item_music.setText(current*100/total+"%");
                                }

                                @Override
                                public void onSuccess(ResponseInfo<File> responseInfo)
                                {
                                    // super.onSuccess(responseInfo);
                                    holder.btn_item_music.setText("打开");
                                    Toast.makeText(mActivity, "保存至"+"/com.cnst.wisdom/download/"+mCode+"/"+downloadInfo
                                                    .getResourceId()+getName(downloadInfo.getFilePath()),
                                            Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(HttpException e, String s)
                                {
                                    holder.btn_item_music.setText("下载失败");
                                }
                            });

                        }

                    }else
                    {
                        try
                        {
                            downloadManager.resumeDownload(downloadInfo, new RequestCallBack<File>()
                            {

                                @Override
                                public void onStart()
                                {
                                }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading)
                                {

                                    if(total != 0)
                                    {
                                        if(holder.tv_item_progress.getVisibility() == View.GONE)
                                        {
                                            holder.tv_item_progress.setVisibility(View.VISIBLE);
                                        }
                                        //设置进度显示
                                        double progress = (double)current/1024/1024;
                                        String aa = progress+"";
                                        int i = aa.lastIndexOf(".");
                                        String file = aa.substring(0, i+2);
                                        holder.tv_item_progress.setText(file+"M/");

                                        holder.btn_item_music.setText(current*100/total+"%");

                                    }

                                }

                                @Override
                                public void onSuccess(ResponseInfo<File> responseInfo)
                                {
                                    holder.btn_item_music.setText("下载完成");
                                    Toast.makeText(mActivity, "保存至"+"/com.cnst.wisdom/download/"+mCode+"/"+downloadInfo
                                                    .getResourceId()+getName(downloadInfo.getFilePath()),
                                            Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(HttpException error, String msg)
                                {
                                    Log.e("error!!!!!", error.toString()+"---------"+msg);
                                    if(msg.equals("maybe the file has downloaded completely"))
                                    {
                                        //已经下载
                                        Toast.makeText(mActivity, "已经完成", Toast.LENGTH_SHORT).show();
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
                }else if(downloadInfo.getState() == HttpHandler.State.CANCELLED)
                {
                    holder.btn_item_music.setText("继续");
                }
                //给按钮设置点击事件
                /**
                 * 点击下载文件
                 */
                holder.btn_item_music.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {

                        if(downloadInfo != null)
                        {
                            if(downloadInfo.getState() == HttpHandler.State.SUCCESS)
                            {
                                String filePath = new String(downloadInfo.getFilePath());
                                String fileName = filePath.substring(filePath.lastIndexOf("."));
                                if(mCode.equals("huiben") || mCode.equals("tupian"))
                                {
                                    if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp".equals
                                            (fileName)|| ".png"
                                            .equals(fileName))
                                    {
                                        Intent intent = new Intent(mActivity, PictureActivity.class);
                                        intent.putExtra("resourceId", downloadInfo.getResourceId());
                                        intent.putExtra("filePath", downloadInfo.getFilePath());
                                        intent.putExtra("pic_name", downloadInfo.getFileName());
                                        intent.putExtra("code", mCode);
                                        mActivity.startActivity(intent);
                                    }else
                                    {
                                        Toast.makeText(mActivity, "不是图片格式", Toast.LENGTH_SHORT).show();
                                    }
                                }else if(mCode.equals("jiaoan"))
                                {
                                    if(".ppt".equals(fileName) && "打开"
                                            .equals(holder.btn_item_music.getText().toString()))
                                    {

                                        Intent intent = new Intent("android.intent.action.VIEW");

                                        intent.addCategory("android.intent.category.DEFAULT");

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        intent.setDataAndType(Uri.parse(
                                                "file://"+getTargetPath(downloadInfo.getResourceId(),
                                                        downloadInfo.getFilePath())), "application/vnd.ms-powerpoint");
                                        mActivity.startActivity(intent);
                                    }else if(".docx".equals(fileName) && "打开"
                                            .equals(holder.btn_item_music.getText().toString()))
                                    {
                                        Intent intent = new Intent("android.intent.action.VIEW");

                                        intent.addCategory("android.intent.category.DEFAULT");

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        intent.setDataAndType(Uri.parse(
                                                "file://"+getTargetPath(downloadInfo.getResourceId(),
                                                        downloadInfo.getFilePath())), "application/msword");
                                        mActivity.startActivity(intent);
                                    }
                                }else if(mCode.equals("tushu"))
                                {
                                    if(".pdf".equals(fileName) && "打开"
                                            .equals(holder.btn_item_music.getText().toString()))
                                    {

                                        Intent intent = new Intent("android.intent.action.VIEW");
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setDataAndType(Uri.parse(
                                                "file://"+getTargetPath(downloadInfo.getResourceId(),
                                                        downloadInfo.getFilePath())), "application/pdf");
                                        mActivity.startActivity(intent);
                                    }
                                }else if(mCode.equals("yinyue"))
                                {

                                    if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma".equals(fileName))
                                    {
                                        Intent it = new Intent(Intent.ACTION_VIEW);
                                        //                                it.setDataAndType(Uri.parse(Constants.SERVER+item.filePath),
                                        //                                        "audio/MP3");
                                        it.setDataAndType(Uri.parse(
                                                "file://"+getTargetPath(downloadInfo.getResourceId(),
                                                        downloadInfo.getFilePath())), "audio/MP3");
                                        mActivity.startActivity(it);

                                    }else
                                    {
                                        Toast.makeText(mActivity, "不是音频格式", Toast.LENGTH_SHORT).show();

                                    }

                                }else
                                {
                                    if("打开".equals(holder.btn_item_music.getText().toString()) || "下载完成"
                                            .equals(holder.btn_item_music.getText().toString()))
                                    {
                                        if(".mp3".equals(fileName) || ".wav".equals(fileName) || ".wma"
                                                .equals(fileName))
                                        {
                                            Intent it = new Intent(Intent.ACTION_VIEW);
                                            //                                it.setDataAndType(Uri.parse(Constants.SERVER+item.filePath),
                                            //
                                            //                                "audio/MP3");
                                            it.setDataAndType(Uri.parse("file://"+downloadInfo.getFileSavePath()),
                                                    "audio/MP3");
                                            mActivity.startActivity(it);
                                        }else if(".jpg".equals(fileName) || ".tiff".equals(fileName) || ".bmp"
                                                .equals(fileName)|| ".png"
                                                .equals(fileName))
                                        {
                                            Intent intent = new Intent(mActivity, PictureActivity.class);
                                            intent.putExtra("resourceId", downloadInfo.getResourceId());
                                            intent.putExtra("filePath", downloadInfo.getFilePath());
                                            intent.putExtra("pic_name", downloadInfo.getFileName());
                                            intent.putExtra("code", mCode);
                                            mActivity.startActivity(intent);
                                        }else if(".pdf".equals(fileName))
                                        {
                                            Intent intent = new Intent("android.intent.action.VIEW");
                                            intent.addCategory("android.intent.category.DEFAULT");
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.setDataAndType(Uri.parse("file://"+downloadInfo.getFileSavePath()),
                                                    "application/pdf");
                                            mActivity.startActivity(intent);
                                        }else if(".ppt".equals(fileName))
                                        {
                                            Intent intent = new Intent("android.intent.action.VIEW");

                                            intent.addCategory("android.intent.category.DEFAULT");

                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                            intent.setDataAndType(Uri.parse("file://"+downloadInfo.getFileSavePath()),
                                                    "application/vnd.ms-powerpoint");
                                            mActivity.startActivity(intent);
                                        }else if(".docx".equals(fileName))
                                        {
                                            Intent intent = new Intent("android.intent.action.VIEW");

                                            intent.addCategory("android.intent.category.DEFAULT");

                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                            intent.setDataAndType(Uri.parse("file://"+downloadInfo.getFileSavePath()),
                                                    "application/msword");
                                            mActivity.startActivity(intent);
                                        }else
                                        {
                                            Toast.makeText(mActivity, "不是支持格式", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }else if(downloadInfo.getState() == HttpHandler.State.LOADING)
                            {//如果是正在下载点击应该暂停
                                HttpHandler handler = downloadInfo.getHandler();
                                if(handler != null)
                                {
                                    handler.cancel();
                                    holder.btn_item_music.setText("继续");

                                }
                            }else if(downloadInfo.getState() == HttpHandler.State.CANCELLED)
                            {
                                //如果是暂停点击应该继续
                                resumeDownload(downloadInfo, holder);

                            }

                        }
                        //                    else
                        //                    {
                        //                        download(item, holder.btn_item_music, holder.tv_item_progress, holder.tv_item_speed);
                        //                    }
                    }

                });
            }
            return convertView;
        }
    }

    public static class ViewHolder
    {
        public ImageView iv_item_music;
        public TextView tv_item_music;
        public TextView tv_id;
        public TextView tv_code;
        public TextView tv_path;
        public TextView tv_item_progress;
        public View underline_gray;
        public TextView tv_item_total;
        public Button btn_item_music;
    }

    /**
     * 继续下载功能
     *
     * @param info
     * @param holder
     */
    private void resumeDownload(final DownloadInfo info, final ViewHolder holder)
    {
        try
        {
            downloadManager.resumeDownload(info, new RequestCallBack<File>(1000)
            {

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
                    double progress = (double)current/1024/1024;
                    String aa = progress+"";
                    int i = aa.lastIndexOf(".");
                    String file = aa.substring(0, i+2);
                    holder.tv_item_progress.setText(file+"M/");
                    holder.btn_item_music.setText(String.valueOf(current*100/total)+"%");

                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo)
                {
                    holder.btn_item_music.setText("下载完成");
                    Toast.makeText(mActivity,
                            "保存至"+"/com.cnst.wisdom/download/"+mCode+"/"+info.getResourceId()+getName(
                                    info.getFilePath()), Toast.LENGTH_LONG).show();

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
                        //                        showNoFoundDailog();
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


}
