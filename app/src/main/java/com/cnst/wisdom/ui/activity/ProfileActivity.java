package com.cnst.wisdom.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;

public class ProfileActivity extends BaseNetActivity implements View.OnClickListener
{
    private TextView textView_head_text;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_profile);
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
            case R.id.linearLayout_childinfo:
                break;
            case R.id.linearLayout_growthRecord:
                break;
            case R.id.linearLayout_profileManage:
                break;
            case R.id.linearLayout_smartcardModify:
                break;
            case R.id.linearLayout_orderSituation:
                break;
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }


}
