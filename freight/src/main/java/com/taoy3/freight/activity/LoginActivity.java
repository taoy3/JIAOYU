package com.taoy3.freight.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.BaseApp;
import com.taoy3.freight.R;
import com.taoy3.freight.bean.UserBean;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.netBean.GeoUtil;
import com.taoy3.freight.util.LogUtils;


public class LoginActivity extends BaseActivity implements LogUtils.NotifyLoginMsg {
    private EditText nameTv;
    private EditText passWordTv;
    private String passWord;
    private Button logBt;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        passWordTv = (EditText) findViewById(R.id.login_passWord);
        nameTv = (EditText) findViewById(R.id.login_userName);
        logBt = (Button) findViewById(R.id.login);
        logBt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        CacheDataConstant.userBean = new UserBean();
        if(CacheDataConstant.userBean!=null){
            nameTv.setText(CacheDataConstant.userBean.getUsername());
            passWordTv.setText(CacheDataConstant.userBean.getPassWord());
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login) {
            view.setClickable(false);
            passWord=passWordTv.getText().toString();
            LogUtils.login(nameTv.getText().toString(),passWord);
            LogUtils.setMsg(this);
        } else {
            passWordTv.setText("");
            BaseApp.loginStatus = BaseApp.LOGINOK;
        }
    }

    @Override
    public void logMsg(int state, String result) {
        logBt.setClickable(true);
        if (state/100 == 2) {
            UserBean bean = new GeoUtil().deserializer(result, UserBean.class);
            if(bean!=null){
                CacheDataConstant.userBean = bean;
                bean.setPassWord(passWord);
                DbUtils.DaoConfig config = new DbUtils.DaoConfig(this);
                config.setDbDir(this.getCacheDir() + Config.SQL_ROOT);
                config.setDbName(Config.USER_INFO); //db名
                config.setDbVersion(Config.DBVER);  //db版本
                DbUtils dataDb = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的
                try {
                    dataDb.save(bean);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                setResult(Config.LOCKCODE, new Intent());
                finish();
                return;
            }
        }
        Toast.makeText(this, getString(R.string.login_fail)+"：" + result, Toast.LENGTH_SHORT).show();
    }
}