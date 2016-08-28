package com.taoy3.freight.bean;

import java.math.BigDecimal;

/**
 * Created by taoy2 on 15-12-13.
 */
public class TotalBean {
    private String name;
    private BigDecimal value = new BigDecimal(0);

    public TotalBean(String paycurr, BigDecimal value) {
        this.name = paycurr;
        this.value = value;
    }
    public TotalBean clone(){
        return new TotalBean(name,new BigDecimal(0).add(value));
    }

    public TotalBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name+":"+value;
    }
}
