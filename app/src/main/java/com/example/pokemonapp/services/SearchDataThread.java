package com.example.pokemonapp.services;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pokemonapp.fragments.HomeFragment;
import com.example.pokemonapp.fragments.PokemonAllFragment;
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

    }
}
