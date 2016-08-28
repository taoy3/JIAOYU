package com.cnst.wisdom.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 简述
 * 详细功能描述  学生信息封装(目前主要包含考勤和健康信息)
 *
 * @author yuhongwen
 * @time 16:55
 * @see
 */
public class StudentBean implements Serializable{

    private int    code;
    private String msg;
    private String status;
    private List<DataInfo>   data;

    public static class DataInfo implements Serializable
    {
        private String code;
        private String parentName;
        private String remark;
        private String createTime;
        private StudentInfo dynAttributes;
        private String headImgUrl;
        private String telPhone;
        private String classNum;
        private String idCard;//学生编号
        private String inschoolDate;
        private String account;
        private String gender;
        private int status;
        private  String password;
        private  String name;
        private String id;

        //添加学生健康信息字段
        private String temperature;
        private boolean isRedeye;
        private boolean isDiarrhea;  //腹泻
        private boolean isNail;  //指甲
        private boolean isSnot;  //鼻涕
        private boolean isRash;    //皮疹
        private boolean isHouyan;  //喉炎
        private boolean isCough;   //咳嗽
        private boolean isMedicine;   //服药
        private boolean isTakeCare;   //重点照顾

        public void setIsTakeCare(boolean isTakeCare)
        {
            this.isTakeCare = isTakeCare;
        }

        public boolean isTakeCare()
        {
            return isTakeCare;
        }

        public boolean isRedeye()
        {
            return isRedeye;
        }

        public boolean isDiarrhea()
        {
            return isDiarrhea;
        }

        public boolean isNail()
        {
            return isNail;
        }

        public boolean isSnot()
        {
            return isSnot;
        }

        public boolean isRash()
        {
            return isRash;
        }

        public void setTemperature(String temperature)
        {
            this.temperature = temperature;
        }

        public void setIsRedeye(boolean isRedeye)
        {
            this.isRedeye = isRedeye;
        }

        public void setIsDiarrhea(boolean isDiarrhea)
        {
            this.isDiarrhea = isDiarrhea;
        }

        public void setIsNail(boolean isNail)
        {
            this.isNail = isNail;
        }

        public void setIsSnot(boolean isSnot)
        {
            this.isSnot = isSnot;
        }

        public void setIsRash(boolean isRash)
        {
            this.isRash = isRash;
        }

        public void setIsHouyan(boolean isHouyan)
        {
            this.isHouyan = isHouyan;
        }

        public void setIsCough(boolean isCough)
        {
            this.isCough = isCough;
        }

        public void setIsMedicine(boolean isMedicine)
        {
            this.isMedicine = isMedicine;
        }

        public void setIsTakeHome(boolean isTakeHome)
        {
            this.isTakeHome = isTakeHome;
        }

        public void setIsWatch(boolean isWatch)
        {
            this.isWatch = isWatch;
        }

        public boolean isHouyan()
        {
            return isHouyan;
        }

        public boolean isCough()
        {
            return isCough;
        }

        public boolean isMedicine()
        {
            return isMedicine;
        }

        public boolean isTakeHome()
        {
            return isTakeHome;
        }

        public boolean isWatch()
        {
            return isWatch;
        }

        public String getTemperature()

        {
            return temperature;
        }

        private boolean isTakeHome;   //带回
        private boolean isWatch;   //观察


        public static class StudentInfo implements Serializable
        {
            private String attendanceInfo;

            public void setHealthInfo(String healthInfo)
            {
                this.healthInfo = healthInfo;
            }

            public String getHealthInfo()
            {
                return healthInfo;

            }

            private String healthInfo;

            public String getAttendanceInfo()
            {
                return attendanceInfo;
            }

            public void setAttendanceInfo(String attendanceInfo)
            {
                this.attendanceInfo = attendanceInfo;
            }
        }

        public StudentInfo getDynAttributes()
        {
            return this.dynAttributes;
        }

        public String getCode()
        {
            return code;
        }

        public String getParentName()
        {
            return parentName;
        }

        public String getRemark()
        {
            return remark;
        }

        public String getCreateTime()
        {
            return createTime;
        }

        public String getHeadImgUrl()
        {
            return headImgUrl;
        }

        public String getTelPhone()
        {
            return telPhone;
        }

        public String getClassNum()
        {
            return classNum;
        }

        public String getIdCard()
        {
            return idCard;
        }

        public String getInschoolDate()
        {
            return inschoolDate;
        }

        public String getAccount()
        {
            return account;
        }

        public String getGender()
        {
            return gender;
        }

        public int getStatus()
        {
            return status;
        }

        public String getPassword()
        {
            return password;
        }

        public String getName()
        {
            return name;
        }

        public String getId()
        {
            return id;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public void setParentName(String parentName)
        {
            this.parentName = parentName;
        }

        public void setRemark(String remark)
        {
            this.remark = remark;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public void setHeadImgUrl(String headImgUrl)
        {
            this.headImgUrl = headImgUrl;
        }

        public void setTelPhone(String telPhone)
        {
            this.telPhone = telPhone;
        }

        public void setClassNum(String classNum)
        {
            this.classNum = classNum;
        }

        public void setIdCard(String idCard)
        {
            this.idCard = idCard;
        }

        public void setInschoolDate(String inschoolDate)
        {
            this.inschoolDate = inschoolDate;
        }

        public void setAccount(String account)
        {
            this.account = account;
        }

        public void setGender(String gender)
        {
            this.gender = gender;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public void setDynAttributes(StudentInfo dynAttributes){
            this.dynAttributes = dynAttributes;
        }
    }

    public List<DataInfo> getData()
    {
        return data;
    }

    public int getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }

    public String getStatus()
    {
        return status;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setData(List<DataInfo> data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "AttendanceBean{"+
                "code="+code+
                ", msg='"+msg+'\''+
                ", status='"+status+'\''+
                ", data="+data+
                '}';
    }
}
