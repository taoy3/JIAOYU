package com.cnst.wisdom.download;


import com.lidroid.xutils.http.HttpHandler;

import java.io.File;

import cn.com.iresearch.mvideotracker.db.annotation.sqlite.Transient;

/**
 * @author tangbinfeng.
 * @date 2016/2/23
 * @des 下载对象
 * @since [产品/模版版本]
 */

public class DownloadInfo
{

    public DownloadInfo()
    {
    }

    private String id;

    @Transient private HttpHandler<File> handler;
    private String code;
    private String thumbnailPath;
    private String isVideo;

    private HttpHandler.State state;

    private String downloadUrl;
    private String resourceId;
    private String fileName;
    private String filePath;
    private String fileSavePath;

    private long progress;

    private long fileLength;

    private boolean autoResume;

    private boolean autoRename;

    public String getIsVideo()
    {
        return isVideo;
    }

    public void setIsVideo(String isVideo)
    {
        this.isVideo = isVideo;
    }

    public String getThumbnailPath()
    {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath)
    {
        this.thumbnailPath = thumbnailPath;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public HttpHandler<File> getHandler()
    {
        return handler;
    }

    public void setHandler(HttpHandler<File> handler)
    {
        this.handler = handler;
    }

    public HttpHandler.State getState()
    {
        return state;
    }

    public void setState(HttpHandler.State state)
    {
        this.state = state;
    }

    public String getDownloadUrl()
    {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileSavePath()
    {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath)
    {
        this.fileSavePath = fileSavePath;
    }

    public long getProgress()
    {
        return progress;
    }

    public void setProgress(long progress)
    {
        this.progress = progress;
    }

    public long getFileLength()
    {
        return fileLength;
    }

    public void setFileLength(long fileLength)
    {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume()
    {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume)
    {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename()
    {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename)
    {
        this.autoRename = autoRename;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!( o instanceof DownloadInfo ))
        {
            return false;
        }

        DownloadInfo that = (DownloadInfo)o;

        if(id != that.id)
        {
            return false;
        }

        return true;
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    //    @Override
    //    public int hashCode()
    //    {
    //        return (int)( id^( id>>>32 ) );
    //    }
}

