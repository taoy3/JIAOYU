package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.OrderBean;

import java.util.List;

/**
 * Created by taoy2 on 15-11-20.
 */
public class OrderAdapter extends AppBaseAdapter<OrderBean>{
    public OrderAdapter(Context context, List<OrderBean> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_order_ticket, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.startPortTv.setText(i+":"+list.get(i).getStart_name());
        holder.destPortTv.setText(list.get(i).getDes_name());
        holder.boxTv.setText("箱型:"+list.get(i).tipConnum());
        holder.accountsTv.setText("应收:"+list.get(i).getReceivable().getTotal().toString().replace("[", "").replace("]", ""));
        holder.payTv.setText("应付:"+list.get(i).getPayable().getTotal().toString().replace("[", "").replace("]", ""));
        holder.operatorTv.setText(list.get(i).getContacts().getShipper().getName());
        holder.numberTv.setText("运单号" + list.get(i).getOrderno());
        return view;
    }
    private static class ViewHolder {
        private TextView startPortTv;
        private TextView destPortTv;
        private TextView boxTv;
        private TextView accountsTv;
        private TextView payTv;
        private TextView numberTv;
        private TextView operatorTv;


        public ViewHolder(View view) {
            startPortTv = (TextView) view.findViewById(R.id.start_port);
            destPortTv = (TextView) view.findViewById(R.id.dest_port);
            boxTv = (TextView) view.findViewById(R.id.order_boxes);
            accountsTv = (TextView) view.findViewById(R.id.order_accounts);
            payTv = (TextView) view.findViewById(R.id.order_pay);
            numberTv = (TextView) view.findViewById(R.id.ticket_number);
            operatorTv = (TextView) view.findViewById(R.id.operator);
        }
    }
}
