package com.taoy3.freight.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.BoxAdapter;
import com.taoy3.freight.bean.AddressBean;
import com.taoy3.freight.bean.BoxBean;
import com.taoy3.freight.bean.CostItemBean;
import com.taoy3.freight.bean.OrderBean;
import com.taoy3.freight.bean.SurchargesEntity;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.util.BaseDataUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    //基础信息
    private TextView startTv;
    private TextView destTv;
    private TextView companyTv;
    //日期
    private final String[] WEEK = {" Sun", " Mon", " Tue", " Wed", " Thu", " Fri", " Sat"};
    private TextView startDateView;
    private TextView bargeDateView;
    private TextView clsDateView;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_DATA_PICK = 0;
    private int mYear;
    private int mMonth;
    private int mDay;
    //下单相关
    private TextView shipperTv;
    private TextView consigneeTv;
    //箱型
    private TextView boxTv;
    private ListView boxLv;
    private BoxAdapter boxAdapter;
    //货物信息
    private TextView cargoTv;
    private LinearLayout cargoLayout;
    private EditText cargoNameEt;
    private EditText cargoBulkEt;//货物体积
    private EditText cargoWeightEt;
    private TextView cargoSava;
    private TextView cargoCancel;

    //备注
    private TextView markTv;
    private ImageView markIv;
    private LinearLayout markLayout;
    private EditText markEt;
    private TextView markSave;
    private TextView markCancel;

    //拖车报关
    private CheckBox customsCb;
    private CheckBox trailerCb;
    private LinearLayout trailerLayout;
    private EditText trailerName;
    private EditText trailerPhone;
    private EditText trailerAddress;
    private TextView trailerSave;
    private TextView trailerCancel;
    private Drawable drawableDown;
    private Drawable drawableUp;
    private Drawable circleDa;
    //费用清单
    private Spinner goodsTypeSp;
    private TextView accountsListTv;
    private TextView payListTv;

    protected OrderBean bean = new OrderBean();
    private Dialog boxDialog;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_book);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        //基础信息
        startTv = (TextView) findViewById(R.id.start);
        destTv = (TextView) findViewById(R.id.dest);
        companyTv = (TextView) findViewById(R.id.company_name);
        //订单相关
        shipperTv = (TextView) findViewById(R.id.shipper);
        consigneeTv = (TextView) findViewById(R.id.consignee);
        shipperTv.setOnClickListener(this);
        consigneeTv.setOnClickListener(this);
        //日期相关
        clsDateView = (TextView) findViewById(R.id.cls_date);
        startDateView = (TextView) findViewById(R.id.start_date);
        bargeDateView = (TextView) findViewById(R.id.barge_date);
        bargeDateView.setOnClickListener(new DateButtonOnClickListener());
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //箱型
        boxTv = (TextView) findViewById(R.id.list_box);
        boxTv.setOnClickListener(this);
        //货物信息
        cargoTv = (TextView) findViewById(R.id.cargo_info);
        cargoTv.setOnClickListener(this);
        cargoLayout = (LinearLayout) findViewById(R.id.cargo_info_layout);
        cargoNameEt = (EditText) findViewById(R.id.cargo_name);
        cargoBulkEt = (EditText) findViewById(R.id.cargo_bulk);
        cargoWeightEt = (EditText) findViewById(R.id.cargo_weight);
        cargoSava = (TextView) findViewById(R.id.save_cargo_info);
        cargoSava.setOnClickListener(this);
        cargoCancel = (TextView) findViewById(R.id.cancel_cargo_info);
        cargoCancel.setOnClickListener(this);
        //备注
        markTv = (TextView) findViewById(R.id.cargo_mark_tip);
        markIv = (ImageView) findViewById(R.id.cargo_mark);
        markIv.setOnClickListener(this);
        markEt = (EditText) findViewById(R.id.remark_edit);
        markLayout = (LinearLayout) findViewById(R.id.remark_layout);
        markSave = (TextView) findViewById(R.id.save_remark);
        markSave.setOnClickListener(this);
        markCancel = (TextView) findViewById(R.id.cancel_remark);
        markCancel.setOnClickListener(this);
        //拖车报关
        customsCb = (CheckBox) findViewById(R.id.cargo_customs);
        trailerCb = (CheckBox) findViewById(R.id.cargo_trailer);
        customsCb.setOnCheckedChangeListener(this);
        trailerCb.setOnCheckedChangeListener(this);
        drawableDown = this.getResources().getDrawable(android.R.drawable.arrow_down_float);
        drawableUp = this.getResources().getDrawable(android.R.drawable.arrow_up_float);
        circleDa = getResources().getDrawable(R.mipmap.circle_white);
        trailerLayout = (LinearLayout) findViewById(R.id.trailer_layout);
        trailerName = (EditText) findViewById(R.id.trailer_name);
        trailerPhone = (EditText) findViewById(R.id.trailer_phone);
        trailerAddress = (EditText) findViewById(R.id.trailer_address);
        trailerSave = (TextView) findViewById(R.id.save_trailer);
        trailerSave.setOnClickListener(this);
        trailerCancel = (TextView) findViewById(R.id.cancel_trailer);
        trailerCancel.setOnClickListener(this);
        //费用清单
        goodsTypeSp = (Spinner) findViewById(R.id.goods_type);
        accountsListTv = (TextView) findViewById(R.id.accounts_list);
        accountsListTv.setOnClickListener(this);
        payListTv = (TextView) findViewById(R.id.pay_list);
        payListTv.setOnClickListener(this);
        //合计提交
        findViewById(R.id.submit).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        CacheDataConstant.orderBean = bean;
        //获取数据
        bean.setStart_name(CacheDataConstant.price.getStartPort());
//        bean.setStart_id(CacheDataConstant.price.getStartId());
//        bean.setDes_name(CacheDataConstant.price.getDestPort());
//        bean.setDes_id(CacheDataConstant.price.getDestId());
//        bean.setSc_name(CacheDataConstant.price.getSc_name());
//        bean.setSc_id(CacheDataConstant.price.getSc_id());
        bean.setCls(CacheDataConstant.price.getCls() + "");
        bean.setEtd(CacheDataConstant.price.getEtd() + "");
        startTv.setText(bean.getStart_name());
        destTv.setText(bean.getDes_name());
        companyTv.setText(bean.getSc_name());
        clsDateView.setText(bean.getCls());
        startDateView.setText(bean.getEtd());
        //初始化附加费
        List<BoxBean> price = CacheDataConstant.price.getBoxBeans();
        bean.getConnum().addAll(price);
        List<SurchargesEntity> surcharges = CacheDataConstant.price.getSurcharges();
        for (int i = 0; i < surcharges.size(); i++) {
            CostItemBean costBean = new CostItemBean();
            costBean.setPaycurr(surcharges.get(i).getCurr());
            if (surcharges.get(i).getTprice() == 0) {
                costBean.setFeename(surcharges.get(i).getName());
                if (surcharges.get(i).getGp20() != 0 && surcharges.get(i).getGp20() > 0) {
                    costBean.getPrices().add(new BoxBean(Config.GP20, surcharges.get(i).getGp20(), 1));
                }
                if (surcharges.get(i).getGp40() != 0 && surcharges.get(i).getGp40() > 0) {
                    costBean.getPrices().add(new BoxBean(Config.GP40, surcharges.get(i).getGp40(), 1));
                }
                if (surcharges.get(i).getHq40() != 0 && surcharges.get(i).getHq40() > 0) {
                    costBean.getPrices().add(new BoxBean(Config.HQ40, surcharges.get(i).getHq40(), 1));
                }
            } else {
                costBean.setType(CostItemBean.Type.TICKET);
                costBean.setFeename(surcharges.get(i).getName());
                costBean.setCost(new BigDecimal(surcharges.get(i).getTprice()));
            }
            bean.getReceivable().getSurcharge().add(costBean);
            bean.getPayable().getSurcharge().add(costBean.clone());
        }
        String[] goodTypes = {"船东正本", "船东电放", "货代正本", "货代电放", "SWB", "目的港放单"};
        ArrayAdapter<String> goodTypeAdapter = new ArrayAdapter(this, R.layout.adapter_array, goodTypes);
        goodsTypeSp.setAdapter(goodTypeAdapter);
        boxAdapter = new BoxAdapter(this, bean.getConnum());
        boxTv.setText(bean.tipConnum());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_box:
                showBoxDialog();
                break;
            case R.id.confirm_box:
                boxTv.setText(bean.tipConnum());
                boxDialog.dismiss();
                break;
            //货物信息
            case R.id.cargo_info:
                if (cargoLayout.getVisibility() == View.GONE) {
                    cargoLayout.setVisibility(View.VISIBLE);
                    cargoTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableDown, null);
                } else {
                    cargoLayout.setVisibility(View.GONE);
                    cargoTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableUp, null);
                }
                break;
            case R.id.save_cargo_info:
                try {
                    bean.getCargoinf().setName(cargoNameEt.getText().toString());
                    bean.getCargoinf().setCbm(cargoBulkEt.getText().toString().trim());
                    bean.getCargoinf().setKgs(cargoWeightEt.getText().toString().trim());
                    cargoTv.setText(bean.getCargoinf().toString());
                } catch (Exception e) {
                    Toast.makeText(this, "填写错误", Toast.LENGTH_SHORT).show();
                }
                cargoNameEt.setEnabled(false);
                cargoBulkEt.setEnabled(false);
                cargoWeightEt.setEnabled(false);
                cargoLayout.setVisibility(View.GONE);
                cargoTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableDown, null);
                break;
            case R.id.cancel_cargo_info:
                cargoNameEt.setEnabled(true);
                cargoBulkEt.setEnabled(true);
                cargoWeightEt.setEnabled(true);
                cargoLayout.setVisibility(View.GONE);
                cargoTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableUp, null);
                break;
            //备注
            case R.id.cargo_mark:
                if (markLayout.getVisibility() == View.VISIBLE) {
                    markLayout.setVisibility(View.GONE);
                } else {
                    markLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.save_remark:
                markEt.setEnabled(false);
                bean.setRemark(markEt.getText().toString());
                markLayout.setVisibility(View.GONE);
                markTv.setText(bean.getRemark());
                break;
            case R.id.cancel_remark:
                markEt.setEnabled(true);
                bean.setRemark(null);
                markLayout.setVisibility(View.GONE);
                break;
            //拖车报关
            case R.id.save_trailer:
                trailerName.setEnabled(false);
                trailerPhone.setEnabled(false);
                trailerAddress.setEnabled(false);
                bean.getContacts().getTrailer().setName(trailerName.getText().toString());
                bean.getContacts().getTrailer().setTel(trailerPhone.getText().toString());
                bean.getContacts().getTrailer().setAddr(trailerAddress.getText().toString());
                trailerLayout.setVisibility(View.GONE);
                break;
            case R.id.cancel_trailer:
                trailerName.setEnabled(true);
                trailerPhone.setEnabled(true);
                trailerAddress.setEnabled(true);
                bean.getContacts().getTrailer().setName(null);
                bean.getContacts().getTrailer().setTel(null);
                bean.getContacts().getTrailer().setAddr(null);
                trailerLayout.setVisibility(View.GONE);
                break;
            //应收费用清单
            case R.id.accounts_list:
                for (int i = 0; i < bean.getConnum().size(); i++) {
                    if (bean.getConnum().get(i).getNumber() == 0) {
                        bean.getConnum().remove(i);
                    }
                }
                boxAdapter.notifyDataSetChanged();
                if (bean.getConnum().size() == 0) {
                    Toast.makeText(this, R.string.cost_list_tip, Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(new Intent(this, AccountsActivity.class), ACCOUNTSCODE);
                }
                break;
            //应付费用清单
            case R.id.pay_list:
                for (int i = 0; i < bean.getConnum().size(); i++) {
                    if (bean.getConnum().get(i).getNumber() == 0) {
                        bean.getConnum().remove(i);
                    }
                }
                boxAdapter.notifyDataSetChanged();
                if (bean.getConnum().size() == 0) {
                    Toast.makeText(this, R.string.cost_list_tip, Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(new Intent(this, PayActivity.class), PAYCODE);
                }
                break;
            //提交
            case R.id.submit:
                submitOrder();
                break;
            case R.id.shipper:
                startActivityForResult(new Intent(this, AddressListActivity.class).putExtra(TYPE, SHIPPER), SHIPPERCODE);
                break;
            case R.id.consignee:
                startActivityForResult(new Intent(this, AddressListActivity.class).putExtra(TYPE, CONSIGNEE), CONSIGNEECODE);
                break;
            default:
                break;
        }
    }

    private void submitOrder() {
        bean.setDeclare(customsCb.isChecked() ? 1 : 0);
        bean.setTrail(trailerCb.isChecked() ? 1 : 0);
        bean.setRelease_type(goodsTypeSp.getSelectedItemPosition());
        OrderBean orderBean = bean.copy();
        orderBean.getContacts().setNotify(orderBean.getContacts().getConsignee());
        try {
            BaseDataUtils.db.save(orderBean);
            Toast.makeText(this, "ok", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBoxDialog() {
        if (boxDialog == null) {
            boxDialog = new Dialog(this, R.style.NotTitleDialog);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_window_box, null);
            view.findViewById(R.id.box_type).setVisibility(View.GONE);
            view.findViewById(R.id.confirm_box).setOnClickListener(this);
            boxLv = (ListView) view.findViewById(R.id.list_box);
            boxLv.setAdapter(boxAdapter);
            boxDialog.setContentView(view);
            boxDialog.setCancelable(true);

            Window dialogWindow = boxDialog.getWindow();
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialogWindow.setGravity(Gravity.CENTER);
        }
        boxDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //发货人返回
            case SHIPPERCODE:
                AddressBean shipper = (AddressBean) data.getSerializableExtra(TYPE);
                if (shipper == null) {
                    return;
                }
                shipperTv.setText(shipper.getName());
                bean.getContacts().setShipper(shipper);
                break;
            //收货人返回
            case CONSIGNEECODE:
                AddressBean consignee = (AddressBean) data.getSerializableExtra(TYPE);
                if (consignee == null) {
                    return;
                }
                consigneeTv.setText(consignee.getName());
                bean.getContacts().setConsignee(consignee);
                break;
            //应收费用清单返回
            case ACCOUNTSCODE:
                accountsListTv.setText("总计：" + bean.getReceivable().getTotal().toString().replace("[", "").replace("]", ""));
                break;
            //应付费用清单返回
            case PAYCODE:
                payListTv.setText("总计：" + bean.getPayable().getTotal().toString().replace("[", "").replace("]", ""));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, circleDa, null);
        } else {
            buttonView.setCompoundDrawablesWithIntrinsicBounds(circleDa, null, null, null);
        }
        if (buttonView.getId() == R.id.cargo_trailer) {
            if (trailerLayout.getVisibility() == View.GONE) {
                trailerLayout.setVisibility(View.VISIBLE);
            } else {
                trailerLayout.setVisibility(View.GONE);
            }
        }
    }

    //日期控件的事件
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            Calendar calendar = Calendar.getInstance(Locale.CHINESE);
            calendar.set(mYear, mMonth, mDay);
            String date = mYear + "-" + (mMonth + 1) + "-" + mDay;
            bean.setBgt(date);
            bargeDateView.setText(date + WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        }
    };

    //选择日期Button的事件处理
    class DateButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Message msg = new Message();
            msg.what = BookActivity.SHOW_DATA_PICK;
            BookActivity.this.saleHandler.sendMessage(msg);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            default:
                break;
        }
    }

    //处理日期控件的Handler
    Handler saleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BookActivity.SHOW_DATA_PICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                default:
                    break;
            }
        }
    };
}
