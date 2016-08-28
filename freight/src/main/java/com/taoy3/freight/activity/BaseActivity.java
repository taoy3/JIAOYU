package com.taoy3.freight.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.taoy3.freight.BaseApp;
import com.taoy3.freight.R;
import com.taoy3.freight.constant.Config;

//import pl.droidsonroids.gif.GifImageView;


/**
 * Activity基类
 * 功能１:为在用户登录并设置了手势锁的情况下，当程序退至后台再返回时启动手势锁验证
 * 功能２：在页面缓存时设置加载对话框
 * 功能３：实现公用接口
 * 功能４：初始化TitleView
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    //判断是从其它Activity返回，还是从后台回到前台
    protected boolean isBack;
    protected Dialog bufferDialog;
//    protected GifImageView emptyView;
    protected TextView rightView;
    private TextView leftView;
    private TextView titleView;
    protected static final int USERINFOCODE = 1;
    protected static final int SHIPCOMPANYCODE = 2;
    protected static final int ENQUIRYCODE = 4;
    protected static final int CUSMANAGECODE = 5;
    protected static final int SHIPPERCODE = 3;
    protected static final int CONSIGNEECODE = 6;
    protected static final int NOTIFYCODE = 7;
    protected static final int SETTINGCODE = 8;
    protected static final int CREATEENQUIRYCODE = 9;
    protected static final int LOCKCODE = Config.LOCKCODE;
    protected static final int CUSTOMERCODE = 11;
    protected static final int ACCOUNTSCODE = 12;
    protected static final int PAYCODE = 13;
    protected static final int STARTPORTCODE = 14;
    protected static final int DESTPORTCODE = 15;
    protected static final int CARGOINFOCODE = 19;
    protected static final int ADDCUSINFO = 21;
    protected static final String STARTPORT = "startPort";
    protected static final String DESTPORT = "destPort";
    protected static final String COMPANY = "company";
    protected static final String ALLSTARTPORT = "allStartPort";
    protected static final String ALLDESTPORT = "allDestPort";
    protected static final String CONTACT = "feename";
    protected static final String CONTACTBEAN = "contactBean";
    protected static final String SEARCHVOYAGEHISTORY = "searchVoyageHistory";
    protected static final String SEARCHPRICEHISTORY = "searchPriceHistory";
    protected static final String SEARCHPRICEHISTORYID = "searchPriceHistoryId";
    protected static final String SCID = "scId";
    protected static final String ID = "id";
    protected static final String CODE = "code";
    protected static final String CARGOINFO = "cargoInfo";
    protected static final String NUMBER="number";
    protected static final String USERNAME="username";
    protected static final String ADD = "add";
    protected static final String RESULT = "result";
    protected static final String TYPE = "type";
    protected static final String STATUE = "statue";
    protected static final String CUSTOMER = "customer";
    protected static final String SHIPPER = "shipper";
    protected static final String CONSIGNEE = "consignee";
    protected static final String NOTIFY = "notify";
    protected static final String PORT_ID = "portId";
    protected static final String VOYAGE = "voyage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        initTitleView();
        initView(leftView, titleView, rightView);
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected abstract void setView();

    protected abstract void initView(TextView leftView, TextView titleView, TextView rightView);

    protected abstract void initData();

    private void initTitleView() {
        titleView = (TextView) findViewById(R.id.title);
        rightView = (TextView) findViewById(R.id.title_right);
        leftView = (TextView) findViewById(R.id.title_left);
        if (titleView != null) {
            leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseActivity.this.setResult(LOCKCODE, new Intent());
                    BaseActivity.this.finish();
                }
            });
            titleView.setText(getTitle());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isBack && BaseApp.loginStatus==BaseApp.LOGINOK && BaseApp.lockIsOk) {
            startActivityForResult(new Intent(this, LockActivity.class), LOCKCODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //启动其它Activity的时加入Code,返回时会调用此方法，改变标记
        isBack = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isBack = false;
    }

    @Override
    public void onBackPressed() {
        if (bufferDialog != null && bufferDialog.isShowing()) {
            bufferDialog.dismiss();
        }
        //启动其它Activity的时加入Code,子类中也应全部用此方法
        setResult(LOCKCODE, new Intent());
        finish();
    }

    protected void showBufferDialog() {
        bufferDialog = new Dialog(this, R.style.dialog_buffer);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_empty, null);
        TextView titleDialog = (TextView) dialogView.findViewById(R.id.title);
        TextView leftDialog = (TextView) dialogView.findViewById(R.id.title_left);
        if (titleDialog != null) {
            leftDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseActivity.this.setResult(LOCKCODE, new Intent());
                    BaseActivity.this.finish();
                }
            });
            titleDialog.setText(getTitle());
        }
//        emptyView = (GifImageView) dialogView.findViewById(R.id.empty_view);
        bufferDialog.setContentView(dialogView);
        bufferDialog.setCancelable(false);
        bufferDialog.show();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onDestroy() {
        if (bufferDialog != null && bufferDialog.isShowing()) {
            bufferDialog.dismiss();
            bufferDialog=null;
        }
        super.onDestroy();
    }
}
