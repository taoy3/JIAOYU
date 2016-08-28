package com.cnst.wisdom.model.bean;

/**
 * Created by Jonas on 2016/2/4.
 */

public class Login
{
    /**
     * data : {"headImgUrl":null,"stationCode":"","username":"chenyu","school":"河西幼儿园1","name":"汪","userId":"40288bd75317ac86015317c3b2a30001","stationName":null}
     * code : 200
     * sessionId : 404EF49AAF160D2F145CC6BC42AD6470
     * msg : success
     */

    private DataEntity data;
    private int code;
    private String sessionId;
    private String msg;

    public void setData(DataEntity data)
    {
        this.data = data;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public DataEntity getData()
    {
        return data;
    }

    public int getCode()
    {
        return code;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public String getMsg()
    {
        return msg;
    }

    public static class DataEntity
    {
        /**
         * headImgUrl : null
         * stationCode :
         * username : chenyu
         * school : 河西幼儿园1
         * name : 汪
         * userId : 40288bd75317ac86015317c3b2a30001
         * stationName : null
         */

        private String headImgUrl;
        private String stationCode;
        private String username;
        private String school;
        private String name;
        private String userId;
        private String stationName;

        public void setHeadImgUrl(String headImgUrl)
        {
            this.headImgUrl = headImgUrl;
        }

        public void setStationCode(String stationCode)
        {
            this.stationCode = stationCode;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public void setSchool(String school)
        {
            this.school = school;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
        }

        public void setStationName(String stationName)
        {
            this.stationName = stationName;
        }

        public String getHeadImgUrl()
        {
            return headImgUrl;
        }

        public String getStationCode()
        {
            return stationCode;
        }

        public String getUsername()
        {
            return username;
        }

        public String getSchool()
        {
            return school;
        }

        public String getName()
        {
            return name;
        }

        public String getUserId()
        {
            return userId;
        }

        public String getStationName()
        {
            return stationName;
        }
    }
}