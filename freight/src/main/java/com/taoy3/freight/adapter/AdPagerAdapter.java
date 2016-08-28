package com.taoy3.freight.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.taoy3.freight.R;
import com.taoy3.freight.util.ResUtils;

import java.util.ArrayList;

/**
 * Created by taoy2 on 15-10-10.
 */
public class AdPagerAdapter extends PagerAdapter {
    private ArrayList<String> urls;
    private LayoutInflater inflater;

    public AdPagerAdapter(ArrayList<String> urls, LayoutInflater inflater) {
        this.urls = urls;
        this.inflater = inflater;
    }

    /**
     * 获得ViewPage要显示的Pager的数量
     * 返回数组或集合的长度
     */
    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    /**
     * 在instantiateItem方法中初始化的视图对象
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 给每个pager来初始化视图
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder viewHolder = new ViewHolder(container);
        String url = urls.get(position);
        viewHolder.imageView.setTag(url);
        viewHolder.imageView.setImageBitmap(ResUtils.getBitmapForAssts(url));
        return viewHolder.view;
    }

    public class ViewHolder {
        private View view;
        private ImageView imageView;

        public ViewHolder(ViewGroup container) {
            this.view = inflater.inflate(R.layout.adapter_ad, null);
            this.imageView = (ImageView) view.findViewById(R.id.ad);
            container.addView(view);  //把pager页面要显示的视图添加到ViewPager中
        }
    }
}
