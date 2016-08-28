package com.taoy3.freight.bean;


import com.taoy3.freight.BaseApp;
import com.taoy3.freight.util.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy2 on 15-12-2.
 */
public class PriceEntity implements Serializable{
    private String sc_name;
    private List<BoxBean> boxBeans = new ArrayList<>();
    private int id;
    private long cls;//截关日期
    private long etd;//开船日期
    private String startPort = "起运港";//起运港
    private String destPort = "目的港";//起运港
    private int startId ;//起运港
    private int destId;//起运港
    private String line = "航线代码";//航线代码
    private int tt;
    private String remarks;
    private int via;
    private int svalid;
    private int evalid;
    private List<SurchargesEntity> surcharges;
    private int sc_id;

    public PriceEntity(Port start, Port dest, PriceBean priceBean) {
        this.sc_name = priceBean.getSc_name();
        this.boxBeans = createBoxes(dest.getId());
        this.etd= DateUtils.after(priceBean.getSc_id());
        this.cls= DateUtils.after(priceBean.getSc_id()+1);
        this.startPort = start.getName_zh();
        this.destPort = dest.getName_zh();
        this.startId = start.getId();
        this.destId = dest.getId();
        this.line = priceBean.getLine();
        this.tt=priceBean.getSc_id()%2;
        this.remarks="mark";
        this.via=priceBean.getSc_id()%2;
        this.svalid = priceBean.getSc_id();
        this.evalid = priceBean.getSc_id();
        this.surcharges = createSurcharges(priceBean.getSc_id());
        this.sc_id = priceBean.getSc_id();

    }

    public PriceEntity() {

    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setVia(int via) {
        this.via = via;
    }

    public void setSvalid(int svalid) {
        this.svalid = svalid;
    }

    public void setEvalid(int evalid) {
        this.evalid = evalid;
    }

    public void setSurcharges(List<SurchargesEntity> surcharges) {
        this.surcharges = surcharges;
    }

    public void setSc_id(int sc_id) {
        this.sc_id = sc_id;
    }

    private List<SurchargesEntity> createSurcharges(int sc_id) {
        List<SurchargesEntity> surchargesEntities = new ArrayList<>();
        for (int i = 0; i < sc_id % 4; i++) {
            surchargesEntities.add(new SurchargesEntity(i));
        }
        return surchargesEntities;
    }

    private List<BoxBean> createBoxes(int id) {
        List<BoxBean> boxes = new ArrayList<>();
        for (int i = 0; i < id % 5; i++) {
            boxes.add(new BoxBean("gp"+i%3,i%4));
        }
        return boxes;
    }


    public int getTt() {
        return tt;
    }

    public void setTt(int tt) {
        this.tt = tt;
    }

    public String getDestPort() {
        return destPort;
    }

    public void setDestPort(String destPort) {
        this.destPort = destPort;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getDestId() {
        return destId;
    }

    public void setDestId(int destId) {
        this.destId = destId;
    }

    private Float getPrice(Float price){
        if(price==null){
            return null;
        }
        return Float.valueOf(price)-(BaseApp.loginStatus==BaseApp.LOGINOK ?50:0);
    }

    public List<BoxBean> getBoxBeans() {
        return boxBeans;
    }

    public void setBoxBeans(List<BoxBean> boxBeans) {
        this.boxBeans = boxBeans;
    }

    public String getSc_name() {
        return sc_name;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCls() {
        return cls;
    }

    public void setCls(long cls) {
        this.cls = cls;
    }

    public long getEtd() {
        return etd;
    }

    public void setEtd(long etd) {
        this.etd = etd;
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

    public String getRemarks() {
        return remarks;
    }

    public int getVia() {
        return via;
    }

    public int getSvalid() {
        return svalid;
    }

    public int getEvalid() {
        return evalid;
    }

    public List<SurchargesEntity> getSurcharges() {
        return surcharges;
    }

    public int getSc_id() {
        return sc_id;
    }
}
