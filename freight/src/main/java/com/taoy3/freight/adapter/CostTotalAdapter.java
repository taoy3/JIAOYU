package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.TotalBean;

import java.util.List;

/**
 * Created by taoy2 on 15-12-13.
 */
public class CostTotalAdapter extends AppBaseAdapter<TotalBean>{
    public CostTotalAdapter(Context context, List<TotalBean> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.adapter_cost_total,viewGroup,false);
        TextView nameTv = (TextView) view.findViewById(R.id.adapter_total_name);
        TextView valueTv = (TextView) view.findViewById(R.id.adapter_total_value);
        nameTv.setText(list.get(i).getName());
        valueTv.setText(list.get(i).getValue()+"");
        return view;
    }
}
