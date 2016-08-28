package com.taoy3.freight.bean;


import com.taoy3.freight.constant.Config;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy2 on 15-12-5.
 */
public class CostItemBean implements Cloneable,Serializable {
    private Type type = Type.BOX;
    private String feename;
    private String feeid = "fsdfg";
    private BigDecimal cost = new BigDecimal(0);
    private List<BoxBean> prices = new ArrayList<>();
    private String paycurr = Config.CNY;
    private Boolean isSelect = false;
    public void setCost() {
        cost =new BigDecimal(0);
        for (int i = 0; i < prices.size(); i++) {
            cost =cost.add(new BigDecimal(prices.get(i).getValue()*prices.get(i).getNumber()));
        }
    }
    public CostItemBean(List<BoxBean> prices, String paycurr) {
        this.prices = prices;
        this.paycurr = paycurr;
    }
    public CostItemBean(List<BoxBean> ocean) {
        this.prices = ocean;
    }

    public enum Type{
        BOX,TICKET//0按箱记　1按票记
    }
    public CostItemBean clone() {
        CostItemBean bean = new CostItemBean();
        bean.type = this.type;
        bean.feename = this.feename;
        bean.feeid = this.feeid;
        bean.cost = this.cost;
        for (int i = 0; i < this.prices.size(); i++) {
            bean.prices.add(this.prices.get(i).copy());
        }
        bean.paycurr = this.paycurr;
        return bean;
    }
    public CostItemBean copy() {
        CostItemBean bean = new CostItemBean();
        bean.feename = this.feename;
        bean.feeid = this.feeid;
        bean.feeid = this.feeid;
        for (int i = 0; i < this.prices.size(); i++) {
            bean.prices.add(this.prices.get(i).copy());
        }
        bean.paycurr = this.paycurr;
        bean.isSelect = null;
        bean.type = null;
        bean.cost = null;
        return bean;
    }

    public CostItemBean(String feename) {
        this.feename = feename;
    }

    public CostItemBean(String name, Type type) {
        this.feename = name;
        this.type = type;
    }
    public CostItemBean() {
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + type +
                ", feename='" + feename + '\'' +
                ", feeid='" + feeid + '\'' +
                ", prices=" + prices +
                ", paycurr='" + paycurr + '\'' +
                '}';
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFeename() {
        return feename;
    }

    public void setFeename(String feename) {
        this.feename = feename;
    }

    public String getFeeid() {
        return feeid;
    }

    public void setFeeid(String feeid) {
        this.feeid = feeid;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public List<BoxBean> getPrices() {
        return prices;
    }

    public void setPrices(List<BoxBean> prices) {
        this.prices = prices;
    }

    public String getPaycurr() {
        return paycurr;
    }

    public void setPaycurr(String paycurr) {
        this.paycurr = paycurr;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
