package com.taoy3.freight.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.taoy3.freight.R;
import com.taoy3.freight.adapter.OrderAdapter;
import com.taoy3.freight.bean.OrderBean;
import com.taoy3.freight.util.BaseDataUtils;
import com.taoy3.freight.view.MySearchView;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseActivity implements AdapterView.OnItemClickListener, MySearchView.OnTextChanged {
    public static final int UNDO = 0;// 0-待商务审批
    public static final int REFUSE = 1;// 1-商务审批通过
    public static final int PASS = 2;// 2-商务审批失败
    private int statue;
    private MySearchView searchView;
    private ListView listView;
    private OrderAdapter adapter;
    private List<OrderBean> orderList;
    private List<OrderBean> list = new ArrayList<>();

    @Override
    protected void setView() {
        setContentView(R.layout.activity_order_ticket);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        searchView = (MySearchView) findViewById(R.id.search_ticket);
        searchView.setHint(R.string.search_order_tip);
        searchView.setOnTextContentChange(this);
        listView = (ListView) findViewById(R.id.ticket_list);
        adapter = new OrderAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    @Override
    protected void initData(){
        statue = getIntent().getIntExtra(TYPE,0);
        try {
            List<OrderBean> temp = BaseDataUtils.db.findAll(Selector.from(OrderBean.class)
                    .where("state", "=", this.statue));
            orderList.addAll(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(list.size()==0){
//                emptyView.setImageResource(R.mipmap.no_msg);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivityForResult(new Intent(this,OrderDetailActivity.class).
                putExtra(ID, list.get(position).getOrderno()),LOCKCODE);
    }

    @Override
    public void onTextChangedListener(CharSequence charSequence, int i, int i1, int i2) {
        list.clear();
        for(OrderBean entity:orderList){
            if(entity.getOrderno().contains(charSequence.toString().trim())
                    ||entity.getSc_name().contains(charSequence.toString().trim().toLowerCase())
                    ||entity.getStart_name().contains(charSequence.toString().trim().toLowerCase())
                    ||entity.getDes_name().contains(charSequence.toString().trim().toLowerCase())
                    ||entity.getSc_id().contains(charSequence.toString().trim().toUpperCase())
                    ||entity.getStart_id().contains(charSequence.toString().trim().toUpperCase())
                    ||entity.getDes_id().contains(charSequence.toString().trim().toUpperCase())){
                list.add(entity);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchCancel() {
        list.clear();
        list.addAll(orderList);
        adapter.notifyDataSetChanged();
    }
}
