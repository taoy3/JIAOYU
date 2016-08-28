package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.EnquiryBean;
import com.taoy3.freight.util.DateUtils;

import java.util.List;

/**
 * Created by taoy2 on 15-10-22.
 */
public class EnquiryAdapter extends AppBaseAdapter<EnquiryBean>{
    public EnquiryAdapter(Context context, List<EnquiryBean> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view = inflater.inflate(R.layout.adapter_enquiry_layout,viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.startTv.setText(list.get(i).getStart());
        holder.destTv.setText(list.get(i).getDest());
//        if()
        holder.dateTv.setText(DateUtils.changeDateFormat(list.get(i).getApplydate()));
        return view;
    }
    private static class ViewHolder{
        private TextView startTv;
        private TextView destTv;
        private TextView dateTv;

        public ViewHolder(View view) {
            startTv = (TextView)view.findViewById(R.id.enquiry_list_start);
            destTv = (TextView) view.findViewById(R.id.enquiry_list_dest);
            dateTv = (TextView) view.findViewById(R.id.enquiry_list_date);
        }
    }
}
