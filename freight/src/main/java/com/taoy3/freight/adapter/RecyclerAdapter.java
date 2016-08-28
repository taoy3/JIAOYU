package com.taoy3.freight.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.CostItemBean;
import com.taoy3.freight.view.ChildGridView;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CostItemBean> list;
    private LayoutInflater inflater;
    private Context context;
    public RecyclerAdapter(Context context, List<CostItemBean> list) {
        this.list = list;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if(viewType==0){
            holder = new RcListViewHolder(inflater.inflate(R.layout.adapter_grid,parent,false));
        }else {
            holder = new RcTextViewHolder(inflater.inflate(R.layout.adapter_array,parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==0){
            RcListViewHolder listViewHolder = (RcListViewHolder) holder;
            listViewHolder.itemView.setNumColumns(list.get(position).getPrices().size());
            List itemList = new ArrayList();
            for (int i = 0; i < list.get(position).getPrices().size(); i++) {
                itemList.add(list.get(position).getPrices().get(i).getValue()*list.get(position).getPrices().get(i).getNumber());
            }
            listViewHolder.itemView.setAdapter(new ArrayAdapter(context,R.layout.adapter_array,itemList));
        }else {
            RcTextViewHolder listViewHolder = (RcTextViewHolder) holder;
            listViewHolder.itemView.setText(list.get(position).getCost()+"");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getPrices()==null&&list.get(position).getPrices().size()>0){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class RcTextViewHolder extends RecyclerView.ViewHolder{
        private TextView itemView;
        public RcTextViewHolder(View view) {
            super(view);
            itemView = (TextView) view;
        }
    }
    public class RcListViewHolder extends RecyclerView.ViewHolder{
        private ChildGridView itemView;
        public RcListViewHolder(View view) {
            super(view);
            itemView = (ChildGridView) view;
        }
    }
}
