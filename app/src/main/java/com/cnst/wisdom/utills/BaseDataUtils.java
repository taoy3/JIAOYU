package com.cnst.wisdom.utills;

import com.android.volley.VolleyError;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Clazz;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.bean.Subject;
import com.cnst.wisdom.model.bean.TeachBaseBean;
import com.cnst.wisdom.model.bean.Term;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author taoyuan
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class BaseDataUtils {
    private static String TAG = BaseDataUtils.class.getName();
    private static String TYPE = "type";
    private static String SUBJECT = "subject";
    private static String TERM = "term";
    //服务器地址
    public static String SERVER = Constants.SERVER;
    public interface ReturnData<T>{
        void refreshData(List<T> list);
    }
    //获取班级列表
    public static void getClazzList(final ReturnData<Clazz> returnData) {
        BaseApplication app=BaseApplication.getApplication();
        Login mlogin=app.getLogin();
        if(mlogin==null||mlogin.getData()==null||mlogin.getData().getUserId()==null){
            return;
        }
        Map classMap = new HashMap();
        classMap.put("teacherId", mlogin.getData().getUserId());
        VolleyManager.getInstance().getString(Constants.SERVER+Constants.loadClassInfo, classMap, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                returnData.refreshData(null);
            }

            @Override
            public void onSucceed(String response) {
                TeachBaseBean<Clazz> temp = null;
                    if (response != null && response.length() > 0) {
                        temp = new GeoUtil()
                                .deserializer(response, new TypeToken<TeachBaseBean<Clazz>>() {
                                }.getType());
                    }
                returnData.refreshData(temp != null ? temp.getData() : null);
            }
        });
    }

    /**
     * type	否	当获取学科信息时值为subject
     * fk	是	预留，当前直接传入空。后期可传入幼儿园id获取某个幼儿园下的学科
     */
    public static void getsSubjectList(final ReturnData<Subject> returnData) {
        Map subjectMap = new HashMap();
        subjectMap.put(TYPE, SUBJECT);//当获取学期信息时值为term 不可空
        //        classMap.put("fk","fk"); //获取学期信息 传入所属学科id；若为空则查询所有学科下的学期信息 不可空
        VolleyManager.getInstance().getString(Constants.SERVER+Constants.getTeachCategorys, subjectMap, TAG, new NetResult<String>() {

            @Override
            public void onFailure(VolleyError error) {
                returnData.refreshData(null);
            }

            @Override
            public void onSucceed(String response) {
                TeachBaseBean<Subject> temp = null;
                if (response != null && response.length() > 0) {
                    temp = new GeoUtil()
                            .deserializer(response, new TypeToken<TeachBaseBean<Subject>>() {
                            }.getType());
                }
                returnData.refreshData(temp != null ? temp.getData() : null);
            }
        });
    }

    public static void getsTermList(final ReturnData<Term> returnData,String fk) {
        //获取学科列表
        Map termMap = new HashMap();
        termMap.put(TYPE, TERM);//当获取学期信息时值为term 不可空
        if(fk!=null){
            termMap.put("fk",fk); //传入所属学科id；若为空则查询所有学科下的学期信息
        }
        VolleyManager.getInstance().getString(Constants.SERVER+Constants.getTeachCategorys, termMap, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                returnData.refreshData(null);
            }

            @Override
            public void onSucceed(String response) {
                TeachBaseBean<Term> temp = null;
                if (response != null && response.length() > 0) {
                    temp = new GeoUtil()
                            .deserializer(response, new TypeToken<TeachBaseBean<Term>>() {
                            }.getType());

                }
                returnData.refreshData(temp != null ? temp.getData() : null);
            }
        });
    }
}
