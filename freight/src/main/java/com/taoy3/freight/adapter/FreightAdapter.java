package com.taoy3.freight.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.BoxBean;
import com.taoy3.freight.bean.PriceBean;
import com.taoy3.freight.bean.PriceEntity;
import com.taoy3.freight.view.ChildGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy2 on 15-10-14.
 */
public class FreightAdapter extends BaseExpandableListAdapter{
    private List<PriceBean> list;
    private LayoutInflater inflater;
    private Context context;
    private OnChildrenClick onChildrenClick;

    public FreightAdapter(List<PriceBean> list, Context context) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getItems() != null ? list.get(i).getItems().size() : 0;
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getItems().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_freight_expandable_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.companyNameTv.setText(list.get(i).getSc_name());
        holder.freightCountTv.setText(list.get(i).getItems().size() + "条运价");
        holder.freightCountTv.setPaintFlags(holder.freightCountTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        return convertView;
    }


    @Override
    public View getChildView(int i, int j, boolean b, View convertView, ViewGroup parent) {
        ChildrenViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_freight_expandable, parent, false);
            holder = new ChildrenViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ChildrenViewHolder) convertView.getTag();
        }
        holder.startTv.setText(list.get(i).getItems().get(j).getStartPort());
        holder.lineTv.setText(list.get(i).getItems().get(j).getLine());
        if (list.get(i).getItems().get(j).getCls() != 0) {
            holder.priceDateTv.setText((list.get(i).getItems().get(j).getCls()) + "/" + (list.get(i).getItems().get(j).getEtd()));
        }
        ArrayList<String> gps = new ArrayList<>();
        getGps(list.get(i).getItems().get(j).getBoxBeans(), gps);
        ArrayAdapter adapter = new ArrayAdapter(context,R.layout.adapter_gp_item,gps);
        holder.gpGridView.setNumColumns(gps.size()/2);
        holder.gpGridView.setAdapter(adapter);
        holder.gpGridView.setOnItemClickListener(new OnGridViewClick(list.get(i).getItems().get(j)));
        return convertView;
    }

    private void getGps(List<BoxBean> items, ArrayList<String> gps) {
        String[] labels = {"20GP","40GP","40HQ","45HQ","20RF","40RF","40NOR"};
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getName()!=null){
                gps.add(gps.size()/2,labels[i]);
                gps.add(gps.size(),items.get(i).getName());
            }
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    private class OnGridViewClick  implements AdapterView.OnItemClickListener{
        private PriceEntity bean;
        public OnGridViewClick(PriceEntity bean) {
            this.bean = bean;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
            onChildrenClick.onChildrenClick(bean);
        }
    }
    public interface OnChildrenClick{
        void onChildrenClick(PriceEntity bean);
    }
    public void setOnChildrenClick(OnChildrenClick onChildrenClick){
        this.onChildrenClick = onChildrenClick;
    }

    private static class ViewHolder {
        private TextView companyNameTv;
        private TextView freightCountTv;

        public ViewHolder(View convertView) {
            companyNameTv = (TextView) convertView.findViewById(R.id.company_name);
            freightCountTv = (TextView) convertView.findViewById(R.id.freight_count);
        }
    }

    private static class ChildrenViewHolder {
        private TextView startTv;
        private TextView lineTv;
        private TextView priceDateTv;
        private ChildGridView gpGridView;
        public ChildrenViewHolder(View convertView) {
            startTv = (TextView) convertView.findViewById(R.id.start_port);
            lineTv = (TextView) convertView.findViewById(R.id.line_port);
            priceDateTv = (TextView) convertView.findViewById(R.id.freight_date);
            gpGridView = (ChildGridView) convertView.findViewById(R.id.freight_gp);
        }
    }
}
