package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.bean.MedicineTipBean;
import com.cnst.wisdom.ui.adapter.MedicineTipAd;
import com.cnst.wisdom.ui.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MedicineTipAt extends BaseNetActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private RefreshListView listView;
    private MedicineTipAd adapter;
    private List<MedicineTipBean> medicines = new ArrayList<>();


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_medicine_tip_at);
        listView = (RefreshListView) findViewById(R.id.medicine_tip_list);
        adapter = new MedicineTipAd(this,medicines);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setDividerHeight(1);
    }

    protected void initData() {
        for (int i = 0; i < 10; i++) {
            MedicineTipBean bean = new MedicineTipBean();
            bean.setState(i%2);
            bean.setName("林雨欣");
            bean.setClazz("小一班");
            bean.setTime("12:32");
            bean.setTimes("1天3次");
            bean.setTip("阿司匹林，退烧药");
            bean.setId(String.valueOf(System.currentTimeMillis()));
            medicines.add(bean);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initView() {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(medicines.get(position).getId()!=null){
            Intent intent = new Intent(this,MedicineTipDetailAt.class);
            intent.putExtra("id",medicines.get(position).getId());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }
}
