package com.taoy3.freight.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.CostTypeAdapter;
import com.taoy3.freight.bean.BoxBean;
import com.taoy3.freight.bean.CostBean;
import com.taoy3.freight.bean.CostItemBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.view.MulSelView;

import java.util.ArrayList;
import java.util.List;

public class SelectCostActivity extends BaseActivity{
    private MulSelView costGv;
    private CostTypeAdapter costTypeAdapter;
    private List<CostItemBean> costType = new ArrayList<>();
    private CostBean bean;

    @Override
    protected void setView() {
        setContentView(R.layout.dialog_window_cost);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_window_cost, null);
        view.findViewById(R.id.cost_confirm).setOnClickListener(this);
        costGv = (MulSelView) view.findViewById(R.id.cost_type);
        costTypeAdapter = new CostTypeAdapter(this, costType);
        costGv.setAdapter(costTypeAdapter);
    }

    @Override
    protected void initData() {
        if (costType.size() == 0) {
            costType.add(new CostItemBean("码头费"));
            costType.add(new CostItemBean("文件费", CostItemBean.Type.TICKET));
            costType.add(new CostItemBean("铅封费"));
            costType.add(new CostItemBean("电放费"));
            costType.add(new CostItemBean("打单费", CostItemBean.Type.TICKET));
            costType.add(new CostItemBean("AFR", CostItemBean.Type.TICKET));
        }
    }
    @Override
    public void onClick(View v) {
        bean.getSurcharge().clear();
        for (int i = 0; i < costType.size(); i++) {
            if(costGv.isSelects[i]){
                CostItemBean costBean = costType.get(i);
                setCargoList(costBean.getPrices());
                bean.getSurcharge().add(costBean);
            }
        }
        costTypeAdapter.notifyDataSetChanged();
    }
    private void setCargoList(List<BoxBean> cargoList) {
        for (int i = 0; i < CacheDataConstant.orderBean.getConnum().size(); i++) {
            boolean isNew = true;
            for (int j = 0; j < cargoList.size(); j++) {
                if (CacheDataConstant.orderBean.getConnum().get(i).getName().equals(cargoList.get(j).getName())) {
                    isNew = false;
                }
            }
            if (isNew) {
                cargoList.add(CacheDataConstant.orderBean.getConnum().get(i).copy());
            }
        }
    }
}
