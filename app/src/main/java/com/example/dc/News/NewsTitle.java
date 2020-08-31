package com.example.dc.News;

public class NewsTitle {
    String imageUri;
    String title;
    String url;

    public NewsTitle(String imageUri,String title,String url){
        this.imageUri = imageUri;
        this.title = title;
        this.url = url;
    }


    public String getTitle() {
        return title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getUrl() {
        return url;
    }
}
