package com.taoy3.freight.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.BaseApp;
import com.taoy3.freight.R;
import com.taoy3.freight.bean.CurrBean;
import com.taoy3.freight.bean.UserBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.constant.UrlConstant;
import com.taoy3.freight.netBean.GeoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by taoy2 on 15-9-23.
 */
public class SQLUtils {
    public static DbUtils dataDb;
    public static void initUserData(Context context) {
        try {
            DbUtils.DaoConfig config = new DbUtils.DaoConfig(context);
            config.setDbDir(context.getCacheDir() + Config.SQL_ROOT);
            config.setDbName(Config.USER_INFO); //db名
            config.setDbVersion(Config.DBVER);  //db版本
            dataDb = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的
            if (dataDb.tableIsExist(UserBean.class)){
                CacheDataConstant.userBean = dataDb.findFirst(UserBean.class);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (CacheDataConstant.userBean != null) {
            Messaging.post(R.string.logining,0);
            BaseApp.loginStatus = BaseApp.LOGINING;
            LogUtils.login(CacheDataConstant.userBean.getUsername(), CacheDataConstant.userBean.getPassWord());
        } else {
            Messaging.post(R.string.not_login,0);
            BaseApp.loginStatus = BaseApp.UNLOGIN;
        }
    }

    public static String queryPortId(String nameEn) {
        String portId = "";
        for (int i = 0; i < CacheDataConstant.ports.size(); i++) {
            if (CacheDataConstant.ports.get(i).getName_en().equals(nameEn)) {
                portId += i+1;
                break;
            }
        }
        return portId;
    }

//    public static String queryScId(String code) {
//        String id = null;
//        for (int j = 0; j < CacheDataConstant.companies.size(); j++) {
//            if (CacheDataConstant.companies.get(j).getCode().equals(code)) {
//                id = CacheDataConstant.companies.get(j).getId();
//            }
//        }
//        return id;
//    }

    public static void getCurrs(final Context context) {
        AppHttpUtils.getUtils(UrlConstant.currUrl, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Config.CURRS.add(new CurrBean("USD"));
                Config.CURRS.add(new CurrBean("CNY"));
                Config.CURRS.add(new CurrBean("HKD"));
                if (msg.what / 100 == 2) {
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String json = object.getString("result");
                        List<CurrBean> currBeans = new GeoUtil().deserializer(json, new TypeToken<List<CurrBean>>() {
                        }.getType());
                        if (currBeans != null && currBeans.size() > 0) {
                            Config.CURRS.clear();
                            Config.CURRS.addAll(currBeans);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < Config.CURRS.size(); i++) {
                    CacheDataConstant.currCodes.add(Config.CURRS.get(i).getCode());
                }
            }
        });
    }
}

