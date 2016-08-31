package com.taoy3.jiaoyu.utills;

import com.taoy3.jiaoyu.base.BaseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by taoy3 on 16/8/30.
 */
public class DataUtils {
    public static String getAssetString(String assetsName) {
        StringBuffer buffer=new StringBuffer();
        try {
            InputStream inputStream = BaseApplication.getContext().getResources().getAssets().open(assetsName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            String temp;
            while ((temp=reader.readLine())!=null){
                buffer.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
