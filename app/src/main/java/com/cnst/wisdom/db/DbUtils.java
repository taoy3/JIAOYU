package com.cnst.wisdom.db;

import com.cnst.wisdom.BaseApplication;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by taoy3 on 16/8/7.
 */
public class DbUtils {
    public static String getAssetString(String path){
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        try {
            inputStream = BaseApplication.getContext().getResources().getAssets().open(path);
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes)!=-1){
                buffer.append(new String(bytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString().trim();
    }
}
