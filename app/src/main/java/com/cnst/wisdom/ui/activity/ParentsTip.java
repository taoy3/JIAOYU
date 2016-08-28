package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.utills.BaseDataUtils;
import com.cnst.wisdom.utills.SPUtills;

public class ParentsTip extends BaseNetActivity implements View.OnClickListener {
    private TextView tipText;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_parents_tip);

        BaseDataUtils.SERVER = SPUtills.get(this, Constants.GET_SERVER, "").toString();
        findViewById(R.id.leave_manage).setOnClickListener(this);
        findViewById(R.id.medicine_tip).setOnClickListener(this);
        findViewById(R.id.other_tip).setOnClickListener(this);
        tipText = (TextView) findViewById(R.id.leave_manage_tip);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.leave_manage:
                //进入请假管理界面
                startActivity(new Intent(this, LeaveManageAt.class));
                break;
            case R.id.medicine_tip:
                //进入带药提醒界面
                startActivity(new Intent(this, MedicineTipAt.class));
                break;
            case R.id.other_tip:
                //其他嘱托界面
                startActivity(new Intent(this, OtherTipAt.class));
                break;
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }
}