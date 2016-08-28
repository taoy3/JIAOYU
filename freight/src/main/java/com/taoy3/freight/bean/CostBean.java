package com.taoy3.freight.bean;


import com.taoy3.freight.constant.Config;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy2 on 15-12-13.
 */
public class CostBean  implements Serializable {
    //应收
    private List<CostItemBean> surcharge = new ArrayList<>();
    private List<BoxBean> ocean = new ArrayList<>();
    private CostItemBean oceanCost = new CostItemBean(ocean, Config.USD);
    private CostItemBean trailerCost = new CostItemBean(Config.TRAILER);
    private CostItemBean customsCost = new CostItemBean(Config.CUSTOMS);
    private CostItemBean otherCost= new CostItemBean(Config.OTHER, CostItemBean.Type.TICKET);
    private String remark;
    private int payMethod;
    //应收费用
    private List<TotalBean> total = new ArrayList<>();
    public void upDateCost() {
        for (int i = 0; i < total.size(); i++) {
            total.get(i).setValue(new BigDecimal(0));
        }
        countCost(oceanCost);
        countCost(trailerCost);
        countCost(customsCost);
        countCost(otherCost);
        for (int i = 0; i < surcharge.size(); i++) {
            countCost(surcharge.get(i));
        }
    }
    private void countCost(CostItemBean bean){
        boolean isNew = true;
        for (int i = 0; i < total.size(); i++) {
            if(total.get(i).getName()!=null&&total.get(i).getName().equals(bean.getPaycurr())){
                total.get(i).setValue(total.get(i).getValue().add(bean.getCost()));
                isNew = false;
            }
        }
        if(isNew){
            total.add(new TotalBean(bean.getPaycurr(),bean.getCost()));
        }
    }

    public CostBean(String remark, int payMethod) {
        this.remark = remark;
        this.payMethod = payMethod;
    }

    public CostBean copy(){
        CostBean bean = new CostBean(remark,payMethod);
        bean.surcharge.add(trailerCost.copy());
        bean.trailerCost=null;
        bean.surcharge.add(customsCost.copy());
        bean.customsCost =null;
        bean.surcharge.add(otherCost.copy());
        bean.otherCost = null;
        for (int i = 0; i < surcharge.size(); i++) {
            bean.surcharge.add(surcharge.get(i).copy());
        }
        for (int i = 0; i < ocean.size(); i++) {
            bean.ocean.add(oceanCost.getPrices().get(i).copy());
        }
        bean.oceanCost =null;
        for (int i = 0; i < total.size(); i++) {
            bean.total.add(total.get(i).clone());
        }
        return bean;
    }
    public CostBean() {
    }

    @Override
    public String toString() {
        return "{" +
                "surcharge=" + surcharge +
                ", oceanCost=" + oceanCost +
                ", trailerCost=" + trailerCost +
                ", customsCost=" + customsCost +
                ", otherCost=" + otherCost +
                ", remark='" + remark + '\'' +
                ", payMethod=" + payMethod +
                ", total=" + total +
                '}';
    }

    public List<CostItemBean> getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(List<CostItemBean> surcharge) {
        this.surcharge = surcharge;
    }

    public List<BoxBean> getOcean() {
        return ocean;
    }

    public void setOcean(List<BoxBean> ocean) {
        this.ocean = ocean;
    }

    public CostItemBean getOceanCost() {
        return oceanCost;
    }

    public void setOceanCost(CostItemBean oceanCost) {
        this.oceanCost = oceanCost;
    }

    public CostItemBean getTrailerCost() {
        return trailerCost;
    }

    public void setTrailerCost(CostItemBean trailerCost) {
        this.trailerCost = trailerCost;
    }

    public CostItemBean getCustomsCost() {
        return customsCost;
    }

    public void setCustomsCost(CostItemBean customsCost) {
        this.customsCost = customsCost;
    }

    public CostItemBean getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(CostItemBean otherCost) {
        this.otherCost = otherCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public List<TotalBean> getTotal() {
        return total;
    }

    public void setTotal(List<TotalBean> total) {
        this.total = total;
    }
}
