package com.taoy3.freight.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taoy2 on 15-9-22.
 */
public class VoyageBean implements Serializable {

    /**
     * result : [{"sc_name":"阿拉伯联合国家轮船公司","schema":"0841W","vessle_name":"THALASSA NIKI","service_name":"AEC3","service_id":"f03c2166-2be3-434b-96ba-18b21b90aa9b","sc_id":"b27be8a6-78a2-4d6c-915d-8fb2a7b349fd","id":"3a2c5b9d-c9b2-4b1c-905c-2fe2b59b7371","state":1,"ports":[{"port_name":"YANTIAN","tt":0,"no":1,"eta":"2015-08-30","etd":"2015-08-31","csi":"2015-08-28","port_id":"cd368ae5-8e13-4317-8dab-92de449c72ba","cls":"2015-08-29","via":0},{"port_name":"ROTTERDAM","tt":24,"no":2,"eta":"2015-09-24","port_id":"31b457f5-7a87-4a36-abce-ff408c2d3314","via":0},{"port_name":"FELIXSTOWE","tt":27,"no":3,"eta":"2015-09-27","port_id":"db31f011-47ee-4da3-b70a-940e7e65e886","via":0},{"port_name":"HAMBURG","tt":30,"no":4,"eta":"2015-09-30","port_id":"e37f27e6-b5e8-45c8-a36b-0c34dbbdc374","via":0}],"via":0},{"sc_name":"阿拉伯联合国家轮船公司","schema":"OMIT","vessle_name":"BLANK SAILING","service_name":"AEC8","service_id":"4116856f-67a4-4c5e-8da2-4e5391bc4914","sc_id":"b27be8a6-78a2-4d6c-915d-8fb2a7b349fd","id":"f8174de3-1b77-485a-a78b-377325813da0","state":1,"ports":[{"port_name":"YANTIAN","tt":0,"no":1,"eta":"2015-10-12","etd":"2015-10-13","csi":"2015-10-09","port_id":"cd368ae5-8e13-4317-8dab-92de449c72ba","cls":"2015-10-11","via":0},{"port_name":"LE HAVRE","tt":22,"no":2,"eta":"2015-11-04","port_id":"93e9f0c6-89e2-4630-8965-334773afe1c0","via":0},{"port_name":"ROTTERDAM","tt":24,"no":3,"eta":"2015-11-06","port_id":"31b457f5-7a87-4a36-abce-ff408c2d3314","via":0},{"port_name":"ANTWERP","tt":26,"no":4,"eta":"2015-11-08","port_id":"5e7b79bc-15c1-4417-a3e4-91439cd3d831","via":0},{"port_name":"HAMBURG","tt":29,"no":5,"eta":"2015-11-11","port_id":"e37f27e6-b5e8-45c8-a36b-0c34dbbdc374","via":0},{"port_name":"FELIXSTOWE","tt":32,"no":6,"eta":"2015-11-14","port_id":"db31f011-47ee-4da3-b70a-940e7e65e886","via":0}],"via":0}]
     * totalCount : 30
     */
    private List<Voyage> result;
    private int totalCount;

    public void setResult(List<Voyage> result) {
        this.result = result;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Voyage> getResult() {
        return result;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
