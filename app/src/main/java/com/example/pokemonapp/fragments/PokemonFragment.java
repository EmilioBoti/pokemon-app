package com.example.pokemonapp.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.EvolutionsAdapter;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PokemonFragment extends Fragment {
    private ImageView pokeImage;
    private ImageButton goBack;
    private TextView namePoke, pokeDescription;
    private LinearLayout typesView, weight, baseExp, abilities, nameContainer1, habitat;
    private RecyclerView evolutions;
    private Pokemon pokemon;
    private Services services;
    private ArrayList<Pokemon> listPokemon;
    private CardView mainContainerDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get all views
        init(view);

        //get datas from home's input(searcher)
        Bundle data = getArguments();

        getNamePoke(data.getString("idPoke"));

        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                getActivity().onBackPressed();
            }
        });

    }

    private void setPokeEvolutions(){

        OkHttpClient client =  new OkHttpClient();

        Request request = services.getEvolutions(pokemon.getUrlEvolutions());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String json = response.body().string();

                    PokemonFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject obj = new JSONObject(json);
                                JSONObject  chain = obj.getJSONObject("chain");
                                JSONArray evol = chain.getJSONArray("evolves_to");

                                //base poke
                                JSONObject poke1 = chain.getJSONObject("species");
                                String pokeName1 = poke1.getString("url");
                                String name1 = poke1.getString("name");

                                String id = pokeName1.substring(42, pokeName1.length()-1);
                                listPokemon.add(new Pokemon(Integer.parseInt(id), name1));

                                for (int i = 0; i < evol.length(); i++){
                                    JSONObject object = (JSONObject)evol.get(i);
                                    JSONObject poke2 = object.getJSONObject("species");
                                    String pokeUrl2 = poke2.getString("url");
                                    String name2 = poke2.getString("name");
                                    String id2 = pokeUrl2.substring(42, pokeUrl2.length()-1);
                                    listPokemon.add(new Pokemon(Integer.parseInt(id2), name2));

                                    JSONArray evol2 = object.getJSONArray("evolves_to");

                                    for (int n = 0; n < evol2.length(); n++) {
                                        JSONObject ob = (JSONObject)evol2.get(n);
                                        JSONObject poke3 = ob.getJSONObject("species");
                                        String pokeName3 = poke3.getString("url");
                                        String id3 = pokeName3.substring(42, pokeName3.length()-1);
                                        String name3 = poke3.getString("name");
                                        listPokemon.add(new Pokemon(Integer.parseInt(id3), name3));
                                    }

                                }
                                Collections.sort(listPokemon);
                                //calling adapter evolutions
                                callAdapter(getContext(), listPokemon);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
    private void callAdapter(Context context, ArrayList<Pokemon> listPokemon){
        EvolutionsAdapter evolutionsAdapter = new EvolutionsAdapter(context, listPokemon);
        evolutions.setHasFixedSize(true);
        evolutions.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        evolutions.setAdapter(evolutionsAdapter);

        evolutionsAdapter.OnClickItemListener(new EvolutionsAdapter.OnClickItemListener() {
            @Override
            public void onclickItem(int post) {
                getNamePoke(String.valueOf(post));
            }

            @Override
            public void onclickItem(String name) {

            }
        });
    }

    private void setDesData() {

        OkHttpClient client = new OkHttpClient();

        Request request = services.getUrLEvolutions(pokemon.getId());

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();

                try {
                    JSONObject obj = new JSONObject(json);

                    JSONObject colorObj = obj.getJSONObject("color");
                    String color = colorObj.getString("name");

                    JSONObject habitatObj = obj.getJSONObject("habitat");
                    String habitat = colorObj.getString("name");

                    JSONObject chain = obj.getJSONObject("evolution_chain");
                    JSONArray varieties = obj.getJSONArray("varieties");

                    JSONArray desObj = obj.getJSONArray("flavor_text_entries");

                    JSONObject ob = (JSONObject) desObj.get(0);
                    String des = ob.getString("flavor_text");
                    pokemon.setDescription(des);
                    pokemon.setUrlEvolutions(chain.getString("url"));
                    pokemon.setColor(color);

                    PokemonFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pokeDescription.setText(pokemon.getDescription());
                            //Log.d("format", color);
                            setColor(pokemon.getColor());
                            pokemon.setHabitat(habitat);
                            try {
                                for (int i = 1; i < varieties.length(); i++){

                                    JSONObject varian = (JSONObject)varieties.get(i);
                                    JSONObject pokemonVarian = varian.getJSONObject("pokemon");
                                    String urlId = pokemonVarian.getString("url");
                                    String name = pokemonVarian.getString("name");
                                    String id = urlId.substring(34, urlId.length()-1);
                                    listPokemon.add(new Pokemon(Integer.parseInt(id), name));
                                }

                            }catch (JSONException e ){
                                e.printStackTrace();
                            }
                        }
                    });
                    //print evolutions
                    setPokeEvolutions();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }
    private void setColor(String color){

        switch (color){
            case "red":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                break;
            case "blue":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.blue_300));
                break;
            case "brown":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.brown_400));
                break;
            case "gray":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.gray_300));
                break;
            case "green":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.green_300));
                break;
            case "purple":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.purple_700));
                break;
            case "pink":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.pink_400));
                break;
            case "yellow":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.yellow_300));
                break;
            case "white":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.white));
                break;
            case "black":
                mainContainerDescription.setCardBackgroundColor(getResources().getColor(R.color.black));
                break;

        }
    }
    private void setViewData(){

        final float DIMEN = 16f;
        TextView weightP, habitatText, baseExperiance, type, ability;

        //load the image
        Helpers.loadImage(pokemon.getSpriteFront(), pokeImage);

        weight.removeAllViewsInLayout();
        baseExp.removeAllViewsInLayout();
        typesView.removeAllViewsInLayout();
        abilities.removeAllViewsInLayout();
        //habitat.removeAllViewsInLayout();

        double num = (pokemon.getWeight() / 4.54);
        String pound = String.format("%.02f", num);


        weightP = new TextView(getContext());
        weightP.setGravity(Gravity.CENTER_HORIZONTAL);
        weightP.setTextSize(DIMEN);
        //weightP.setTextColor(getResources().getColor(R.color.white, getActivity().getTheme()));
        weightP.setText(pound+" pounds");

        habitatText = new TextView(getContext());
        habitatText.setGravity(Gravity.CENTER_HORIZONTAL);
        habitatText.setTextSize(DIMEN);
        //habitatText.setTextColor(getResources().getColor(R.color.white, getActivity().getTheme()));
        habitatText.setText(String.valueOf(pokemon.getHabitat()));

        baseExperiance = new TextView(getContext());
        baseExperiance.setGravity(Gravity.CENTER_HORIZONTAL);
        baseExperiance.setTextSize(DIMEN);
        //baseExperiance.setTextColor(getResources().getColor(R.color.white, getActivity().getTheme()));
        baseExperiance.setText(String.valueOf(pokemon.getBaseExperience()));
        namePoke.setText(pokemon.getName());

        //habitat.addView(habitatText);
        weight.addView(weightP);
        baseExp.addView(baseExperiance);

        for (String types: pokemon.getTypes()) {
            type = new TextView(getContext());
            type.setText(types);
            type.setTextSize(DIMEN);
            //type.setTextColor(getResources().getColor(R.color.white));
            type.setGravity(Gravity.CENTER_HORIZONTAL);
            typesView.addView(type);
        }
        for (String elem: pokemon.getAbilities()) {
            ability = new TextView(getContext());
            ability.setText(elem);
            ability.setTextSize(DIMEN);
            //ability.setTextColor(getResources().getColor(R.color.white));
            ability.setGravity(Gravity.CENTER_HORIZONTAL);
            abilities.addView(ability);
        }
    }

    public void getNamePoke(String input){
        OkHttpClient client = new OkHttpClient();
        listPokemon = new ArrayList<>();
        pokemon = new Pokemon();

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

                        try {
                            JSONObject json = new JSONObject(jsonString);
                            String id = json.getString("id");
                            String name = json.getString("name");
                            JSONObject sprite = json.getJSONObject("sprites");
                            JSONObject other = sprite.getJSONObject("other");
                            JSONObject officialArtwork = other.getJSONObject("official-artwork");

                            JSONArray types = json.getJSONArray("types");
                            JSONArray abilities = json.getJSONArray("abilities");
                            double weight = Double.parseDouble(json.getString("weight"));
                            double baseExp = Double.parseDouble(json.getString("base_experience"));

                            PokemonFragment.this.getActivity().runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    try {
                                        pokemon.setId(Integer.parseInt(id));
                                        pokemon.setName(name);
                                        pokemon.setTypes(setElements(types, "type"));
                                        pokemon.setAbilities(setElements(abilities, "ability"));
                                        pokemon.setSpriteBack(sprite.getString("back_default"));
                                        pokemon.setSpriteFront(officialArtwork.getString("front_default"));
                                        pokemon.setWeight(weight);
                                        pokemon.setBaseExperience(baseExp);

                                        setViewData();
                                        setDesData();

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
        ArrayList<String> listElements = new ArrayList<>();
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

    private void init(View view){
        typesView = view.findViewById(R.id.typePokemon);
        abilities = view.findViewById(R.id.abilitiesPoke);
        weight = view.findViewById(R.id.weight);
        baseExp = view.findViewById(R.id.expPoke);
        namePoke = view.findViewById(R.id.namePoke);
        pokeImage = view.findViewById(R.id.pokeImage);
        evolutions = view.findViewById(R.id.evolutions);
        goBack = view.findViewById(R.id.goBack);
        pokeDescription = view.findViewById(R.id.pokeDescription);
        nameContainer1 = view.findViewById(R.id.nameContainer);
        mainContainerDescription = view.findViewById(R.id.descriptionMainContainer);
        //habitat = view.findViewById(R.id.habitat);
        services = new Services();
        listPokemon = new ArrayList<>();
    }
}
