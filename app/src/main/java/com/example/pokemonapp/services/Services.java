package com.example.pokemonapp.services;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pokemonapp.models.Pokemon;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;

public class Services {
    private String urlChain;

    public static Request getDatas(String path) throws IOException, JSONException {

        Request request = new Request.Builder()
                            .url(path)
                            .build();
        return  request;
    }

    public Request getUrLEvolutions(int idName){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon-species/"+idName+"/")
                .build();

        return request;
    }

    public String testing(String path) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(path)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Request getEvolutions(String path){

        Request request = new Request.Builder()
                                .url(path)
                                .build();

        return request;
    }

}
