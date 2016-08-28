package com.taoy3.freight.util;

import android.content.res.AssetManager;
import android.os.Environment;

import com.taoy3.freight.BaseApp;
import com.taoy3.freight.constant.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    public static String getStrFormAssets(String name) {
        StringBuffer stringBuffer = null;
        try {
            //得到资源
            AssetManager am = BaseApp.getApp().getAssets();
            //得到数据库的输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(am.open(name), "utf-8"));
            stringBuffer = new StringBuffer();
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                stringBuffer.append(temp);
            }
            //最后关闭就可以了

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    /**
     * sdk/xytg
     *
     * @return
     */
    public static String getAppPath() {
        String appPath;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            appPath = Environment.getExternalStorageDirectory() + File.separator + Config.ROOT_CACHE;
        } else {
            appPath = Environment.getDataDirectory() + File.separator + Config.SYSTEM_CACHE;
        }
        File file = new File(appPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return appPath;
    }

    /**
     * sdk/xytg/image
     *
     * @return
     */
    public static File getImageCache() {

        String imageCache = getAppPath() + File.separator + Config.IMAGE_CACHE;
        File imageFile = new File(imageCache);
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
        return imageFile;

    }

    public static void writeStr(String s, String nane) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(getSdPath(nane));
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static File getSdPath(String name) {
        File file = new File(Config.sdcardPath + File.separator + name);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }
}
