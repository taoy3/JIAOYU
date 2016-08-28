package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.CustomerBean;

import java.util.List;

/**
 * Created by taoy2 on 15-12-7.
 */
public class CusAdapter extends AppBaseAdapter<CustomerBean>{
    private ModifyListener listener;

    public void setListener(ModifyListener listener) {
        this.listener = listener;
    }

    public CusAdapter(Context context, List<CustomerBean> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view = inflater.inflate(R.layout.adapter_address,viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        holder.nameTv.setText(list.get(i).getName_cn());
        holder.phoneTv.setText(list.get(i).getTel());
        holder.addressTv.setText(list.get(i).getAddress_en());
        holder.editTv.setOnClickListener(new OnClick(i));
        return view;
    }
    private class OnClick implements View.OnClickListener{
        private int index;
        public OnClick(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {

            if(listener!=null){
                listener.onModify(index);
            }
        }
    }


    private static class ViewHolder{
        private TextView nameTv;
        private TextView phoneTv;
        private TextView addressTv;
        private ImageView editTv;
        public ViewHolder(View view) {
            nameTv = (TextView) view.findViewById(R.id.address_name);
            phoneTv = (TextView) view.findViewById(R.id.address_phone);
            addressTv = (TextView) view.findViewById(R.id.address);
            editTv = (ImageView) view.findViewById(R.id.address_edit);
        }
    }
    public interface ModifyListener{
        void onModify(int index);
    }
}
