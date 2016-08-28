package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-9-22.
 */
public class FreightItemEntity implements Serializable {
    /**
     * sc_name : MSC
     * gp40 : 200.0
     * hq40 : 200.0
     * gp20 : 200.0
     * count : 0
     * sc_id : b68db877-9462-4412-9464-bfbcc5912d87
     * id : a5685727-0508-4879-9994-d616bd255983
     */
    private String sc_name;
    private String gp20;
    private String gp40;
    private String hq40;
    private String hq45;
    private String rf20;
    private String rf40;
    private String nor40;
    private int count;
    private String sc_id;
    private String id;
    private Integer cls;//截关日期
    private Integer etd;//开船日期
    private String startPort = "起运港";//起运港
    private String line = "航线代码";//航线代码

    public String getHq45() {
        return hq45;
    }

    public void setHq45(String hq45) {
        this.hq45 = hq45;
    }

    public String getRf20() {
        return rf20;
    }

    public void setRf20(String rf20) {
        this.rf20 = rf20;
    }

    public String getRf40() {
        return rf40;
    }

    public void setRf40(String rf40) {
        this.rf40 = rf40;
    }

    public String getNor40() {
        return nor40;
    }

    public void setNor40(String nor40) {
        this.nor40 = nor40;
    }

    public Integer getCls() {
        return cls;
    }

    public void setCls(Integer cls) {
        this.cls = cls;
    }

    public Integer getEtd() {
        return etd;
    }

    public void setEtd(Integer etd) {
        this.etd = etd;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public void setGp40(String gp40) {
        this.gp40 = gp40;
    }

    public void setHq40(String hq40) {
        this.hq40 = hq40;
    }

    public void setGp20(String gp20) {
        this.gp20 = gp20;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setSc_id(String sc_id) {
        this.sc_id = sc_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSc_name() {
        return sc_name;
    }

    public String getGp40() {
        return gp40;
    }

    public String getHq40() {
        return hq40;
    }

    public String getGp20() {
        return gp20;
    }

    public int getCount() {
        return count;
    }

    public String getSc_id() {
        return sc_id;
    }

    public String getId() {
        return id;
    }

    public String getStartPort() {
        return startPort;
    }

    public void setStartPort(String startPort) {
        this.startPort = startPort;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
