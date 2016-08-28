package com.cnst.wisdom.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.view.AttenView;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author jiangzuyun.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class AttenPresenter extends BasePresenter<AttenView>
{
    private String TAG = "AttenPresenter";
    //下面变量作为请求参数
    private String mTeacherId;
    private String mClassId;
    private String mWhichDay;
    private Context mContext;

    public AttenPresenter(Context context, AttenView view)
    {
        super(context, view);
    }

   /* public void getData(Date query, final Object tag)
    {
        mView.showLoadingView(true);
        VolleyManager.getInstance().getString(NetInterface.baidumeinv, null, tag, new NetResult<String>()
        {
            @Override
            public void onFailure(VolleyError error)
            {
                mView.showLoadingView(false);
                mView.showErrorView(true);
                Log.i(TAG, "VolleyError is:"+error.toString());
            }

            @Override
            public void onSucceed(String response)
            {
                mView.showLoadingView(false);
                mView.showSucceedView(true);
                mView.refreshData(response);
            }
        });


    }*/
    //add by yhw

    /**
     *
     * @param classId-->班级类型
     * @param tag-->考勤时间
     *
     */
    public void  getData(String classId,String whichDay ,Object tag){
        mView.showLoadingView(true);
        //        teacherId--->是在用户登录成功后,后台返回的(包含在json字符串中)
        if(BaseApplication.getLogin() != null && BaseApplication.getLogin().getData()!=null)
        {
            Log.i(TAG, "ClassDataBean is :"+BaseApplication.getLogin().getData().toString());
            mTeacherId = BaseApplication.getLogin().getData().getUserId();
            Log.i(TAG, "mTeacherId is: "+mTeacherId);
            Log.i(TAG, "mclassId is: "+classId);
            String attenceUrl = Constants.SERVER+Constants.LOAD_ATTENDANCE;
//            Map<String,String> attenMap = new HashMap<String,String>();
//            attenMap.put("teacherId", mTeacherId);
//            attenMap.put("classId", classId);
//            attenMap.put("whichDay", whichDay);
            //修改的原因是:HashMap不能按照插入顺序,输出数据,而LinkedHashMap可以实现这种功能
            Map<String,String> attenMap = new LinkedHashMap<String,String>();
            attenMap.put("teacherId", mTeacherId);
            attenMap.put("classId", classId);
            attenMap.put("whichDay", whichDay);
            VolleyManager.getInstance().getString(attenceUrl, attenMap, tag, new NetResult()
            {
                @Override
                public void onFailure(VolleyError error)
                {
                    mView.showLoadingView(false);
                    mView.showErrorView(true);
                    Log.i(TAG, "VolleyError is:"+error.toString());  // VolleyError is:com.android.volley.ServerError  这个错误最有可能是服务器响应错误,http 状态码 4xx 5xx

                }
                @Override
                public void onSucceed(Object response)
                {
                    mView.showLoadingView(false);
                    mView.showSucceedView(true);
                    mView.refreshData(response);//mView--->Attendance
                    Log.i(TAG, "response success json--"+response.toString());
                }
            });
        }else{
            if(BaseApplication.getLogin() == null){
                Log.i(TAG,"BaseApplication.getLogin():"+"is null");
            }else if(BaseApplication.getLogin().getData()==null){
                Log.i(TAG,"classDataBean is null");
            }

        }
    }

    public void cancelNet(Object tag)
    {
        mView.showLoadingView(false);
        VolleyManager.getInstance().cancel(tag);
    }
}
