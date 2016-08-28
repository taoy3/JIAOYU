package com.cnst.wisdom.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.MedicineTipBean;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author taoyuan
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class MedicineTipAd extends TeachPlanBaseAdapter<MedicineTipBean>{
    public MedicineTipAd(Context context, List<MedicineTipBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.item_medicine_tip,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        MedicineTipBean bean = mList.get(position);
        holder.timeTv.setText(bean.getTime());
        holder.nameTv.setText(bean.getClazz()+"\t\t"+bean.getName());
        holder.textTv.setText(bean.getTip());
        holder.timesTv.setText(bean.getTimes());
        if(bean.getState()==0){
            holder.dueIv.setImageResource(R.mipmap.un_complete);
        }else {
            holder.dueIv.setImageResource(R.mipmap.complete);
        }
        return convertView;
    }
    private static class ViewHolder{
        private TextView timeTv;
        private TextView nameTv;
        private TextView textTv;
        private TextView timesTv;
        private ImageView dueIv;
        public ViewHolder(View convertView) {
            timeTv = (TextView) convertView.findViewById(R.id.medicine_tip_time);
            nameTv = (TextView) convertView.findViewById(R.id.medicine_tip_name);
            textTv = (TextView) convertView.findViewById(R.id.medicine_tip_text);
            timesTv = (TextView) convertView.findViewById(R.id.medicine_tip_times);
            dueIv = (ImageView) convertView.findViewById(R.id.medicine_tip_due);
        }
    }
}
