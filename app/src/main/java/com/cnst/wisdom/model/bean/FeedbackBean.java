package com.cnst.wisdom.model.bean;

import java.util.List;

/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author hanshuai
 * @see [相关类/方法]
 * @since [产品/模板版本]
 */
public class FeedbackBean
{

    /**
     * code : 200
     * data : {"headImg":"/upload/headImg/40288bd75317ac86015317c3b2a300011456993300381.png","needList":[{"content":"???","createTime":"2016-03-04 14:26:17","id":"ff808181533f9b290153404e29180005","isAnonymous":0},{"content":"????","createTime":"2016-03-04 14:09:29","id":"ff808181533f9b290153403ec7ac0004","isAnonymous":1},{"content":"?","createTime":"2016-03-03 14:26:19","id":"ff8081815336ab2501533b27d55e0040","isAnonymous":0},{"content":"yy","createTime":"2016-02-26 14:29:30","id":"ff80818153174fbf01531c4498ac0018","isAnonymous":0}],"teachName":"汪"}
     * msg : 查询正常
     */

    private String code;
    private DataEntity data;
    private String msg;

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setData(DataEntity data)
    {
        this.data = data;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getCode()
    {
        return code;
    }

    public DataEntity getData()
    {
        return data;
    }

    public String getMsg()
    {
        return msg;
    }

    public static class DataEntity
    {
        /**
         * headImg : /upload/headImg/40288bd75317ac86015317c3b2a300011456993300381.png
         * needList : [{"content":"???","createTime":"2016-03-04 14:26:17","id":"ff808181533f9b290153404e29180005","isAnonymous":0},{"content":"????","createTime":"2016-03-04 14:09:29","id":"ff808181533f9b290153403ec7ac0004","isAnonymous":1},{"content":"?","createTime":"2016-03-03 14:26:19","id":"ff8081815336ab2501533b27d55e0040","isAnonymous":0},{"content":"yy","createTime":"2016-02-26 14:29:30","id":"ff80818153174fbf01531c4498ac0018","isAnonymous":0}]
         * teachName : 汪
         */

        private String headImg;
        private String teachName;
        private List<NeedListEntity> needList;

        public void setHeadImg(String headImg)
        {
            this.headImg = headImg;
        }

        public void setTeachName(String teachName)
        {
            this.teachName = teachName;
        }

        public void setNeedList(List<NeedListEntity> needList)
        {
            this.needList = needList;
        }

        public String getHeadImg()
        {
            return headImg;
        }

        public String getTeachName()
        {
            return teachName;
        }

        public List<NeedListEntity> getNeedList()
        {
            return needList;
        }

        public static class NeedListEntity
        {
            /**
             * content : ???
             * createTime : 2016-03-04 14:26:17
             * id : ff808181533f9b290153404e29180005
             * isAnonymous : 0
             */

            private String content;
            private String createTime;
            private String id;
            private int isAnonymous;

            public void setContent(String content)
            {
                this.content = content;
            }

            public void setCreateTime(String createTime)
            {
                this.createTime = createTime;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public void setIsAnonymous(int isAnonymous)
            {
                this.isAnonymous = isAnonymous;
            }

            public String getContent()
            {
                return content;
            }

            public String getCreateTime()
            {
                return createTime;
            }

            public String getId()
            {
                return id;
            }

            public int getIsAnonymous()
            {
                return isAnonymous;
            }
        }
    }
}
