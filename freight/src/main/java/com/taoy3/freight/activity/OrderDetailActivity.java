package com.taoy3.freight.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.bean.CostBean;
import com.taoy3.freight.bean.OrderBean;
import com.taoy3.freight.constant.UrlConstant;
import com.taoy3.freight.netBean.GeoUtil;
import com.taoy3.freight.util.AppHttpUtils;
import com.taoy3.freight.view.ChildListView;
import com.taoy3.freight.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity{
    private ScrollView scrollView;
    private TextView orderTv;
    private TextView startTv;
    private TextView destTv;
    private TextView scNameTv;
    private TextView clsTv;
    private TextView etdTv;
    private TextView bargeTv;
    private TextView cargoInfoTv;
    private TextView customsTv;
    private TextView trailerTv;
    private TextView markTv;
    //存放费用名称
    private OrderBean bean;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        showBufferDialog();
        scrollView = (ScrollView) findViewById(R.id.order_scrollView);
        orderTv = (TextView) findViewById(R.id.order_id);
        startTv = (TextView) findViewById(R.id.order_start);
        destTv = (TextView) findViewById(R.id.order_dest);
        scNameTv = (TextView) findViewById(R.id.order_sc);
        clsTv = (TextView) findViewById(R.id.order_cls);
        etdTv = (TextView) findViewById(R.id.order_etd);
        bargeTv = (TextView) findViewById(R.id.order_barge);
        cargoInfoTv = (TextView) findViewById(R.id.order_cargo_info);
        customsTv = (TextView) findViewById(R.id.order_customs);
        trailerTv = (TextView) findViewById(R.id.order_trailer);
        markTv = (TextView) findViewById(R.id.order_remark);


    }

    @Override
    protected void initData() {
        AppHttpUtils.getUtils(UrlConstant.submitOrder + "/" + getIntent().getStringExtra(ID), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.obj.toString();
                bean = new GeoUtil().deserializer(result, OrderBean.class);
                setData();
            }
        });
    }

    private void setData() {
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        orderTv.setText("运单号:" + bean.getOrderno());
        startTv.setText(bean.getStart_name());
        destTv.setText(bean.getDes_name());
        scNameTv.setText("船公司:"+bean.getSc_name());
        clsTv.setText("截关日期:"+bean.getCls());
        etdTv.setText("开船日期:"+bean.getEtd());
        bargeTv.setText("驳船日期:" + bean.getBgt());
        cargoInfoTv.setText("货物信息:"+bean.getCargoinf().toString());
        customsTv.setText("是否需要报关:" + (bean.getDeclare() == 0 ? "否" : "是"));
        trailerTv.setText("是否需要拖车:" + (bean.getTrail() == 0 ? "否" : "是"));
        markTv.setText("备注:" + bean.getRemark());

        ChildListView receiveNameLv = (ChildListView) findViewById(R.id.order_receive_name);;
        MyGridView receiveDataGv = (MyGridView) findViewById(R.id.order_receive_box);
        setCost(bean.getReceivable(), receiveNameLv, receiveDataGv);

        ChildListView payNameLv = (ChildListView) findViewById(R.id.order_pay_name);
        MyGridView payDataGv = (MyGridView) findViewById(R.id.order_pay_box);
        setCost(bean.getPayable(), payNameLv, payDataGv);
        bufferDialog.dismiss();
    }

    private void setCost(CostBean costBean, ChildListView childListView, MyGridView gridView) {
        List<List<String>> boxList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        names.add("费用名称");
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < bean.getConnum().size(); i++) {
            titleList.add(bean.getConnum().get(i).getName());
        }
        boxList.add(titleList);
        names.add("海运费");
        List<String> oceanList = new ArrayList<>();
        for (int i = 0; i < costBean.getOcean().size(); i++) {
            oceanList.add(costBean.getOcean().get(i).getNumber()*costBean.getOcean().get(i).getValue()+"");
        }
        boxList.add(oceanList);
        for (int i = 0; i < costBean.getSurcharge().size(); i++) {
            boxList.add(new ArrayList<String>());
        }
        for (int i = 0; i < costBean.getSurcharge().size(); i++) {
            names.add(costBean.getSurcharge().get(i).getFeename());
            if(boxList.get(i+2).size()==0){
                boxList.get(i+2).add(costBean.getSurcharge().get(i).getCost()+"/票");
            }else {
                for (int j = 0; j < costBean.getSurcharge().get(j).getPrices().size(); j++) {
                    boxList.get(i+2).add(costBean.getSurcharge().get(j).getPrices().get(j).getValue()*
                            costBean.getSurcharge().get(j).getPrices().get(j).getNumber()+"");
                }
            }
        }
        if(bean.getConnum().size()>0&&costBean.getOcean().size()>0){
            childListView.setAdapter(new ArrayAdapter(this, R.layout.adapter_array, names));
            gridView.setMinimumHeight(getHeight(childListView));
            gridView.setData(boxList, 200);
        }else {
            ((HorizontalScrollView)childListView.getParent().getParent()).setVisibility(View.GONE);
        }
    }

    public  int getHeight(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        return params.height;
    }
}
