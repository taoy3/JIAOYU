package com.cnst.wisdom.ui.activity;

import android.view.View;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;

public class OtherTipAt extends BaseNetActivity implements View.OnClickListener {


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_other_tip_at);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }
}
