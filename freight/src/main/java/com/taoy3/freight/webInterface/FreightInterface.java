package com.taoy3.freight.webInterface;

import com.taoy3.freight.bean.Port;
import com.taoy3.freight.bean.PriceBean;
import com.taoy3.freight.db.FreightDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy3 on 16/8/27.
 */
public class FreightInterface {
    public static List<PriceBean> create(Port start, Port dest) {
        List<PriceBean> freights  = new ArrayList<>();
        for (int i = 0; i < (start.getId()+dest.getId())%30; i++) {
            freights.add(new PriceBean(start,dest,i));
        }
        FreightDB.getInstance().query(start.getId(),dest.getId());
        return freights;
    }
}
