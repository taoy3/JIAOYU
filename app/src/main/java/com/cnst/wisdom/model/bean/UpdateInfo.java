package com.cnst.wisdom.model.bean;

/**
 * Created by Jonas on 2016/3/11.
 */
public class UpdateInfo
{
    /**
     * status : success
     * msg : 修改成功
     * code : 200
     */

    private String status;
    private String msg;
    private int code;

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getStatus()
    {
        return status;
    }

    public String getMsg()
    {
        return msg;
    }

    public int getCode()
    {
        return code;
    }
}
