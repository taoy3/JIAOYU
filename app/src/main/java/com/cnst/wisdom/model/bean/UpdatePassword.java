package com.cnst.wisdom.model.bean;

/**
 * Created by Jonas on 2016/2/23.
 */
public class UpdatePassword
{
    /**
     * code : 200
     * msg : 修改密码成功.
     */

    private String code;
    private String msg;

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }
}
