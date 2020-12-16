package com.example.zapchastochki.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class SyncSomeTable<T> {
    private OkHttpClient client;
    private final TypeToken type;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public SyncSomeTable(OkHttpClient client, TypeToken type){
        this.client = client;
        this.type = type;
    }

    public ArrayList<T> update(int date, int time){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(String.format("%s/%s", Utils.serverAddress, getNameClass())).newBuilder();
        urlBuilder.addQueryParameter("date", String.valueOf(date));
        urlBuilder.addQueryParameter("time", String.valueOf(time));

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() == 200) {
                Gson gson = new Gson();
                String responseBody = response.body().string();

                ArrayList<T> list = gson.fromJson(responseBody, type.getType());
                return list;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<T> updateSratuses(int date, int time, long id){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(String.format("%s/%s/%s", Utils.serverAddress, getNameClass(), "UpdateStatuses")).newBuilder();
        urlBuilder.addQueryParameter("date", String.valueOf(date));
        urlBuilder.addQueryParameter("time", String.valueOf(time));
        urlBuilder.addQueryParameter("id", String.valueOf(id));

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() == 200) {
                Gson gson = new Gson();
                String responseBody = response.body().string();

                ArrayList<T> list = gson.fromJson(responseBody, type.getType());
                return list;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void upgrade(ArrayList<T> list){
        Gson gson = new Gson();
        String url = String.format("%s/%s", Utils.serverAddress, getNameClass());
        String bodyText = gson.toJson(list);

        RequestBody requestBody = RequestBody.create(JSON, bodyText);
        try {
            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).execute();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private String getNameClass(){
        String[] arr = type.getType().toString().split("[.]");
        return arr[arr.length - 1].toLowerCase().replace(">","");
    }
}
