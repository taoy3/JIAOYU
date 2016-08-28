package com.taoy3.freight.bean;

import android.os.Parcel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by taoy2 on 15-10-22.
 */
public class EnquiryBean implements Serializable {
    private int id;

    protected EnquiryBean(Parcel in) {
        id = in.readInt();
        start = in.readString();
        startid = in.readString();
        dest = in.readString();
        destid = in.readString();
        remarks = in.readString();
        applicant = in.readString();
        applicantid = in.readString();
        replier = in.readString();
        replierid = in.readString();
        response = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id++;
        this.id = id;
    }

    private Integer type;// smallint, -- 0- 海运整柜...1-散货　2－空运　3-快递
    private String start;// character varying(100), -- 开始地点
    private String startid;//uuid,
    private String dest;// character varying(100), -- 目的地点
    private String destid;// uuid,
    private String remarks;//character varying(500), -- 备注
    private String applicant;// character varying(50), -- 申请人
    private String applicantid;// uuid, -- 申请人id
    private Date applydate;//date, -- 申请日期
    private String replier;// character varying(50), -- 回复人
    private String replierid;//uuid,
    private Date replydate;// timestamp(6) with time zone, -- 回复日期
    private String response;//uuid, -- 回复内容
    private Integer responsetype;// smallint, -- 0-正常,1-临时
    private Integer state;// smallint DEFAULT 1, -- 状态  0无效,1 未回复,2已回复

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStartid() {
        return startid;
    }

    public void setStartid(String startid) {
        this.startid = startid;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getDestid() {
        return destid;
    }

    public void setDestid(String destid) {
        this.destid = destid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplicantid() {
        return applicantid;
    }

    public void setApplicantid(String applicantid) {
        this.applicantid = applicantid;
    }

    public Date getApplydate() {
        return applydate;
    }

    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }

    public String getReplierid() {
        return replierid;
    }

    public void setReplierid(String replierid) {
        this.replierid = replierid;
    }

    public Date getReplydate() {
        return replydate;
    }

    public void setReplydate(Date replydate) {
        this.replydate = replydate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getResponsetype() {
        return responsetype;
    }

    public void setResponsetype(Integer responsetype) {
        this.responsetype = responsetype;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public EnquiryBean(String start, String dest, Date applydate,Integer type,Integer state) {
        this.start = start;
        this.dest = dest;
        this.applydate = applydate;
        this.type = type;
        this.state = state;
    }

    public EnquiryBean() {
    }
}
