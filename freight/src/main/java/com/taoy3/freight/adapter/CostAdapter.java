package com.taoy3.freight.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.activity.AccountsActivity;
import com.taoy3.freight.activity.PayActivity;
import com.taoy3.freight.bean.CostItemBean;
import com.taoy3.freight.constant.CacheDataConstant;

import java.math.BigDecimal;
import java.util.List;

/**
 * <<<<<<< HEAD
 * Created by taoy2 on 15-12-5.
 */
public class CostAdapter extends AppBaseAdapter<CostItemBean> implements AdapterView.OnItemSelectedListener {
    private OnItemClick listener;
    private boolean isFast;

    public CostAdapter(Context context, List<CostItemBean> list) {
        super(context, list);
    }

    public void setIsFast(boolean isFast) {
        this.isFast = isFast;
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        if (list.get(i).getType()==CostItemBean.Type.BOX) {
            view = inflater.inflate(R.layout.adapter_cost_text, viewGroup, false);
            getItemText(i, view);
        } else {
            view = inflater.inflate(R.layout.adapter_cost_edit, viewGroup, false);
            getItemEdit(i, view);
        }
        return view;
    }

    private void getItemEdit(int i, View view) {
        TextView costNameTv = (TextView) view.findViewById(R.id.adapter_cost_name);
        EditText costEt = (EditText) view.findViewById(R.id.adapter_cost);
        Spinner currenciesSp = (Spinner) view.findViewById(R.id.adapter_cost_currencies);
        currenciesSp.setSelection(CacheDataConstant.currCodes.indexOf(list.get(i).getPaycurr()));
        TextView currTv = (TextView) view.findViewById(R.id.adapter_cost_curr);
        costEt.addTextChangedListener(new OnPriceChange(i));
        costNameTv.setText(list.get(i).getFeename());
        if(list.get(i).getCost()!=null)
        costEt.setText(list.get(i).getCost()+"");
        if (isFast) {
            currenciesSp.setAdapter(new SimpleAdapter(context, CacheDataConstant.currCodes));
            currenciesSp.setVisibility(View.VISIBLE);
            currTv.setVisibility(View.GONE);
            currenciesSp.setTag(i);
            currenciesSp.setOnItemSelectedListener(this);
        } else {
            currTv.setVisibility(View.VISIBLE);
            currenciesSp.setVisibility(View.GONE);
            if(list.get(i).getPaycurr()==null||! CacheDataConstant.currCodes.contains(list.get(i).getPaycurr())){
                list.get(i).setPaycurr(CacheDataConstant.currCodes.get(0));
            }
            currTv.setText(list.get(i).getPaycurr());
        }
    }
    private class OnPriceChange implements TextWatcher {
        private int index;

        public OnPriceChange(int index) {
            this.index = index;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().equals("null"))
                return;
            if (s.toString().trim().length() > 0) {
                list.get(index).setCost(new BigDecimal(s.toString().trim()));
                changeCost();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        list.get((Integer) parent.getTag()).setPaycurr(CacheDataConstant.currCodes.get(position));
        changeCost();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private void changeCost() {
        if(context instanceof AccountsActivity){
            AccountsActivity activity = (AccountsActivity) context;
                    activity.costChange();
        }else if(context instanceof PayActivity){
            PayActivity activity = (PayActivity) context;
            activity.costChange();
        }
    }

    private void getItemText(int i, View view) {
        TextView costNameTv = (TextView) view.findViewById(R.id.adapter_cost_name);
        TextView currTv = (TextView) view.findViewById(R.id.adapter_cost_curr);
        costNameTv.setText(list.get(i).getFeename());
        Spinner currenciesSp = (Spinner) view.findViewById(R.id.adapter_cost_currencies);
        if (isFast) {
            currenciesSp.setAdapter(new SimpleAdapter(context, CacheDataConstant.currCodes));
            currenciesSp.setVisibility(View.VISIBLE);
            currTv.setVisibility(View.GONE);
            currenciesSp.setTag(i);
            currenciesSp.setOnItemSelectedListener(this);
        } else {
            currTv.setVisibility(View.VISIBLE);
            currenciesSp.setVisibility(View.GONE);
            if(list.get(i).getPaycurr()==null||!CacheDataConstant.currCodes.contains(list.get(i).getPaycurr())){
                list.get(i).setPaycurr(CacheDataConstant.currCodes.get(0));
            }
            currTv.setText(list.get(i).getPaycurr());
        }
        TextView costTv= (TextView) view.findViewById(R.id.adapter_cost);
        if(list.get(i).getCost()!=null){
            costTv.setText("合计：" + list.get(i).getCost());
        }
        costTv.setOnClickListener(new OnClickListener(i));
    }

    private class OnClickListener implements View.OnClickListener {
        private int position;

        public OnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClickListener(v, position);
            }
        }
    }



    public void setOnItemClickListener(OnItemClick listener) {
        this.listener = listener;
    }

    public interface OnItemClick {
        void onItemClickListener(View view, int position);
    }
}
