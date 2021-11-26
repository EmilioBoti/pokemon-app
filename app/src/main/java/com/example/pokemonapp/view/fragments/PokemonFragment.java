package com.example.pokemonapp.view.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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


public class PokemonFragment extends Fragment implements Callback, Runnable{
    private ImageView pokeImage;
    private ImageButton goBack;
    private TextView namePoke, pokeDescription, titleBar;
    private LinearLayout typesView, weight,height, baseExp, abilities, habitatLayout;
    private RecyclerView evolutions;
    private Pokemon pokemon;
    private Services services;
    private ArrayList<Pokemon> listPokemon;
    private ProgressBar progressBar;
    private ScrollView scrollViewContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_pokemon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get all views
        init(view);
        titleBar.setText(R.string.pokeInfo);

        //get datas from home's input(searcher)
        Bundle data = getArguments();
        getNamePoke(data.get("idPoke").toString());

        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                getActivity().onBackPressed();
            }
        });

    }
    private void init(View view){
        typesView = view.findViewById(R.id.typePokemon);
        abilities = view.findViewById(R.id.abilitiesPoke);
        weight = view.findViewById(R.id.weight);
        height = view.findViewById(R.id.height);
        baseExp = view.findViewById(R.id.expPoke);
        namePoke = view.findViewById(R.id.namePoke);
        pokeImage = view.findViewById(R.id.pokeImage);
        evolutions = view.findViewById(R.id.evolutions);
        goBack = view.findViewById(R.id.goBack);
        titleBar = view.findViewById(R.id.titleBar);
        pokeDescription = view.findViewById(R.id.pokeDescription);
        habitatLayout = view.findViewById(R.id.habitat);
        progressBar = view.findViewById(R.id.loader);
        scrollViewContainer = view.findViewById(R.id.scrollViewContainerr);
        services = new Services();
        listPokemon = new ArrayList<>();
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

                                for(int i = 0; i < listPokemon.size(); i++){
                                    Log.d("poke", listPokemon.get(i).getName());
                                }

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

                    String habitat = obj.getString("habitat");
                    JSONObject chain = obj.getJSONObject("evolution_chain");

                    JSONArray varieties = obj.getJSONArray("varieties");

                    JSONArray desObj = obj.getJSONArray("flavor_text_entries");

                    JSONObject ob = (JSONObject) desObj.get(1);
                    String des = ob.getString("flavor_text");
                    pokemon.setDescription(des);
                    pokemon.setUrlEvolutions(chain.getString("url"));
                    pokemon.setColor(color);
                    pokemon.setHabitat(habitat);

                    for (int i = 1; i < varieties.length(); i++){

                        JSONObject varian = (JSONObject)varieties.get(i);
                        JSONObject pokemonVarian = varian.getJSONObject("pokemon");
                        String urlId = pokemonVarian.getString("url");
                        String name = pokemonVarian.getString("name");
                        String id = urlId.substring(34, urlId.length()-1);
                        listPokemon.add(new Pokemon(Integer.parseInt(id), name));
                    }

                    PokemonFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pokeDescription.setText(pokemon.getDescription());
                            habitatLayout.removeAllViewsInLayout();
                            TextView habitatText = new TextView(getContext());
                            habitatText.setGravity(Gravity.CENTER_HORIZONTAL);
                            habitatText.setTextSize(16f);
                            habitatText.setText(pokemon.getHabitat());
                            habitatLayout.addView(habitatText);
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

    private void setViewData(){

        progressBar.setVisibility(getView().GONE);
        scrollViewContainer.setVisibility(getView().VISIBLE);

        final float DIMEN = 16f;
        TextView weightP, heightP,habitatText, baseExperiance, type, ability;
        pokeImage.setTranslationX(-1000);
        //load the image
        Helpers.loadImage(pokemon.getSpriteFront(), pokeImage);

        //animation
        ObjectAnimator animatorImg = ObjectAnimator.ofFloat(pokeImage, "translationX", 0f);
        animatorImg.setDuration(700);
        animatorImg.start();

        weight.removeAllViewsInLayout();
        baseExp.removeAllViewsInLayout();
        typesView.removeAllViewsInLayout();
        abilities.removeAllViewsInLayout();
        habitatLayout.removeAllViewsInLayout();
        height.removeAllViewsInLayout();

        double num = (pokemon.getWeight() / 4.54);
        String pound = String.format("%.02f", num);

        weightP = new TextView(getContext());
        weightP.setGravity(Gravity.CENTER_HORIZONTAL);
        weightP.setTextSize(DIMEN);
        weightP.setText(pound+" pounds");

        heightP = new TextView(getContext());
        heightP.setGravity(Gravity.CENTER_HORIZONTAL);
        heightP.setTextSize(DIMEN);
        heightP.setText((pokemon.getHeight() * 10) + " cm");

        habitatText = new TextView(getContext());
        habitatText.setGravity(Gravity.CENTER_HORIZONTAL);
        habitatText.setTextSize(DIMEN);
        habitatText.setText(pokemon.getHabitat());

        baseExperiance = new TextView(getContext());
        baseExperiance.setGravity(Gravity.CENTER_HORIZONTAL);
        baseExperiance.setTextSize(DIMEN);
        baseExperiance.setText(String.valueOf(pokemon.getBaseExperience()));
        String n = pokemon.getName();

        namePoke.setText(Helpers.ToUpperName(n));

        habitatLayout.addView(habitatText);
        weight.addView(weightP);
        height.addView(heightP);
        baseExp.addView(baseExperiance);

        for (String types: pokemon.getTypes()) {
            type = new TextView(getContext());
            type.setText(types);
            type.setTextSize(DIMEN);
            type.setGravity(Gravity.CENTER_HORIZONTAL);
            typesView.addView(type);
        }
        for (String elem: pokemon.getAbilities()) {
            ability = new TextView(getContext());
            ability.setText(elem);
            ability.setTextSize(DIMEN);
            ability.setGravity(Gravity.CENTER_HORIZONTAL);
            abilities.addView(ability);
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

    public void getNamePoke(String input){
        OkHttpClient client = new OkHttpClient();
        listPokemon = new ArrayList<>();
        pokemon = new Pokemon();

        try {
            Request request = Services.getDatas(Constants.PATH+input);
            client.newCall(request).enqueue(this);

        }catch (IOException | JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

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

                pokemon.setId(Integer.parseInt(id));
                pokemon.setName(name);
                pokemon.setTypes(setElements(types, "type"));
                pokemon.setAbilities(setElements(abilities, "ability"));
                pokemon.setSpriteBack(sprite.getString("back_default"));
                pokemon.setSpriteFront(officialArtwork.getString("front_default"));
                pokemon.setWeight(weight);
                pokemon.setBaseExperience(baseExp);
                pokemon.setHeight(Double.parseDouble(json.getString("height")));

                PokemonFragment.this.getActivity().runOnUiThread(this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        setViewData();
        setDesData();
    }
}
