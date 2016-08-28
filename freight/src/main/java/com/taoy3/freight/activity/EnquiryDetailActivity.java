package com.taoy3.freight.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.PriceEntity;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.netBean.GeoUtil;
import com.taoy3.freight.view.ChildGridView;
import com.taoy3.freight.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class EnquiryDetailActivity extends BaseActivity{

    private TextView startTv;
    private TextView destTv;

    private TextView actionbarRightTv;
    private TextView companyNameTv;
    private TextView st_portTv;
    private TextView remarkTv;
    private ChildGridView detailGv;
    private EditText remarkEt;
    private MyGridView surchargesLv;

    private String allStartPort;
    private String allDestPort;
    private String companyName;
    private PriceEntity entity;
    private String tempPrice ="{\"id\":\"d900ed6b-2c7e-4aad-82c5-f81a695520a3\",\"start\":\"SHEKOU\",\"startid\":\"e4f6ec89-44f4-4468-8feb-7ed9bd1f93e4\",\"destid\":\"d158985f-fb78-4ba1-9aa4-d173e606e6f8\",\"dest\":\"BUSAN\",\"scid\":\"a99e732c-6db8-4128-a26a-a7426dc02179\",\"sccode\":\"CMA\",\"prices\":[{\"name\":\"20GP\",\"value\":\"10\"},{\"name\":\"40GP\",\"value\":\"20\"},{\"name\":\"40HQ\",\"value\":\"20\"}],\"ruleid\":\"b99c0a29-d4c9-4083-9820-fae5359659f4\",\"surcharges\":[],\"svalid\":\"2015-12-01\",\"evalid\":\"2015-12-31\",\"routecode\":\"KPS \",\"tt\":5,\"cls\":7,\"etd\":2,\"state\":2,\"valid\":1}";


    @Override
    protected void setView() {
        setContentView(R.layout.activity_enquiry_detail);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        rightView.setText(R.string.order);
        rightView.setOnClickListener(this);
        startTv = (TextView) findViewById(R.id.start_port);
        destTv = (TextView) findViewById(R.id.dest_port);
        companyNameTv = (TextView) findViewById(R.id.companyName);
        st_portTv = (TextView) findViewById(R.id.st_port);
        detailGv = (ChildGridView) findViewById(R.id.price_details);
        remarkEt = (EditText) findViewById(R.id.remark_edit);

        surchargesLv = (MyGridView) findViewById(R.id.price_details_list);
        showBufferDialog();
    }
    @Override
    protected void initData() {
        bufferDialog.dismiss();
        PriceEntity bean = new GeoUtil().deserializer(tempPrice,PriceEntity.class);
        setData(bean);
    }

    private void setData(PriceEntity bean) {
        allStartPort = getIntent().getStringExtra(STARTPORT);
        allDestPort = getIntent().getStringExtra(DESTPORT);
        startTv.setText(allStartPort);
        destTv.setText(allDestPort);
        List<String> prices = new ArrayList<>();
        for (int i = 0; i < bean.getBoxBeans().size(); i++) {
            prices.add(i,bean.getBoxBeans().get(i).getName());
            prices.add(bean.getBoxBeans().get(i).getValue()+"");
        }
        detailGv.setNumColumns(prices.size()/2);
        detailGv.setAdapter(new ArrayAdapter(this,R.layout.adapter_array,prices));
        companyName = bean.getSc_name();
        companyNameTv.setText("船公司：" + companyName);
        if (bean.getCls() != 0) {
            st_portTv.setText("截关/开船：" + bean.getCls() + "/" + bean.getEtd());
        }
        if (bean.getRemarks() != null) {
            remarkTv.setText("备注：" + bean.getRemarks());
        }
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < prices.size()/2+2; i++) {
            lists.add(new ArrayList<String>());
        }
        lists.get(0).add("费用名称");
        lists.get(0).add("币种");
        for (int i = 0; i < prices.size() / 2; i++) {
            lists.get(0).add(prices.get(i));
        }
        for (int i = 1; i < lists.size(); i++) {
            lists.get(i).add(bean.getSurcharges().get(i).getName());
            lists.get(i).add(bean.getSurcharges().get(i).getCurr());
            if(bean.getSurcharges().get(i).getTprice()!=0){
                if(bean.getSurcharges().get(i).getGp20()!=0){
                    lists.get(i).add(bean.getSurcharges().get(i).getGp20() + Config.GP20);
                }
                if(bean.getSurcharges().get(i).getGp40()!=0){
                    lists.get(i).add(bean.getSurcharges().get(i).getGp40() + Config.GP40);
                }
                if(bean.getSurcharges().get(i).getHq40()!=0){
                    lists.get(i).add(bean.getSurcharges().get(i).getHq40() + Config.HQ40);
                }
            }else {
//                lists.get(i).add(bean.getSurcharges().get(i).getTprice());
            }
        }
        surchargesLv.setData(lists,50);
        findViewById(R.id.activity_price_detail).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        CacheDataConstant.price = entity;
        startActivityForResult(new Intent(this,BookActivity.class),LOCKCODE);
    }
}
