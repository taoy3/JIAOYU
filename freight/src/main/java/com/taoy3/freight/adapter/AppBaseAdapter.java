package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/9/20.
 */
public abstract class AppBaseAdapter<T> extends BaseAdapter {
    protected LayoutInflater inflater;
    protected List<T> list;
    protected Context context;
    private int[] layoutId;

    public AppBaseAdapter(Context context, List<T> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }
    public AppBaseAdapter(Context context, List<T> list,int... layoutId) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getItemView(i,view,viewGroup);
    }



    public abstract View getItemView(int i, View view, ViewGroup viewGroup);
    public boolean getSwipeEnableByPosition(int position){
        return true;
    }
    protected abstract static class ViewHolder{
        ViewHolder(View view){

        }
    }
}
