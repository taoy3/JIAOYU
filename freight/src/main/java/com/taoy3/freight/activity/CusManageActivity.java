package com.taoy3.freight.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.R;
import com.taoy3.freight.adapter.CusMangerAdapter;
import com.taoy3.freight.bean.SearchBean;
import com.taoy3.freight.util.SQLUtils;
import com.taoy3.freight.view.IndexListView;
import com.taoy3.freight.view.MyListView;

import java.util.Collections;
import java.util.List;

public abstract class CusManageActivity<T extends SearchBean> extends BaseActivity implements MyListView.OnScrolling, CusMangerAdapter.OnItemChangeListener {

    protected List<T> list;
    private TextView headView;
    private IndexListView listView;
    private TextView footerView;
    private TextView searchView;
    private CusMangerAdapter adapter;
    protected String contact;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_customers_management);
    }
    @Override
    protected void initData() {
        contact = getIntent().getStringExtra(CONTACT);
        list = initCusData();
        Collections.sort(list);
        headView.setText("共有" + list.size() + "人");
//        footerView.setText("没有了");
        listView.addHeaderView(headView);
//        listView.addFooterView(footerView);

        adapter =listView.setAdapter(list,this,headView);
        adapter.setOnItemClickListener(this);
        searchView.setOnClickListener(this);
    }

    protected abstract List<T> initCusData();
    @Override
    protected void initView(TextView leftView,TextView titleView,TextView rightView) {
        rightView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.add_green), null);
        rightView.setOnClickListener(this);
        searchView = (TextView) findViewById(R.id.custom_search);
        listView = (IndexListView) findViewById(R.id.custom_list);
        headView = (TextView) getLayoutInflater().inflate(R.layout.adapter_text, null);
        footerView = (TextView) getLayoutInflater().inflate(R.layout.adapter_text, null);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_right:
                addCus();
                break;
            case R.id.custom_search:
                searchCus();
                break;
            default:
                break;
        }
    }

    protected abstract void addCus();

    protected abstract void searchCus();

    @Override
    public void onScrollToBottom() {
        footerView.setText("没有更多了");
    }

    @Override
    public void onScrollToTop() {
        headView.setText("已是最新");
    }

    @Override
    public void onItemSelect(int position) {
        list.remove(position);
    }

    @Override
    public void onItemDel(SearchBean data) {
        try {
            SQLUtils.dataDb.delete(data);
            list.remove(data);
            headView.setText("共有" + list.size() + "人");
            adapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(data!=null){
//            Serializable serializable = data.getSerializableExtra(CONTACT);
//            if(serializable!=null){
//                T bean = (T) serializable;
//                list.add(bean);
//            }
//        }
        adapter.notifyDataChanged();
    }
}
