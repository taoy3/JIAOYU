package com.cnst.wisdom.model.bean;

import java.util.ArrayList;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author meilianbing.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class RelativeResourceBean
{

    /**
     * 请求返回码
     */
    private int code;

    /**
     * 网络请求返回信息
     */
    private String msg;

    /**
     * 网络请求数据
     */
    private RelativeResource data;

    public int getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }

    public RelativeResource getData()
    {
        return data;
    }

    public class RelativeResource
    {
        private String className;
        private String filePath;
        private String introduction;
        private String subject;
        private String term;
        private String title;

        private ArrayList<ResourceDetail> otherList;

        public String getClassName()
        {
            return className;
        }

        public String getFilePath()
        {
            return filePath;
        }

        public String getIntroduction()
        {
            return introduction;
        }

        public String getSubject()
        {
            return subject;
        }

        public String getTerm()
        {
            return term;
        }

        public String getTitle()
        {
            return title;
        }

        public ArrayList<ResourceDetail> getOtherList()
        {
            return otherList;
        }

        @Override
        public String toString()
        {
            return "RelativeResource{"+
                    "title='"+title+'\''+
                    ", otherList="+otherList+
                    '}';
        }

        public class ResourceDetail
        {
            private String className;
            private String introduction;
            private String resourceId;
            private String subject;
            private String term;
            private String week;
            private String classification;
            private String thumbnailPath;
            private String title;
            private String videoId;

            public String getWeek()
            {
                return week;
            }

            public String getClassification()
            {
                return classification;
            }

            public String getClassName()
            {
                return className;
            }

            public String getIntroduction()
            {
                return introduction;
            }

            public String getResourceId()
            {
                return resourceId;
            }

            public String getSubject()
            {
                return subject;
            }

            public String getTerm()
            {
                return term;
            }

            public String getThumbnailPath()
            {
                return thumbnailPath;
            }

            public String getTitle()
            {
                return title;
            }

            public String getVideoId()
            {
                return videoId;
            }

            @Override
            public String toString()
            {
                return "ResourceDetail{"+
                        "thumbnailPath='"+thumbnailPath+'\''+
                        ", title='"+title+'\''+
                        '}';
            }
        }

    }

    @Override
    public String toString()
    {
        return "RelativeResourceBean{"+
                "data="+data+
                '}';
    }
}
