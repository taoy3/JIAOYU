package com.taoy3.freight.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taoy3.freight.R;
import com.taoy3.freight.adapter.OrderAdapter;
import com.taoy3.freight.bean.OrderBean;
import com.taoy3.freight.constant.UrlConstant;
import com.taoy3.freight.netBean.GeoUtil;
import com.taoy3.freight.util.AppHttpUtils;
import com.taoy3.freight.view.MySearchView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderSearchActivity  extends BaseActivity implements MySearchView.OnTextChanged, AdapterView.OnItemClickListener {
    private MySearchView searchView;
    private ListView listView;
    protected List<OrderBean> list = new ArrayList<>();//搜索出的结果存放
    private OrderAdapter adapter;
    private List<OrderBean> data = new ArrayList<>();//搜索数据源

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivityForResult(new Intent(this,OrderDetailActivity.class).
                putExtra(ID, list.get(position).getOrderno()),LOCKCODE);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        searchView = (MySearchView) findViewById(R.id.search);
        searchView.setOnTextContentChange(this);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new OrderAdapter(this,list);
        listView.setAdapter(adapter);
        showBufferDialog();
        AppHttpUtils.getUtils(UrlConstant.submitOrder, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what / 100 == 2 && msg.obj != null && msg.obj.toString().trim().length() > 0) {
                    try {
                        String json = new JSONObject(msg.obj.toString()).getString(RESULT);
                        if (json != null) {
                            List<OrderBean> temp = new GeoUtil().deserializer(json, new TypeToken<List<OrderBean>>() {
                            }.getType());
                            if (temp != null && temp.size() > 0) {
                                data.clear();
                                data.addAll(temp);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (data.size() > 0) {
                    bufferDialog.dismiss();
                } else {
//                    emptyView.setImageResource(R.mipmap.no_msg);
                }
            }
        });
    }

    @Override
    public void onTextChangedListener(CharSequence charSequence, int i, int i1, int i2) {
        list.clear();
        for(OrderBean item:data){
            if(getSearch(item.getOrderno(),charSequence)||getSearch(item.getSc_name(),charSequence)
                    ||getSearch(item.getStart_name(),charSequence)||getSearch(item.getDes_name(),charSequence)){
                list.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
    private boolean getSearch(String s,CharSequence c){
        if(s==null||s.length()==0){
            return false;
        }
        return s.trim().toLowerCase().contains(c)||s.trim().toUpperCase().contains(c);
    }

    @Override
    public void onSearchCancel() {
        list.clear();
        adapter.notifyDataSetChanged();
    }
}
