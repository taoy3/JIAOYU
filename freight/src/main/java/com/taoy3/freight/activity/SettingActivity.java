package com.taoy3.freight.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.taoy3.freight.BaseApp;
import com.taoy3.freight.R;


public class SettingActivity extends BaseActivity{

    @Override
    protected void setView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        findViewById(R.id.set_lock).setOnClickListener(this);
        findViewById(R.id.log_out).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_lock:
                if(BaseApp.loginStatus==BaseApp.LOGINOK){
                    Intent intent = new Intent(this,LockSetupActivity.class);
                    startActivityForResult(intent, LOCKCODE);
                }
                break;
            case R.id.log_out:
                if(BaseApp.loginStatus==BaseApp.LOGINOK){
                    BaseApp.loginStatus = BaseApp.LOGOUT;
                    Toast.makeText(this,R.string.succeed,Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
