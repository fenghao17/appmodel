package com.example.dc.News;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {
    @SerializedName("code")
    public int id;
    @SerializedName("msg")
    public String flag;
    @SerializedName("newslist")
    public List<NewsContent>  newsList;

}
