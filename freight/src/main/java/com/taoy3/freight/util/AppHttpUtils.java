package com.taoy3.freight.util;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.taoy3.freight.constant.CacheDataConstant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

/**
 * Created by KX-09 on 2015/9/16.
 */
public class AppHttpUtils {
    public static void putUtils(final String srcUrl, Context context, final String obj, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int status = 0;
                String result = "";
                try {
//                    HttpClient httpclient = new DefaultHttpClient();
//                    HttpPut httpPut = new HttpPut(srcUrl);
//                    httpPut.addHeader("Content-Type", "application/thrift");
//                    if (CacheDataConstant.userBean != null && CacheDataConstant.userBean.getAuthorization() != null) {
//                        httpPut.addHeader("authorization", CacheDataConstant.userBean.getAuthorization());
//                    }
//                    StringEntity entity = new StringEntity(obj,"utf-8");
//                    httpPut.setEntity(entity);
//                    HttpResponse response = httpclient.execute(httpPut);
//                    status = response.getStatusLine().getStatusCode();
//
//                    if (status == HttpStatus.SC_OK) {
//                        HttpEntity httpEntity = response.getEntity();
//                        result = EntityUtils.toString(httpEntity);
//                        Log.e("TTTT",result);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (handler != null) {
                        Message msg = handler.obtainMessage(status, result.toString());
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();
    }

    public static void postUtils(final String srcUrl, final String method, final String obj, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                int state = 0;
                StringBuffer result = new StringBuffer();
                BufferedReader reader = null;
                BufferedWriter writer = null;
                try {
                    URL url = new URL(srcUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoInput(true);//读数据
                    conn.setDoOutput(true);//默认是 false
                    conn.setRequestMethod(method);//请求方法为
                    conn.addRequestProperty("Content-Type", "application/json");
                    if (CacheDataConstant.userBean != null && CacheDataConstant.userBean.getAuthorization() != null) {
                        conn.addRequestProperty("authorization", CacheDataConstant.userBean.getAuthorization());
                    }
                    if (obj != null && obj.trim().length() > 0) {
                        writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                        writer.write(obj);
                        writer.flush();
                    }
                    state = conn.getResponseCode();
                    if (state == 401) {
                        LogUtils.login(CacheDataConstant.userBean.getUsername(), CacheDataConstant.userBean.getPassWord());
                        LogUtils.setMsg(new LogUtils.NotifyLoginMsg() {
                            @Override
                            public void logMsg(int state, String result) {
                                if (state / 100 == 2) {
                                    postUtils(srcUrl, method, obj, handler);
                                }
                            }
                        });
                        return;
                    }
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    conn.connect();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                } catch (Exception e) {
                    if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                        result = new StringBuffer("连接超时");
                    } else {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        if (reader != null)
                            reader.close();
                        if (writer != null)
                            writer.close();
                        if (conn != null)
                            conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (handler != null) {
                            Message msg = handler.obtainMessage(state, result.toString());
                            handler.sendMessage(msg);
                        }
                    }
                }
            }
        }).start();
    }

    public static void getUtils(final String httpUrl, final Handler handler) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                StringBuffer result = new StringBuffer();
//                int state = 0;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL(httpUrl);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.addRequestProperty("Content-Type", "application/json");
//                    if (CacheDataConstant.userBean != null && CacheDataConstant.userBean.getAuthorization() != null) {
//                        conn.addRequestProperty("authorization", CacheDataConstant.userBean.getAuthorization());
//                    }
//                    conn.setConnectTimeout(10000);
//                    state = conn.getResponseCode();
//                    if (state / 100 == 2) {
//                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                        conn.connect();
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            result.append(line);
//                        }
//                        conn.disconnect();
//                        reader.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (handler != null) {
//                        Message msg = handler.obtainMessage(state, result);
//                        handler.sendMessage(msg);
//                    }
//                }
//            }
//        }).start();
    }
}
