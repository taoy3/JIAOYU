package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.R;

import java.util.List;

/**
 * Created by taoy2 on 15-12-8.
 */
public class BoxTypeAdapter extends AppBaseAdapter<String>{
    public BoxTypeAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = inflater.inflate(R.layout.adapter_cost_type,viewGroup,false);
        }
        TextView nameText = (TextView) view.findViewById(R.id.cost_type_name);
        nameText.setText(list.get(i));
        return view;
    }
}
