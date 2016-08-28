package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.Voyage;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.util.DateUtils;

import java.util.List;


/**
 * Created by taoy2 on 15-9-21.
 */
public class VoyageAdapter extends AppBaseAdapter<Voyage> {
    public VoyageAdapter(Context context, List<Voyage> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_voyage, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.companyNameTv.setText(getScCode(list.get(i).getSc_name()));
        holder.startPortZhTv.setText(getNameZh(list.get(i).getPorts().get(0).getPort_name()) + "/" + list.get(i).getSchema());
        holder.startPortTv.setText(list.get(i).getPorts().get(0).getPort_name());
        holder.clsTv.setText("CLS:" + DateUtils.changeDateFormat(list.get(i).getPorts().get(0).getCls()));
        holder.etdTv.setText("ETD:" + DateUtils.changeDateFormat(list.get(i).getPorts().get(0).getEtd()));
        holder.destPortTv.setText(list.get(i).getPorts().get(list.get(i).getPorts().size() - 1).getPort_name());
        holder.etaTv.setText("ETA:" + DateUtils.changeDateFormat(list.get(i).getPorts().get(0).getEta()));
        holder.ttTv.setText("TT:" + (list.get(i).getPorts().get(list.get(i).getPorts().size() - 1).getTt() - list.get(i).getPorts().get(0).getTt()) + "天");
        return view;
    }

    private String getNameZh(String port_name) {
        String name_zh = "";
        for (int i = 0; i < CacheDataConstant.ports.size(); i++) {
            if (CacheDataConstant.ports.get(i).getName_en().equals(port_name)) {
                name_zh = CacheDataConstant.ports.get(i).getName_zh();
            }
        }
        return name_zh;
    }

    private String getScCode(String sc_name) {
        String sc_code = "";
        for (int i = 0; i < CacheDataConstant.companies.size(); i++) {
            if (CacheDataConstant.companies.get(i).getName_zh().equals(sc_name)) {
                sc_code = CacheDataConstant.companies.get(i).getCode();
            }
        }
        return sc_code;
    }

    private static class ViewHolder {
        private TextView companyNameTv;
        private TextView startPortZhTv;
        private TextView startPortTv;
        private TextView clsTv;
        private TextView etdTv;
        private TextView destPortTv;
        private TextView etaTv;
        private TextView ttTv;

        public ViewHolder(View view) {
            companyNameTv = (TextView) view.findViewById(R.id.company_name);
            startPortZhTv = (TextView) view.findViewById(R.id.start_port_zh);
            startPortTv = (TextView) view.findViewById(R.id.start_port);
            clsTv = (TextView) view.findViewById(R.id.cls);
            etdTv = (TextView) view.findViewById(R.id.etd);
            destPortTv = (TextView) view.findViewById(R.id.dest_port);
            etaTv = (TextView) view.findViewById(R.id.eta);
            ttTv = (TextView) view.findViewById(R.id.tt);
        }
    }
}
