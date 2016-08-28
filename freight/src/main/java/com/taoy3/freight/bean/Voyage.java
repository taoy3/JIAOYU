package com.taoy3.freight.bean;

import com.taoy3.freight.netBean.GeoUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy2 on 15-9-22.
 */
public class Voyage implements Serializable {
    /**
     * sc_name : 阿拉伯联合国家轮船公司
     * schema : 0841W
     * vessle_name : THALASSA NIKI
     * service_name : AEC3
     * service_id : f03c2166-2be3-434b-96ba-18b21b90aa9b
     * sc_id : b27be8a6-78a2-4d6c-915d-8fb2a7b349fd
     * id : 3a2c5b9d-c9b2-4b1c-905c-2fe2b59b7371
     * state : 1
     * ports : [{"port_name":"ROTTERDAM","tt":24,"no":2,"eta":"2015-09-24","port_id":"31b457f5-7a87-4a36-abce-ff408c2d3314","via":0},{"port_name":"FELIXSTOWE","tt":27,"no":3,"eta":"2015-09-27","port_id":"db31f011-47ee-4da3-b70a-940e7e65e886","via":0},{"port_name":"HAMBURG","tt":30,"no":4,"eta":"2015-09-30","port_id":"e37f27e6-b5e8-45c8-a36b-0c34dbbdc374","via":0}]
     * via : 0
     */
    private String sc_name;
    private String schema;//航次
    private String vessle_name;
    private String service_name;
    private String service_id;
    private int sc_id;
    private int start_id;
    private int dest_id;
    private int id;
    private int state;
    private List<PortsInfo> ports;
    private int via;

    public Voyage(Port startPort, Port destPort, Company company,int i) {
        this.sc_name = company.getName_zh();
        this.schema=company.getCode()+i;
        this.vessle_name="AEC3"+i;
        this.service_name="0841W"+i;
        this.service_id=startPort.getCode()+i;
        this.sc_id=company.getId();
        this.state=i%2;
        this.via=i%2;
        this.start_id = startPort.getId();
        this.dest_id = destPort.getId();
        this.ports=createPorts(startPort,destPort,i);
    }

    public Voyage() {
    }

    private List<PortsInfo> createPorts(Port startPort, Port destPort, int i) {
        List<PortsInfo> list = new ArrayList<>();
        for (int j = 0; j < i%5+2; j++) {
            list.add(new PortsInfo(i,id));
        }
        list.get(0).setPort_id(startPort.getId());
        list.get(0).setPort_name(startPort.getName_zh());
        list.get(list.size()-1).setPort_id(destPort.getId());
        list.get(list.size()-1).setPort_name(destPort.getName_zh());
        return list;
    }

    public int getStart_id() {
        return start_id;
    }

    public void setStart_id(int start_id) {
        this.start_id = start_id;
    }

    public int getDest_id() {
        return dest_id;
    }

    public void setDest_id(int dest_id) {
        this.dest_id = dest_id;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setVessle_name(String vessle_name) {
        this.vessle_name = vessle_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public void setSc_id(int sc_id) {
        this.sc_id = sc_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setPorts(List<PortsInfo> ports) {
        this.ports = ports;
    }

    public void setVia(int via) {
        this.via = via;
    }

    public String getSc_name() {
        return sc_name;
    }

    public String getSchema() {
        return schema;
    }

    public String getVessle_name() {
        return vessle_name;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_id() {
        return service_id;
    }

    public int getSc_id() {
        return sc_id;
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public List<PortsInfo> getPorts() {
        return ports;
    }

    public int getVia() {
        return via;
    }
    @Override
    public String toString() {
        return new GeoUtil().serializer(this);
    }
}
