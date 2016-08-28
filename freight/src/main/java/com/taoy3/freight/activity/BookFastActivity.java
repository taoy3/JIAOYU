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
import android.widget.Button;
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

import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.R;
import com.taoy3.freight.adapter.BoxAdapter;
import com.taoy3.freight.adapter.BoxTypeAdapter;
import com.taoy3.freight.bean.AddressBean;
import com.taoy3.freight.bean.BoxBean;
import com.taoy3.freight.bean.OrderBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.util.BaseDataUtils;
import com.taoy3.freight.util.SQLUtils;
import com.taoy3.freight.view.MulSelView;

import java.util.Arrays;
import java.util.Calendar;

public class BookFastActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, MulSelView.OnItemClickChange {
    //基础信息
    private TextView startTv;
    private TextView destTv;
    private TextView companyTv;
    //日期
    private TextView startDateView;
    private TextView bargeDateView;
    private TextView clsDateView;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_DATA_PICK = 0;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int clickId;
    //箱型
    private ListView boxLv;
    private TextView boxTv;
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
    private Drawable cricleDa;
    //费用清单
    private Spinner goodsTypeSp;
    private TextView accountsListTv;
    private TextView payListTv;

    private OrderBean bean = new OrderBean();
    private Dialog boxDialog;
    //下单相关
    private TextView shipperTv;
    private TextView consigneeTv;
    private Calendar startDate = Calendar.getInstance();
    private MulSelView boxGv;
    private BoxTypeAdapter boxTypeAdapter;
    private Button submitBt;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_book_fast);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        //基础信息
        startTv = (TextView) findViewById(R.id.start);
        destTv = (TextView) findViewById(R.id.dest);
        companyTv = (TextView) findViewById(R.id.company_name);
        startTv.setOnClickListener(this);
        destTv.setOnClickListener(this);
        companyTv.setOnClickListener(this);
        clsDateView = (TextView) findViewById(R.id.cls_date);
        startDateView = (TextView) findViewById(R.id.start_date);
        bargeDateView = (TextView) findViewById(R.id.barge_date);
        clsDateView.setOnClickListener(new DateButtonOnClickListener());
        startDateView.setOnClickListener(new DateButtonOnClickListener());
        bargeDateView.setOnClickListener(new DateButtonOnClickListener());
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //订单相关
        shipperTv = (TextView) findViewById(R.id.shipper);
        consigneeTv = (TextView) findViewById(R.id.consignee);
        shipperTv.setOnClickListener(this);
        consigneeTv.setOnClickListener(this);
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
        cricleDa = getResources().getDrawable(R.mipmap.circle_white);
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
        submitBt = (Button) findViewById(R.id.submit);
        submitBt.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        CacheDataConstant.orderBean = bean;
        String[] goodTypes = {"船东正本", "船东电放", "货代正本", "货代电放", "SWB", "目的港放单"};
        ArrayAdapter<String> goodTypeAdapter = new ArrayAdapter(this, R.layout.adapter_array, goodTypes);
        goodsTypeSp.setAdapter(goodTypeAdapter);
        boxAdapter = new BoxAdapter(this, bean.getConnum());
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
                    cargoTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableUp, null);
                } else {
                    cargoLayout.setVisibility(View.GONE);
                    cargoTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableDown, null);
                }
                break;
            case R.id.save_cargo_info:
                try {
                    bean.getCargoinf().setName(cargoNameEt.getText().toString());
                    bean.getCargoinf().setCbm(cargoBulkEt.getText().toString().trim());
                    bean.getCargoinf().setKgs(cargoWeightEt.getText().toString().trim());
                    cargoLayout.setVisibility(View.GONE);
                    cargoTv.setText(bean.getCargoinf().toString());
                } catch (Exception e) {
                    Toast.makeText(this, "填写错误", Toast.LENGTH_SHORT).show();
                }
                cargoNameEt.setEnabled(false);
                cargoBulkEt.setEnabled(false);
                cargoWeightEt.setEnabled(false);
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
                    if (bean.getConnum().get(i).getNumber()==0) {
                        bean.getConnum().remove(i);
                    }
                }
                boxAdapter.notifyDataSetChanged();
                if (bean.getConnum().size() == 0) {
                    Toast.makeText(this,R.string.cost_list_tip, Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(new Intent(this, AccountsActivity.class).putExtra(TYPE, true), ACCOUNTSCODE);
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
                    startActivityForResult(new Intent(this, PayActivity.class).putExtra(TYPE, true), PAYCODE);
                }
                break;
            case R.id.start:
                startActivityForResult(new Intent(this, PortActivity.class), STARTPORTCODE);
                break;
            case R.id.dest:
                startActivityForResult(new Intent(this, PortActivity.class), DESTPORTCODE);
                break;
            case R.id.company_name:
                startActivityForResult(new Intent(this, SearchScActivity.class), SHIPCOMPANYCODE);
                break;
            case R.id.shipper:
                startActivityForResult(new Intent(this, AddressListActivity.class).putExtra(TYPE, SHIPPER), SHIPPERCODE);
                break;
            case R.id.consignee:
                startActivityForResult(new Intent(this, AddressListActivity.class).putExtra(TYPE, CONSIGNEE), CONSIGNEECODE);
                break;
            //提交
            case R.id.submit:
                submitBt.setClickable(false);
                submitOrder();
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
        } catch (DbException e) {
            Toast.makeText(this, "fail", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //起始港返回
            case STARTPORTCODE:
                String startPort = data.getStringExtra(STARTPORT);
                if (startPort == null) {
                    return;
                }
                bean.setStart_name(startPort);
                bean.setStart_id(SQLUtils.queryPortId(startPort));
                if (bean.getStart_id()!=null)
                    startTv.setText(startPort);
                break;
            //目的港返回
            case DESTPORTCODE:
                String destPort = data.getStringExtra(DESTPORT);
                if (destPort == null) {
                    return;
                }
                bean.setDes_name(destPort);
                bean.setDes_id(SQLUtils.queryPortId(destPort));
                if (bean.getDes_id() != null) {
                    destTv.setText(destPort);
                }
                break;
            //船公司返回
            case SHIPCOMPANYCODE:
                String shipCompanyCode = data.getStringExtra(CODE);
                if (shipCompanyCode == null) {
                    return;
                }
                bean.setSc_name(shipCompanyCode);
                bean.setSc_id(data.getStringExtra(SCID));
                if (bean.getSc_id() != null)
                    companyTv.setText(shipCompanyCode);
                break;
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

    private void showBoxDialog() {
        if (boxDialog == null) {
            boxDialog = new Dialog(this, R.style.NotTitleDialog);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_window_box, null);
            boxGv = (MulSelView) view.findViewById(R.id.box_type);
            boxTypeAdapter = new BoxTypeAdapter(this, Arrays.asList(Config.BOXNAMES));
            boxGv.setAdapter(boxTypeAdapter);
            boxGv.setItemCliCKChange(this);
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
    public void itemClickChange(int position, boolean isSelect) {
        if (isSelect) {
            bean.getConnum().add(new BoxBean(Config.BOXNAMES[position], 1));
        } else {
            for (int j = 0; j < bean.getConnum().size(); j++) {
                if (bean.getConnum().get(j).getName().equals(Config.BOXNAMES[position])) {
                    bean.getConnum().remove(j);
                }
            }
        }
        boxAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, cricleDa, null);
        } else {
            buttonView.setCompoundDrawablesWithIntrinsicBounds(cricleDa, null, null, null);
        }
        if (buttonView.getId() == R.id.cargo_trailer) {
            if (trailerLayout.getVisibility() == View.GONE) {
                trailerLayout.setVisibility(View.VISIBLE);
            } else {
                trailerLayout.setVisibility(View.GONE);
            }
        }
    }

    private final String[] WEEK = {" Sun", " Mon", " Tue", " Wed", " Thu", " Fri", " Sat"};
    private Calendar calendar;
    //日期控件的事件
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            calendar = Calendar.getInstance();
            calendar.set(mYear, mMonth, mDay);
            String date = mYear + "-" + (mMonth + 1) + "-" + mDay;
            updateDisplay(date);
        }
    };

    //更新日期
    private void updateDisplay(String date) {
        switch (clickId) {
            case R.id.cls_date:
                bean.setCls(date);
                startDate.set(mYear, mMonth, mDay);
                clsDateView.setText(date + WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
                break;
            case R.id.start_date:
                Calendar clsDate = Calendar.getInstance();
                clsDate.set(mYear, mMonth, mDay);
                if (startDate.after(clsDate)) {
                    Toast.makeText(this, "开船日期必须晚于截关日期", Toast.LENGTH_SHORT).show();
                } else {
                    startDateView.setText(date + WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
                    bean.setEtd(date);
                }
                break;
            case R.id.barge_date:
                bargeDateView.setText(date + WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
                bean.setBgt(date);
                break;
            default:
                break;
        }
    }

    //选择日期Button的事件处理
    class DateButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Message msg = new Message();
            clickId = v.getId();
            msg.what = BookFastActivity.SHOW_DATA_PICK;
            BookFastActivity.this.saleHandler.sendMessage(msg);
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
                case BookFastActivity.SHOW_DATA_PICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                default:
                    break;
            }
        }
    };
}
