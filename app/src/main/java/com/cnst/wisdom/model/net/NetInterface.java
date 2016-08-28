package com.cnst.wisdom.model.net;

/**
 * 所有接口
 * @author jiangzuyun.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class NetInterface
{
    private static final String baseUrl ="http://192.168.11.87:8080/school";//此接口作为零时使用,从百度上拉取图片
    public static final String BDMZ = "http://image.baidu.com/channel/listjson";
    public static final String baidumeinv="http://image.baidu.com/channel/listjson?rn=6&tag1=美女&tag2=可爱&ie=utf8&pn=1";

    /**
     * 获取学科信息 subject
     * 获取学期信息 term
     * 获取课名信息 classname
     */
    public static String getTeachCategorys= baseUrl+"/teachPlan/getTeachCategorys_app.kq";
    //教学进度和修改计划时展示的教学计划列表
    public static String getTeachPlanList= baseUrl+"/teachPlan/getTeachPlanList_app.kq";
    //保存设定的教学计划
    public static String saveTeachPlan= baseUrl+"/teachPlan/saveTeachPlan_app.kq";
    //修改教学计划
    public static String modifyTeachPlan= baseUrl+"modifyTeachPlan_app.kg";
    //教学进度确认或还原
    public static String changePlanState=  baseUrl+"changePlanState_app.kg";
}
