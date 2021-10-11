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

    public static Request getPokemon(String idName) throws IOException, JSONException {

        Request request = new Request.Builder()
                            .url("https://pokeapi.co/api/v2/pokemon/"+idName+"/")
                            .build();
        return  request;
    }


    public static void getEvoles(){
        ArrayList<String> listName = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon-species/6/")
                .build();

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONObject c = obj.getJSONObject("evolution_chain");
                    Log.d("json", c.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });





    }
}
