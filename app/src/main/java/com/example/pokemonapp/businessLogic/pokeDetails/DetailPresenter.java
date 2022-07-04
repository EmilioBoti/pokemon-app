package com.example.pokemonapp.businessLogic.pokeDetails;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pokemonapp.models.Move;
import com.example.pokemonapp.models.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailPresenter implements IDetail.Presenter {
    private IDetail.ViewPresenter viewPresenter;
    private IDetail.ModelPresenter modelPresenter;
    private Pokemon pokemon;
    private ArrayList<Pokemon> listPokemon;

    public DetailPresenter(IDetail.ViewPresenter viewPresenter, IDetail.ModelPresenter modelPresenter) {
        this.viewPresenter = viewPresenter;
        this.modelPresenter = modelPresenter;
        this.listPokemon = new ArrayList<>();
        this.pokemon = new Pokemon();
    }

    @Override
    public void callPoke(String id) {
        Call response = modelPresenter.getPokemon(id);
        response.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();
                    ArrayList<Move> moves = new ArrayList<>();

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
                        String species = json.getJSONObject("species").getString("name");
                        JSONArray movesArr = json.optJSONArray("moves");
                        JSONArray stats = json.getJSONArray("stats");

                        pokemon.setId(Integer.parseInt(id));
                        pokemon.setName(name);
                        pokemon.setTypes(setElements(types, "type"));
                        pokemon.setAbilities(setElements(abilities, "ability"));
                        pokemon.setSpriteBack(sprite.getString("back_default"));
                        pokemon.setSpriteFront(officialArtwork.getString("front_default"));
                        pokemon.setWeight(weight);
                        pokemon.setBaseExperience(baseExp);
                        pokemon.setHeight(Double.parseDouble(json.getString("height")));
                        pokemon.setMoves(moves);
                        pokemon.setStats(getStats(stats));

                        DetailPresenter.this.setDesData(species);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    private ArrayList<HashMap<String, Integer>> getStats(JSONArray statsArr) {
        JSONArray stats = statsArr;
        ArrayList listStats = new ArrayList<HashMap<String, Integer>>();

        try {
            if (stats != null) {
                for (int i = 0; i < stats.length(); i++) {
                    JSONObject obj = (JSONObject) stats.get(i);
                    int num = obj.getInt("base_stat");
                    JSONObject stat = obj.getJSONObject("stat");
                    HashMap map = new HashMap<String, Integer>();
                    listStats.add(map.put(stat.getString("name"), num));
                }
            }
        } catch (JSONException err) { err.printStackTrace(); }
        return listStats;
    }
    @Override
    public void pokeEvolutions(String id) {

    }

    @Override
    public void clickPoke(int id) {
        viewPresenter.onClickItem(id);
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

    private void setDesData(String species) {
        listPokemon = new ArrayList<>();
        Call response = modelPresenter.getUrlEvolution(species);
        response.enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();

                try {
                    JSONObject obj = new JSONObject(json);

                    String habitat = isNull(obj.getString("habitat"), obj); //valid is has Habitat know else is Unknow
                    JSONObject chain = obj.getJSONObject("evolution_chain");
                    JSONArray varieties = obj.getJSONArray("varieties");
                    JSONArray desObj = obj.getJSONArray("flavor_text_entries");
                    JSONObject ob = (JSONObject) desObj.get(3);
                    String des = ob.getString("flavor_text").replaceAll("\\n", " ");

                    pokemon.setDescription(des);
                    pokemon.setUrlEvolutions(chain.getString("url"));
                    pokemon.setHabitat(habitat);

                    for (int i = 1; i < varieties.length(); i++){

                        JSONObject varian = (JSONObject) varieties.get(i);
                        JSONObject pokemonVarian = varian.getJSONObject("pokemon");
                        String urlId = pokemonVarian.getString("url");
                        String name = pokemonVarian.getString("name");
                        String id = urlId.substring(34, urlId.length()-1);
                        listPokemon.add(new Pokemon(Integer.parseInt(id), name));
                    }
                    //print evolutions
                    viewPresenter.setViewData(pokemon);
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

    private String isNull(String isNull, JSONObject obj){
        String habitat = "Unknow";
        try {
            if(!isNull.equals("null") || !isNull.equals("")){
                JSONObject habi = obj.getJSONObject("habitat");
                habitat = habi.getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return habitat;
    }

    private void setPokeEvolutions() {
        Call res = modelPresenter.getEvolutions(pokemon.getUrlEvolutions());
        res.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String json = response.body().string();

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
                            JSONObject object = (JSONObject) evol.get(i);
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
                        viewPresenter.setEvolutions(listPokemon);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
