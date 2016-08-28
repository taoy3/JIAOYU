package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-12-17.
 */
public class CurrBean implements Serializable{

    /**
     * code : HKD
     * name : 港元
     * id : 29a75a23-85fd-4b14-b915-42e65fb427d9
     * type : currencyBean
     */
    private String code;
    private String name;
    private String id;
    private String type;

    public CurrBean(String code) {
        this.code = code;
    }

    public CurrBean() {
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
