package com.taoy3.freight.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.taoy3.freight.netBean.GeoUtil;

import java.io.Serializable;

@Table(name = "TB_ShipCompany")
public class CompanyTemp implements Serializable{
    /**
     * name_zh : æ¾³å¤§åˆ©äºšèˆªè¿
     * code : ANL
     * type_bk : 0
     * desc_sut : https://www.anl.com.au
     * tel_sutc : NIL
     * name_sutc : NIL
     * valid : 1
     * type_sut : 1
     * id : 495cf9bc-9925-459d-a125-fdec887f2330
     * desc_bk : NIL
     * tel_bkc : NIL
     * name_en : ANL CONTAINER LINE
     * name_bkc : NIL
     */
    @Column(column = "name_zh")
    private String name_zh;
    @Column(column = "code")
    private String code;
    @Column(column = "type_bk")
    private int type_bk;
    @Column(column = "desc_sut")
    private String desc_sut;
    @Column(column = "tel_sutc")
    private String tel_sutc;
    @Column(column = "name_sutc")
    private String name_sutc;
    @Column(column = "valid")
    private int valid;
    @Column(column = "type_sut")
    private int type_sut;
    @Column(column = "id")
    private int id;
    @Column(column = "desc_bk")
    private String desc_bk;
    @Column(column = "tel_bkc")
    private String tel_bkc;
    @Column(column = "name_en")
    private String name_en;
    @Column(column = "name_bkc")
    private String name_bkc;

    public void setName_zh(String name_zh) {
        this.name_zh = name_zh;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setType_bk(int type_bk) {
        this.type_bk = type_bk;
    }

    public void setDesc_sut(String desc_sut) {
        this.desc_sut = desc_sut;
    }

    public void setTel_sutc(String tel_sutc) {
        this.tel_sutc = tel_sutc;
    }

    public void setName_sutc(String name_sutc) {
        this.name_sutc = name_sutc;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public void setType_sut(int type_sut) {
        this.type_sut = type_sut;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDesc_bk(String desc_bk) {
        this.desc_bk = desc_bk;
    }

    public void setTel_bkc(String tel_bkc) {
        this.tel_bkc = tel_bkc;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public void setName_bkc(String name_bkc) {
        this.name_bkc = name_bkc;
    }

    public String getName_zh() {
        return name_zh;
    }

    public String getCode() {
        return code;
    }

    public int getType_bk() {
        return type_bk;
    }

    public String getDesc_sut() {
        return desc_sut;
    }

    public String getTel_sutc() {
        return tel_sutc;
    }

    public String getName_sutc() {
        return name_sutc;
    }

    public int getValid() {
        return valid;
    }

    public int getType_sut() {
        return type_sut;
    }

    public int getId() {
        return id;
    }

    public String getDesc_bk() {
        return desc_bk;
    }

    public String getTel_bkc() {
        return tel_bkc;
    }

    public String getName_en() {
        return name_en;
    }

    public String getName_bkc() {
        return name_bkc;
    }

    @Override
    public String toString() {
        return new GeoUtil().serializer(this);
    }
}
