package com.taoy3.freight.util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.taoy3.freight.BaseApp;

import java.io.InputStream;

/**
 * Created by taoy3 on 16/8/25.
 */
public class ResUtils {
    private static AssetManager assetManager;
    public static Bitmap getBitmapForAssts(String url){
        AssetManager assetManager = BaseApp.getApp().getAssets();
        try {
            InputStream in=assetManager.open(url);
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
