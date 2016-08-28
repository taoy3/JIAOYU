package com.taoy3.freight.util;

import android.util.Log;

import com.taoy3.freight.bean.Company;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.db.CompanyDB;
import com.taoy3.freight.db.PortDB;
import com.taoy3.freight.netBean.GeoUtil;

import java.util.List;

/**
 * Created by taoy3 on 16/8/25.
 */
public class DataUtils {
    private static String TAG = "DateUtils";

    public static void initData() {
        initCompany();
        initPort();
    }

    private static void initCompany() {
        if(CompanyDB.getInstance().queryAll().size()==0){
            List<Company> shipCompany = new GeoUtil().deserializerArray(
                    FileUtils.getStrFormAssets(Config.SHIP_COMPANY_JSON), Company[].class);
            Log.i(TAG,"insert company");
            CompanyDB.getInstance().insert(shipCompany);
            Log.i(TAG, shipCompany.size()+"");
        }
    }

    private static void initPort() {
        if(PortDB.getInstance().queryAll().size()==0){
            List<Port> ports = new GeoUtil().deserializerArray(
                    FileUtils.getStrFormAssets(Config.PORT_JSON), Port[].class);
            Log.i(TAG,"insert ports");
            PortDB.getInstance().insert(ports);
            Log.i(TAG, ports.size()+"and"+ PortDB.getInstance().queryAll().size());

        }
    }
}
