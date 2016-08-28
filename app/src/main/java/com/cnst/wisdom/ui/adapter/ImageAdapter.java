package com.cnst.wisdom.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author taoyuan
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class ImageAdapter extends TeachPlanBaseAdapter<String> {
    private BitmapUtils mBitmapUtils;
    public ImageAdapter(Context context, List<String> list) {
        super(context, list);
        mBitmapUtils = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new ImageView(mContext);
        }
        ImageView imageView = (ImageView) convertView;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        mBitmapUtils.display(convertView, mList.get(position));
        return convertView;
    }
}
