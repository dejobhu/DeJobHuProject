package com.dejobhu.skhu.dejobhu.Singleton;


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
        this.client = new OkHttpClient();
    }

    public void requestWebServer(String php, Callback callback, String ... params){
        String realURL = URL + php+"/";
        if(params.length != 0) {
            for (int i = 0; i < params.length; i++) {
                if(i!=0)
                    realURL +="&";
                realURL += params[i];

            }
        }

        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder().url(realURL).build();
        if(request != null)
        client.newCall(request).enqueue(callback);

    }

}