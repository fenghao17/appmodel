package com.example.dc.Weather.db;


import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;

    public int getId() {
        return id;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public String getCountyName() {
        return countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setCountyId(int cityId) {
        this.cityId = cityId;
    }
}
