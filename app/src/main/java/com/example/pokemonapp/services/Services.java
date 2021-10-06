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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;

public class Services {

    public Request getPokemon(String idName) throws IOException, JSONException {

        Request request = new Request.Builder()
                            .url("https://pokeapi.co/api/v2/pokemon-form/"+idName+"/")
                            .build();
        return  request;
    }

}
