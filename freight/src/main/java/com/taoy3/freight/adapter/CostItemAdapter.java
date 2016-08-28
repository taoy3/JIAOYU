package com.taoy3.freight.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.BoxBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by taoy2 on 15-12-5.
 */
public class CostItemAdapter extends AppBaseAdapter<BoxBean>{
    private EditText totalEt;

    public CostItemAdapter(Context context, List<BoxBean> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.adapter_cost_item, viewGroup, false);
        TextView nameTv = (TextView) view.findViewById(R.id.adapter_cargo_type);
        EditText unitEt = (EditText) view.findViewById(R.id.adapter_cargo_unit);
        TextView qtyTv = (TextView) view.findViewById(R.id.adapter_cargo_qty);
        nameTv.setText(list.get(i).getName());
        if(i==list.size()-1){
            totalEt = unitEt;
            qtyTv.setVisibility(View.GONE);
        }else {
            if(list.get(i).getValue() >0){
                unitEt.setText(list.get(i).getValue() + "");
            }
            unitEt.addTextChangedListener(new UnitChangeListener(i));
            qtyTv.setText("x"+list.get(i).getNumber());
        }
        return view;
    }


    private class UnitChangeListener implements TextWatcher{
        private int index;

        public UnitChangeListener(int index) {
            this.index = index;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            float total = 0f;
            for (int i = 0; i < list.size()-1; i++) {
                total+=list.get(i).getValue() *list.get(i).getNumber();
            }
                totalEt.setText(new BigDecimal(total)+"");
        }

    }
}

