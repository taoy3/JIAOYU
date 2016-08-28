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
 * 简述
 * 详细功能描述
 *
 * @author yuhongwen
 * @time 14:13
 * @see
 */
public class HealthPresenter extends BasePresenter<AttenView>{
    private String TAG = "HealthPresenter";
    private String mTeacherId;

    public HealthPresenter(Context context, AttenView view)
    {
        super(context, view);
    }

    //    public void getData(Date query, final Object tag){
    //        mView.showLoadingView(true);
     /*   VolleyManager.getInstance().getString(NetInterface.baidumeinv, null, tag, new NetResult<String>()
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
        });*/

//        VolleyManager.getInstance().getString(attenceUrl,);
//    }

    public void  getData(String classId,String whichDay ,Object tag){
        mView.showLoadingView(true);
        //        teacherId--->是在用户登录成功后,后台返回的(包含在json字符串中)
        if(BaseApplication.getLogin()!=null && BaseApplication.getLogin().getData()!=null)
        {
            Log.i(TAG, "ClassDataBean is :"+BaseApplication.getLogin().getData().toString());
            mTeacherId = BaseApplication.getLogin().getData().getUserId();
            Log.i(TAG, "mTeacherId is: "+mTeacherId);
            String attenceUrl = Constants.SERVER+Constants.LOAD_HEALTH;
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
                    Log.i(TAG, "VolleyError is:"+error.toString());

                }

                @Override
                public void onSucceed(Object response)
                {
                    mView.showLoadingView(false);
                    mView.showSucceedView(true);
                    mView.refreshData(response);
                    Log.i(TAG, "response success json--"+response.toString());
                }
            });
        }else{
            Log.i(TAG,"mTeacherId is not response");
        }
    }





    public void cancelNet(Object tag)
    {
        mView.showLoadingView(false);
        VolleyManager.getInstance().cancel(tag);
    }
}
