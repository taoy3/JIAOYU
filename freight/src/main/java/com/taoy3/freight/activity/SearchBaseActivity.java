package com.taoy3.freight.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.SearchAdapter;
import com.taoy3.freight.bean.SearchBean;
import com.taoy3.freight.view.MySearchView;

import java.util.ArrayList;
import java.util.List;

//import pl.droidsonroids.gif.GifImageView;

/**
 * 搜索数据的基类
 * @param <T>搜索的数据类型，搜索时需要显示的字段为SearchBean的contact，因此所有T需要继承SearchBean，
 *           显示的字段也根据contact排序
 */

public abstract class SearchBaseActivity<T extends SearchBean> extends BaseActivity implements MySearchView.OnTextChanged, AdapterView.OnItemClickListener {
    private MySearchView searchView;
    private ListView listView;
    protected List<T> list = new ArrayList<>();//搜索出的结果存放
    private BaseAdapter adapter;
    private List<T> data = new ArrayList<>();//搜索数据源
    private Intent intent;
    protected String customer;
//    private GifImageView listEmptyV;


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
        customer = getIntent().getStringExtra(CONTACT);
        showBufferDialog();
        setData(data);
        if(data!=null){
            adapter = new SearchAdapter(this,list);
            listView.setAdapter(adapter);
        }
    }

    protected abstract void setData(List<T> data);

    @Override
    public void onTextChangedListener(CharSequence charSequence, int i, int i1, int i2) {
        list.clear();
        onSearchDataChange(charSequence);
        adapter.notifyDataSetChanged();
    }

    protected  void onSearchDataChange(CharSequence charSequence){
        for(T item:data){
            if(item.getId().contains(charSequence.toString().toLowerCase())||item.getId().contains(charSequence.toString().toUpperCase())){
                list.add(item);
            }
        }
    }

    @Override
    public void onSearchCancel() {
        list.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        data.remove(list.get(position));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        intent = data;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        setResult(LOCKCODE,intent);
        super.finish();
    }
}
