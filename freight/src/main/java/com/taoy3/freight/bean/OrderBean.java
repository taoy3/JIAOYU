package com.taoy3.freight.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy2 on 15-12-4.
 */
public class OrderBean implements Serializable,Cloneable,Type{
    private int state;
    private String orderno;
    private String insert_time;
    private String start_name;
    private String start_id;
    private String des_name;
    private String des_id;
    private String sc_name;
    private String sc_id;
    private String cls;
    private String etd;
    private String bgt;
    //联系人
    private ContactsBean contacts = new ContactsBean();
    //箱型
    private List<BoxBean> connum = new ArrayList<>();
    //货物信息
    private Cargoinf cargoinf = new Cargoinf();
    private String remark;
    //报关
    private int declare;//0否，１是
    private int trail;
    private int release_type;//0船东正本
    //应收
    private CostBean receivable = new CostBean();
    //应付
    private CostBean payable = new CostBean();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String tipConnum() {
        String box="";
        for (int i = 0; i < connum.size(); i++) {
            box+= connum.get(i).getName()+"："+ connum.get(i).getNumber();
            if(i< connum.size()-1){
                box+="/";
            }
        }
        return box;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getStart_name() {
        return start_name;
    }

    public void setStart_name(String start_name) {
        this.start_name = start_name;
    }

    public String getStart_id() {
        return start_id;
    }

    public void setStart_id(String start_id) {
        this.start_id = start_id;
    }

    public String getDes_name() {
        return des_name;
    }

    public void setDes_name(String des_name) {
        this.des_name = des_name;
    }

    public String getDes_id() {
        return des_id;
    }

    public void setDes_id(String des_id) {
        this.des_id = des_id;
    }

    public String getSc_name() {
        return sc_name;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public String getSc_id() {
        return sc_id;
    }

    public void setSc_id(String sc_id) {
        this.sc_id = sc_id;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getBgt() {
        return bgt;
    }

    public void setBgt(String bgt) {
        this.bgt = bgt;
    }

    public ContactsBean getContacts() {
        return contacts;
    }

    public void setContacts(ContactsBean contacts) {
        this.contacts = contacts;
    }

    public List<BoxBean> getConnum() {
        return connum;
    }

    public void setConnum(List<BoxBean> connum) {
        this.connum = connum;
    }

    public Cargoinf getCargoinf() {
        return cargoinf;
    }

    public void setCargoinf(Cargoinf cargoinf) {
        this.cargoinf = cargoinf;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDeclare() {
        return declare;
    }

    public void setDeclare(int declare) {
        this.declare = declare;
    }

    public int getTrail() {
        return trail;
    }

    public void setTrail(int trail) {
        this.trail = trail;
    }

    public int getRelease_type() {
        return release_type;
    }

    public void setRelease_type(int release_type) {
        this.release_type = release_type;
    }

    public CostBean getReceivable() {
        return receivable;
    }

    public void setReceivable(CostBean receivable) {
        this.receivable = receivable;
    }

    public CostBean getPayable() {
        return payable;
    }

    public void setPayable(CostBean payable) {
        this.payable = payable;
    }

    public OrderBean copy() {
        List<BoxBean> connums= new ArrayList<>();
        for (int i = 0; i < connum.size(); i++) {
            connums.add(connum.get(i).clone());
        }
        return  new OrderBean(start_name,start_id,des_name,des_id,sc_name, sc_id, cls, etd,bgt,
                contacts.copy(), connums, cargoinf.copy(), remark,declare,trail,release_type,
                receivable.copy(),payable.copy());
    }

    public OrderBean() {
    }

    public OrderBean(String start_name, String start_id, String des_name, String des_id,
                     String sc_name, String sc_id, String cls, String etd, String bgt,
                     ContactsBean contacts, List<BoxBean> connum, Cargoinf cargoinf, String remark,
                     int declare, int trail, int release_type, CostBean receivable, CostBean payable) {
        this.start_name = start_name;
        this.start_id = start_id;
        this.des_name = des_name;
        this.des_id = des_id;
        this.sc_name = sc_name;
        this.sc_id = sc_id;
        this.cls = cls;
        this.etd = etd;
        this.bgt = bgt;
        this.contacts = contacts;
        this.connum = connum;
        this.cargoinf = cargoinf;
        this.remark = remark;
        this.declare = declare;
        this.trail = trail;
        this.release_type = release_type;
        this.receivable = receivable;
        this.payable = payable;
    }

    @Override
    public String toString() {
        return "{" +
                "start_name='" + start_name + '\'' +
                ", start_id='" + start_id + '\'' +
                ", des_name='" + des_name + '\'' +
                ", des_id='" + des_id + '\'' +
                ", sc_name='" + sc_name + '\'' +
                ", sc_id='" + sc_id + '\'' +
                ", cls='" + cls + '\'' +
                ", etd='" + etd + '\'' +
                ", bgt='" + bgt + '\'' +
                ", contacts=" + contacts +
                ", connum=" + connum +
                ", cargoinf=" + cargoinf +
                ", remark='" + remark + '\'' +
                ", declare=" + declare +
                ", trail=" + trail +
                ", release_type=" + release_type +
                ", receivable=" + receivable +
                ", payable=" + payable +
                '}';
    }
}
