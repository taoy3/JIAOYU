package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-10-15.
 */
public class SurchargesEntity implements Serializable,Comparable<SurchargesEntity> {
    /**
     * gp40 : 1080
     * hq40 : 1080
     * fee_code : THC
     * gp20 : 740
     * feename : 码头费
     * fee_id : a74b83cc-8ae4-4066-a1fd-00244cb28d25
     * pay_type : ​1
     * usingType : ​2
     * paycurr : CNY
     * type : ​1
     */
    private float gp40;
    private float hq40;
    private String fee_code;
    private float gp20;
    private String name;
    private int fee_id;
    private int pay_type;
    private int usingType;
    private String curr;
    private int type;
    private int tprice;
private int id;
    public SurchargesEntity(int i) {
        this.gp40 = i%40;
        this.hq40 = i%400;
        this.fee_code = "THC"+i;
        this.gp20 = i%20;
        this.name = "码头费"+i;
        this.fee_id = i;
        this.pay_type = i%2;
        this.usingType = i%2;
        this.curr = "CNY";
        this.type = i%2;
        this.tprice = i%10;
    }

    public int getTprice() {
        return tprice;
    }

    public SurchargesEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SurchargesEntity(String name) {
        this.name = name;
    }

    public void setTprice(int tprice) {
        this.tprice = tprice;
    }

    public void setGp40(float gp40) {
        this.gp40 = gp40;
    }

    public void setHq40(float hq40) {
        this.hq40 = hq40;
    }

    public void setFee_code(String fee_code) {
        this.fee_code = fee_code;
    }

    public void setGp20(float gp20) {
        this.gp20 = gp20;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFee_id(int fee_id) {
        this.fee_id = fee_id;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public void setUsingType(int usingType) {
        this.usingType = usingType;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getGp40() {
        return gp40;
    }

    public float getHq40() {
        return hq40;
    }

    public String getFee_code() {
        return fee_code;
    }

    public float getGp20() {
        return gp20;
    }

    public String getName() {
        return name;
    }

    public int getFee_id() {
        return fee_id;
    }

    public int getPay_type() {
        return pay_type;
    }

    public int getUsingType() {
        return usingType;
    }

    public String getCurr() {
        return curr;
    }

    public int getType() {
        return type;
    }

    @Override
    public int compareTo(SurchargesEntity another) {
        if(another.getTprice()!=0){
            return -1;
        }
        return 1;
    }
}
