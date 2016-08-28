//package com.taoy3.freight.util;
//
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.content.res.Resources;
//import android.graphics.drawable.Drawable;
//import android.os.Looper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.taoy3.freight.BaseApp;
//
///**
// * Created by taoy3 on 16/8/24.
// */
//public class AppUtils {
//    private static int screenwidth;
//    private static int screenHeight;
//
//    public static int getScreenHeight()
//    {
//        return screenHeight == 0 ? (int)SPUtills.get(getApplication(), Constants.screenh, 0) : screenHeight;
//    }
//
//    public static void setScreenHeight(int screenHeight)
//    {
//        if(screenHeight == 0)
//        {
//            SPUtills.put(getApplication(), Constants.screenh, screenHeight);
//            BaseApplication.screenHeight = screenHeight;
//        }
//    }
//
//    public static int getScreenwidth()
//    {
//        return screenwidth == 0 ? (int)SPUtills.get(getApplication(), Constants.screenw, 0) : screenwidth;
//    }
//
//    public static void setScreenwidth(int screenwidth)
//    {
//        if(BaseApplication.screenwidth == 0)
//        {
//            SPUtills.put(getApplication(), Constants.screenw, screenHeight);
//            BaseApplication.screenwidth = screenwidth;
//        }
//    }
//
//    //登录时保存用户信息：username,userId,SESSIONID,name,school,stationname
//    public static Login getLogin()
//    {
//        return mLogin;
//    }
//
//    public static void setLogin(Login login)
//    {
//        mLogin=login;
//    }
//
//    public static Context getContext()
//    {
//        return BaseApplication.getApplication();
//    }
//
//    public static View inflate(int resId, ViewGroup root)
//    {
//        return LayoutInflater.from(getContext()).inflate(resId, root, true);
//    }
//
//    public static View inflate(int resId)
//    {
//        return LayoutInflater.from(getContext()).inflate(resId, null);
//    }
//
//    /** 获取资源 */
//    public static Resources findResources()
//    {
//
//        return getContext().getResources();
//    }
//
//    /** 获取文字 */
//    public static String findString(int resId)
//    {
//        return findResources().getString(resId);
//    }
//
//    /** 获取文字数组 */
//    public static String[] findStringArray(int resId)
//    {
//        return findResources().getStringArray(resId);
//    }
//
//    /** 获取dimen */
//    public static int findDimens(int resId)
//    {
//        return findResources().getDimensionPixelSize(resId);
//    }
//
//    /** 获取drawable */
//    public static Drawable findDrawable(int resId)
//    {
//        return findResources().getDrawable(resId);
//    }
//
//    /** 获取颜色 */
//    public static int findColor(int resId)
//    {
//        return findResources().getColor(resId);
//    }
//
//    /** 获取颜色选择器 */
//    public static ColorStateList findColorStateList(int resId)
//    {
//        return findResources().getColorStateList(resId);
//    }
//
//
//    /** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
//    public static void showToastSafe(final int resId)
//    {
//        showToastSafe(findString(resId));
//    }
//
//    /** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
//    public static void showToastSafe(final String str)
//    {
//        Looper.prepare();
//        showToast(str);
//        Looper.loop();
//    }
//
//    private static void showToast(String str)
//    {
//        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
//    }
//}
