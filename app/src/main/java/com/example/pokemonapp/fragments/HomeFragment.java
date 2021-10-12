package com.example.pokemonapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pokemonapp.MainActivity;
import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;

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

public class HomeFragment extends Fragment {
    private ImageButton btnSearch;
    public SearchView searchView;
    private Pokemon pokemon = new Pokemon();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.search);
        btnSearch = view.findViewById(R.id.btnSearch);


        btnSearch.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               if(!searchView.getQuery().toString().isEmpty()){
                   //callFragment(searchView.getQuery().toString().toLowerCase());
                   getNamePoke(searchView.getQuery().toString().toLowerCase());
               }
           }
        });

        return view;
    }
    private void getNamePoke(String input){
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getPokemon(input);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();

                    HomeFragment.this.getActivity().runOnUiThread(new Runnable(){
                        @Override
                       public void run(){
                            //Toast.makeText(getContext(), "The search was not successfull", Toast.LENGTH_SHORT).show();
                        }
                    });
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

                            HomeFragment.this.getActivity().runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    try {
                                        pokemon.setName(name);
                                        pokemon.setTypes(setElements(types, "type"));
                                        pokemon.setAbilities(setElements(abilities, "ability"));
                                        pokemon.setSpriteBack(sprite.getString("back_default"));
                                        pokemon.setSpriteFront(sprite.getString("front_default"));
                                        pokemon.setId(Integer.parseInt(id));
                                        pokemon.setWeight(weight);
                                        pokemon.setBaseExperience(baseExp);

                                        //load new screen
                                        callFragment(pokemon);

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

    private void callFragment(Pokemon pokemon){

        //save data of input
        Bundle dataInput = new Bundle();
        dataInput.putSerializable("pokemonData", pokemon);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PokemonFragment pokemonFragment = new PokemonFragment();
        pokemonFragment.setArguments(dataInput); //send data to PokemonFragment

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, pokemonFragment, null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getActivity().finish();
    }
}