package com.cnst.wisdom.model.bean;

import java.util.List;

/**
 * Created by Jonas on 2016/3/10.
 */
public class Userinfo
{
    /**
     * code : 200
     * data : {"classList":[{"id":"40288bd75311de15015311df19710001","name":"小班上"},{"id":"40288bd8535e69ac01535e72c2180007","name":"小班下"},{"id":"40288bd8535e69ac01535e7327be0008","name":"中班上"},{"id":"40288bfd532bdd2a01532bf7070d000d","name":"中班下"},{"id":"40288bfd532bdd2a01532bf754a0000e","name":"大班上"},{"id":"40288bfd532bdd2a01532bf791de000f","name":"大班下"}],"stationList":[{"configName":"com.qstone.school.stations","deleteFlag":"0","id":"78d8g3484tyd7feuy3498fhidf893487","key":"asdfhjhfdjkh","remark":"sdsdf","value":"数学教师"},{"configName":"com.qstone.school.stations","deleteFlag":"0","id":"78dfw778rf78rhg78rds87f78fd87df7","key":"ssd","remark":"dsd","value":"语文教师"},{"configName":"com.qstone.school.stations","deleteFlag":"0","id":"s7dfiuasdhf78sdudsf78dsf89dfs89s","key":"sdahjfdsdf","remark":"sdafds","value":"临时助教"}],"teachInfo":{"accountName":"abc","classIds":"ff808181535fdc5d0153603b68b20082","email":"","gender":"","headImgUrl":"/upload/headImg/40288bfd532bdd2a01532c09df3e00281457612786780.png","hobby":"","id":"40288bfd532bdd2a01532c09df3e0028","jobNumber":"3","mobileNum":"","name":"chenyu","qqNum":"","stationCode":"","stationName":""}}
     * msg : 查询正常
     */

    private String code;
    private DataEntity data;
    private String msg;

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setData(DataEntity data)
    {
        this.data = data;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getCode()
    {
        return code;
    }

    public DataEntity getData()
    {
        return data;
    }

    public String getMsg()
    {
        return msg;
    }

    public static class DataEntity
    {
        /**
         * classList : [{"id":"40288bd75311de15015311df19710001","name":"小班上"},{"id":"40288bd8535e69ac01535e72c2180007","name":"小班下"},{"id":"40288bd8535e69ac01535e7327be0008","name":"中班上"},{"id":"40288bfd532bdd2a01532bf7070d000d","name":"中班下"},{"id":"40288bfd532bdd2a01532bf754a0000e","name":"大班上"},{"id":"40288bfd532bdd2a01532bf791de000f","name":"大班下"}]
         * stationList : [{"configName":"com.qstone.school.stations","deleteFlag":"0","id":"78d8g3484tyd7feuy3498fhidf893487","key":"asdfhjhfdjkh","remark":"sdsdf","value":"数学教师"},{"configName":"com.qstone.school.stations","deleteFlag":"0","id":"78dfw778rf78rhg78rds87f78fd87df7","key":"ssd","remark":"dsd","value":"语文教师"},{"configName":"com.qstone.school.stations","deleteFlag":"0","id":"s7dfiuasdhf78sdudsf78dsf89dfs89s","key":"sdahjfdsdf","remark":"sdafds","value":"临时助教"}]
         * teachInfo : {"accountName":"abc","classIds":"ff808181535fdc5d0153603b68b20082","email":"","gender":"","headImgUrl":"/upload/headImg/40288bfd532bdd2a01532c09df3e00281457612786780.png","hobby":"","id":"40288bfd532bdd2a01532c09df3e0028","jobNumber":"3","mobileNum":"","name":"chenyu","qqNum":"","stationCode":"","stationName":""}
         */

        private TeachInfoEntity teachInfo;
        private List<ClassListEntity> classList;
        private List<StationListEntity> stationList;

        public void setTeachInfo(TeachInfoEntity teachInfo)
        {
            this.teachInfo = teachInfo;
        }

        public void setClassList(List<ClassListEntity> classList)
        {
            this.classList = classList;
        }

        public void setStationList(List<StationListEntity> stationList)
        {
            this.stationList = stationList;
        }

        public TeachInfoEntity getTeachInfo()
        {
            return teachInfo;
        }

        public List<ClassListEntity> getClassList()
        {
            return classList;
        }

        public List<StationListEntity> getStationList()
        {
            return stationList;
        }

        public static class TeachInfoEntity
        {
            /**
             * accountName : abc
             * classIds : ff808181535fdc5d0153603b68b20082
             * email :
             * gender :
             * headImgUrl : /upload/headImg/40288bfd532bdd2a01532c09df3e00281457612786780.png
             * hobby :
             * id : 40288bfd532bdd2a01532c09df3e0028
             * jobNumber : 3
             * mobileNum :
             * name : chenyu
             * qqNum :
             * stationCode :
             * stationName :
             */

            private String accountName;
            private String classIds;
            private String email;
            private String gender;
            private String headImgUrl;
            private String hobby;
            private String id;
            private String jobNumber;
            private String mobileNum;
            private String name;
            private String qqNum;
            private String stationCode;
            private String stationName;

            public void setAccountName(String accountName)
            {
                this.accountName = accountName;
            }

            public void setClassIds(String classIds)
            {
                this.classIds = classIds;
            }

            public void setEmail(String email)
            {
                this.email = email;
            }

            public void setGender(String gender)
            {
                this.gender = gender;
            }

            public void setHeadImgUrl(String headImgUrl)
            {
                this.headImgUrl = headImgUrl;
            }

            public void setHobby(String hobby)
            {
                this.hobby = hobby;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public void setJobNumber(String jobNumber)
            {
                this.jobNumber = jobNumber;
            }

            public void setMobileNum(String mobileNum)
            {
                this.mobileNum = mobileNum;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public void setQqNum(String qqNum)
            {
                this.qqNum = qqNum;
            }

            public void setStationCode(String stationCode)
            {
                this.stationCode = stationCode;
            }

            public void setStationName(String stationName)
            {
                this.stationName = stationName;
            }

            public String getAccountName()
            {
                return accountName;
            }

            public String getClassIds()
            {
                return classIds;
            }

            public String getEmail()
            {
                return email;
            }

            /**
             * @return 0=男 1=女 -1=未设置
             */
            public int getGender()
            {
                if(gender.equals("0"))
                {
                    return 0;
                }else if(gender.equals("1"))
                {
                    return 1;
                }else
                {
                    return -1;
                }

            }

            public String getHeadImgUrl()
            {
                return headImgUrl;
            }

            public String getHobby()
            {
                return hobby;
            }

            public String getId()
            {
                return id;
            }

            public String getJobNumber()
            {
                return jobNumber;
            }

            public String getMobileNum()
            {
                return mobileNum;
            }

            public String getName()
            {
                return name;
            }

            public String getQqNum()
            {
                return qqNum;
            }

            public String getStationCode()
            {
                return stationCode;
            }

            public String getStationName()
            {
                return stationName;
            }
        }

        public static class ClassListEntity
        {
            /**
             * id : 40288bd75311de15015311df19710001
             * name : 小班上
             */

            private String id;
            private String name;

            public void setId(String id)
            {
                this.id = id;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getId()
            {
                return id;
            }

            public String getName()
            {
                return name;
            }
        }

        public static class StationListEntity
        {
            /**
             * configName : com.qstone.school.stations
             * deleteFlag : 0
             * id : 78d8g3484tyd7feuy3498fhidf893487
             * key : asdfhjhfdjkh
             * remark : sdsdf
             * value : 数学教师
             */

            private String configName;
            private String deleteFlag;
            private String id;
            private String key;
            private String remark;
            private String value;

            public void setConfigName(String configName)
            {
                this.configName = configName;
            }

            public void setDeleteFlag(String deleteFlag)
            {
                this.deleteFlag = deleteFlag;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public void setKey(String key)
            {
                this.key = key;
            }

            public void setRemark(String remark)
            {
                this.remark = remark;
            }

            public void setValue(String value)
            {
                this.value = value;
            }

            public String getConfigName()
            {
                return configName;
            }

            public String getDeleteFlag()
            {
                return deleteFlag;
            }

            public String getId()
            {
                return id;
            }

            public String getKey()
            {
                return key;
            }

            public String getRemark()
            {
                return remark;
            }

            public String getValue()
            {
                return value;
            }
        }
    }

    public String getClassToString()
    {
        String classes = "";
        for(int i = 0; i<getData().getClassList().size(); i++)
        {
            if(i == getData().getClassList().size()-1)
            {
                classes = classes+getData().getClassList().get(i).getName();
            }else
            {
                classes = classes+getData().getClassList().get(i).getName()+"，";
            }
        }
        return classes;
    }


}
