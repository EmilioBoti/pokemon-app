package com.example.pokemonapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonapp.MainActivity;
import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Pokemon;
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
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private ImageButton btnSearch;
    public SearchView searchView;
    public TextView pokemons, habitat, types;
    private Pokemon pokemon = new Pokemon();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceSate){
        super.onViewCreated(view, saveInstanceSate);

        searchView = view.findViewById(R.id.search);
        btnSearch = view.findViewById(R.id.btnSearch);
        pokemons = view.findViewById(R.id.pokemons);

        pokemons.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PokemonAllFragment pokemonAllFragment = new PokemonAllFragment();
                callFragment(pokemonAllFragment);
            }

        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.getQuery().toString().isEmpty()){
                    getNamePoke(searchView.getQuery().toString().toLowerCase());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!searchView.getQuery().toString().isEmpty()){
                    getNamePoke(searchView.getQuery().toString().toLowerCase());
                }
            }
        });

    }

    private void getNamePoke(String input){
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getDatas(Constants.PATH+input);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        String jsonString = response.body().string();

                        HomeFragment.this.getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){

                                //save data of input
                                Bundle dataInput = new Bundle();
                                dataInput.putString("idPoke", input);

                                PokemonFragment pokemonFragment = new PokemonFragment();
                                pokemonFragment.setArguments(dataInput); //send data to PokemonFragment

                                //load new screen
                                callFragment(pokemonFragment);
                            }
                        });
                    }
                }
            });

        }catch (IOException | JSONException e){
            e.printStackTrace();
        }

    }

    private void callFragment(Fragment fragment){

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, fragment, null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getActivity().finish();
    }
}