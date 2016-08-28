package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.view.View;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.utills.BaseDataUtils;
import com.cnst.wisdom.utills.SPUtills;

/**
 * 教学计划主界面
 * 点击教学科目、教学进度、修改计划分别进入相关界面
 * @author taoyuan.
 * @since 1.0
 */
public class TeachPlan extends BaseNetActivity implements View.OnClickListener
{

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_teach_plan);

        BaseDataUtils.SERVER = SPUtills.get(this, Constants.GET_SERVER, "").toString();
        findViewById(R.id.teach_plan_progress).setOnClickListener(this);
        findViewById(R.id.teach_plan_mof).setOnClickListener(this);
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
            case R.id.teach_plan_progress:
                //进入教学进度，更新教学进度
                startActivity(new Intent(this, TeachPlanProgress.class));
                break;
            case R.id.teach_plan_mof:
                //进入修改计划，修改教学计划
                startActivity(new Intent(this, TeachPlanModList.class));
                break;
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }
}
