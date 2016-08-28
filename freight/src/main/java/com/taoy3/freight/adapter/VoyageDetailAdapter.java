package com.taoy3.freight.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.PortsInfo;
import com.taoy3.freight.util.DateUtils;

import java.util.List;

/**
 * Created by taoy2 on 15-10-15.
 */
public class VoyageDetailAdapter extends AppBaseAdapter<PortsInfo> {
    String startPort;
    String destPort;

    public VoyageDetailAdapter(Context context, List<PortsInfo> list, String startPort, String destPort) {
        super(context, list);
        this.startPort = startPort;
        this.destPort = destPort;
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_voyage_detail, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (list.get(i).getPort_name().equals(startPort)) {
            holder.iconIv.setImageResource(R.mipmap.start_port);
            holder.portNameTv.setTextColor(Color.GREEN);
            holder.etaTv.setTextColor(Color.GREEN);
            holder.etdTv.setTextColor(Color.GREEN);
            holder.ttTv.setTextColor(Color.GREEN);
        } else if (list.get(i).getPort_name().equals(destPort)) {
            holder.iconIv.setImageResource(R.mipmap.dest_port);
            holder.portNameTv.setTextColor(Color.RED);
            holder.etaTv.setTextColor(Color.RED);
            holder.etdTv.setTextColor(Color.RED);
            holder.ttTv.setTextColor(Color.RED);
        }else {
            holder.iconIv.setImageResource(R.mipmap.pass_port);
            holder.portNameTv.setTextColor(Color.GRAY);
            holder.etaTv.setTextColor(Color.GRAY);
            holder.etdTv.setTextColor(Color.GRAY);
            holder.ttTv.setTextColor(Color.GRAY);
        }
        holder.portNameTv.setText(list.get(i).getPort_name());
        holder.etaTv.setText("ETA:" + DateUtils.changeDateFormat(list.get(i).getEta()));
//        if (list.get(i).getEtd() == null) {
//            holder.etdTv.setText("ETD:" + DateUtils.getEtdDate(list.get(i).getEta(), list.get(i).getTt()));
//        } else {
//            holder.etdTv.setText("ETD:" + DateUtils.changeDateFormat(list.get(i).getEtd()));
//        }
        holder.ttTv.setText("TT:" + list.get(i).getTt() + "天");
        return view;
    }

    private static class ViewHolder {
        private TextView portNameTv;
        private TextView ttTv;
        private ImageView iconIv;
        private TextView etaTv;
        private TextView etdTv;

        public ViewHolder(View view) {
            portNameTv = (TextView) view.findViewById(R.id.port_name);
            ttTv = (TextView) view.findViewById(R.id.tt);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            etaTv = (TextView) view.findViewById(R.id.eta);
            etdTv = (TextView) view.findViewById(R.id.etd);
        }
    }
}
