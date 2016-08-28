package com.taoy3.freight.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.R;
import com.taoy3.freight.bean.AddressBean;
import com.taoy3.freight.util.BaseDataUtils;

public class AddressDetailActivity extends BaseActivity {
    private EditText companyEt;
    private EditText addressEt;
    private EditText phoneEt;
    private EditText zipEt;
    private EditText faxEt;
    private Button confirmBt;
    private AddressBean bean;
    private int statue;
    public static final int VIEW = 0;//打开此界面为查看
    public static final int MODIFY = 1;//打开此界面为编辑
    public static final int ADD = 2;//打开此界面为添加
    private String type;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_address_detail);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        rightView.setOnClickListener(this);
        type = getIntent().getStringExtra(BaseActivity.TYPE);
        companyEt = (EditText) findViewById(R.id.company);
        addressEt = (EditText) findViewById(R.id.address);
        phoneEt = (EditText) findViewById(R.id.phone);
        zipEt = (EditText) findViewById(R.id.zip);
        faxEt = (EditText) findViewById(R.id.cus_deatil_fax);
        confirmBt = (Button) findViewById(R.id.address_confirm);
        confirmBt.setOnClickListener(this);
        checkStatus();
    }
    //检查状态
    private void checkStatus() {
        statue = getIntent().getIntExtra(STATUE, 0);
        switch (statue){
            case VIEW:
                setUneditable();
                bean = (AddressBean) getIntent().getSerializableExtra(CONTACTBEAN);
                break;
            case MODIFY:
                bean = (AddressBean) getIntent().getSerializableExtra(CONTACTBEAN);
                break;
            case ADD:
                bean = new AddressBean();
                break;
            default:
                break;
        }
    }

    private void setUneditable() {
        companyEt.setEnabled(false);
        addressEt.setEnabled(false);
        phoneEt.setEnabled(false);
        zipEt.setEnabled(false);
        faxEt.setEnabled(false);
        confirmBt.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        companyEt.setText(bean.getName());
        phoneEt.setText(bean.getTel());
        addressEt.setText(bean.getAddr());
        faxEt.setText(bean.getFax());
        zipEt.setText(bean.getZip());
    }

    @Override
    public void onClick(View v) {
        confirmBt.setClickable(false);
        bean.setName(companyEt.getText().toString());
        bean.setTel(phoneEt.getText().toString());
        bean.setAddr(addressEt.getText().toString());
        bean.setZip(zipEt.getText().toString());
        bean.setFax(faxEt.getText().toString());
        bean.setType(type);
        if(bean.getTel()==null||bean.getTel().trim().length()==0){
            Toast.makeText(AddressDetailActivity.this, "tel不能为空", Toast.LENGTH_SHORT).show();
            confirmBt.setClickable(true);
            return;
        }
        //判断状态
        switch (statue) {
            case MODIFY:
            case ADD:
                try {
                    BaseDataUtils.db.saveOrUpdate(bean);
                    Toast.makeText(AddressDetailActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (DbException e) {
                    Toast.makeText(AddressDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    confirmBt.setClickable(true);
                }
                break;
            default:
                break;
        }
    }
}