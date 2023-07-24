package com.miniproj.activity.realm;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Register extends RealmObject {
    @PrimaryKey
    private long id;
    private String sid;
    private String name;
    private String email;
    private RealmList<LatilngClone> latlng;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public RealmList<LatilngClone> getLatlng() {
        return latlng;
    }

    public void setLatlng(RealmList<LatilngClone> latlng) {
        this.latlng = latlng;
    }

    public Register(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
