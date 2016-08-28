package com.taoy3.freight;

import android.app.Application;

import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.util.DataUtils;
import com.taoy3.freight.view.LockPatternView;


/**
 * Created by KX-09 on 2015/9/17.
 */
public class BaseApp extends Application {
    public final static int UNLOGIN = 0;
    public final static int LOGINFAIL = 1;
    public final static int LOGOUT = 2;
    public final static int LOGINING = 3;
    public final static int LOGINOK = 4;
    private static BaseApp app;
    public static int loginStatus = LOGINOK;
    public static boolean lockIsOk;
    public static boolean portDataIsOk;
    public static boolean companyDataIsOk;
    public static BaseApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
//        check();
//        SQLUtils.getCurrs(this);
//        SQLUtils.initUserData(this);
        DataUtils.initData();
    }

    public void check() {
        CacheDataConstant.choosePattern = LockPatternView.stringToPattern(getSharedPreferences(
                Config.LOCK, MODE_PRIVATE).getString(Config.LOCK_KEY,null));
        lockIsOk = (CacheDataConstant.choosePattern != null);
    }
}
