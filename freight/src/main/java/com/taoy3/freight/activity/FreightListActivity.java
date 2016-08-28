package com.taoy3.freight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.BaseListAdapter;
import com.taoy3.freight.adapter.FreightAdapter;
import com.taoy3.freight.adapter.ViewHolderHelper;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.bean.PriceBean;
import com.taoy3.freight.bean.PriceEntity;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.constant.MyColor;
import com.taoy3.freight.db.FreightDB;
import com.taoy3.freight.db.PortDB;
import com.taoy3.freight.view.MulSelView;
import com.taoy3.freight.webInterface.FreightInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FreightListActivity extends BaseActivity implements FreightAdapter.OnChildrenClick, ExpandableListView.OnGroupClickListener {

    private ExpandableListView listView;
    private FreightAdapter adapter;
    private List<PriceBean> list = new ArrayList<>();
    private List<String> scNames = new ArrayList<>();
    private List<String> scNameList = new ArrayList<>();
    private int total;
    private TextView startPortTv;
    private TextView destPortTv;
    private Port startPort;
    private Port destPort;
    private FrameLayout selectLayout;
    private PopupWindow selectBoxPw;
    private PopupWindow selectScPw;
    private int page = 1;
    private int scId;
    private PortDB dbAccess = PortDB.getInstance();

    @Override
    protected void setView() {
        setContentView(R.layout.activity_freight_list);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        startPortTv = (TextView) findViewById(R.id.start);
        destPortTv = (TextView) findViewById(R.id.dest);
        selectLayout = (FrameLayout) findViewById(R.id.select_layout);
        findViewById(R.id.select_box).setOnClickListener(this);
        findViewById(R.id.select_sc).setOnClickListener(this);
        listView = (ExpandableListView) findViewById(R.id.activity_price_list_list);
        adapter = new FreightAdapter(list, this);
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        adapter.setOnChildrenClick(this);
        listView.setOnGroupClickListener(this);
        Intent intent = getIntent();
        startPort = dbAccess.query(intent.getIntExtra(STARTPORT, 0));
        destPort = dbAccess.query(intent.getIntExtra(DESTPORT, 0));
        startPortTv.setText(startPort.getName_zh());
        destPortTv.setText(destPort.getName_zh());
    }

    private MulSelView boxSv;
    private BaseListAdapter costTypeAdapter;

    private void showBoxWindow(View parent) {
        if (selectBoxPw == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.popup_price_box, null);
            view.findViewById(R.id.price_box_confirm).setOnClickListener(this);
            view.findViewById(R.id.price_box_cancel).setOnClickListener(this);
            boxSv = (MulSelView) view.findViewById(R.id.price_box_type);
            costTypeAdapter = new BaseListAdapter<String>(this, Arrays.asList(Config.BOXNAMES), R.layout.adapter_cost_type) {
                @Override
                protected void convert(ViewHolderHelper helper, String item) {
                    helper.setText(R.id.cost_type_name, item);
                }
            };
            boxSv.setAdapter(costTypeAdapter);
            // 创建一个PopuWidow对象,并设置宽，高
            selectBoxPw = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 使其聚集
        selectBoxPw.setFocusable(true);
        // mPopupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
        selectBoxPw.setTouchable(true); // 设置PopupWindow可触摸
        // 设置允许在外点击消失
        selectBoxPw.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        selectBoxPw.setBackgroundDrawable(new BitmapDrawable());
        selectBoxPw.update();
        selectBoxPw.showAsDropDown(parent, 0, parent.getPaddingBottom() + 10);
    }

    private ListView scSelectLv;

    private void showScWindow(View parent) {
        if (selectScPw == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.popup_price_sc, null);
            scSelectLv = (ListView) view.findViewById(R.id.price_select_sc);
            scSelectLv.setAdapter(new ArrayAdapter(this, R.layout.adapter_array, scNameList));
            scSelectLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getSc_name().equals(scNameList.get(position))) {
                            scId = list.get(i).getSc_id();
                        }
                    }
                    selectScPw.dismiss();
                    initData();
                }
            });
            view.findViewById(R.id.price_select_sc_all).setOnClickListener(this);
            // 创建一个PopuWidow对象,并设置宽，高
            selectScPw = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 使其聚集
        selectScPw.setFocusable(true);
        // mPopupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
        selectScPw.setTouchable(true); // 设置PopupWindow可触摸
        // 设置允许在外点击消失
        selectScPw.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        selectScPw.setBackgroundDrawable(new BitmapDrawable());
        selectScPw.update();
        selectScPw.showAsDropDown(parent, 205, parent.getPaddingBottom() + 10);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_box:
                if (selectBoxPw != null && selectBoxPw.isShowing()) {
                    selectBoxPw.dismiss();
                } else {
                    showBoxWindow(v);
                }
                break;
            case R.id.select_sc:
                if (selectScPw != null && selectScPw.isShowing()) {
                    selectScPw.dismiss();
                } else {
                    showScWindow(v);
                }
                break;
            case R.id.price_box_confirm:
                selectBoxPw.dismiss();
                initData();
                break;
            case R.id.price_box_cancel:
                selectBoxPw.dismiss();
                break;
            case R.id.price_select_sc_all:
                scId = 0;
                selectScPw.dismiss();
                initData();
                break;
        }
    }

    @Override
    protected void initData() {
        List<PriceBean> temp = FreightDB.getInstance().query(startPort.getId(), destPort.getId(), scId);
        if (temp.size() == 0) {
            temp = FreightInterface.create(startPort, destPort);
        }
        total = temp.size();
        //添加数据
        scNames.clear();
        list.clear();
        list.addAll(temp);
        adapter.notifyDataSetChanged();

    }

//    private List<PriceBean> changBean(List<Freight> temp) {
//        List<PriceBean> priceBeans = new ArrayList<>();
//        for (int i = 0; i < temp.size(); i++) {
//            if (!scNames.contains(temp.get(i).getSccode())) {
//                scNames.add(temp.get(i).getSccode());
//                priceBeans.add(new PriceBean(temp.get(i)));
//            }
//        }
//        if (scNameList.size() == 0) {
//            scNameList.addAll(scNames);
//        }
//        for (int i = 0; i < temp.size(); i++) {
//            for (int j = 0; j < priceBeans.size(); j++) {
//                if (priceBeans.get(j).getSc_name().equals(temp.get(i).getSccode())) {
//                    priceBeans.get(j).getItems().add(new PriceEntity(temp.get(i)));
//                }
//            }
//        }
//        return priceBeans;
//    }

    @Override
    public void onChildrenClick(PriceEntity bean) {
        Intent intent = new Intent(this, FreightDetailActivity.class);
        intent.putExtra(TYPE, bean);
        startActivityForResult(intent, LOCKCODE);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (listView.isGroupExpanded(groupPosition)) {
            listView.collapseGroup(groupPosition);
        } else {
            listView.expandGroup(groupPosition);
        }
        for (int i = 0; i < list.size(); i++) {
            if (listView.isGroupExpanded(i)) {
                selectLayout.setBackgroundColor(Color.WHITE);
                listView.setBackgroundColor(MyColor.GPSELECT);
                return true;
            }
        }
        selectLayout.setBackgroundColor(MyColor.GPEXPAND);
        listView.setBackgroundColor(Color.WHITE);
        return true;
    }
}
