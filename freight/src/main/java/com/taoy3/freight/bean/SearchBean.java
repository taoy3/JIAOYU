package com.taoy3.freight.bean;

import android.os.SystemClock;

import com.lidroid.xutils.db.annotation.Id;
import com.taoy3.freight.util.GetFirstLetter;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-11-23.
 */
public class SearchBean implements Serializable,Comparable<SearchBean>{
    @Id
    protected String id = String.valueOf(SystemClock.currentThreadTimeMillis());
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(SearchBean another) {
        if(another!=null&&this.getId()!=null&&this.getId().length()>0&&
        another.getId()!=null&&another.getId().length()>0){
            return GetFirstLetter.getFirstLetter(this.getId()).toUpperCase().charAt(0)-
                    GetFirstLetter.getFirstLetter(another.getId()).toUpperCase().charAt(0);
        }
        return 0;
    }
}
