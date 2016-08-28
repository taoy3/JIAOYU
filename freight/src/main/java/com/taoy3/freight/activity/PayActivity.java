package com.taoy3.freight.activity;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.CostAdapter;
import com.taoy3.freight.adapter.CostItemAdapter;
import com.taoy3.freight.adapter.CostTotalAdapter;
import com.taoy3.freight.bean.BoxBean;
import com.taoy3.freight.bean.CostBean;
import com.taoy3.freight.bean.CostItemBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.view.ChildListView;
import com.taoy3.freight.view.MyGridView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PayActivity extends BaseActivity implements CostAdapter.OnItemClick, TextWatcher {
    //费用
    private TextView seaTv;
    private TextView trailerCurTv;
    private TextView trailerTv;
    private TextView customsCurTv;
    private TextView customsTv;
    private EditText otherEt;
    private TextView otherCurTv;
    private ChildListView payLv;
    private CostAdapter costAdapter;
    private RadioButton button;
    //备注
    private TextView markTv;
    private ImageView markIv;
    private LinearLayout remarkLayout;
    private EditText markEt;
    private RadioGroup payWayBg;
    //编辑费用弹出的对话框
    private CargoDialogHolder dialogHolder;
    //合计
    private ListView totalLv;
    private CostTotalAdapter totalAdapter;
    /**
     * 数据
     */
    private CostBean bean;
    private boolean isFast;
    private String TOTAL = "total";

    @Override
    protected void setView() {
        setContentView(R.layout.activity_cost);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        isFast = getIntent().getBooleanExtra(TYPE, false);
        rightView.setText(R.string.save);
        rightView.setOnClickListener(this);

        seaTv = (TextView) findViewById(R.id.cost_sea);
        seaTv.setOnClickListener(this);
        trailerTv = (TextView) findViewById(R.id.cost_trailer);
        trailerTv.setOnClickListener(this);
        trailerCurTv = (TextView) findViewById(R.id.trailer_cost_curr);
        trailerCurTv.setVisibility(View.VISIBLE);
        findViewById(R.id.trailer_cost_currencies).setVisibility(View.GONE);
        customsTv = (TextView) findViewById(R.id.cost_customs);
        customsTv.setOnClickListener(this);
        customsCurTv = (TextView) findViewById(R.id.customs_cost_curr);
        customsCurTv.setVisibility(View.VISIBLE);
        findViewById(R.id.customs_cost_currencies).setVisibility(View.GONE);
        //固定费用及附加费
        payLv = (ChildListView) findViewById(R.id.cost_surcharges_list);
        dialogHolder = new CargoDialogHolder(this);
        otherEt = (EditText) findViewById(R.id.cost_other);
        otherEt.addTextChangedListener(this);
        otherCurTv = (TextView) findViewById(R.id.other_cost_curr);
        otherCurTv.setVisibility(View.VISIBLE);
        findViewById(R.id.other_cost_currencies).setVisibility(View.GONE);
        //备注
        markTv = (TextView) findViewById(R.id.cost_mark_tip);
        markIv = (ImageView) findViewById(R.id.cost_mark);
        markIv.setOnClickListener(this);
        findViewById(R.id.save_remark).setOnClickListener(this);
        remarkLayout = (LinearLayout) findViewById(R.id.remark_layout);
        markEt = (EditText) findViewById(R.id.remark_edit);
        payWayBg = (RadioGroup) findViewById(R.id.cost_pay_way);
        button = (RadioButton) payWayBg.getChildAt(0);
        payWayBg = (RadioGroup) findViewById(R.id.cost_pay_way);
        button = (RadioButton) payWayBg.getChildAt(0);
        button.setChecked(true);
        //合计
        totalLv = (ListView) findViewById(R.id.cost_list_total);
    }

    @Override
    protected void initData() {
        //初始化数据
        bean = CacheDataConstant.orderBean.getPayable();
        bean.getTrailerCost().setPaycurr(CacheDataConstant.orderBean.getReceivable().getTrailerCost().getPaycurr());
        trailerCurTv.setText(bean.getTrailerCost().getPaycurr());
        bean.getCustomsCost().setPaycurr(CacheDataConstant.orderBean.getReceivable().getCustomsCost().getPaycurr());
        customsCurTv.setText(bean.getCustomsCost().getPaycurr());
        bean.getOtherCost().setPaycurr(CacheDataConstant.orderBean.getReceivable().getOtherCost().getPaycurr());
        otherCurTv.setText(bean.getOtherCost().getPaycurr());

        //应收
        if (isFast) {
            for (int i = 0; i < CacheDataConstant.orderBean.getReceivable().getSurcharge().size(); i++) {
                bean.getSurcharge().add(CacheDataConstant.orderBean.getReceivable().getSurcharge().get(i).clone());
            }
            costAdapter = new CostAdapter(this, bean.getSurcharge());
            payLv.setAdapter(costAdapter);
            costAdapter.setOnItemClickListener(this);
        } else {
            //设置GridView
            MyGridView gridView = (MyGridView) findViewById(R.id.cost_surcharges_grid);
            gridView.setVisibility(View.VISIBLE);
            //设置数据
            List<List<String>> lists = new ArrayList<>();
            //设置表头
            List<String> titleList = new ArrayList<>();
            titleList.add("费用名称");
            titleList.add("币种");
            for (int i = 0; i < CacheDataConstant.orderBean.getConnum().size(); i++) {
                titleList.add(CacheDataConstant.orderBean.getConnum().get(i).getName());
            }
            lists.add(titleList);
            for (int i = 1; i < lists.size(); i++) {
                lists.add(new ArrayList<String>());
                CostItemBean itemBean = bean.getSurcharge().get(i);
                lists.get(i).add(itemBean.getFeename());
                lists.get(i).add(itemBean.getPaycurr());
                if (itemBean.getType() == CostItemBean.Type.BOX) {
                    for (int j = 0; j < itemBean.getPrices().size(); j++) {
                        lists.get(i).add(itemBean.getPrices().get(j).getValue() + "");
                    }
                } else {
                    lists.get(i).add(itemBean.getCost() + "");
                }
            }
            if(lists.size()>1){
                gridView.setData(lists, 120);
            }else {
                gridView.setVisibility(View.GONE);
            }
        }

        setCargoList(bean.getOceanCost().getPrices());
        setCargoList(bean.getTrailerCost().getPrices());
        setCargoList(bean.getCustomsCost().getPrices());
        for (int i = 0; i < bean.getSurcharge().size(); i++) {
            setCargoList(bean.getSurcharge().get(i).getPrices());
        }
        trailerCurTv.setText(bean.getTrailerCost().getPaycurr());
        customsCurTv.setText(bean.getCustomsCost().getPaycurr());
        //各项合计
        if (bean.getOceanCost().getCost() != null) {
            seaTv.setText(getString(R.string.total)+ bean.getOceanCost().getCost());
        }
        if (bean.getTrailerCost().getCost() != null) {
            trailerTv.setText(getString(R.string.total) + bean.getTrailerCost().getCost());
        }
        if (bean.getCustomsCost().getCost() != null) {
            customsTv.setText(getString(R.string.total) + bean.getCustomsCost().getCost());
        }
        if (bean.getOtherCost().getCost() != null) {
            otherEt.setText(bean.getOtherCost().getCost() + "");
        }
        //总合计
        totalAdapter = new CostTotalAdapter(this, bean.getTotal());
        totalLv.setAdapter(totalAdapter);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.title_right:
                if (otherEt.getText().toString().trim().length() > 0)
                    bean.getOtherCost().setCost(new BigDecimal(otherEt.getText().toString().trim()));
                bean.setRemark(markEt.getText().toString());
                bean.setPayMethod((payWayBg.getCheckedRadioButtonId() == button.getId()) ? 1 : 0);
                finish();
                break;
            case R.id.cost_sea:
                if(v instanceof TextView){
                    dialogHolder.showDialog((TextView) v, bean.getOtherCost());
                }

                break;
            case R.id.cost_trailer:
                if(v instanceof TextView){
                    dialogHolder.showDialog((TextView) v, bean.getTrailerCost());
                }

                break;
            case R.id.cost_customs:
                if(v instanceof TextView){
                    dialogHolder.showDialog((TextView) v, bean.getCustomsCost());
                }

                break;
            case R.id.save_remark:
                bean.setRemark(markEt.getText().toString());
                remarkLayout.setVisibility(View.GONE);
                markTv.setText(bean.getRemark());
                break;
            case R.id.cost_mark:
                remarkLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0)
            bean.getOtherCost().setCost(new BigDecimal(s.toString().trim()));
        costChange();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class CargoDialogHolder implements View.OnClickListener {
        private Dialog dialog;
        private ListView listView;
        private CostItemAdapter adapter;
        private TextView totalTv;
        private CostItemBean entity;
        private List<BoxBean> boxBeans = new ArrayList<>();
        private TextView costView;

        public CargoDialogHolder(Context context) {
            dialog = new Dialog(context, R.style.NotTitleDialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_item_cargo, null);
            view.findViewById(R.id.item_cargo_confirm).setOnClickListener(this);
//            totalTv = (TextView) view.findViewById(R.id.item_cargo_count);
            listView = (ListView) view.findViewById(R.id.item_list_cost);
            adapter = new CostItemAdapter(context, boxBeans);
            listView.setAdapter(adapter);
            dialog.setContentView(view);
            dialog.setCancelable(true);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialogWindow.setGravity(Gravity.CENTER);
        }

        private void showDialog(TextView costView, CostItemBean entity) {
            totalTv.setText(entity.getPaycurr());
            this.entity = entity;
            this.costView = costView;
            boxBeans.clear();

            if(!entity.getPrices().get(entity.getPrices().size()-1).getName().equals(TOTAL)){
                float total = 0;
                for (int i = 0; i < entity.getPrices().size(); i++) {
                    total +=entity.getPrices().get(i).getNumber()*entity.getPrices().get(i).getValue();
                }
                entity.getPrices().add(new BoxBean(TOTAL,total,0));
            }
            if (entity.getCost().intValue() <= 0) {
                entity.setCost();
            }
            entity.getPrices().get(entity.getPrices().size()-1).setValue(entity.getCost().floatValue());
            boxBeans.addAll(entity.getPrices());
            adapter.notifyDataSetChanged();
            dialog.show();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_cargo_confirm:
                    dialog.dismiss();
                    entity.setCost(new BigDecimal(entity.getPrices().get(entity.getPrices().size()-1).getValue()));
                    costChange();
                    String text = getString(R.string.total) + entity.getCost();
                    costView.setText(text);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemClickListener(View view, int position) {
        dialogHolder.showDialog((TextView) view, bean.getSurcharge().get(position));
    }

    public void costChange() {
        bean.upDateCost();
        if (totalAdapter != null)
            totalAdapter.notifyDataSetChanged();
    }
}
