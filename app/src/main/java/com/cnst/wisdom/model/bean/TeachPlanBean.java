package com.cnst.wisdom.model.bean;

import java.io.Serializable;

/**
 * 教学计划
 * <功能详细描述>
 *
 * @author pengjingnag.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class TeachPlanBean implements Serializable, Comparable<TeachPlanBean> {
    @Override
    public int compareTo(TeachPlanBean another) {
        return this.state - another.state;
    }

    /**
     * pk : ddsddaf
     * planid : 67ds8sd6fd6fd7fsd8sd
     * subjectname : 逻辑数学
     * classdes : 小一班
     * termname : 第一期
     * classname : 颜色配对
     * state : 0
     */
    private String weekId;
    private String pk;
    private String startdate;
    private String enddate;
    private String planid;
    private String subjectname;
    private String classdes;
    private String termname;
    private String classname;
    private String subjectId;
    private String classId;
    private String termId;
    private String detailid;
    private int state;

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getWeekId() {
        return weekId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public void setClassdes(String classdes) {
        this.classdes = classdes;
    }

    public void setTermname(String termname) {
        this.termname = termname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPk() {
        return pk;
    }

    public String getPlanid() {
        return planid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public String getClassdes() {
        return classdes;
    }

    public String getTermname() {
        return termname;
    }

    public String getClassname() {
        return classname;
    }

    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        return "TeachPlanBean{" +
                "weekId='" + weekId + '\'' +
                ", pk='" + pk + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", planid='" + planid + '\'' +
                ", subjectname='" + subjectname + '\'' +
                ", classdes='" + classdes + '\'' +
                ", termname='" + termname + '\'' +
                ", classname='" + classname + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", classId='" + classId + '\'' +
                ", termId='" + termId + '\'' +
                ", state=" + state +
                '}';
    }

    public TeachPlanBean() {
    }

    public TeachPlanBean(String beginTime, String endTime, String pk, String planid, String subjectname,
                         String classdes, String termname, String classname, int state) {
        this.startdate = beginTime;
        this.enddate = endTime;
        this.pk = pk;
        this.planid = planid;
        this.subjectname = subjectname;
        this.classdes = classdes;
        this.termname = termname;
        this.classname = classname;
        this.state = state;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }
}
