package com.cnst.wisdom.model.bean;

/**
 * Created by Jonas on 2015/11/22.
 */
public class Recommend {
//    public String title;
//    public bdmeizhi meizhi;
//
//    public Recommend(String title, bdmeizhi meizhi){
//        this.title = title;
//        this.meizhi = meizhi;
//    }

//    add by yhw
    public String title;
    public StudentBean attendanceData;
    public Recommend(String title,StudentBean attendanceData){
        this.title = title;
        this.attendanceData = attendanceData;
    }
}
