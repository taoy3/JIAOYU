package com.taoy3.freight.bean;

import com.taoy3.freight.db.CompanyDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy2 on 15-12-2.
 */
public class PriceBean {
    private int id;
    private String sc_name;
    private int sc_id;
    private String startPort;//起运港
    private int startPortId;
    private int destPortId;
    private String line ;//航线代码
    private List<PriceEntity> items;

    public int getStartPortId() {
        return startPortId;
    }

    public void setStartPortId(int startPortId) {
        this.startPortId = startPortId;
    }

    public int getDestPortId() {
        return destPortId;
    }

    public void setDestPortId(int destPortId) {
        this.destPortId = destPortId;
    }

    public PriceBean(Port start, Port dest, int i) {
        this.sc_id = i;
        this.sc_name = CompanyDB.getInstance().queryName(this.sc_id);
        this.startPort = start.getName_zh();
        this.startPortId = start.getId();
        this.destPortId = dest.getId();
        this.line = "DFG"+sc_id;
        this.items=createItem(start,dest);

    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public void setStartPort(String startPort) {
        this.startPort = startPort;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setItems(List<PriceEntity> items) {
        this.items = items;
    }

    public void setId(int id) {

        this.id = id;
    }

    public PriceBean() {

    }

    private List<PriceEntity> createItem(Port start, Port dest) {
        List<PriceEntity> priceEntities = new ArrayList<>();
        for (int j = 0; j < (start.getId() + sc_id) % 7; j++) {
            priceEntities.add(new PriceEntity(start,dest,this));
        }
        return priceEntities;
    }

    public int getId() {
        return id;
    }

    public int getSc_id() {
        return sc_id;
    }

    public void setSc_id(int sc_id) {
        this.sc_id = sc_id;
    }

    public String getStartPort() {
        return startPort;
    }
    public String getLine() {
        return line;
    }

    public String getSc_name() {
        return sc_name;
    }
    public List<PriceEntity> getItems() {
        return items;
    }
}
