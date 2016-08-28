package com.taoy3.freight.bean;

/**
 * Created by Administrator on 2015/11/29.
 */
public class ConsigneesBean extends SearchBean {
    private String fax;
    private String phone;
    private String address;
    private String company = super.id;
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    @Override
    public String getId() {
        return company;
    }

    public ConsigneesBean(String fax, String phone, String address, String company,String contact) {
        this.fax = fax;
        this.phone = phone;
        this.address = address;
        this.company = company;
        this.contact = contact;
    }

    public ConsigneesBean() {
    }
}
