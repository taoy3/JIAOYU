package com.cnst.wisdom.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.UpdatePassword;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ModifyPasswordActivity extends BaseNetActivity implements View.OnClickListener
{
    private EditText editText_oldPassword;
    private EditText editText_newPassword;
    private EditText editText_repeatPassword;
    private TextView textView_submit;
    private TextView textView_head_text;

    private String oldPassword;
    private String newPassword;
    private String repeatPassword;

    private VolleyManager mVolleyManager = VolleyManager.getInstance();


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_modify_password);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setViews();
    }

    private void setViews()
    {

        editText_oldPassword = (EditText)findViewById(R.id.editText_oldPassword);
        editText_newPassword = (EditText)findViewById(R.id.editText_newPassword);
        editText_repeatPassword = (EditText)findViewById(R.id.editText_repeatPassword);
        textView_submit = (TextView)findViewById(R.id.textView_submit);
        textView_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.textView_submit:
                submit();
                break;

            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * <确定-修改密码>
     * 向服务器发请求，校验原密码是否正确，新密码与重复新密码是否一致、合法
     * 根据返回的状态码弹出文字提示
     */
    private void submit()
    {
        textView_submit.setClickable(false);
        oldPassword = editText_oldPassword.getText().toString();
        if(oldPassword.equals(""))
        {
            Toast.makeText(ModifyPasswordActivity.this, "请输入原密码", Toast.LENGTH_SHORT).show();
            textView_submit.setClickable(true);
            return;
        }
        newPassword = editText_newPassword.getText().toString();
        if(newPassword.equals(""))
        {
            Toast.makeText(ModifyPasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
            textView_submit.setClickable(true);
            return;
        }
        repeatPassword = editText_repeatPassword.getText().toString();
        if(repeatPassword.equals(""))
        {
            Toast.makeText(ModifyPasswordActivity.this, "请重复新密码", Toast.LENGTH_SHORT).show();
            textView_submit.setClickable(true);
            return;
        }
        if(oldPassword.equals(newPassword))
        {
            Toast.makeText(ModifyPasswordActivity.this, "新密码不能与原密码相同", Toast.LENGTH_SHORT).show();
            textView_submit.setClickable(true);
            return;
        }
        checkInfo();
    }

    /**
     * <发请求验证>
     * 将教师ID(同userId)、原、新、确认密码3个发送到服务器进行验证。
     */
    private void checkInfo()
    {
        BaseApplication app = (BaseApplication)getApplication();
        Map<String,String> mMap = new HashMap<String,String>();
        mMap.put("teacherId", app.getLogin().getData().getUserId());
        mMap.put("password", oldPassword);
        mMap.put("newPassword", newPassword);
        mMap.put("confirmPassword", repeatPassword);
        mVolleyManager.getString(
                SPUtills.get(getApplicationContext(), Constants.GET_SERVER, Constants.SERVER).toString()+Constants.UPDATE_PASSWORD, mMap,
                "UpdatePassword", new NetResult<String>()
                {
                    @Override
                    public void onFailure(VolleyError error)
                    {
                        Toast.makeText(ModifyPasswordActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                        textView_submit.setClickable(true);
                    }

                    @Override
                    public void onSucceed(String response)
                    {
                        textView_submit.setClickable(true);
                        Gson gson = new Gson();
                        UpdatePassword updatePassword = gson.fromJson(response, UpdatePassword.class);
                        int code = Constants.STATUS_FAIL;
                        try
                        {
                            code = Integer.parseInt(updatePassword.getCode());
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        Toast.makeText(ModifyPasswordActivity.this, updatePassword.getMsg(), Toast.LENGTH_LONG).show();

                        switch(code)
                        {
                            case Constants.STATUS_ARGUMENT_ERROR:

                                break;
                            case Constants.STATUS_DATA_NOTFOUND:

                                break;
                            case Constants.STATUS_FAIL:
                                break;
                            case Constants.STATUS_ILLEGAL:

                                break;
                            case Constants.STATUS_SERVER_EXCEPTION:

                                break;
                            case Constants.STATUS_SUCCESS:
                                if(SPUtills.get(getApplicationContext(), "autoLogin_checked", "false").toString().equals("true"))
                                {
                                    SPUtills.put(getApplicationContext(), "autoLogin_password", newPassword);
                                }
                                ModifyPasswordActivity.this.finish();
                                break;
                            case Constants.STATUS_TIMEOUT:

                                break;
                        }

                    }
                });
    }
}
