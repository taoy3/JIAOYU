package com.cnst.wisdom.model.net;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.db.DbUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求工具类
 *
 * @author jiangzuyun.
 * @see [ getString，postString，getJsonArray，getJsonObject，postJsonObject，cancel，setCoolie]
 * @since [产品/模版版本]
 */
public class VolleyManager {
    public static final String PROTOCOL_CHARSET = "utf-8";
    private static VolleyManager sNetworkManager;
    public final RequestQueue mRequestQueue;
    private String cookie;
    private static String TAG = "VolleyManager";

    private VolleyManager(Context context) {
        //        mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyManager getInstance() {
        return create(BaseApplication.getContext());
    }

    private static VolleyManager create(Context context) {
        context = context.getApplicationContext();
        if (sNetworkManager == null) {
            synchronized (VolleyManager.class) {
                if (sNetworkManager == null) {
                    sNetworkManager = new VolleyManager(context);
                }
            }
        }
        return sNetworkManager;
    }
public void logIn(String url, Map<String, String> urlParams, Object tag, final NetResult result){
//    JSONObject object = new JSONObject();
//    try {
//        object.put("code",200);
//        object.put("sessionId","404EF49AAF160D2F145CC6BC42AD6470");
//        object.put("msg","success");
//        JSONObject data = new JSONObject();
//        data.put("headImgUrl","head.jpg");
//        data.put("stationCode","");
//        data.put("username",urlParams.get("username"));
//        data.put("school","河西幼儿园");
//        data.put("name","汪");
//        data.put("userId","40288bd75317ac86015317c3b2a30001");
//        data.put("stationName",null);
//        object.put("data",data);
//        Log.e("LogInfo",object.toString());
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
    result.onSucceed(DbUtils.getAssetString("Login.json"));
}
    public void cancel(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    /**
     * GET请求 string数据
     *
     * @param url       url 不带？
     * @param urlParams 将会拼在url后面
     * @param tag
     * @param result
     */
    public void getString(String url, Map<String, String> urlParams, Object tag, final NetResult result) {
        requestString(Request.Method.GET, urlWithParam(url, urlParams), null, tag, result);

    }

    /**
     * POST请求string数据
     *
     * @param url
     * @param params
     * @param tag
     * @param result
     */
    public void postString(String url, Map<String, String> params, Object tag, final NetResult result) {
        requestString(Request.Method.POST, url, params, tag, result);
    }

    private void requestString(int method, String url, final Map<String, String> params, Object tag, final NetResult result) {
        StringRequest request = new StringRequest(method, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String msg = object.getString("msg");
                    Toast.makeText(BaseApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result.onSucceed(s);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                StackTraceElement[] stackTrace = volleyError.getStackTrace();
                for (int i = stackTrace.length - 1; i >= 0; i--) {
                    StackTraceElement stackTraceElement = stackTrace[i];
                    com.orhanobut.logger.Logger.e(stackTraceElement.toString());
                }
                Logger.e(volleyError.getStackTrace() + "");
                if (volleyError.networkResponse != null) {
                    Log.i(TAG, "volleyError: " + volleyError.toString() + ">>" +
                            volleyError.networkResponse.statusCode + ">>" +
                            volleyError.networkResponse.data + ">>" +
                            volleyError.getCause() + ">>" +
                            volleyError.getMessage());

                } else {
                    Log.i(TAG, "volleyError.networkResponse is null");
                }
                result.onFailure(volleyError);
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
//                Log.i(TAG,"response.headers :" +response.headers);
                Log.i(TAG, "response.headers :" + response.headers);
                String rawCookies = responseHeaders.get("Set-Cookie");
                Logger.d("getCookie：" + rawCookies);
                String parsed;
                try {
                    parsed = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }

            //传递post请求参数  get时null
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();
                mHeaders.put("Cookie", cookie);
                Logger.d("setCookie：" + cookie);
                return mHeaders;
            }
        };
        request.setTag(tag);
        mRequestQueue.add(request);
        //        mRequestQueue.start(); // save code


    }

    /**
     * 只支持GET请求
     *
     * @param url
     * @param map    拼接在url后的参数
     * @param tag
     * @param result
     */
    public void getJsonArray(String url, Map map, Object tag, final NetResult result) {
        url = urlWithParam(url, map);
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                result.onSucceed(jsonArray);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result.onFailure(volleyError);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();
                mHeaders.put("Cookie", cookie);
                Logger.d("setCookie：" + cookie);
                return mHeaders;
            }
        };
        request.setTag(tag);
        mRequestQueue.add(request);
    }


    /**
     * @param url
     * @param tag
     * @param result
     */
    private void requestJsonObject(String url, JSONObject jsonRequest, Object tag, final NetResult result) {
        //当jsonRequest为null的时候 为get请求 否则为post
        JsonObjectRequest request = new JsonObjectRequest(url, jsonRequest,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        result.onSucceed(jsonObject);
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result.onFailure(volleyError);
            }

        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                Logger.d("getCookie：" + rawCookies);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();
                mHeaders.put("Cookie", cookie);
                Logger.d("setCookie：" + cookie);
                return mHeaders;
            }
        };
        request.setTag(tag);
        mRequestQueue.add(request);
    }

    /**
     * POST请求 带有cookies   JSONID=.........
     *
     * @param url
     * @param map
     * @param tag
     * @param netResult
     */
    public void postJsonObject(String url, Map map, Object tag, final NetResult netResult) {
        //        SparseArray优化的hashmap高效 但是键 只能是 int数据
        //        JSONObject json = new JSONObject(map);
        if (map != null) {
            requestJsonObject(url, new JSONObject(map), tag, netResult);
        } else {
            requestJsonObject(url, null, tag, netResult);
        }
    }

    /**
     * GET请求 jsonobject类型数据
     *
     * @param url    接口
     * @param map    GET后的请求参数
     * @param tag
     * @param result
     */
    public void getJsonObject(String url, Map<String, String> map, Object tag, final NetResult result) {
        url = urlWithParam(url, map);
        requestJsonObject(url, null, tag, result);
    }

    @NonNull
    private String urlWithParam(String url, Map<String, String> map) {
        url += "?";
        StringBuffer stringBuffer = new StringBuffer(url);
        if (null != map && map.size() > 0) {
            for (String key : map.keySet()) {
                //这里要判断teacherId参数值是否为null
                if (map.get(key) != null) {
                    stringBuffer.append(key + "=" + map.get(key).toString() + "&");
                } else {
                    Log.i(TAG, "params value is:" + map.get(key));
                }

            }
        }
        return stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
    }

    /**
     * 设置请求头中的cookie
     *
     * @param cookie
     */
    public void setCoolie(String cookie) {
        this.cookie = "JSESSIONID=" + cookie;
    }

    /**
     * 获取请求头中的cookie
     *
     * @return
     */
    public String getCookie() {
        return this.cookie;
    }

}
