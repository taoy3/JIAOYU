package com.cnst.wisdom.model.bean;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author pengjingnag.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */

import java.util.ArrayList;

/**
 * 资源分类列表对象
 * <功能详细描述>
 *
 * @author tangbinfeng
 * @see [相关类/方法]
 * @since [产品/模板版本]
 */
public class OnlineBean
{
    private int code;
    private String msg;
    private ArrayList<OnlineData> data;

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setData(ArrayList<OnlineData> data)
    {
        this.data = data;
    }

    public int getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }

    public ArrayList<OnlineData> getData()
    {
        return data;
    }

    public class OnlineData
    {
        private String code;
        private String id;
        private String name;

        public void setCode(String code)
        {
            this.code = code;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getCode()
        {
            return code;
        }

        public String getId()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public String toString()
        {
            return "OnlineData{"+
                    "code='"+code+'\''+
                    ", id='"+id+'\''+
                    ", name='"+name+'\''+
                    '}';
        }
    }

    @Override
    public String toString()
    {
        return "OnlineBean{"+
                "code="+code+
                ", msg='"+msg+'\''+
                ", data="+data+
                '}';
    }
}

