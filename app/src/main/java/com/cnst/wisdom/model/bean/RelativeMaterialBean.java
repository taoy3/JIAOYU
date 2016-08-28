package com.cnst.wisdom.model.bean;

import java.util.ArrayList;

/**
 * @author jiangzuyun.
 * @date 2016/3/4
 * @des [一句话描述]
 * @since [产品/模版版本]
 */

public class RelativeMaterialBean
{
    /**
     * 请求返回码
     */
    public int code;
    /**
     * 网络请求返回信息
     */
    public String msg;
    public RelativeBean data;

    /**
     * introduction  素材(视频)简介
     * title  素材(视频)标题
     * videoId 视频对应优酷id
     * filePath 文件路径
     * otherList 相关素材(视频)列表
     * id  素材id
     * introduction简介
     * thumbnailPath缩略图地址
     * title  标题
     */
    public class RelativeBean
    {
        public String filePath;
        public String introduction;
        public boolean isVideo;
        public ArrayList<AboutMaterialBean> otherList;
        public String title;
        public String videoId;
    }

    public class AboutMaterialBean
    {
        public String introduction;
        public boolean isVideo;
        public String resourceId;
        public String thumbnailPath;
        public String title;
        public String videoId;
    }
}
