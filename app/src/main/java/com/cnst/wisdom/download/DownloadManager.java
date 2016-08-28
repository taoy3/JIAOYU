package com.cnst.wisdom.download;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.cnst.wisdom.utills.MD5Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangbinfeng.
 * @date 2016/2/23
 * @des 下载管理器
 * @since [产品/模版版本]
 */

public class DownloadManager
{

    private ArrayList<DownloadInfo> downloadInfoList;

    private int maxDownloadThread = 3;

    private Context mContext;
    private DbUtils db;

    /*package*/ DownloadManager(Context appContext)
    {
        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class, new HttpHandlerStateConverter());
        mContext = appContext;
        db = DbUtils.create(mContext);
        try
        {
            downloadInfoList = (ArrayList)db.findAll(Selector.from(DownloadInfo.class));
        }catch(DbException e)
        {
            LogUtils.e(e.getMessage(), e);
        }
        if(downloadInfoList == null)
        {
            downloadInfoList = new ArrayList<DownloadInfo>();
        }
    }

    public int getDownloadInfoListCount()
    {
        return downloadInfoList.size();
    }

    /**
     * 获取download
     *
     * @param id
     * @return
     */

    public DownloadInfo getDownloadInfo(String id)
    {
        for(DownloadInfo info : downloadInfoList)
        {

            String infoId = info.getId();
            if(infoId.equals(id))
            {
                return info;
            }
        }
        return null;
    }

    /**
     * 根据分类获取集合
     *
     * @param code
     * @return
     */

    public List<DownloadInfo> getCodeDownloadList(String code)
    {
        ArrayList<DownloadInfo> CodeDownloadList = new ArrayList<DownloadInfo>();
        for(DownloadInfo info : downloadInfoList)
        {
            String infoCode = info.getCode();
            if(infoCode.equals(code))
            {
                CodeDownloadList.add(info);
            }
        }
        return CodeDownloadList;
    }

    public DownloadInfo getDownloadInfo(int index)
    {
        return downloadInfoList.get(index);
    }

    /**
     * 把void改成DownloadInfo
     *
     * @param url
     * @param fileName
     * @param target
     * @param autoResume
     * @param autoRename
     * @param callback
     * @return
     *
     * @throws DbException
     */

    public void addNewDownload(String url, String fileName, String target, String code, String thumbnailPath, String resourceId, String filePath, boolean autoResume, boolean autoRename, String isVideo, final RequestCallBack<File> callback) throws DbException
    {

        final DownloadInfo downloadInfo = new DownloadInfo();
        /**
         * 测试-------------------------------------------------------------------------------------------------
         */

        DownloadInfo downloadInfo1 = db.findById(DownloadInfo.class, MD5Utils.Md5(url));
        if(downloadInfo1 != null)
        {
            Toast.makeText(mContext, "已添加下载到下载管理", Toast.LENGTH_SHORT).show();
            return;
        }

        downloadInfo.setId(MD5Utils.Md5(url));
        downloadInfo.setCode(code);
        downloadInfo.setThumbnailPath(thumbnailPath);
        downloadInfo.setIsVideo(isVideo);
        downloadInfo.setResourceId(resourceId);
        downloadInfo.setFilePath(filePath);

        /**
         * 测试-------------------------------------------------------------------------------------------------
         */

        downloadInfo.setDownloadUrl(url);
        downloadInfo.setAutoRename(autoRename);
        downloadInfo.setAutoResume(autoResume);
        downloadInfo.setFileName(fileName);
        downloadInfo.setFileSavePath(target);
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http
                .download(url, target, autoResume, autoRename, new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        downloadInfoList.add(0, downloadInfo);
        db.saveBindingId(downloadInfo);

    }

    public void resumeDownload(int index, final RequestCallBack<File> callback) throws DbException
    {
        final DownloadInfo downloadInfo = downloadInfoList.get(index);
        resumeDownload(downloadInfo, callback);
    }

    public void resumeDownload(DownloadInfo downloadInfo, final RequestCallBack<File> callback) throws DbException
    {
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http
                .download(downloadInfo.getDownloadUrl(), downloadInfo.getFileSavePath(), downloadInfo.isAutoResume(),
                        downloadInfo.isAutoRename(), new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        db.saveOrUpdate(downloadInfo);
    }

    public void removeDownload(int index) throws DbException
    {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        removeDownload(downloadInfo);
    }

    public void removeDownload(DownloadInfo downloadInfo) throws DbException
    {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if(handler != null && !handler.isCancelled())
        {
            handler.cancel();
        }
        downloadInfoList.remove(downloadInfo);
        db.delete(downloadInfo);
    }

    public void stopDownload(int index) throws DbException
    {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
    }

    public void stopDownload(DownloadInfo downloadInfo) throws DbException
    {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if(handler != null && !handler.isCancelled())
        {
            handler.cancel();
        }else
        {
            downloadInfo.setState(HttpHandler.State.CANCELLED);
        }
        db.saveOrUpdate(downloadInfo);
    }

    public void stopAllDownload() throws DbException
    {
        for(DownloadInfo downloadInfo : downloadInfoList)
        {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if(handler != null && !handler.isCancelled())
            {
                handler.cancel();
            }else
            {
                downloadInfo.setState(HttpHandler.State.CANCELLED);
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    public void backupDownloadInfoList() throws DbException
    {
        for(DownloadInfo downloadInfo : downloadInfoList)
        {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if(handler != null)
            {
                downloadInfo.setState(handler.getState());
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    public int getMaxDownloadThread()
    {
        return maxDownloadThread;
    }

    public void setMaxDownloadThread(int maxDownloadThread)
    {
        this.maxDownloadThread = maxDownloadThread;
    }

    public class ManagerCallBack extends RequestCallBack<File>
    {
        private DownloadInfo downloadInfo;
        private RequestCallBack<File> baseCallBack;

        public RequestCallBack<File> getBaseCallBack()
        {
            return baseCallBack;
        }

        public void setBaseCallBack(RequestCallBack<File> baseCallBack)
        {
            this.baseCallBack = baseCallBack;
        }

        private ManagerCallBack(DownloadInfo downloadInfo, RequestCallBack<File> baseCallBack)
        {
            this.baseCallBack = baseCallBack;
            this.downloadInfo = downloadInfo;
        }

        @Override
        public Object getUserTag()
        {
            if(baseCallBack == null)
            {
                return null;
            }
            return baseCallBack.getUserTag();
        }

        @Override
        public void setUserTag(Object userTag)
        {
            if(baseCallBack == null)
            {
                return;
            }
            baseCallBack.setUserTag(userTag);
        }

        @Override
        public void onStart()
        {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if(handler != null)
            {
                downloadInfo.setState(handler.getState());
            }
            try
            {
                db.saveOrUpdate(downloadInfo);
            }catch(DbException e)
            {
                LogUtils.e(e.getMessage(), e);
            }
            if(baseCallBack != null)
            {
                baseCallBack.onStart();
            }
        }

        @Override
        public void onCancelled()
        {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if(handler != null)
            {
                downloadInfo.setState(handler.getState());
            }
            try
            {
                db.saveOrUpdate(downloadInfo);
            }catch(DbException e)
            {
                LogUtils.e(e.getMessage(), e);
            }
            if(baseCallBack != null)
            {
                baseCallBack.onCancelled();
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading)
        {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if(handler != null)
            {
                downloadInfo.setState(handler.getState());
            }
            downloadInfo.setFileLength(total);
            downloadInfo.setProgress(current);
            try
            {
                db.saveOrUpdate(downloadInfo);
            }catch(DbException e)
            {
                LogUtils.e(e.getMessage(), e);
            }
            if(baseCallBack != null)
            {
                baseCallBack.onLoading(total, current, isUploading);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo)
        {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if(handler != null)
            {
                downloadInfo.setState(handler.getState());
            }
            try
            {
                db.saveOrUpdate(downloadInfo);
            }catch(DbException e)
            {
                LogUtils.e(e.getMessage(), e);
            }
            if(baseCallBack != null)
            {
                baseCallBack.onSuccess(responseInfo);
            }
        }

        @Override
        public void onFailure(HttpException error, String msg)
        {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if(handler != null)
            {
                downloadInfo.setState(handler.getState());
            }
            try
            {
                db.saveOrUpdate(downloadInfo);
            }catch(DbException e)
            {
                LogUtils.e(e.getMessage(), e);
            }
            if(baseCallBack != null)
            {
                baseCallBack.onFailure(error, msg);
            }
        }
    }

    private class HttpHandlerStateConverter implements ColumnConverter<HttpHandler.State>
    {

        @Override
        public HttpHandler.State getFieldValue(Cursor cursor, int index)
        {
            return HttpHandler.State.valueOf(cursor.getInt(index));
        }

        @Override
        public HttpHandler.State getFieldValue(String fieldStringValue)
        {
            if(fieldStringValue == null)
            {
                return null;
            }
            return HttpHandler.State.valueOf(fieldStringValue);
        }

        @Override
        public Object fieldValue2ColumnValue(HttpHandler.State fieldValue)
        {
            return fieldValue.value();
        }

        @Override
        public ColumnDbType getColumnDbType()
        {
            return ColumnDbType.INTEGER;
        }
    }
}
