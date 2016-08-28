package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/6.
 */
public class AddressBean extends SearchBean implements Serializable {
    private String name;
    private String tel;
    private String addr;
    private String zip;
    private String fax;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AddressBean(String name, String tel, String addr, String zip, String fax) {
        this.name = name;
        this.tel = tel;
        this.addr = addr;
        this.zip = zip;
        this.fax = fax;
    }
    public AddressBean copy(){
        return new AddressBean( name,  tel,  addr,  zip,  fax);
    }

    public AddressBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
