package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-12-5.
 */
public class BoxBean implements Cloneable, Serializable {
    private int id;
    private String name;
    private Float value;
    private int number =1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BoxBean copy(){
        float temp = value==null?0:value;
        return new BoxBean(name, temp, number);
    }
    public BoxBean clone(){
        return new BoxBean(name, number);
    }

    public BoxBean(String name, int number) {
        this.name = name;
        this.number = number;
    }
    public BoxBean(String name, Float value) {
        this.name = name;
        this.value = value;
    }

    public BoxBean(String name, float value, int number) {
        this.name = name;
        this.value = value;
        this.number = number;
    }

    public BoxBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
