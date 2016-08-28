package com.cnst.wisdom.model.bean;

import java.io.Serializable;

/**
 * 用于图片轮播
 * 记录每张图片的地址，序号
 */
public class FlipImage implements Serializable

{
    private int order;
    private String url;

    public FlipImage(int order, String url)
    {
        this.order = order;
        this.url = url;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "FlipImage{"+
                "order="+order+
                ", url='"+url+'\''+
                '}';
    }
}
