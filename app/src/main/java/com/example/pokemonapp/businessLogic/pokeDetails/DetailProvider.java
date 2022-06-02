package com.example.pokemonapp.businessLogic.pokeDetails;

import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;
import org.json.JSONException;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DetailProvider implements IDetail.ModelPresenter {
    private OkHttpClient client = new OkHttpClient();

    @Override
    public Call getPokemon(String id) {
        Call call = null;
        try {
            Request request = Services.getDatas(Constants.PATH+id);
            call = client.newCall(request);
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return call;
    }

    @Override
    public Call getUrlEvolution(String id) {
        Request request = Services.getUrLEvolutions(Integer.parseInt(id));
        Call call = client.newCall(request);
        return call;
    }

    @Override
    public Call getEvolutions(String path) {
        Request request = Services.getEvolutions(path);
        return client.newCall(request);
    }
}
