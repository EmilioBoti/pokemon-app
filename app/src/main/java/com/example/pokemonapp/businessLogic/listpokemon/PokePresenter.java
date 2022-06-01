package com.example.pokemonapp.businessLogic.listpokemon;

import androidx.annotation.NonNull;

import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.view.fragments.PokemonAllFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PokePresenter implements IPokemon.Presenter{
    private ArrayList<Pokemon> listPokemon;
    private IPokemon.ViewPresenter viewPresenter;
    private IPokemon.ModelPresenter modelPresenter;

    public PokePresenter(IPokemon.ViewPresenter viewPresenter, IPokemon.ModelPresenter modelPresenter) {
        this.viewPresenter = viewPresenter;
        this.modelPresenter = modelPresenter;
        this.listPokemon = new ArrayList<>();
    }

    @Override
    public void requestAllPoke(int count) {
       Call res = modelPresenter.getAllPokemons(count);
       if (res != null) {
           res.enqueue(new Callback() {
               @Override
               public void onFailure(@NonNull Call call, @NonNull IOException e) {

               }

               @Override
               public void onResponse(@NonNull Call call, @NonNull Response response) {
                   if (response.isSuccessful()) {
                       try {
                           String json = response.body().string();
                           JSONObject object = new JSONObject(json);
                           JSONArray results = object.getJSONArray("results");

                           for (int i = 0; i < results.length(); i++) {
                               JSONObject js = (JSONObject) results.get(i);
                               String name = js.getString("name");
                               String url = js.getString("url").substring(34, js.getString("url").length() - 1);
                               Pokemon p1 = new Pokemon();
                               p1.setId(Integer.parseInt(url));
                               p1.setName(name);
                               listPokemon.add(p1);
                           }
                           viewPresenter.showPokemons(listPokemon);
                           //PokemonAllFragment.this.getActivity().runOnUiThread(this);

                       } catch (JSONException | IOException e) {
                           e.printStackTrace();
                       }
                   }
               }
           });
       }
    }
}
