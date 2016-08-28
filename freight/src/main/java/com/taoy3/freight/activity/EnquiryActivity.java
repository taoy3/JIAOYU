package com.taoy3.freight.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.R;
import com.taoy3.freight.adapter.EnquiryAdapter;
import com.taoy3.freight.bean.EnquiryBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.util.EnquiryType;
import com.taoy3.freight.util.SQLUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import pl.droidsonroids.gif.GifImageView;


public class EnquiryActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private PopupWindow addWindow;
    private RadioGroup.LayoutParams repliedParams;
    private RadioGroup.LayoutParams noReplyParams;
    //用于存放需要显示的内容
    private List<EnquiryBean> list = new ArrayList<>();
    //用于存放各种条件下需要显示的内容，当用户切换时将对应的Item放入list中
    private List<EnquiryBean>[][] baseList = new List[5][2];
    private EnquiryAdapter adapter;
    private ArrayAdapter<String> spinnerAdapter;
    private int type;
    private int state =1;
    private RadioGroup group;
    private int page = 1;
    private String items[];
//    private GifImageView listEmptyV;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_enquiry);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        findViewById(R.id.enquiry_back).setOnClickListener(this);
        findViewById(R.id.enquiry_add).setOnClickListener(this);
        for (int i = 0; i < baseList.length; i++) {
            for (int j = 0; j < baseList[i].length; j++) {
                baseList[i][j] = new ArrayList<>();
            }
        }
        //设置添加询价的Spinner选项
        Spinner spinner = (Spinner) findViewById(R.id.enquiry_selector);
        items = new String[]{getString(R.string.all), getString(R.string.full_container_load), getString(R.string.bulk_cargo), getString(R.string.air_lift), getString(R.string.express)};
        spinnerAdapter = new ArrayAdapter(this, R.layout.adapter_spinner, R.id.adapter_spinner_text, items);
        spinner.setAdapter(spinnerAdapter);
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            spinner.setDropDownWidth(display.getWidth());
        }
        spinner.setOnItemSelectedListener(this);
        //设置已回复与未回复的RadioGroup,获得他们的LayoutParams
        group = (RadioGroup) findViewById(R.id.enquiry_express_radioGroup);
        group.setOnCheckedChangeListener(this);
        repliedParams = (RadioGroup.LayoutParams) group.getChildAt(0).getLayoutParams();
        noReplyParams = (RadioGroup.LayoutParams) group.getChildAt(1).getLayoutParams();

        ListView listView = (ListView) findViewById(R.id.enquiry_list);
        adapter = new EnquiryAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
//        listEmptyV = (GifImageView) getLayoutInflater().inflate(R.layout.view_empty,null);
//        ((ViewGroup)listView.getParent()).addView(listEmptyV);
//        listView.setEmptyView(listEmptyV);
    }
    @Override
    protected void initData() {
        List<EnquiryBean> temp = null;
        try {
            temp = SQLUtils.dataDb.findAll(EnquiryBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(temp==null){
            temp = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                temp.add(new EnquiryBean(CacheDataConstant.ports.get(i).getName_en(),
                        CacheDataConstant.ports.get(CacheDataConstant.ports.size()-i-1).getName_en(),new Date(115,11,i%30),i%3,1));
            }
            try {
                SQLUtils.dataDb.saveOrUpdateAll(temp);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < temp.size(); i++) {
            baseList[0][temp.get(i).getState()%2].add(temp.get(i));
            baseList[(temp.get(i).getType()+1)%5][temp.get(i).getState()%2].add(temp.get(i));
        }
        upDataList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enquiry_back:
                setResult(LOCKCODE, new Intent());
                finish();
                break;
            case R.id.enquiry_add:
                showAddWindow(view);
                break;
            case R.id.add_full_container_load:
                Intent intent = new Intent(this, EnquiryCreateActivity.class);
                intent.putExtra(TYPE, EnquiryType.FULL);
                startActivityForResult(intent, LOCKCODE);
                addWindow.dismiss();
                break;
            case R.id.add_bulk_cargo:
                intent = new Intent(this, EnquiryCreateActivity.class);
                intent.putExtra(TYPE, EnquiryType.BULK);
                startActivityForResult(intent, LOCKCODE);
                addWindow.dismiss();
                break;
            case R.id.add_air_lift:
                intent = new Intent(this, EnquiryCreateActivity.class);
                intent.putExtra(TYPE, EnquiryType.AIRLIFT);
                startActivityForResult(intent, LOCKCODE);
                addWindow.dismiss();
                break;
            case R.id.add_express:
                intent = new Intent(this, EnquiryCreateActivity.class);
                intent.putExtra(TYPE, EnquiryType.EXPRESS);
                startActivityForResult(intent, LOCKCODE);
                break;
            default:
                break;
        }
    }

    private void showAddWindow(View parent) {
        if (addWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.popup_window_enquiry_add, null);
            view.findViewById(R.id.add_full_container_load).setOnClickListener(this);
            view.findViewById(R.id.add_bulk_cargo).setOnClickListener(this);
            view.findViewById(R.id.add_air_lift).setOnClickListener(this);
            view.findViewById(R.id.add_express).setOnClickListener(this);
            // 创建一个PopuWidow对象,并设置宽，高
            addWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 使其聚集
        addWindow.setFocusable(true);
        // mPopupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
        addWindow.setTouchable(true); // 设置PopupWindow可触摸
        // 设置允许在外点击消失
        addWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        addWindow.setBackgroundDrawable(new BitmapDrawable());
        addWindow.update();
        addWindow.showAsDropDown(parent, -parent.getWidth() * 4 / 2, -parent.getHeight() / 2);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.enquiry_no_reply) {
            repliedParams.setMargins(0, 0, 0, 0);
            noReplyParams.setMargins(0, 0, 0, 2);
            state = 0;
        } else {
            repliedParams.setMargins(0, 0, 0, 2);
            noReplyParams.setMargins(0, 0, 0, 0);
            state = 1;
        }
        group.getChildAt(0).setLayoutParams(repliedParams);
        group.getChildAt(1).setLayoutParams(noReplyParams);
        upDataList();
    }

    private void upDataList() {
        list.clear();
        list.addAll(baseList[type][state]);
        adapter.notifyDataSetChanged();
        if(list.size()==0){
//            listEmptyV.setImageResource(R.mipmap.no_msg);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, EnquiryDetailActivity.class);
        intent.putExtra(STARTPORT, list.get(i).getStart());
        intent.putExtra(DESTPORT, list.get(i).getDest());
        intent.putExtra(ID, list.get(i).getResponse());
        startActivityForResult(intent, ENQUIRYCODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
        type = index;
        upDataList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            Serializable serializable = data.getSerializableExtra("enquiry");
            if(serializable!=null&&serializable instanceof EnquiryBean){
                EnquiryBean bean = (EnquiryBean) serializable;
                baseList[bean.getType()+1][0].add(0,bean);
                baseList[0][0].add(0,bean);
                upDataList();
            }
        }
    }
}
