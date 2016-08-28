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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.CostAdapter;
import com.taoy3.freight.adapter.CostItemAdapter;
import com.taoy3.freight.adapter.CostTotalAdapter;
import com.taoy3.freight.adapter.CostTypeAdapter;
import com.taoy3.freight.adapter.SimpleAdapter;
import com.taoy3.freight.bean.BoxBean;
import com.taoy3.freight.bean.CostBean;
import com.taoy3.freight.bean.CostItemBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.MyColor;
import com.taoy3.freight.listener.CostChangeListener;
import com.taoy3.freight.view.ChildListView;
import com.taoy3.freight.view.MulSelView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountsActivity extends BaseActivity implements CostAdapter.OnItemClick, AdapterView.OnItemSelectedListener, TextWatcher, CostChangeListener, AdapterView.OnItemClickListener {
    //费用
    private TextView seaTv;
    private Spinner trailerSp;
    private TextView trailerTv;
    private Spinner customsSp;
    private TextView customsTv;
    private TextView addCostTypeTv;
    private ChildListView accountsLv;
    private CostAdapter costAdapter;
    private EditText otherEt;
    private Spinner otherSp;
    //备注
    private TextView markTv;
    private ImageView markIv;
    private LinearLayout remarkLayout;
    private EditText markEt;
    //付款方式
    private RadioGroup payWayBg;
    private RadioButton button;
    //新添费用弹出的对话框
    private Dialog costDialog;
    private MulSelView costGv;
    public final static List<CostItemBean> costType = new ArrayList<>();
    private CostTypeAdapter costTypeAdapter;
    //编辑费用弹出的对话框
    private CargoDialogHolder dialogHolder;
    //合计
    private ListView totalLv;
    private CostTotalAdapter totalAdapter;
    /**
     * 数据
     */
    private CostBean bean;
    //判断是不是直接下单
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
        trailerSp = (Spinner) findViewById(R.id.trailer_cost_currencies);
        trailerSp.setOnItemSelectedListener(this);
        customsTv = (TextView) findViewById(R.id.cost_customs);
        customsTv.setOnClickListener(this);
        customsSp = (Spinner) findViewById(R.id.customs_cost_currencies);
        customsSp.setOnItemSelectedListener(this);
        addCostTypeTv = (TextView) findViewById(R.id.box_add);
        if (isFast) {
            addCostTypeTv.setVisibility(View.VISIBLE);
            addCostTypeTv.setOnClickListener(this);
        } else {
            addCostTypeTv.setVisibility(View.INVISIBLE);
        }
        //固定费用及附加费
        accountsLv = (ChildListView) findViewById(R.id.cost_surcharges_list);
        dialogHolder = new CargoDialogHolder(this);
        otherEt = (EditText) findViewById(R.id.cost_other);
        otherEt.addTextChangedListener(this);
        otherSp = (Spinner) findViewById(R.id.other_cost_currencies);
        otherSp.setOnItemSelectedListener(this);
        //备注
        markTv = (TextView) findViewById(R.id.cost_mark_tip);
        markIv = (ImageView) findViewById(R.id.cost_mark);
        markIv.setOnClickListener(this);
        findViewById(R.id.save_remark).setOnClickListener(this);
        remarkLayout = (LinearLayout) findViewById(R.id.remark_layout);
        markEt = (EditText) findViewById(R.id.remark_edit);
        //付款方式
        payWayBg = (RadioGroup) findViewById(R.id.cost_pay_way);
        button = (RadioButton) payWayBg.getChildAt(0);
        button.setChecked(true);
        //合计
        totalLv = (ListView) findViewById(R.id.cost_list_total);
    }
    @Override
    protected void initData() {
        //初始化数据
        bean = CacheDataConstant.orderBean.getReceivable();
        //应收
        costAdapter = new CostAdapter(this, bean.getSurcharge());
        costAdapter.setIsFast(isFast);
        accountsLv.setAdapter(costAdapter);
        costAdapter.setOnItemClickListener(this);
        //更新箱型
        setCargoList(bean.getOceanCost().getPrices());
        setCargoList(bean.getTrailerCost().getPrices());

        trailerSp.setAdapter(new SimpleAdapter(this, CacheDataConstant.currCodes));
        trailerSp.setSelection(CacheDataConstant.currCodes.indexOf(bean.getTrailerCost().getPrices()));
        setCargoList(bean.getCustomsCost().getPrices());
        customsSp.setAdapter(new SimpleAdapter(this, CacheDataConstant.currCodes));
        customsSp.setSelection(CacheDataConstant.currCodes.indexOf(bean.getCustomsCost().getPaycurr()));
        otherSp.setAdapter(new SimpleAdapter(this,  CacheDataConstant.currCodes));
        otherSp.setSelection(CacheDataConstant.currCodes.indexOf(bean.getOtherCost().getPaycurr()));
        for (int i = 0; i < bean.getSurcharge().size(); i++) {
            setCargoList(bean.getSurcharge().get(i).getPrices());
        }
        //点击费用列表弹出框数据
        if (costType.size() == 0) {
            costType.add(new CostItemBean("码头费"));
            costType.add(new CostItemBean("铅封费"));
            costType.add(new CostItemBean("打单费"));
            costType.add(new CostItemBean("ISPS"));
            costType.add(new CostItemBean("订舱费"));
            costType.add(new CostItemBean("港建费"));
            costType.add(new CostItemBean("操作费"));
            costType.add(new CostItemBean("安保费"));
            costType.add(new CostItemBean("箱单费"));
            costType.add(new CostItemBean("EDI"));
            costType.add(new CostItemBean("AMS", CostItemBean.Type.TICKET));
            costType.add(new CostItemBean("文件费", CostItemBean.Type.TICKET));
            costType.add(new CostItemBean("舱单费", CostItemBean.Type.TICKET));
            costType.add(new CostItemBean("电放费", CostItemBean.Type.TICKET));
            costType.add(new CostItemBean("ENS", CostItemBean.Type.TICKET));
            costType.add(new CostItemBean("AFR", CostItemBean.Type.TICKET));
        }
        //各项合计
        if (bean.getOceanCost().getCost() != null) {
            seaTv.setText(getString(R.string.total) + bean.getOceanCost().getCost());
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
                dialogHolder.showDialog((TextView) v, bean.getOceanCost());
                break;
            case R.id.cost_trailer:
                dialogHolder.showDialog((TextView) v, bean.getTrailerCost());
                break;
            case R.id.cost_customs:
                dialogHolder.showDialog((TextView) v, bean.getCustomsCost());
                break;
            //点击弹出添加附加费对话框
            case R.id.box_add:
                showCostDialog();
                break;
            //添加费用对话框的确认键
            case R.id.cost_confirm:
                costDialog.dismiss();
                bean.getSurcharge().clear();
                for (int i = 0; i < costType.size(); i++) {
                    if (costGv.isSelects[i]) {
                        CostItemBean costBean = costType.get(i);
                        setCargoList(costBean.getPrices());
                        bean.getSurcharge().add(costBean);
                    }
                }
                costAdapter.notifyDataSetChanged();
                costTypeAdapter.notifyDataSetChanged();
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

    //添加附加费对话框
    private void showCostDialog() {
        if (costDialog == null) {
            costDialog = new Dialog(this, R.style.NotTitleDialog);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_window_cost, null);
            view.findViewById(R.id.cost_confirm).setOnClickListener(this);
            costGv = (MulSelView) view.findViewById(R.id.cost_type);
            costTypeAdapter = new CostTypeAdapter(this, costType);
            costGv.setAdapter(costTypeAdapter);
            costDialog.setContentView(view);
            costDialog.setCancelable(true);
            Window dialogWindow = costDialog.getWindow();
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialogWindow.setGravity(Gravity.CENTER);
        }
        costDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        costType.get(position).setSelect(!costType.get(position).getSelect());
        if (costType.get(position).getSelect()) {
            TextView nameTv = (TextView) view.findViewById(R.id.cost_type_name);
            nameTv.setTextColor(MyColor.WHITE);
            nameTv.setBackgroundColor(MyColor.BLUE);
        } else {
            TextView nameTv = (TextView) view.findViewById(R.id.cost_type_name);
            nameTv.setTextColor(MyColor.GRAY);
            nameTv.setBackgroundColor(MyColor.WHITE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.trailer_cost_currencies:
                bean.getTrailerCost().setPaycurr(CacheDataConstant.currCodes.get(position));
                break;
            case R.id.customs_cost_currencies:
                bean.getCustomsCost().setPaycurr(CacheDataConstant.currCodes.get(position));
                break;
            case R.id.other_cost_currencies:
                bean.getOtherCost().setPaycurr(CacheDataConstant.currCodes.get(position));
                break;
            default:
                break;
        }
        costChange();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0){
            bean.getOceanCost().setCost(new BigDecimal(s.toString()));
            costChange();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    //编辑费用对话框
    private class CargoDialogHolder implements View.OnClickListener {
        private Dialog dialog;
        private ListView listView;
        private CostItemAdapter adapter;
//        private TextView totalTv;
        private CostItemBean entity;
        private List<BoxBean> boxBeans = new ArrayList<>();
        private TextView costView;

        public CargoDialogHolder(Context context) {
            dialog = new Dialog(context, R.style.NotTitleDialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_item_cargo, null);
            view.findViewById(R.id.item_cargo_confirm).setOnClickListener(this);
            listView = (ListView) view.findViewById(R.id.item_list_cost);
            adapter = new CostItemAdapter(context, boxBeans);
            listView.setAdapter(adapter);
            dialog.setContentView(view);
            dialog.setCancelable(true);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialogWindow.setGravity(Gravity.CENTER);
        }

        /**
         * @param costView 　编辑完成后需要显示总金额的控件
         * @param entity   　需要更新的数据
         */
        private void showDialog(TextView costView, CostItemBean entity) {
            if (totalAdapter != null) {
//                totalTv.setText(entity.getPaycurr());
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
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_cargo_confirm:
                    dialog.dismiss();
                    //每项费用的总额以合计里面的数据为准(合计的金额先是根据各单项计算，如果用户再编辑则以编辑后的为准)
                    entity.setCost(new BigDecimal(entity.getPrices().get(entity.getPrices().size()-1).getValue()));
                    costChange();
                    costView.setText(getString(R.string.total)+ entity.getCost());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if(view instanceof TextView) {
            dialogHolder.showDialog((TextView) view, bean.getSurcharge().get(position));
        }
    }

    //当每项费用编辑完成后，更新总费用
    public void costChange() {
        bean.upDateCost();
        if (totalAdapter != null)
            totalAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        dialogHolder = null;
        costDialog = null;
        super.onDestroy();
    }
}
