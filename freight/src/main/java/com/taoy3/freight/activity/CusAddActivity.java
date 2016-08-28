package com.taoy3.freight.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.R;
import com.taoy3.freight.bean.CustomerBean;
import com.taoy3.freight.util.BaseDataUtils;


public class CusAddActivity extends BaseActivity {
    private EditText nameEnEt;
    private EditText nameZhEt;
    private EditText telEt;
    private EditText mobileEt;
    private EditText emailEt;
    private EditText wechatEt;
    private EditText adrEnEt;
    private EditText adrZhEt;
    private EditText remarkEt;
    private EditText faxEt;
    private RadioGroup typeRg;
    private Button confirmBt;
    public static final int MODIFY = 1;//打开此界面为编辑
    public static final int ADD = 2;//打开此界面为添加
    private int statue;
    private CustomerBean bean;
    private RadioButton[] buttons;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_cus_add);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        nameEnEt = (EditText) findViewById(R.id.cus_detail_name_en);
        nameZhEt = (EditText) findViewById(R.id.cus_deatil_name_zh);
        telEt = (EditText) findViewById(R.id.cus_deatil_tel);
        mobileEt = (EditText) findViewById(R.id.cus_detail_mobile);
        emailEt = (EditText) findViewById(R.id.cus_detail_email);
        wechatEt = (EditText) findViewById(R.id.cus_deatil_wechat);
        adrEnEt = (EditText) findViewById(R.id.cus_detail_address_en);
        adrZhEt = (EditText) findViewById(R.id.cus_detail_address_cn);
        remarkEt = (EditText) findViewById(R.id.cus_deatil_remark);
        faxEt = (EditText) findViewById(R.id.cus_deatil_fax);
        typeRg = (RadioGroup) findViewById(R.id.cus_detail_customer_type);
        buttons = new RadioButton[typeRg.getChildCount()];
        for (int i = 0; i < typeRg.getChildCount(); i++) {
            buttons[i] = (RadioButton) typeRg.getChildAt(i);
        }
        confirmBt = (Button) findViewById(R.id.cus_add_submit);
        confirmBt.setOnClickListener(this);
        checkStatus();
    }
    //检查状态
    private void checkStatus() {
        statue = getIntent().getIntExtra(STATUE, 0);
        switch (statue){
            case MODIFY:
                bean = (CustomerBean) getIntent().getSerializableExtra(CONTACTBEAN);
                break;
            case ADD:
                bean = new CustomerBean();
                break;
            default:
                break;
        }
    }
    @Override
    protected void initData() {
        nameEnEt.setText(bean.getName_en());
        nameZhEt.setText(bean.getName_cn());
        telEt.setText(bean.getTel());
        mobileEt.setText(bean.getMobile());
        emailEt.setText(bean.getEmail());

        wechatEt.setText(bean.getWechat());
        adrEnEt.setText(bean.getAddress_en());
        adrZhEt.setText(bean.getAddress_cn());
        remarkEt.setText(bean.getRemark());
        faxEt.setText(bean.getFax());
        buttons[bean.getType()%3].setChecked(true);
    }
    @Override
    public void onClick(View v) {
        confirmBt.setClickable(false);
        bean.setName_en(nameEnEt.getText().toString());
        bean.setName_cn(nameZhEt.getText().toString());
        bean.setTel(telEt.getText().toString());
        bean.setMobile(mobileEt.getText().toString());
        bean.setEmail(emailEt.getText().toString());
        bean.setWechat(wechatEt.getText().toString());
        bean.setAddress_en(adrEnEt.getText().toString());
        bean.setAddress_cn(adrZhEt.getText().toString());
        bean.setRemark(remarkEt.getText().toString());
        bean.setFax(faxEt.getText().toString());
        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i].isChecked()){
                bean.setType(i);
                break;
            }
        }
        if(bean.getMobile()==null||bean.getMobile().trim().length()==0){
            Toast.makeText(CusAddActivity.this, "请填写手机号", Toast.LENGTH_SHORT).show();
            confirmBt.setClickable(true);
            return;
        }
        try {
            BaseDataUtils.db.saveOrUpdate(bean);
            finish();
        } catch (DbException e) {
            Toast.makeText(CusAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            confirmBt.setClickable(true);
        }
    }
}
