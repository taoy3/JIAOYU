package com.cnst.wisdom.model;

import com.cnst.wisdom.model.bean.Login;

/**
 * Created by Jonas on 2015/11/19.
 * 常量
 */
public class Constants {

    public static final String baidumeinv = "http://image.baidu.com/channel/listjson?rn=6&tag1=美女&tag2=可爱&ie=utf8&pn=1";
    public static final String baidumeinv2 = "http://image.baidu.com/channel/listjson?rn=6&tag1=美女&tag2=可爱&ie=utf8&pn=";

    public static String screenh = "screenhwith";
    public static String screenw = "screenwidth";

    //服务器地址
    public static String SERVER = "http://192.168.1.102:8080/Jiaoyu/";


    //APP教师端登录接口
    public static final String LOGIN = "tchLogin";
    //APP教师端登录接口
    public static final String Registe = "tchRegister";

    //个人中心-修改密码接口
    public static final String UPDATE_PASSWORD = "app/updatePassword";

    //个人中心-上传用户头像
    public static final String UPLOAD_HEAD_IMG = "app/uploadHeadImg";

    //状态码-操作成功
    public static final int STATUS_SUCCESS = 200;
    //状态码-操作失败
    public static final int STATUS_FAIL = -1;
    //状态码-服务器内部异常
    public static final int STATUS_SERVER_EXCEPTION = 500;
    //状态码-数据查询不存在
    public static final int STATUS_DATA_NOTFOUND = 404;
    //状态码-缺少参数，或参数不正确
    public static final int STATUS_ARGUMENT_ERROR = 417;
    //状态码-连接超时
    public static final int STATUS_TIMEOUT = 504;
    //状态码-非法请求
    public static final int STATUS_ILLEGAL = 503;


    //状态码-操作成功
    public static final String STATUS_SUCCESS_STRING = "200";
    //状态码-操作失败
    public static final String STATUS_FAIL_STRING = "-1";
    //状态码-服务器内部异常
    public static final String STATUS_SERVER_EXCEPTION_STRING = "500";
    //状态码-数据查询不存在
    public static final String STATUS_DATA_NOTFOUND_STRING = "404";
    //状态码-缺少参数，或参数不正确
    public static final String STATUS_ARGUMENT_ERROR_STRING = "417";
    //状态码-连接超时
    public static final String STATUS_TIMEOUT_STRING = "504";
    //状态码-非法请求
    public static final String STATUS_ILLEGAL_STRING = "503";
    //从sharepreference获取服务器地址
    public static final String GET_SERVER = "server";

    //从sharepreference获取登录帐号username
    public static final String GET_USERNAME = "username";

    public static Login mlogin = null;

    /**
     * 获取素材分类列表信息
     * 获取列表对应资源素材信息
     */

    //获取素材分类列表
    public static final String MATERIAL_DICTLIST = "app/queryDictList";
    //获取列表对应资源素材信息
    public static final String QUERY_MATERIAL = "app/queryResourceMaterial";
    public static final String QUERYRESOURCEMATERIALLIKE = "app/queryResourceMaterialLike";

    //获取素材详情加评论列表
    public static final String DETAIL_EVALUE = "/app/queryResourceMaterialDetail";
    //获取反馈记录列表
    public static final String FEEDBACK_LIST = "/app/queryDemandNeedList";
    //删除反馈
    public static final String DELETE_DEMANDNEED = "/app/deleteDemandNeed";
    //发表评论内容
    public static final String ADD_EVALUE = "/app/addEvalute";
    //提交需求反馈
    public static final String FEED_BACK = "/app/addDemandNeed";
    /*add by yhw
    接入考勤接口和健康管理接口*/
    public static final String LOAD_ATTENDANCE = "app/student/loadAttendance"; //考勤接口
    public static final String LOAD_HEALTH = "app/student/loadHealth"; //健康管理接口


    //Guidance
    public static String GuidanceResource = SERVER + "app/queryResourceMaterialkqzd";
    public static String GuidanceCategory = SERVER + "teachPlan/getTeachCategorys";
    public static String GuidanceRelative = SERVER + "app/queryResourceMaterialDetailAbout_app";
    public static String MaterialRelative = SERVER + "/app/queryResourceMaterialDetailAboutZysc_app";

    /**
     * 获取学科信息 subject
     * 获取学期信息 term
     * 获取课名信息 classname
     */
    public static String getTeachCategorys = "teachPlan/getTeachCategorys";
    //保存设定的教学计划
    public static String saveTeachPlan = "teachPlan/saveTeachPlan_app";
    //教学进度时展示的教学进度列表
    public static String getTeachPlanList = "teachPlan/getTeachPlanList_app";
    //进入教学计划修改页面时展示的列表数据
    public static String getPlanModifyList = "teachPlan/getPlanModifyList_app";
    //修改教学计划
    public static String modifyTeachPlan = "teachPlan/modifyTeachPlan_app";
    //教学进度确认或还原
    public static String changePlanState = "teachPlan/changePlanState_app";
    //删除教学计划
    public static String delTeachPlan = "teachPlan/delTeachPlan_app";
    //跳到教学计划修改界面根据panId重新获取数据
    public static String getTeachPlanInfo = "teachPlan/getTeachPlanInfo_app";
    //根据教师id查询班级列表
    public static String loadClassInfo = "app/teacher/loadClassInfo_app";
    //查询教学计划起始周列表/teachPlan/getStartWeeks_app.kq
    public static String getStartWeeks = "teachPlan/getStartWeeks_app";
    //获取个人信息
    public static String QUERY_INFO = "app/queryTeacherInfo_app";
    //提交个人信息
    public static String UPDATE_INFO = "app/updateTeachByid_app";


}

