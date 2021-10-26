package com.example.pokemonapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.PokemonsAdapter;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.SearchDataThread;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.sql.PooledConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PokemonAllFragment extends Fragment {
    private RecyclerView listContainer;
    private ArrayList<String> list;
    private Context context;
    private ArrayList<Pokemon> listPokemon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_allpokemons, container, false);
        return  view;
    }
    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);

        context = getContext();

        String s = "pink";
        String color = String.format("#%X", s.hashCode());
        Log.d("color", color);

        //get views
        init(view);
        listAllPokemon();
        //setListPokemon();
    }

    private void init(View view){
        listContainer = view.findViewById(R.id.pokemonContainer);
        list = new ArrayList<>();
        listPokemon = new ArrayList<>();
    }
    private void callAdapter(ArrayList<Pokemon> list){
        //set adapter
        PokemonsAdapter pokemonsAdapter = new PokemonsAdapter(context, list);
        listContainer.setHasFixedSize(true);
        listContainer.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
        listContainer.setAdapter(pokemonsAdapter);
    }


    private void listAllPokemon(){

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = Services.getDatas(Constants.PATH);

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        String json = response.body().string();

                        PokemonAllFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(json);
                                    JSONArray results = object.getJSONArray("results");

                                    for (int i = 0; i < results.length(); i++){
                                        JSONObject js = (JSONObject) results.get(i);
                                        String name = js.getString("name");
                                        String url = js.getString("url").substring(34, js.getString("url").length()-1);
                                        Pokemon p1 = new Pokemon();
                                        p1.setId(Integer.parseInt(url));
                                        p1.setName(name);
                                        listPokemon.add(p1);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //set pokemons
                                callAdapter(listPokemon);
                            }
                        });
                    }
                }
            });
        }catch (IOException | JSONException err){

        }
    }
    /*private void setListPokemon(){
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getDatas(Constants.PATH);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        String json = response.body().string();

                        try {
                            JSONObject obj = new JSONObject(json);
                            JSONArray results = obj.getJSONArray("results");

                            PokemonAllFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    for (int i = 0; i < results.length(); i++){
                                        try {
                                            JSONObject poke = (JSONObject) results.get(i);
                                            Pokemon p1 = new Pokemon();
                                            //p1.setName(poke.getString("name"));
                                            String url = poke.getString("url");
                                            OkHttpClient client1 = new OkHttpClient();

                                            /*do not try this at home please
                                            Request request1 = Services.getDatas(url);
                                            client1.newCall(request1).enqueue(new Callback() {
                                                @Override
                                                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                                                }

                                                @Override
                                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                    if (response.isSuccessful()) {
                                                        String json1 = response.body().string();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(json1);
                                                            String n = jsonObject.getString("name");
                                                            JSONObject sprite = jsonObject.getJSONObject("sprites");                            JSONObject other = sprite.getJSONObject("other");
                                                            JSONObject other1 = sprite.getJSONObject("other");
                                                            JSONObject officialArtwork = other1.getJSONObject("official-artwork");

                                                            p1.setId(Integer.parseInt(jsonObject.getString("id")));
                                                            p1.setName(n);
                                                            p1.setSpriteFront(officialArtwork.getString("front_default"));
                                                            listPokemon.add(p1);
                                                            Log.d("name", n);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });
                                            listPokemon.add(p1);

                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    //calling adapter
                                    callAdapter(listPokemon);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
