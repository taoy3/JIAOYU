package com.taoy3.freight.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taoy2 on 15-9-22.
 */
@Table(name = "TB_PORTS")
public class PortsBean implements Serializable {

    /**
     * result : [{"sc_name":"é˜¿æ\u2039\u2030ä¼¯è\u0081\u201då\u0090ˆå\u203a½å®¶è½®èˆ¹å\u2026¬å\u008f¸","schema":"1533W","vessle_name":"SAJIR","service_name":"AEC1","service_id":"43df2265-a28a-4ea8-9cf0-89deb204514e","sc_id":"b27be8a6-78a2-4d6c-915d-8fb2a7b349fd","id":"bba5a3e4-d9ff-4447-add1-4f75e0fc79ad","state":1,"ports":[{"port_name":"YANTIAN","tt":0,"no":1,"eta":"2015-08-22","etd":"2015-08-23","csi":"2015-08-20","port_id":"cd368ae5-8e13-4317-8dab-92de449c72ba","cls":"2015-08-21","via":0},{"port_name":"FELIXSTOWE","tt":24,"no":2,"eta":"2015-09-16","port_id":"db31f011-47ee-4da3-b70a-940e7e65e886","via":0},{"port_name":"ROTTERDAM","tt":27,"no":3,"eta":"2015-09-19","port_id":"31b457f5-7a87-4a36-abce-ff408c2d3314","via":0},{"port_name":"HAMBURG","tt":29,"no":4,"eta":"2015-09-21","port_id":"e37f27e6-b5e8-45c8-a36b-0c34dbbdc374","via":0},{"port_name":"ZEEBRUGGE","tt":33,"no":5,"eta":"2015-09-25","port_id":"ae70d11b-cdda-41d1-be4e-c287ee5c361c","via":0}],"via":0},{"sc_name":"é˜¿æ\u2039\u2030ä¼¯è\u0081\u201då\u0090ˆå\u203a½å®¶è½®èˆ¹å\u2026¬å\u008f¸","schema":"1532W","vessle_name":"BARZAN","service_name":"AEC1","service_id":"43df2265-a28a-4ea8-9cf0-89deb204514e","sc_id":"b27be8a6-78a2-4d6c-915d-8fb2a7b349fd","id":"075b95c8-343a-449e-934b-cf49ddafa4fb","state":1,"ports":[{"port_name":"YANTIAN","tt":0,"no":1,"eta":"2015-08-15","etd":"2015-08-16","csi":"2015-08-13","port_id":"cd368ae5-8e13-4317-8dab-92de449c72ba","cls":"2015-08-14","via":0},{"port_name":"FELIXSTOWE","tt":24,"no":2,"eta":"2015-09-09","port_id":"db31f011-47ee-4da3-b70a-940e7e65e886","via":0},{"port_name":"ROTTERDAM","tt":27,"no":3,"eta":"2015-09-12","port_id":"31b457f5-7a87-4a36-abce-ff408c2d3314","via":0},{"port_name":"HAMBURG","tt":29,"no":4,"eta":"2015-09-14","port_id":"e37f27e6-b5e8-45c8-a36b-0c34dbbdc374","via":0},{"port_name":"ZEEBRUGGE","tt":33,"no":5,"eta":"2015-09-18","port_id":"ae70d11b-cdda-41d1-be4e-c287ee5c361c","via":0}],"via":0}]
     * totalCount : 219
     */
    @Column(column = "result")
    private List<Port> result;
    @Column(column = "totalCount")
    private int totalCount;

    public void setResult(List<Port> result) {
        this.result = result;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Port> getResult() {
        return result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public PortsBean() {
    }

    public PortsBean(List<Port> result, String firstName) {
        this.result = result;
    }
}
