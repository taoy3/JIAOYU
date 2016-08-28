package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.SurchargesEntity;

import java.util.List;

/**
 * Created by taoy2 on 15-10-8.
 */
public class SurchargesAdapter extends AppBaseAdapter<SurchargesEntity> {
    public SurchargesAdapter(Context context, List<SurchargesEntity> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_surcharge, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (list.get(i).getTprice() == 0) {
            holder.detailTypeTv2.setVisibility(View.VISIBLE);
            holder.detailTypeTv1.setVisibility(View.GONE);
            holder.surchargeNameTv.setText(list.get(i).getName());
            if (list.get(i).getCurr() != null) {
                holder.surchargeCurrTv.setText(list.get(i).getCurr());
            }
            holder.surchargeGp20Tv.setText(list.get(i).getGp20()+"");
            holder.surchargeGp40Tv.setText(list.get(i).getGp40()+"");
            holder.surchargeHq40Tv.setText(list.get(i).getHq40()+"");
        } else {
            holder.detailTypeTv1.setVisibility(View.VISIBLE);
            holder.detailTypeTv2.setVisibility(View.GONE);
            holder.detailNameTv.setText(list.get(i).getName());
            holder.detailCurTv.setText(list.get(i).getCurr());
            holder.detailPriceTv.setText(list.get(i).getTprice() + "/票");
        }
        return view;
    }

    private static class ViewHolder {
        private LinearLayout detailTypeTv1;
        private TextView detailNameTv;
        private TextView detailCurTv;
        private TextView detailPriceTv;

        private LinearLayout detailTypeTv2;
        private TextView surchargeNameTv;
        private TextView surchargeCurrTv;
        private TextView surchargeGp20Tv;
        private TextView surchargeGp40Tv;
        private TextView surchargeHq40Tv;

        private ViewHolder(View view) {
            detailTypeTv1 = (LinearLayout) view.findViewById(R.id.layout_price);
            detailNameTv = (TextView) view.findViewById(R.id.layout_price_name);
            detailCurTv = (TextView) view.findViewById(R.id.layout_price_cur);
            detailPriceTv = (TextView) view.findViewById(R.id.layout_price_price);

            detailTypeTv2 = (LinearLayout) view.findViewById(R.id.layout);
            surchargeNameTv = (TextView) view.findViewById(R.id.name);
            surchargeCurrTv = (TextView) view.findViewById(R.id.cur);
            surchargeGp20Tv = (TextView) view.findViewById(R.id.gp20);
            surchargeGp40Tv = (TextView) view.findViewById(R.id.gp40);
            surchargeHq40Tv = (TextView) view.findViewById(R.id.hq40);
        }
    }
}
