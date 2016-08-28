package com.cnst.wisdom.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.utills.SPUtills;

/**
 * <设置>修改密码，退出登录
 */
public class SettingActivity extends BaseNetActivity implements View.OnClickListener
{

    private TextView dialogTitle;
    private TextView textView_head_text;
    private Dialog mDialog;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_setting);
        setViews();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    private void setViews()
    {

        findViewById(R.id.linearLayout_modifyPassword).setOnClickListener(this);
        findViewById(R.id.linearLayout_logout).setOnClickListener(this);

        textView_head_text = (TextView)findViewById(R.id.head_text);
        textView_head_text.setText("设置");

        mDialog = new Dialog(SettingActivity.this, R.style.no_title_dialog);
        mDialog.setContentView(R.layout.dialog_sel_subject_state);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogTitle = (TextView)mDialog.findViewById(R.id.dialog_title);
        mDialog.findViewById(R.id.cancel).setOnClickListener(this);
        mDialog.findViewById(R.id.confirm).setOnClickListener(this);
    }

    /**
     * <点击事件>
     * 修改密码：转到修改密码页
     * 退出：弹出确认对话框
     * 返回：返回个人中心
     * 对话框-取消：关闭对话框
     * 对话框-确定：关闭对话框，执行登出操作
     */
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.linearLayout_modifyPassword:
                modifyPassword();
                break;
            case R.id.linearLayout_logout:
                logout();
                break;
            case R.id.head_back_action:
                finish();
                break;
            case R.id.cancel:
                mDialog.dismiss();
                break;
            case R.id.confirm:
                mDialog.dismiss();
                clearInfo();
                break;
            default:
                break;
        }
    }

    /**
     * <弹出确认对话框>
     * 点击“退出登录”时弹出的对话框，点击确定后执行登出方法
     */
    private void logout()
    {
        dialogTitle.setText("确定退出登录吗？");
        mDialog.show();
    }

    /**
     * <清空登录信息>
     * 确认登出时，清空所有用户信息，如自动登录状态
     * 转到登录页
     */
    private void clearInfo()
    {
        BaseApplication app = (BaseApplication)getApplication();
        app.setLogin(null);
        SPUtills.remove(getApplicationContext(), "autoLogin_user");
        SPUtills.remove(getApplicationContext(), "autoLogin_password");
        SPUtills.remove(getApplicationContext(), "autoLogin_time");
        SPUtills.put(getApplicationContext(), "autoLogin_checked", "false");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("logout",true);
        startActivity(intent);
        finish();
    }

    /**
     * <修改密码>
     */
    private void modifyPassword()
    {
        Intent intent = new Intent(this, ModifyPasswordActivity.class);
        startActivity(intent);
    }
}
