package com.taoy3.freight.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.taoy3.freight.netBean.GeoUtil;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-9-21.
 */
/**
 * Created by taoy2 on 15-9-21.
 */
@Table(name = "TB_PORT")
public class PortTemp implements Serializable{
    /**
     * name_zh : ç‘žèŽ«ä¸¹
     * code : EGRAM
     * porttype : 0
     * countryname : EGYPT
     * id : 437508f2-d3d7-43e0-ba77-b0e94361aa6f
     * state : 1
     * countryid : 9e9eedaf-30ed-4922-a4e4-cf2fb88553ff
     * name_en : 10TH OF RAMADAN CITY
     */
    @Column(column = "name_zh")
    private String name_zh;
    @Column(column = "code")
    private String code;
    @Column(column = "porttype")
    private int porttype;
    @Column(column = "countryname")
    private String countryname;
    @Column(column = "id")
    private int id;
    @Column(column = "state")
    private int state;
    @Column(column = "countryid")
    private String countryid;
    @Column(column = "name_en")
    private String name_en;
    @Column(column = "locationtype")
    private String locationtype;

    public String getLocationtype() {
        return locationtype;
    }

    public void setLocationtype(String locationtype) {
        this.locationtype = locationtype;
    }

    public void setName_zh(String name_zh) {
        this.name_zh = name_zh;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPorttype(int porttype) {
        this.porttype = porttype;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_zh() {
        return name_zh;
    }

    public String getCode() {
        return code;
    }

    public int getPorttype() {
        return porttype;
    }

    public String getCountryname() {
        return countryname;
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public String getCountryid() {
        return countryid;
    }

    public String getName_en() {
        return name_en;
    }
    @Override
    public String toString() {
        return new GeoUtil().serializer(this);
    }
}

