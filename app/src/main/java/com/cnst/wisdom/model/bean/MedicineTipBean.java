package com.cnst.wisdom.model.bean;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author taoyuan
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class MedicineTipBean implements Serializable{
    private String time;
    private String clazz;
    private String name;
    private String tip;
    private String times;
    private int state;
    private String id;

    @Override
    public String toString() {
        return "MedicineTipBean{" +
                "time='" + time + '\'' +
                ", clazz='" + clazz + '\'' +
                ", name='" + name + '\'' +
                ", tip='" + tip + '\'' +
                ", times='" + times + '\'' +
                ", state=" + state +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    public void setId(String id){
        this.id= id;
    }

    public String getId() {
        return id;
    }
}
