package com.cnst.wisdom.ui.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author taoyuan
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public abstract class TeachPlanBaseAdapter<T> extends BaseAdapter {
    protected AppCompatActivity mContext;
    protected List<T> mList;
    protected LayoutInflater mLayoutInflater;

    public TeachPlanBaseAdapter(Context context, List<T> list)
    {
        this.mContext = (AppCompatActivity) context;
        this.mList = list;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
