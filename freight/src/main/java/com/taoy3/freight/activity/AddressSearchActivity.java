package com.taoy3.freight.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taoy3.freight.R;
import com.taoy3.freight.adapter.SearchAdapter;
import com.taoy3.freight.bean.AddressBean;
import com.taoy3.freight.constant.UrlConstant;
import com.taoy3.freight.netBean.GeoUtil;
import com.taoy3.freight.util.AppHttpUtils;
import com.taoy3.freight.view.MySearchView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddressSearchActivity extends BaseActivity implements MySearchView.OnTextChanged, AdapterView.OnItemClickListener {
    private MySearchView searchView;
    private ListView listView;
    protected List<AddressBean> list = new ArrayList<>();//搜索出的结果存放
    private BaseAdapter adapter;
    private List<AddressBean> data = new ArrayList<>();//搜索数据源
//    private GifImageView listEmptyV;
    private String url;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivityForResult(new Intent(this, CusDetailActivity.class)
                .putExtra(CONTACTBEAN, list.get(position)), LOCKCODE);
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
//        listEmptyV = (GifImageView) getLayoutInflater().inflate(R.layout.view_empty,null);
//        ((ViewGroup)listView.getParent()).addView(listEmptyV);
//        listView.setEmptyView(listEmptyV);
//        listEmptyV.setImageResource(R.mipmap.no_msg);
    }

    @Override
    protected void initData() {
        String type = getIntent().getStringExtra(TYPE);
        switch (type){
            case SHIPPER:
                url = UrlConstant.getShippersUrl(null, null);
                setTitle(R.string.shipper);
                break;
            case CONSIGNEE:
                url = UrlConstant.getConsigneesUrl(null,null);
                setTitle(R.string.consignee);
                break;
            case NOTIFY:
                url = UrlConstant.getNotifysUrl(null,null);
                setTitle(R.string.notify);
                break;
            default:
                break;
        }
        adapter = new SearchAdapter(this,list);
        listView.setAdapter(adapter);
        AppHttpUtils.getUtils(url,new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what/100==2&&msg.obj!=null&&msg.obj.toString().trim().length()>0) {
                    try {
                        String json = new JSONObject(msg.obj.toString()).getString(RESULT);
                        if (json != null) {
                            List<AddressBean> temp = new GeoUtil().deserializer(json, new TypeToken<List<AddressBean>>() {
                            }.getType());
                            if (temp != null && temp.size() > 0) {
                                data.clear();
                                data.addAll(temp);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        bufferDialog.dismiss();
                    }
                }
                bufferDialog.dismiss();
            }
        });
    }

    @Override
    public void onTextChangedListener(CharSequence charSequence, int i, int i1, int i2) {
        for(AddressBean item:data){
            if(item.getName().contains(charSequence.toString().toLowerCase())||item.getName().contains(charSequence.toString().toUpperCase())
                    ||item.getTel().contains(charSequence.toString())||item.getAddr().contains(charSequence.toString())){
                list.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchCancel() {
        list.clear();
        adapter.notifyDataSetChanged();
    }
}
