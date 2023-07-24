package com.miniproj.activity.realm;

import io.realm.RealmObject;

public class LatilngClone extends RealmObject {
    private String  Lat;
    private String lng;
    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }


}
