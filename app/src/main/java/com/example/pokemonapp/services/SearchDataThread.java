package com.example.pokemonapp.services;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pokemonapp.fragments.HomeFragment;
import com.example.pokemonapp.fragments.PokemonFragment;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchDataThread implements Runnable{
    private String path;
    private Pokemon pokemon;


    public SearchDataThread(String path, Pokemon pokemon){
        this.path = path;
        this.pokemon = pokemon;
    }

    @Override
    public void run(){
        //getNamePoke(path);
        synchronized(pokemon){
            if(!pokemon.getName().isEmpty()){
                //getNamePoke(this.path);
            }
        }
    }


    private void getNamePoke(String input){
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getDatas(Constants.PATH+input);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        String jsonString = response.body().string();

                        try {
                            JSONObject json = new JSONObject(jsonString);
                            String id = json.getString("id");
                            String name = json.getString("name");
                            JSONObject sprite = json.getJSONObject("sprites");
                            JSONArray types = json.getJSONArray("types");
                            JSONArray abilities = json.getJSONArray("abilities");
                            double weight = Double.parseDouble(json.getString("weight"));
                            double baseExp = Double.parseDouble(json.getString("base_experience"));
                            Log.d("t", "executing!!");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }catch (IOException | JSONException e){
            e.printStackTrace();
        }

    }
}
