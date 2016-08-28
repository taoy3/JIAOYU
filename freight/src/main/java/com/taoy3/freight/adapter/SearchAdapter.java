package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.SearchBean;

import java.util.List;

/**
 * Created by taoy2 on 15-11-27.
 */
public class SearchAdapter<T extends SearchBean> extends AppBaseAdapter<SearchBean>{
    public SearchAdapter(Context context, List<SearchBean> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_array, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.itemTv.setText(list.get(i).getId());
        return view;
    }
    private static class ViewHolder{
        private TextView itemTv;
        public ViewHolder(View view) {
            itemTv = (TextView) view.findViewById(R.id.text);
        }
    }
}
