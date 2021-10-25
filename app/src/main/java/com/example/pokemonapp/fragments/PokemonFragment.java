package com.example.pokemonapp.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.EvolutionsAdapter;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PokemonFragment extends Fragment {
    private ImageView pokeImage;
    private ImageButton goBack;
    private TextView namePoke, pokeDescription;
    private LinearLayout typesView, weight, baseExp, abilities;
    private RecyclerView evolutions;
    private Pokemon pokemon;
    private Services services;
    private ArrayList<Pokemon> listPokemon;
    private ArrayList<Integer> listId;
    private ArrayList<String> listNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get all views
        getViews(view);

        //get datas from home's input(searcher)
        Bundle data = getArguments();
        pokemon = (Pokemon) data.getSerializable("pokemonData");

        //load pokemon's datas
        setViewData();

        //get Evolution's url and set poke's description
        setDesData();

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
                                callAdapter(getContext(), listPokemon);
                                //int dimen = 450;
                                /*int padding = 35;

                                for (int i = 0; i < listPokemon.size(); i++){
                                    Context context = getContext();

                                    LinearLayout imgContainer = new LinearLayout(getContext());
                                    imgContainer.setBackgroundColor(getResources().getColor(R.color.white, getResources().newTheme()));
                                    imgContainer.setOrientation(LinearLayout.VERTICAL);
                                    imgContainer.setPadding(padding, padding, padding, padding);

                                    ImageView imgP = new ImageView(context);

                                    TextView nameP = new TextView(getContext());
                                    nameP.setText(listPokemon.get(i).getName());
                                    nameP.setTextSize(20f);
                                    nameP.setTextColor(getResources().getColor(R.color.black));
                                    nameP.setGravity(Gravity.CENTER_HORIZONTAL);

                                    imgContainer.addView(imgP);
                                    imgContainer.addView(nameP);

                                    loadImage(Constants.URL_IMG +listPokemon.get(i).getId()+".png", imgP);
                                    evolutions.addView(imgContainer);
                                }*/


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
    private void callFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, fragment, null);
        fragmentTransaction.commit();
    }
    private void setDesData() {

        //Services services = new Services();
        OkHttpClient client = new OkHttpClient();

        Request request = services.getUrLEvolutions(pokemon.getId());

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();

                try {
                    JSONObject obj = new JSONObject(json);
                    JSONObject c = obj.getJSONObject("evolution_chain");
                    JSONArray varieties = obj.getJSONArray("varieties");

                    JSONArray desObj = obj.getJSONArray("flavor_text_entries");

                    JSONObject ob = (JSONObject) desObj.get(1);
                    pokemon.setDescription(ob.getString("flavor_text"));
                    pokemon.setUrlEvolutions(c.getString("url"));


                    PokemonFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pokeDescription.setText(pokemon.getDescription());
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
        //load the image
        loadImage(pokemon.getSpriteFront(), pokeImage);

        TextView weightP = new TextView(getContext());
        weightP.setGravity(Gravity.CENTER_HORIZONTAL);
        weightP.setText(String.valueOf(pokemon.getWeight()));

        TextView baseExperiance = new TextView(getContext());
        baseExperiance.setGravity(Gravity.CENTER_HORIZONTAL);
        baseExperiance.setText(String.valueOf(pokemon.getBaseExperience()));
        namePoke.setText(pokemon.getName());

        weight.addView(weightP);
        baseExp.addView(baseExperiance);

        for (String type: pokemon.getTypes()) {
            TextView t = new TextView(getContext());
            t.setText(type);
            t.setGravity(Gravity.CENTER_HORIZONTAL);
            typesView.addView(t);
        }
        for (String elem: pokemon.getAbilities()) {
            TextView ability = new TextView(getContext());
            ability.setText(elem);
            ability.setGravity(Gravity.CENTER_HORIZONTAL);
            abilities.addView(ability);
        }
    }

    //load pokemon's image
    private void loadImage(String url, ImageView pokeImage){
        Picasso.get()
                .load(url)
                //.placeholder(R.mipmap.ic_launcher_defaultimg_foreground)
                .into(pokeImage);
    }
    private void addId(int id){
        this.listId.add(id);
    }
    private void addName(String name){
        if(!name.isEmpty()){
            this.listNames.add(name);
        }
    }

    public void getNamePoke(String input){
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

                                        //save data of input
                                        Bundle dataInput = new Bundle();
                                        dataInput.putSerializable("pokemonData", pokemon);

                                        PokemonFragment pokemonFragment = new PokemonFragment();
                                        pokemonFragment.setArguments(dataInput); //send data to PokemonFragment

                                        //load new screen
                                        callFragment(pokemonFragment);

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
    private void getViews(View view){
        typesView = view.findViewById(R.id.typePokemon);
        abilities = view.findViewById(R.id.abilitiesPoke);
        weight = view.findViewById(R.id.weight);
        baseExp = view.findViewById(R.id.expPoke);
        namePoke = view.findViewById(R.id.namePoke);
        pokeImage = view.findViewById(R.id.pokeImage);
        evolutions = view.findViewById(R.id.evolutions);
        goBack = view.findViewById(R.id.goBack);
        pokeDescription = view.findViewById(R.id.pokeDescription);

        services = new Services();
        listId = new ArrayList<>();
        listNames = new ArrayList<>();
        listPokemon = new ArrayList<>();
    }
}
