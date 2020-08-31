package com.example.dc.Utility;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Util {

    public static void sendRequsetWithOkhttp(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
