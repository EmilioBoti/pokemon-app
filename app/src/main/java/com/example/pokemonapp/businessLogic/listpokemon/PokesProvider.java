package com.example.pokemonapp.businessLogic.listpokemon;

import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PokesProvider implements IPokemon.ModelPresenter{
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public Call getAllPokemons(int count) {
        Call call = null;
        try {
            Request request = Services.getDatas(Constants.PATH+"?offset=0&limit="+count);
            call = client.newCall(request);

        }catch (IOException | JSONException err){
            err.printStackTrace();
        }
        return call;
    }
}
