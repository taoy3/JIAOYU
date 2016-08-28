package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-10-15.
 */
public class Price implements Serializable {
    /**
     * gp40 : 250.0
     * hq40 : 250.0
     * gp20 : 250.0
     */
    private String gp40;
    private String hq40;
    private String gp20;
    private String hor40;

    public String getHor40() {
        return hor40;
    }

    public void setHor40(String hor40) {
        this.hor40 = hor40;
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

    public String getGp40() {
        return gp40;
    }

    public String getHq40() {
        return hq40;
    }

    public String getGp20() {
        return gp20;
    }
}
