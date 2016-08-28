package com.taoy3.freight.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taoy2 on 15-10-15.
 */
public class Freight implements Serializable {


    /**
     * svalid : 2015-12-01
     * tt : 5
     * sccode : CMA
     * start : SHEKOU
     * cls : 7
     * dest : BUSAN
     * valid : 1
     * evalid : 2015-12-31
     * routecode : KPS
     * destid : d158985f-fb78-4ba1-9aa4-d173e606e6f8
     * etd : 2
     * startid : e4f6ec89-44f4-4468-8feb-7ed9bd1f93e4
     * surcharges : []
     * id : d900ed6b-2c7e-4aad-82c5-f81a695520a3
     * state : 2
     * prices : [{"name":"20GP","value":"10"},{"name":"40GP","value":"20"},{"name":"40HQ","value":"20"}]
     * ruleid : b99c0a29-d4c9-4083-9820-fae5359659f4
     * scid : a99e732c-6db8-4128-a26a-a7426dc02179
     */
    private String svalid;
    private int tt;
    private String sccode;
    private String start;
    private int cls;
    private String dest;
    private int valid;
    private String evalid;
    private String routecode;
    private String destid;
    private int etd;
    private String startid;
    private List<SurchargesEntity> surcharges;
    private String id;
    private int state;
    private List<BoxBean> prices;
    private String ruleid;
    private int scid;
    private String remarks;

    public void setSvalid(String svalid) {
        this.svalid = svalid;
    }

    public void setTt(int tt) {
        this.tt = tt;
    }

    public void setSccode(String sccode) {
        this.sccode = sccode;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setCls(int cls) {
        this.cls = cls;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public void setEvalid(String evalid) {
        this.evalid = evalid;
    }

    public void setRoutecode(String routecode) {
        this.routecode = routecode;
    }

    public void setDestid(String destid) {
        this.destid = destid;
    }

    public void setEtd(int etd) {
        this.etd = etd;
    }

    public void setStartid(String startid) {
        this.startid = startid;
    }

    public void setSurcharges(List<SurchargesEntity> surcharges) {
        this.surcharges = surcharges;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setPrices(List<BoxBean> prices) {
        this.prices = prices;
    }

    public void setRuleid(String ruleid) {
        this.ruleid = ruleid;
    }

    public void setScid(int scid) {
        this.scid = scid;
    }

    public String getSvalid() {
        return svalid;
    }

    public int getTt() {
        return tt;
    }

    public String getSccode() {
        return sccode;
    }

    public String getStart() {
        return start;
    }

    public int getCls() {
        return cls;
    }

    public String getDest() {
        return dest;
    }

    public int getValid() {
        return valid;
    }

    public String getEvalid() {
        return evalid;
    }

    public String getRoutecode() {
        return routecode;
    }

    public String getDestid() {
        return destid;
    }

    public int getEtd() {
        return etd;
    }

    public String getStartid() {
        return startid;
    }

    public List<SurchargesEntity> getSurcharges() {
        return surcharges;
    }

    public String getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public List<BoxBean> getPrices() {
        return prices;
    }

    public String getRuleid() {
        return ruleid;
    }

    public int getScid() {
        return scid;
    }

    public String getRemarks() {
        return remarks;
    }
}
