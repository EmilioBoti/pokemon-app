package com.example.pokemonapp.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonapp.R;
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

public class HomeFragment extends Fragment implements Callback, View.OnClickListener    {
    private ImageButton btnSearch;
    public SearchView searchView;
    public TextView pokemons, moves, types;
    private String input;
    private Pokemon pokemon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceSate){
        super.onViewCreated(view, saveInstanceSate);

        searchView = view.findViewById(R.id.search);
        btnSearch = view.findViewById(R.id.btnSearch);
        pokemons = view.findViewById(R.id.pokemons);
        moves = view.findViewById(R.id.move);

        //events click
        btnSearch.setOnClickListener(this);
        pokemons.setOnClickListener(this);
        moves.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String queryValue = searchView.getQuery().toString().replaceAll(" ", "-");
                if(!queryValue.isEmpty()){
                    input = queryValue.toLowerCase();
                    getNamePoke(input);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getNamePoke(String input){
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getDatas(Constants.PATH+input);
            //make the request
            client.newCall(request).enqueue(this);

        }catch (IOException | JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if(response.isSuccessful()){
            pokemon = new Pokemon();
            //save data of input
            Bundle dataInput = new Bundle();
            //dataInput.putSerializable("poke", pokemon);
            dataInput.putString("idPoke", String.valueOf(input));

            PokemonFragment pokemonFragment = new PokemonFragment();
            pokemonFragment.setArguments(dataInput); //send data to PokemonFragment

            //load new screen
            Helpers.callFragment(getParentFragmentManager(), pokemonFragment);
        }else{
            HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "This Pokémon might not exist.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnSearch){
            String queryValue = searchView.getQuery().toString().replaceAll(" ", "-");

            if(!queryValue.isEmpty()){
                input = queryValue.toLowerCase();
                getNamePoke(input);
            }
        }else if(v.getId() == R.id.pokemons){
            PokemonAllFragment pokemonAllFragment = new PokemonAllFragment();
            Helpers.callFragment(getParentFragmentManager(), pokemonAllFragment);

        }else if (v.getId() == R.id.move){
            Toast.makeText(getContext(), "This action is disabled for now.", Toast.LENGTH_SHORT).show();
            /*CategoryMoveFragment categoryMoveFragment = new CategoryMoveFragment();
            Helpers.callFragment(getParentFragmentManager(), categoryMoveFragment);*/
        }
    }
}