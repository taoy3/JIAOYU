package com.taoy3.freight.webInterface;

import com.taoy3.freight.bean.Company;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.bean.Voyage;
import com.taoy3.freight.db.VoyageDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy3 on 16/8/26.
 */
public class VoyageInterface {
    public static List<Voyage> create(Port startPort, Port destPort, Company company) {
        List<Voyage> voyages = new ArrayList<>();
        for (int i = 0; i < (company.getId()+100)%100+1; i++) {
            voyages.add(new Voyage(startPort,destPort,company,i));
        }
        VoyageDB.getInstance().insert(voyages);
        return voyages;
    }
}
