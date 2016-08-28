package com.taoy3.freight.constant;

import android.os.Environment;

import com.taoy3.freight.bean.CurrBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public final static String GP20 = "20GP";
    public final static String GP40 = "40GP";
    public final static String HQ40 = "40HQ";
    public final static String HQ45 = "45HQ";
    public final static String RF20 = "20RF";
    public final static String RF40 = "40RF";
    public final static String NOR40 = "40NOR";
    public final static String[] BOXNAMES = {GP20,GP40,HQ40,HQ45,RF20,RF40,NOR40};
    public final static String SEA = "海运费";
    public final static String TRAILER="拖车费";
    public final static String CUSTOMS="报关费";
    public final static String OTHER="其它费";
    public final static String CNYSIGN="￥";
    public final static String USDSIGN="$";
    public final static String CNY="CNY";
    public final static String USD="USD";
    public final static String HKD="HKD";
    public static List<CurrBean> CURRS = new ArrayList<>();

    /**
     * 应用程序的sd卡个根目录
     */
    public static final String ROOT_CACHE = "kxscs";
    /**
     *
     */
    public static final String SYSTEM_CACHE = "com.kxscs";


    /**
     * 应用程序保存图片的文件夹
     */
    public static final  File photoPath = new File(Environment.getExternalStorageDirectory() + "/kxscs/image");
    public static final String sdcardPath = Environment.getExternalStorageDirectory()+File.separator+"freight";

    /**
     * 数据库根目录
     */
    public static final String SQL_ROOT = "/db";
    /**
     *港口数据库名
     */
    public static final String PORT = "port.db";
    /**
     *船运公司数据库名
     */
    public static final String SHIP_COMPANY = "shipCompany.db";
    /**
     *用户信息数据库名
     */
    public static final String USER_INFO = "userInfo.db";
    public static final String USER_DATA = "userData.db";
    public static final String PORT_JSON = "port.json";
    public static final int DBVER = 2;
    public static final String LOCK = "lock";
    public static final String LOCK_KEY = "lock_key";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static int LOCKCODE = 10;
    public static String IMAGE_CACHE = "image";
    public static final String SHIP_COMPANY_JSON = "company.json";
}
