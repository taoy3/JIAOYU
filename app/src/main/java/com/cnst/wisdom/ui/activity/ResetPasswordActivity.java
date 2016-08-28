package com.cnst.wisdom.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;


/**
 * <重置密码>
 *     在登录页点击“忘记密码”后转到该页
 *     输入手机号，点击“获取验证码”，验证码输入框由禁用变为可用
 *     输入验证码和新密码后，发请求进行校验
 */
public class ResetPasswordActivity extends BaseNetActivity implements View.OnClickListener
{

    private TextView textView_getCode;
    private TextView textView_submit;
    private TextView textView_head_text;
    private EditText editText_phone;
    private EditText editText_code;
    private EditText editText_newPassword;
    private String phone;
    private String code;
    private String password;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_reset_password);
        setviews();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    private void setviews()
    {

        textView_getCode=(TextView)findViewById(R.id.textView_getCode);
        textView_getCode.setOnClickListener(this);
        textView_submit=(TextView)findViewById(R.id.textView_submit);
        textView_submit.setOnClickListener(this);
        editText_phone=(EditText)findViewById(R.id.editText_phone);
        editText_code=(EditText)findViewById(R.id.editText_code);
        editText_newPassword=(EditText)findViewById(R.id.editText_newPassword);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.textView_getCode:
                getCode();
                break;
            case R.id.textView_submit:
                submit();
                break;
            case R.id.head_back_action:
                finish();
                break;
            default:break;
        }
    }

    /**
     * <获取验证码>
     * 1)校验用户所输入的手机号的格式，如果格式不正确则显示提示信息
     * 2)向服务器发送请求，服务器向指定的手机号发送验证码信息
     * 3)将验证码输入框的状态从“禁用”变为“启用”
     */
    private void getCode()
    {
        phone=editText_phone.getText().toString();
        editText_code.setEnabled(true);
        editText_newPassword.setEnabled(true);
    }

    /**
     * <确定>
     * 输入完验证码和新密码后进行校验
     */

    private void submit()
    {
        code=editText_code.getText().toString();
        password=editText_newPassword.getText().toString();
    }
}
