package com.taoy3.freight.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-9-25.
 */
@Table(name = "TB_PORT")
public class UserBean implements Serializable {
    @Column(column = "uid")
    private String uid;
    @Column(column = "username")
    private String username;
    @Column(column = "age")
    private int age;
    @Column(column = "gender")
    private int gender;
    @Column(column = "type")
    private int type;
    @Column(column = "state")
    private int state;
    @Column(column = "passWord")
    private String passWord;
    @Column(column = "id")
    private int id;
    @Column(column = "nickname")
    private String nickname;
    @Column(column = "supplier_id")
    private String supplier_id;
    @Column(column = "authorization")
    private String authorization;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public UserBean() {
    }

    public UserBean(String uid, String username, int age, String passWord) {
        this.uid = uid;
        this.username = username;
        this.age = age;
        this.passWord = passWord;
    }
}
