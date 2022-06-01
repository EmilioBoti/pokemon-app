package com.example.pokemonapp.businessLogic.home;

import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HomeProvider implements IHome.ModelPresenter{
    OkHttpClient client = new OkHttpClient();

    public HomeProvider() {}

    @Override
    public Call getpokemon(String input) {
        Call call = null;
        try {
            Request request = Services.getDatas(Constants.PATH+input);
            call = client.newCall(request);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return call;
    }
}
