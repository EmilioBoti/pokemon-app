package com.example.pokemonapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PokemonAllFragment extends Fragment {
    private RecyclerView listContainer;
    private ArrayList<String> list;
    private Context context;
    private ArrayList<Pokemon> listPokemon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_allpokemons, container, false);

        context = getContext();
        //get views
        getViewer(view);

        getPokeInfo();

        return  view;
    }



    private void getPokeInfo() {
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getDatas("https://pokeapi.co/api/v2/pokemon/?offset=0&limit=20");

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        String json = response.body().string();

                        PokemonAllFragment.this.getActivity().runOnUiThread(new Runnable() {
                            public Pokemon poke;

                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(json);
                                    JSONArray arrObj = object.getJSONArray("results");

                                    for (int i = 0; i <= arrObj.length(); i++) {
                                        poke  = new Pokemon();
                                        JSONObject object1 = (JSONObject)arrObj.get(i);
                                        String url = object1.getString("url");
                                        poke.setName(object1.getString("name"));

                                        SearchDataThread searchDataThread = new SearchDataThread(url, poke);
                                        Thread thread = new Thread(searchDataThread);
                                        thread.start();

                                        synchronized (poke){
                                            if (!poke.getName().isEmpty()){
                                                poke.notify();
                                                listPokemon.add(poke);
                                            }
                                        }

                                        //list.add(object1.getString("name"));
                                        //getNamePoke(url);
                                    }

                                }catch (JSONException e){

                                }
                                callAdapter(listPokemon);
                            }
                        });
                    }
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //get pokemon data
    /*private void getNamePoke(String path){
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getDatas(path);

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

                            PokemonAllFragment.this.getActivity().runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    try {
                                        Pokemon pokemon = new Pokemon();
                                        pokemon.setId(Integer.parseInt(id));
                                        pokemon.setName(name);
                                        pokemon.setTypes(setElements(types, "type"));
                                        pokemon.setAbilities(setElements(abilities, "ability"));
                                        pokemon.setSpriteBack(sprite.getString("back_default"));
                                        pokemon.setSpriteFront(sprite.getString("front_default"));
                                        pokemon.setId(Integer.parseInt(id));
                                        pokemon.setWeight(weight);
                                        pokemon.setBaseExperience(baseExp);
                                        listPokemon.add(pokemon);
                                        String ok = "ok";
                                        Log.d("pok", listPokemon.toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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
    private ArrayList<String> setElements(JSONArray array, String objName){
        ArrayList<String> listElements = new ArrayList<String>();
        try {
            for (int i = 0; i < array.length(); i++){
                JSONObject object = (JSONObject) array.get(i);
                JSONObject type = object.getJSONObject(objName);
                listElements.add(type.getString("name"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return listElements;
    }
    */
    private void getViewer(View view){
        listContainer = view.findViewById(R.id.pokemonContainer);
        list = new ArrayList<>();
        listPokemon = new ArrayList<>();
    }
    private void callAdapter(ArrayList<Pokemon> list){
        //set adapter
        PokemonsAdapter pokemonsAdapter = new PokemonsAdapter(context, list);
        listContainer.setHasFixedSize(true);
        listContainer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listContainer.setAdapter(pokemonsAdapter);
    }


}
