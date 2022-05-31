package com.example.pokemonapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.PokemonsAdapter;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PokemonAllFragment extends Fragment implements Callback, Runnable, PokemonsAdapter.OnClickPokeListener {
    private RecyclerView listContainer;
    private Context context;
    private ArrayList<Pokemon> listPokemon;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_allpokemons, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);

        context = getContext();

        //get views
        init(view);
        listAllPokemon(1118);
    }

    private void init(View view){
        listContainer = view.findViewById(R.id.pokemonContainer);
        progressBar = view.findViewById(R.id.loader);
        listPokemon = new ArrayList<>();
    }

    private void callAdapter(ArrayList<Pokemon> list) {
        //set adapter

        PokemonsAdapter pokemonsAdapter = new PokemonsAdapter(context, list);
        listContainer.setHasFixedSize(true);
        listContainer.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
        listContainer.setAdapter(pokemonsAdapter);

        //event click on pokemon
        pokemonsAdapter.OnClickPokeListener(this);
        progressBar.setVisibility(getView().GONE);
    }

    private void listAllPokemon(int n){
        int count = n;

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = Services.getDatas(Constants.PATH+"?offset=0&limit="+count);
            client.newCall(request).enqueue(this);

        }catch (IOException | JSONException err){
            err.printStackTrace();
        }
    }

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
                PokemonAllFragment.this.getActivity().runOnUiThread(this);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void run() {
        callAdapter(listPokemon);
    }

    @Override
    public void onClickPoke(int pos) {
        Bundle data = new Bundle();
        data.putString("idPoke", String.valueOf(pos));

        PokemonFragment pokemonFragment = new PokemonFragment();
        pokemonFragment.setArguments(data);
        Helpers.callFragment(getParentFragmentManager(),pokemonFragment);
    }
}
