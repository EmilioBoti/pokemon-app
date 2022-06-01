package com.example.pokemonapp.services;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.constants.Constants;

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
    public static Request getDatas(String path) throws IOException, JSONException {
        return  new Request.Builder().url(path).build();
    }

    public Request getUrLEvolutions(int idName){
        return new Request.Builder().url(Constants.URL_SPECIES +idName+"/").build();
    }
    public Request getEvolutions(String path){
        return new Request.Builder().url(path).build();
    }

}
