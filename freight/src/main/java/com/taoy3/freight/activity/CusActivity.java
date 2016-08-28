package com.taoy3.freight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.CusAdapter;
import com.taoy3.freight.bean.CustomerBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.util.BaseDataUtils;
import com.taoy3.freight.view.MySearchView;
import com.taoy3.freight.view.swipemenu.SwipeMenu;
import com.taoy3.freight.view.swipemenu.SwipeMenuCreator;
import com.taoy3.freight.view.swipemenu.SwipeMenuItem;
import com.taoy3.freight.view.swipemenu.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class CusActivity extends BaseActivity implements MySearchView.OnTextChanged,CusAdapter.ModifyListener,
        AdapterView.OnItemClickListener,SwipeMenuCreator,SwipeMenuListView.OnMenuItemClickListener {
    private MySearchView searchView;
    private SwipeMenuListView listView;
    private Button addBt;
    private List<CustomerBean> list = new ArrayList<>();//搜索出的结果存放
    private CusAdapter adapter;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_address);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        listView = (SwipeMenuListView) findViewById(R.id.address_list);
        adapter = new CusAdapter(this,list);
        listView.setAdapter(adapter);
        adapter.setListener(this);
        listView.setOnItemClickListener(this);
        listView.setMenuCreator(this);
        listView.setOnMenuItemClickListener(this);
        addBt = (Button) findViewById(R.id.address_add);
        addBt.setOnClickListener(this);

        searchView = (MySearchView) findViewById(R.id.search);
        searchView.setOnTextContentChange(this);
    }
    @Override
    public void onTextChangedListener(CharSequence charSequence, int i, int i1, int i2) {
        list.clear();
        for(CustomerBean item: CacheDataConstant.customers){
            if(item.getName_cn().contains(charSequence.toString().toLowerCase())
                    ||item.getId().contains(charSequence.toString().toUpperCase())
                    ||item.getName_en().contains(charSequence.toString().toLowerCase())
                    ||item.getName_en().contains(charSequence.toString().toUpperCase())
                    ||item.getMobile().contains(charSequence.toString())
                    ||item.getTel().contains(charSequence.toString())){
                list.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchCancel() {
        list.clear();
        list.addAll(CacheDataConstant.customers);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void create(SwipeMenu menu) {
        // create "open" item
        SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
        // set item background
        openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
        // set item width
        openItem.setWidth(dp2px(90));
        // set item title
        openItem.setTitle("查看");
        // set item title fontsize
        openItem.setTitleSize(18);
        // set item title font color
        openItem.setTitleColor(Color.BLUE);
        // add to menu
        menu.addMenuItem(openItem);

        // create "delete" item
        SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
        // set item background
        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
        // set item width
        deleteItem.setWidth(dp2px(90));
        // set a icon
        deleteItem.setIcon(R.mipmap.ic_delete);
        // add to menu
        menu.addMenuItem(deleteItem);
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void initData() {
        CacheDataConstant.customers.clear();
                try {
                    CacheDataConstant.customers.addAll(BaseDataUtils.db.findAll(CustomerBean.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }

        list.clear();
        list.addAll(CacheDataConstant.customers);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, CusAddActivity.class)
                .putExtra(STATUE, AddressDetailActivity.ADD), LOCKCODE);
    }

    @Override
    public void onModify(int position) {
        startActivityForResult(new Intent(this, CusAddActivity.class)
                .putExtra(CONTACTBEAN, list.get(position)).putExtra(STATUE,AddressDetailActivity.MODIFY)
                .putExtra(TYPE,list.get(position).getId()), LOCKCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivityForResult(new Intent(this, CusDetailActivity.class)
                .putExtra(CONTACTBEAN, list.get(position)), LOCKCODE);
    }

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                startActivityForResult(new Intent(this, CusDetailActivity.class)
                        .putExtra(CONTACTBEAN, list.get(position)), LOCKCODE);
                break;
            case 1:
                try {
                    BaseDataUtils.db.delete(list.get(position));
                } catch (Exception e) {
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                initData();
                break;
            default:
                break;
        }
        return false;
    }
}
