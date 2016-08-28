package com.taoy3.freight.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.BaseApp;
import com.taoy3.freight.bean.Company;
import com.taoy3.freight.bean.CompanyTemp;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.bean.PortTemp;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.constant.UrlConstant;
import com.taoy3.freight.netBean.GeoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by taoy2 on 15-12-18.
 */
public class BaseDataUtils {
    public static DbUtils db;
    public static void initData(Context context) {
        String pathStr = context.getCacheDir() + Config.SQL_ROOT + "/";
        String dbName = Config.USER_DATA;
        File jhPath = new File(pathStr + dbName);
        //查看数据库文件是否存在
        if (jhPath.exists()) {
            //存在则读取到内存中
            initCacheData(context,dbName);
            return;
        } else {
            //不存在先创建文件夹
            File path = new File(pathStr);
            if (!path.mkdir()) {
                path.mkdirs();
            }
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到数据库的输入流
                InputStream is = am.open(dbName);
                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
                initData(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 1.数据库数据存在，将其读取到内存中
     * 2.数据库数据不存在，assets目录中存在，先将其读取到数据库，将其读取到内存中
     * 3.数据库数据不存在，assets目录中也不存在，先从网络下载，再将其读取到数据库，最后读取到内存中
     */
    private static void initCacheData(final Context context,String dbName) {
        DbUtils.DaoConfig config = new DbUtils.DaoConfig(context);
        config.setDbDir(context.getCacheDir() + Config.SQL_ROOT);
        config.setDbName(dbName); //db名
        config.setDbVersion(Config.DBVER);  //db版本与系统版本相同
        db = DbUtils.create(config);
        try {
            List<PortTemp> tempPort = db.findAll(PortTemp.class);
            Map<String,PortTemp> portMap = new HashMap<>();
            for (int i = 0; i < tempPort.size(); i++) {
                portMap.put(tempPort.get(i).getCode(),tempPort.get(i));
            }
            tempPort.clear();
            Iterator<Map.Entry<String, PortTemp>> portIterator = portMap.entrySet().iterator();
            while (portIterator.hasNext()){
                Map.Entry<String, PortTemp> next = portIterator.next();
                next.getValue().setId(tempPort.size()+1);
                tempPort.add(next.getValue());
            }
            FileUtils.writeStr(tempPort.toString(),Config.PORT_JSON);
//                    CacheDataConstant.ports.addAll(tempPort);
            BaseApp.portDataIsOk = true;
            Messaging.post(Messaging.PORTEVENT, Messaging.OK);
            List<CompanyTemp> tempCompany = db.findAll(CompanyTemp.class);
            Map<String,CompanyTemp> companyMap = new HashMap<>();
            for (int i = 0; i < tempCompany.size(); i++) {
                companyMap.put(tempCompany.get(i).getCode(),tempCompany.get(i));
            }
            tempCompany.clear();
            Iterator<Map.Entry<String, CompanyTemp>> iterator = companyMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, CompanyTemp> next = iterator.next();
                next.getValue().setId(tempCompany.size()+1);
                tempCompany.add(next.getValue());
            }
            FileUtils.writeStr(tempCompany.toString(),Config.SHIP_COMPANY_JSON);
//                    CacheDataConstant.companies.addAll(tempCompany);
            BaseApp.companyDataIsOk = true;
            Messaging.post(Messaging.SHIPCOMPANY, Messaging.OK);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public static void getPorts(final Context context) {
        AppHttpUtils.getUtils(UrlConstant.getPortUrl(5294), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what / 100 == 2) {
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String json = object.getString("result");
                        List<Port> temp = new GeoUtil().deserializer(json, new TypeToken<List<Port>>() {
                        }.getType());
                        if (temp != null && temp.size() > 0) {
                            try {
                                db.saveOrUpdateAll(temp);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void getScs(final Context context) {
        AppHttpUtils.getUtils(UrlConstant.getShipCompanyUrl(62), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what / 100 == 2) {
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String json = object.getString("result");
                        List<Company> temp = new GeoUtil().deserializer(json, new TypeToken<List<Company>>() {
                        }.getType());
                        if (temp != null && temp.size() > 0) {
                            try {
                                db.saveOrUpdateAll(temp);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
