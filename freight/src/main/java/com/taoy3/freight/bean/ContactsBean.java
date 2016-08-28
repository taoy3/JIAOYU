package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-12-13.
 */
public class ContactsBean  implements Serializable {
    private AddressBean trailer = new AddressBean();
    private AddressBean shipper = new AddressBean();
    private AddressBean consignee = new AddressBean();
    private AddressBean notify = new AddressBean();

    public ContactsBean() {
    }
    public ContactsBean copy() {
        return new ContactsBean(trailer.copy(),shipper.copy(), consignee.copy(),notify.copy());
    }

    public ContactsBean(AddressBean trailer, AddressBean shipper, AddressBean consignee, AddressBean notify) {
        this.trailer = trailer;
        this.shipper = shipper;
        this.consignee = consignee;
        this.notify = notify;
    }

    public AddressBean getTrailer() {
        return trailer;
    }

    public void setTrailer(AddressBean trailer) {
        this.trailer = trailer;
    }

    public AddressBean getShipper() {
        return shipper;
    }

    public void setShipper(AddressBean shipper) {
        this.shipper = shipper;
    }

    public AddressBean getConsignee() {
        return consignee;
    }

    public void setConsignee(AddressBean consignee) {
        this.consignee = consignee;
    }

    public AddressBean getNotify() {
        return notify;
    }

    public void setNotify(AddressBean notify) {
        this.notify = notify;
    }
}
