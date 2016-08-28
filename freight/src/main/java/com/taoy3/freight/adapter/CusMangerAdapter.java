package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.SearchBean;
import com.taoy3.freight.util.GetFirstLetter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by taoy2 on 15-11-20.
 */
public class CusMangerAdapter extends BaseAdapter{
    protected LayoutInflater inflater;
    protected List<? extends SearchBean> list;
    protected Context context;
    private HashMap<Integer,Integer> indexPositions = new HashMap<>();
    private final String[] firstLetters;
    private float mDownX;
    private OnItemChangeListener listener;
    private TextView mDeleteTv;
    private TextView headView;
    public CusMangerAdapter(Context context, List<? extends SearchBean> list,HashMap<Integer,
            Integer> indexPositions,String[] firstLetters,TextView headView) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.indexPositions = indexPositions;
        this.firstLetters = firstLetters;
        this.headView = headView;
    }
    @Override
    public int getCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_cus_manage, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(indexPositions.containsValue(i)){
            holder.firstLetterTv.setVisibility(View.VISIBLE);
            holder.firstLetterTv.setText(GetFirstLetter.getFirstLetter(list.get(i).getId()).toUpperCase());
        }else {
            holder.firstLetterTv.setVisibility(View.GONE);
        }
        holder.deleteTv.setOnClickListener(new ClickListener(list.get(i)));
        holder.deleteTv.setVisibility(View.GONE);
        if(list.get(i).getId()==null||list.get(i).getId().equals("")){
            holder.itemTv.setText(R.string.no_name);
        }else {
            holder.itemTv.setText(list.get(i).getId());
        }
        view.setOnTouchListener(new OnTouchListener(i, holder.deleteTv));
        return view;
    }
    private class ClickListener implements View.OnClickListener{
        private SearchBean data;
        public ClickListener(SearchBean data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onItemDel(data);
            upDataIndex();
        }
    }
    public void notifyDataChanged(){
        headView.setText("共有" + list.size() + "人");
        Collections.sort(list);
        upDataIndex();
    }

    private void upDataIndex() {
        indexPositions.clear();
        for (int j = 0; j < firstLetters.length; j++) {
            for (int i = 0; i < list.size(); i++) {
                if(GetFirstLetter.getFirstLetter(list.get(i).getId()).toUpperCase().equals(firstLetters[j])){
                    indexPositions.put(j,i);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        private TextView itemTv;
        private TextView deleteTv;
        private TextView firstLetterTv;
        public ViewHolder(View view) {
            itemTv = (TextView) view.findViewById(R.id.adapter_item);
            deleteTv = (TextView) view.findViewById(R.id.delete);
            firstLetterTv = (TextView) view.findViewById(R.id.firstLetter);
        }
    }

    private class OnTouchListener implements View.OnTouchListener{
        private int position;
        private TextView deleteTv;
        public OnTouchListener(int position, TextView deleteTv) {
            this.position = position;
            this.deleteTv = deleteTv;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()== MotionEvent.ACTION_DOWN){
                mDownX = event.getX();
                if(mDeleteTv!=null&&!mDeleteTv.equals(deleteTv)){
                    mDeleteTv.setVisibility(View.GONE);
                }
                mDeleteTv = deleteTv;
            }else if(event.getAction()==MotionEvent.ACTION_MOVE){
                if(mDownX - event.getX()>10){
                    mDeleteTv.setVisibility(View.VISIBLE);
                }else if (event.getX() - mDownX>10){
                    if(mDeleteTv.getVisibility()==View.VISIBLE){
                        mDeleteTv.setVisibility(View.GONE);
                    }
                }
            }else if(event.getAction()==MotionEvent.ACTION_UP){
                if(event.getX()==mDownX){
                    if(mDeleteTv.getVisibility() == View.GONE){
                        listener.onItemSelect(position);
                    }else {
                        mDeleteTv.setVisibility(View.GONE);
                    }
                }
            }
            return true;
        }
    }
    public void setOnItemClickListener(OnItemChangeListener listener){
        this.listener = listener;
    }
    public interface OnItemChangeListener {
        void onItemSelect(int position);
        void onItemDel(SearchBean data);
    }
}
