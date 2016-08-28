package com.cnst.wisdom.model.bean;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author meilianbing.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 "classification": "课例解析",
 "classname": "归类",
 "fileExtName": "avi",
 "filePath": "upload/kljx/2016-02-23/0ba89ada-41eb-4774-8f44-29b73313efd5_软件测试综合讲解（一）-实例操作.avi",
 "fileSize": "72822.44",
 "isVideo": true,
 "resourceId": "40288bfd530d18b101530d2a5c720004",
 "subject": "逻辑数学",
 "term": "2",
 "title": "tt",
 "videoId": "XMTQ3OTkyMTk5Mg=="
 */
public class CourseDetail implements Parcelable,Comparable<CourseDetail>{
    private String classification;
    private String classname;
    private String fileExtName;
    private String filePath;
    private String fileSize;
    private boolean isVideo;
    private String resourceId;
    private String subject;
    private String term;
    private String title;
    private String videoId;
    private String thumbnailPath;
    private String week;
    private String classnameRemark;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getClassnameRemark() {
        return classnameRemark;
    }

    public void setClassnameRemark(String classnameRemark) {
        this.classnameRemark = classnameRemark;
    }

    protected CourseDetail(Parcel in) {
        classification = in.readString();
        classname = in.readString();
        fileExtName = in.readString();
        filePath = in.readString();
        fileSize = in.readString();
        isVideo = in.readByte() != 0;
        resourceId = in.readString();
        subject = in.readString();
        term = in.readString();
        title = in.readString();
        videoId = in.readString();
        week = in.readString();
    }

    public static final Creator<CourseDetail> CREATOR = new Creator<CourseDetail>() {
        @Override
        public CourseDetail createFromParcel(Parcel in) {
            return new CourseDetail(in);
        }

        @Override
        public CourseDetail[] newArray(int size) {
            return new CourseDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classification);
        dest.writeString(classname);
        dest.writeString(fileExtName);
        dest.writeString(filePath);
        dest.writeString(fileSize);
        dest.writeByte((byte) (isVideo ? 1 : 0));
        dest.writeString(resourceId);
        dest.writeString(subject);
        dest.writeString(term);
        dest.writeString(title);
        dest.writeString(videoId);
        dest.writeString(week);
    }

    @Override
    public int compareTo(CourseDetail another) {
        try{
            String anotherIndex = another.classname.substring(0, another.classname.indexOf("."));
            String index = this.classname.substring(0, this.classname.indexOf("."));
            return Integer.parseInt(index) - Integer.parseInt(anotherIndex);
        }catch (Exception e){
            return 0;
        }
    }
}
