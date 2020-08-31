package com.example.dc.News;

import com.google.gson.annotations.SerializedName;

public class NewsContent {
    @SerializedName("ctime")
    String time;

    String title;

    String description;

    String picUrl;

    String url;

    public String getTitle() {
        return title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
