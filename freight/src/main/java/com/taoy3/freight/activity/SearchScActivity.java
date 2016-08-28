package com.taoy3.freight.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.taoy3.freight.R;
import com.taoy3.freight.adapter.BaseListAdapter;
import com.taoy3.freight.adapter.ViewHolderHelper;
import com.taoy3.freight.bean.Company;
import com.taoy3.freight.db.CompanyDB;
import com.taoy3.freight.util.Messaging;
import com.taoy3.freight.view.MySearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchScActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.activity_select_ship_company)
    private MySearchView searchView;
    private List<Company> data = new ArrayList<>();
    private BaseListAdapter<Company> adapter;
    private List<Company> hotList;
    private ListView listView;
    private CompanyDB dbAccess;
    private String TAG = "SearchScActivity";

    @Override
    protected void setView() {
        setContentView(R.layout.activity_ship_company);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
//        if(!BaseApp.companyDataIsOk){
//            showBufferDialog();
//            Messaging.register(this);
//        }
        dbAccess = CompanyDB.getInstance();
        ViewUtils.inject(this);
        searchView.setHint(R.string.whl);
        searchView.setOnTextContentChange(new MySearchView.OnTextChanged() {
            @Override
            public void onTextChangedListener(CharSequence charSequence, int j, int i1, int i2) {
                data.clear();
                data.addAll(dbAccess.search(charSequence.toString()));
                Log.i(TAG, "search:" + data.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSearchCancel() {
                data.clear();
                data.addAll(hotList);
                adapter.notifyDataSetChanged();
            }
        });
        listView = (ListView) findViewById(R.id.ship_company_list);
    }
    public void onEvent(Integer event,Integer status) {
        if(event== Messaging.SHIPCOMPANY&&status==Messaging.OK){
            bufferDialog.dismiss();
        }
    }

    @Override
    protected void initData() {
        hotList = dbAccess.query(1,10);
        data.addAll(hotList);
        adapter = new BaseListAdapter<Company>(this, data, R.layout.adapter_array) {
            @Override
            protected void convert(ViewHolderHelper helper, Company item) {
                helper.setText(R.id.text, "(" + item.getName_zh() + ")" + item.getName_en());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra(ID, data.get(i).getId());
        setResult(0, intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Messaging.unregister(this);
    }
}