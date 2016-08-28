package com.taoy3.freight.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.CustomerBean;


public class CusDetailActivity extends BaseActivity {
    private TextView nameEnTv;
    private TextView nameZhTv;
    private TextView telTv;
    private TextView mobileTv;
    private TextView emailTv;
    private TextView wechatTv;
    private TextView adrEnTv;
    private TextView adrZhTv;
    private TextView remarkTv;
    private TextView faxTv;
    private TextView typeTv;
    private CustomerBean bean;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_cus_info);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        nameEnTv = (TextView) findViewById(R.id.cus_detail_name_en);
        nameZhTv = (TextView) findViewById(R.id.cus_deatil_name_zh);
        telTv = (TextView) findViewById(R.id.cus_deatil_tel);
        mobileTv = (TextView) findViewById(R.id.cus_detail_mobile);
        emailTv = (TextView) findViewById(R.id.cus_detail_email);
        wechatTv = (TextView) findViewById(R.id.cus_deatil_wechat);
        adrEnTv = (TextView) findViewById(R.id.cus_detail_address_en);
        adrZhTv = (TextView) findViewById(R.id.cus_detail_address_cn);
        remarkTv = (TextView) findViewById(R.id.cus_deatil_remark);
        faxTv = (TextView) findViewById(R.id.cus_deatil_fax);
        typeTv = (TextView) findViewById(R.id.cus_detail_customer_type);
        findViewById(R.id.cus_add_shipper).setOnClickListener(this);
        findViewById(R.id.cus_add_consignee).setOnClickListener(this);
        findViewById(R.id.cus_add_notify).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        bean = (CustomerBean) getIntent().getSerializableExtra(CONTACTBEAN);
        nameEnTv.setText(bean.getName_en());
        nameZhTv.setText(bean.getName_cn());
        telTv.setText(bean.getTel());
        mobileTv.setText(bean.getMobile());
        emailTv.setText(bean.getEmail());

        wechatTv.setText(bean.getWechat());
        adrEnTv.setText(bean.getAddress_en());
        adrZhTv.setText(bean.getAddress_cn());
        remarkTv.setText(bean.getRemark());
        faxTv.setText(bean.getFax());
        typeTv.setText(getResources().getStringArray(R.array.customer_type)[bean.getType()%3]);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cus_add_shipper:
                startActivityForResult(new Intent(this, AddressListActivity.class).putExtra(TYPE, SHIPPER).putExtra(ID,bean.getId()), SHIPPERCODE);
                break;
            case R.id.cus_add_consignee:
                startActivityForResult(new Intent(this, AddressListActivity.class).putExtra(TYPE, CONSIGNEE).putExtra(ID, bean.getId()), CONSIGNEECODE);
                break;
            case R.id.cus_add_notify:
                startActivityForResult(new Intent(this, AddressListActivity.class).putExtra(TYPE, NOTIFY).putExtra(ID,bean.getId()), NOTIFYCODE);
                break;
            default:
                break;
        }
    }
}
