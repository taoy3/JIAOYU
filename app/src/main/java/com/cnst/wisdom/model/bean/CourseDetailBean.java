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
public class CourseDetailBean {

    private int code;
    private String msg;
    private ArrayList<CourseDetail> data;
    private PageInfo pageInfo;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<CourseDetail> getData() {
        return data;
    }

    private class PageInfo{
        public int pageCount;
        public int rowCount;
    }

}
