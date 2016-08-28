package com.taoy3.freight.constant;

import java.util.Date;

/**
 * Created by KX-09 on 2015/9/18.
 */
public class UrlConstant {
//   http://180.76.143.79:8080/restapi/webapi/basedata/currencys?count=10&page=1

    private static final String baseUrl="http://180.76.143.79:8080/restapi/webapi/";

    public static String currUrl = baseUrl+"basedata/currencys?count=10&page=1";

    public static String submitOrder = baseUrl+"orderzone/oceans";

    public static String getSurchargeUrl(String id){
        return baseUrl+"general/prices/wechat/"+id;
    }

    public static String getShipCompanyUrl(int count){
        return "http://webapi.hyun-young.com/base/shipcompany?count=62";
    }

    public static String getPortUrl(int count){
        return "http://webapi.hyun-young.com/base/location/search?condition=&page=1&count=5294";
    }

    public static String getVoyageUrl(int page,String startPort,String destPort,String pidScom){
        String url = "http://webapi.hyun-young.com/general/voyage/search?count=20&page=" +page;
        if (startPort!=null&&(!"".equals(startPort))){
            url+= "&start_name="+startPort.replace(" ","+");
        }
        if (destPort!=null&&(!"".equals(destPort))){
            url+= "&des_name="+destPort.replace(" ","+");
        }
        if (pidScom!=null&&(!"".equals(pidScom))){
            url+= "&pid_scom="+pidScom.replace(" ","+");
        }
        return url;
    }
    public static String getPriceUrl(int page,String startId, String destId,String scId){
        return baseUrl+"ocean/prices?count=20&page="+page+"&startid="+startId
                +"&destid="+destId+"&sc_id="+scId;
    }

    public static String getLogInUrl(){
        return baseUrl+"user/login";
    }

    /**
     * @param customerId 客户id
     * @return 请求方式  获取发货人地址列表 Get 不用id
                        获取单条发货人地址Get
                        创建发货人地址  PUT
                        更新发货人地址  POST
                        删除发货人地址  DELETE
     */
    public static String getShippersUrl(String id,String customerId){
        return baseUrl+"usermanage/address/shippers"+appendId(id)+appendCusId(customerId);
    }
    public static String getConsigneesUrl(String id,String customerId){
        return baseUrl+"usermanage/address/consignees"+appendId(id)+appendCusId(customerId);
    }
    public static String getNotifysUrl(String id,String customerId){
        return baseUrl+"usermanage/address/notifys"+appendId(id)+appendCusId(customerId);
    }
    private static String appendId(String id){
        if(id!=null&&id.trim().length()>0){
            return "/"+id;
        }
        return "";
    }
    private static String appendCusId(String customerId){
        if(customerId!=null&&customerId.trim().length()>0){
            return "?customer_id="+customerId;
        }
        return "";
    }
    public static String getCustomersUrl(){
        return baseUrl+"usermanage/customers";
    }
    public static String getVersionUrl(int platform){
        return baseUrl+"user/login";
//        http://webapi.hyun-young.com/user/account/version?platform=1
    }
    public static String getNewVersionUrl(){
        return baseUrl+"user/account/version?platform=android";
    }
    public static String appDownLoadUrl(String appName){
        return baseUrl+"download/android/"+appName;
    }
    public static String getEnquiryUrl(int page, Date date){
        return baseUrl+"general/inquiry/list?count=10&page="+page+"&date="+date;
    }
    public static String getEnquiryDetailUrl(String response){
//        http://webapi.hyun-young.com/general/inquiry/list?response=
        return baseUrl+"general/inquiry/list?response="+response;
    }
}
