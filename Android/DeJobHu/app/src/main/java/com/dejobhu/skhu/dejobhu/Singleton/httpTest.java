package com.dejobhu.skhu.dejobhu.Singleton;


import android.util.Log;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class httpTest {
    private static String URL = "http://18.222.237.32/";
    private OkHttpClient client;
    private static httpTest instance = new httpTest();
    public static httpTest getInstance(){
        return instance;
    }

    private httpTest(){this.client = new OkHttpClient();}



    public void requestWebServer(String php, Callback callback, String ... params){
        String realURL = URL + php;

//        for(int i = 0; i < params.length ; i++) {
//            if(i > 0){
//                realURL += "&";
//            }else realURL += "?";
//            realURL += params[i];
//        }

        RequestBody body=new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(realURL)
                .build();
       if(request!=null)Log.d("URL","test");
        client.newCall(request).enqueue(callback);

    }
}
