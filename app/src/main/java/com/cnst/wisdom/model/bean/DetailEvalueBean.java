package com.cnst.wisdom.model.bean;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author pengjingnag.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class DetailEvalueBean
{

    private String msg;
    private int code;
    private Data data;

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setData(Data data)
    {
        this.data = data;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public Data getData()
    {
        return data;
    }

    public int getCode()
    {
        return code;
    }

    public class Data
    {

        private String videoId;
        private String title;
        private String introduction;
        private List<EvaluteList> evaluteList;

        public void setVideoId(String videoId)
        {
            this.videoId = videoId;
        }

        public void setEvaluteList(List<EvaluteList> evaluteList)
        {
            this.evaluteList = evaluteList;
        }

        public void setIntroduction(String introduction)
        {
            this.introduction = introduction;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getVideoId()
        {
            return videoId;
        }

        public String getTitle()
        {
            return title;
        }

        public String getIntroduction()
        {
            return introduction;
        }

        public List<EvaluteList> getEvaluteList()
        {
            return evaluteList;
        }

        public class EvaluteList
        {

            private String teacherName;
            private String createTime;
            private String id;
            private String content;
            private String headImgUrl;

            @Override
            public String toString()
            {
                return "EvaluteList{"+
                        "teacherName='"+teacherName+'\''+
                        ", createTime='"+createTime+'\''+
                        ", id='"+id+'\''+
                        ", content='"+content+'\''+
                        ", headImgUrl='"+headImgUrl+'\''+
                        '}';
            }

            public String getHeadImgUrl()
            {
                return headImgUrl;
            }

            public void setHeadImgUrl(String headImgUrl)
            {
                this.headImgUrl = headImgUrl;
            }

            public void setTeacherName(String teacherName)
            {
                this.teacherName = teacherName;
            }

            public void setCreateTime(String createTime)
            {
                this.createTime = createTime;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public void setContent(String content)
            {
                this.content = content;
            }

            public String getTeacherName()
            {
                return teacherName;
            }

            public String getCreateTime()
            {
                return createTime;
            }

            public String getId()
            {
                return id;
            }

            public String getContent()
            {
                return content;
            }
        }

        @Override
        public String toString()
        {
            return "Data{"+
                    "videoId='"+videoId+'\''+
                    ", title='"+title+'\''+
                    ", introduction='"+introduction+'\''+
                    ", evaluteList='"+evaluteList+'\''+
                    '}';

        }
    }

    @Override
    public String toString()
    {

        return "videoId{"+
                "msg='"+msg+'\''+
                ", code='"+code+'\''+
                ", data='"+data+'\''+
                '}';

    }
}