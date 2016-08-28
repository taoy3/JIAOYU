package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.BoxBean;
import com.taoy3.freight.view.AddCutButton;

import java.util.List;

/**
<<<<<<< HEAD
 * Created by taoy2 on 15-12-5.
 */
public class BoxAdapter extends AppBaseAdapter<BoxBean> implements AddCutButton.QtyChangeListener {
    public BoxAdapter(Context context, List<BoxBean> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view = inflater.inflate(R.layout.adapter_cargo,viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        holder.nameTv.setText(list.get(i).getName());
        holder.qtyAb.setQty(list.get(i).getNumber());
        holder.qtyAb.setTag(i);
        holder.qtyAb.setQtyChangeListener(this);
        return view;
    }

    @Override
    public void onQtyChange(View view, int qty) {
        if(view.getTag()!=null&&(Integer) view.getTag()<list.size()){
            list.get((Integer) view.getTag()).setNumber(qty);
        }

    }
    private static class ViewHolder{
        private TextView nameTv;
        private AddCutButton qtyAb;
        public ViewHolder(View view) {
            this.nameTv = (TextView) view.findViewById(R.id.adapter_cargo_name);
            this.qtyAb = (AddCutButton) view.findViewById(R.id.adapter_cargo_qty);
        }
    }
}
