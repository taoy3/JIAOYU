package com.taoy3.freight.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.BaseListAdapter;
import com.taoy3.freight.adapter.ViewHolderHelper;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.db.PortDB;
import com.taoy3.freight.util.Messaging;
import com.taoy3.freight.view.MySearchView;

import java.util.ArrayList;
import java.util.List;

public class PortActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private List<Port> data = new ArrayList<>();
    private BaseListAdapter<Port> adapter;
    private List<Port> hotList;
    private ListView listView;
    private PortDB portDBAccess;
    private String TAG = "searchStartActivity";

    @Override
    protected void setView() {
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
//        if (!BaseApp.companyDataIsOk) {
//            showBufferDialog();
//            Messaging.register(this);
//        }
        portDBAccess = PortDB.getInstance();
        hotList = portDBAccess.query(1,10);
        MySearchView searchView = (MySearchView) findViewById(R.id.start_searchView);
        searchView.setHint(R.string.please_enter_the_port_information);
        searchView.setOnTextContentChange(new MySearchView.OnTextChanged() {
            @Override
            public void onTextChangedListener(CharSequence charSequence, int j, int j1, int i2) {
                data.clear();
                data.addAll(portDBAccess.search(charSequence.toString()));
                Log.i(TAG, "search:" + data.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSearchCancel() {
                data.clear();
                data.addAll(hotList);
                adapter.notifyDataSetChanged();
            }
        });
        listView = (ListView) findViewById(R.id.start_port);

    }

    public void onEvent(Integer event, Integer status) {
        if (event == Messaging.PORTEVENT && status == Messaging.OK) {
            bufferDialog.dismiss();
        }
    }

    @Override
    protected void initData() {
        data.addAll(hotList);
        adapter = new BaseListAdapter<Port>(this, data, R.layout.adapter_array) {
            @Override
            protected void convert(ViewHolderHelper helper, Port item) {
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
