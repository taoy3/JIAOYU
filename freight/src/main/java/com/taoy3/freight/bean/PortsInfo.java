package com.taoy3.freight.bean;

import com.taoy3.freight.db.PortDB;
import com.taoy3.freight.util.DateUtils;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-10-14.
 */
public class PortsInfo implements Serializable {

    /**
     * port_name : YANTIAN
     * tt : 0
     * no : 1
     * eta : 2015-08-30
     * etd : 2015-08-31
     * csi : 2015-08-28
     * port_id : cd368ae5-8e13-4317-8dab-92de449c72ba
     * cls : 2015-08-29
     * via : 0
     */
    private int voyageId;
    private int port_id;
    private String port_name;
    private int tt;
    private int no;
    private int via;

    private long csi;
    private long cls;
    private long eta;
    private long etd;

    public PortsInfo() {
    }

    public PortsInfo(int i,int voyageId) {
        this.voyageId = voyageId;
        this.port_id = i*10+1;
        this.port_name = PortDB.getInstance().queryName(port_id);
        this.tt=i%2;
        this.no=i%2;
        this.via=i%2;
        this.csi= DateUtils.after(i);
        this.cls= DateUtils.after(i+1);
        this.eta= DateUtils.after(i+2);
        this.etd= DateUtils.after(i+3);
    }


    public void setPort_name(String port_name) {
        this.port_name = port_name;
    }

    public void setTt(int tt) {
        this.tt = tt;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setEta(long eta) {
        this.eta = eta;
    }

    public void setEtd(long etd) {
        this.etd = etd;
    }

    public void setCsi(long csi) {
        this.csi = csi;
    }

    public void setPort_id(int port_id) {
        this.port_id = port_id;
    }

    public void setCls(long cls) {
        this.cls = cls;
    }

    public void setVia(int via) {
        this.via = via;
    }

    public String getPort_name() {
        return port_name;
    }

    public int getTt() {
        return tt;
    }

    public int getNo() {
        return no;
    }

    public long getEta() {
        return eta;
    }

    public long getEtd() {
        return etd;
    }

    public long getCsi() {
        return csi;
    }

    public int getPort_id() {
        return port_id;
    }

    public long getCls() {
        return cls;
    }

    public int getVia() {
        return via;
    }

    public int getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }
}
