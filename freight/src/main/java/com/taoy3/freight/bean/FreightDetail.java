package com.taoy3.freight.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taoy2 on 15-10-15.
 */
public class FreightDetail implements Serializable {

    /**
     * svalid : 2015-10-01
     * tt : ​5.0
     * start : SHEKOU
     * sc_id : ad962bff-c1ab-49d9-a765-905a7e45231f
     * cls : ​2
     * dest : TOKYO
     * via : DIRECT
     * sc_name : MOL
     * rule_id : d216e183-a481-4b26-bb42-1449093a7781
     * evalid : 2015-12-31
     * etd : ​3
     * prices : {"gp40":"70.0","hq40":"70.0","gp20":"60.0"}
     * surcharge : [{"gp40":"1080","hq40":"1080","fee_code":"THC","gp20":"740","feename":"码头费","fee_id":"a74b83cc-8ae4-4066-a1fd-00244cb28d25","pay_type":"\u200b1","usingType":"\u200b2","paycurr":"CNY","type":"\u200b1"},{"tprice":"450","fee_code":"DOC","feename":"文件费","fee_id":"f0df96e8-9764-45c5-8ace-fd2645d96c26","pay_type":"\u200b1","usingType":"\u200b2","paycurr":"CNY","type":"\u200b0"},{"tprice":"450","fee_code":"TRS","feename":"电放费","fee_id":"3a8c8226-f9d2-474a-892a-341f50899e9f","pay_type":"\u200b1","usingType":"\u200b2","paycurr":"CNY","type":"\u200b0"},{"gp40":"40","hq40":"40","fee_code":"EIR","gp20":"40","feename":"打单费","fee_id":"901c24b7-b39d-4b16-a61d-f10b3590a98f","pay_type":"\u200b1","usingType":"\u200b2","paycurr":"CNY","type":"\u200b1"},{"gp40":"50","hq40":"50","fee_code":"SEAL","gp20":"50","feename":"铅封费","fee_id":"df91d7c9-a150-404f-9d05-a26ca26dd487","pay_type":"\u200b1","usingType":"\u200b2","paycurr":"CNY","type":"\u200b1"}]
     * service_code : CHS4
     * id : d30c7ca5-053c-4d42-abd5-967ea7cfd45d
     * state : ​1
     * paycurr : USD
     */
    private String svalid;
    private String tt;
    private String start;
    private String start_id;
    private String sc_id;
    private String cls;
    private String dest;
    private String des_id;
    private String via;
    private String sc_name;
    private String rule_id;
    private String evalid;
    private String etd;
    private Price price;
    private List<SurchargesEntity> surcharges;
    private String service_code;
    private String id;
    private String state;
    private String curr;
    private String remarks;

    public String getStart_id() {
        return start_id;
    }

    public void setStart_id(String start_id) {
        this.start_id = start_id;
    }

    public String getDes_id() {
        return des_id;
    }

    public void setDes_id(String des_id) {
        this.des_id = des_id;
    }

    public FreightDetail() {
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setSvalid(String svalid) {
        this.svalid = svalid;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setSc_id(String sc_id) {
        this.sc_id = sc_id;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public void setEvalid(String evalid) {
        this.evalid = evalid;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void setSurcharges(List<SurchargesEntity> surcharges) {
        this.surcharges = surcharges;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }

    public String getSvalid() {
        return svalid;
    }

    public String getTt() {
        return tt;
    }

    public String getStart() {
        return start;
    }

    public String getSc_id() {
        return sc_id;
    }

    public String getCls() {
        return cls;
    }

    public String getDest() {
        return dest;
    }

    public String getVia() {
        return via;
    }

    public String getSc_name() {
        return sc_name;
    }

    public String getRule_id() {
        return rule_id;
    }

    public String getEvalid() {
        return evalid;
    }

    public String getEtd() {
        return etd;
    }

    public Price getPrice() {
        return price;
    }

    public List<SurchargesEntity> getSurcharges() {
        return surcharges;
    }

    public String getService_code() {
        return service_code;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getCurr() {
        return curr;
    }
}
