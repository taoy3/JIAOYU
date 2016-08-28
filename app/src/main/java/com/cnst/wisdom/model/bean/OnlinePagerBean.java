package com.cnst.wisdom.model.bean;

import java.util.ArrayList;

/**
 * 资源素材页面对象
 * <功能详细描述>
 *
 * @author pengjingang
 * @see [相关类/方法]
 * @since [产品/模板版本]
 *
 */
public class OnlinePagerBean
{
    /**
     * 字段释义：
     id   素材id
     isVideo  是不是视频  true 是   false 不是
     thumbnailPath  文件缩略图地址
     fileSize  文件大小  单位KB
     title  素材标题
     videoId  视频对应的优酷id
     pageInfo  分页信息
     pageCount  总页数
     rowCount  总记录数
     */
    private  int code;
    private ArrayList<OnlinePager> data;

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setData(ArrayList<OnlinePager> data)
    {
        this.data = data;
    }

    public void setPageInfo(PageInfo pageInfo)
    {
        this.pageInfo = pageInfo;
    }

    public int getCode()
    {
        return code;
    }

    public ArrayList<OnlinePager> getData()
    {
        return data;
    }

    public PageInfo getPageInfo()
    {
        return pageInfo;
    }

    public class OnlinePager{
        private String resourceId;
        private String isVideo;
        private String thumbnailPath;
        private String fileSize;
        private String title;
        private String videoId;

        @Override
        public String toString()
        {
            return "OnlinePager{"+
                    "id='"+resourceId+'\''+
                    ", isVideo='"+isVideo+'\''+
                    ", thumbnailPath='"+thumbnailPath+'\''+
                    ", fileSize='"+fileSize+'\''+
                    ", title='"+title+'\''+
                    ", videoId='"+videoId+'\''+
                    '}';
        }

        public void setResourceId(String resourceId)
        {
            this.resourceId = resourceId;
        }

        public void setIsVideo(String isVideo)
        {
            this.isVideo = isVideo;
        }

        public void setThumbnailPath(String thumbnailPath)
        {
            this.thumbnailPath = thumbnailPath;
        }

        public void setFileSize(String fileSize)
        {
            this.fileSize = fileSize;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public void setVideoId(String videoId)
        {
            this.videoId = videoId;
        }

        public String getResourceId()
        {
            return resourceId;
        }

        public String getIsVideo()
        {
            return isVideo;
        }

        public String getThumbnailPath()
        {
            return thumbnailPath;
        }

        public String getFileSize()
        {
            return fileSize;
        }

        public String getVideoId()
        {
            return videoId;
        }

        public String getTitle()
        {
            return title;
        }
    }
    private PageInfo pageInfo;
    public class PageInfo{
        private String pageCount;
        private int rowCount;

        public void setPageCount(String pageCount)
        {
            this.pageCount = pageCount;
        }

        public void setRowCount(int rowCount)
        {
            this.rowCount = rowCount;
        }

        public String getPageCount()
        {
            return pageCount;
        }

        public int getRowCount()
        {
            return rowCount;
        }
    }

    @Override
    public String toString()
    {
        return "OnlinePagerBean{"+
                "code="+code+
                ", data="+data+
                ", pageInfo="+pageInfo+
                '}';
    }
}
