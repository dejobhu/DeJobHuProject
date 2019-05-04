package com.dejobhu.skhu.dejobhu.Singleton;


import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GetJoson{
    private static final String URL = "http://18.222.237.32/";
    private static OkHttpClient client;

    private static GetJoson instance = new GetJoson();
    public static GetJoson getInstance(){
        return instance;
    }
    public GetJoson(){
        this.client = this.client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

    }

    public void requestWebServer(String php, Callback callback, String ... params){
        //Get방식
        String realURL = URL + php+"/";
        if(params.length != 0) {
            for (int i = 0; i < params.length; i++) {
                if(i!=0)
                    realURL +="&";
                realURL += params[i];

            }
        }
    Request request = new Request.Builder().url(realURL).build();
        if(request != null)
        client.newCall(request).enqueue(callback);
    }
    public void PageRequest(String PHP, Callback callback, String page) {
        String url = URL+PHP+"?"+page;

        Log.d("url",url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void PostRequest(String PHP, Callback callback, HashMap<String, String> map) {

        String url=URL+PHP;

        FormBody.Builder bodybuilder= new FormBody.Builder();
        for (String key:map.keySet()) {

            bodybuilder.add(key,map.get(key));
        }
        RequestBody body=bodybuilder.build();

        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }



}