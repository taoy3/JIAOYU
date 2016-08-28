package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.R;

import java.util.List;

/**
 * Created by taoy2 on 15-12-24.
 */
public class SimpleAdapter extends AppBaseAdapter<String>{
    public SimpleAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view=inflater.inflate(R.layout.adapter_simple,viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        holder.textView.setText(list.get(i));
        return view;
    }
    private static class ViewHolder{
        private TextView textView;
        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
