package com.cnst.wisdom.utills;

import android.os.Environment;

import java.io.File;

/**
 * @author tangbinfeng.
 * @date 2016/2/23
 * @des sd卡工具类
 * @since [产品/模版版本]
 */

public class SDCardUtils
{
    /**
     * 获取sd卡路径
     *
     * @return
     */
    public static String getSDPath()
    {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

}
