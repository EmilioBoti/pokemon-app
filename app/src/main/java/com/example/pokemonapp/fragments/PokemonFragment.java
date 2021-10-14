package com.example.pokemonapp.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.services.ThreadService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageView;


public class PokemonFragment extends Fragment {
    private ImageView pokeImage;
    private ImageButton goBack;
    private TextView namePoke, pokeDescription;
    private LinearLayout typesView, weight, baseExp, evoleContainer, abilities;
    private LinearLayout evolutions;
    private Pokemon pokemon = new Pokemon();
    private Services services;
    private ArrayList<String> listId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);

        //get all views
        getViews(view);

        //get datas from home's input or searcher
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

        return view;
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
                                listId = new ArrayList<>();
                                JSONObject obj = new JSONObject(json);
                                JSONObject  chain = obj.getJSONObject("chain");
                                JSONArray evol = chain.getJSONArray("evolves_to");

                                //base poke
                                JSONObject poke1 = chain.getJSONObject("species");
                                String pokeName1 = poke1.getString("url");

                                String id = pokeName1.substring(42, pokeName1.length()-1);
                                addId(id);

                                for (int i = 0; i < evol.length(); i++){
                                    JSONObject object = (JSONObject)evol.get(i);
                                    JSONObject poke2 = object.getJSONObject("species");
                                    String pokeName2 = poke2.getString("url");
                                    String id2 = pokeName2.substring(42, pokeName2.length()-1);
                                    addId(id2);

                                    JSONArray evol2 = object.getJSONArray("evolves_to");

                                    for (int n = 0; n < evol2.length(); n++) {
                                        JSONObject ob = (JSONObject)evol2.get(n);
                                        JSONObject poke3 = ob.getJSONObject("species");
                                        String pokeName3 = poke3.getString("url");
                                        String id3 = pokeName3.substring(42, pokeName3.length()-1);
                                        addId(id3);

                                    }

                                }
                                int dimen = 400;

                                for (int i = 0; i < listId.size(); i++){
                                    Context context = getContext();

                                    ImageView im1 = new ImageView(context);
                                    im1.setLayoutParams(new LinearLayout.LayoutParams(dimen,dimen));
                                    loadImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+listId.get(i)+".png", im1);
                                    evolutions.addView(im1);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
    private void addId(String id){
        if(!id.isEmpty()){
            this.listId.add(id);
        }
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

                    JSONArray desObj = obj.getJSONArray("flavor_text_entries");

                    JSONObject ob = (JSONObject) desObj.get(1);
                    pokemon.setDescription(ob.getString("flavor_text"));
                    pokemon.setUrlEvolutions(c.getString("url"));


                    setPokeEvolutions();

                    PokemonFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pokeDescription.setText(pokemon.getDescription());
                        }
                    });

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

        TextView we = new TextView(getContext());
        we.setGravity(R.integer.material_motion_duration_long_1);
        we.setText(String.valueOf(pokemon.getWeight()));

        TextView be = new TextView(getContext());
        be.setGravity(R.integer.material_motion_duration_long_1);
        be.setText(String.valueOf(pokemon.getBaseExperience()));
        namePoke.setText(pokemon.getName());

        weight.addView(we);
        baseExp.addView(be);

        for (String type: pokemon.getTypes()) {
            TextView t = new TextView(getContext());
            t.setText(type);
            t.setGravity(R.integer.material_motion_duration_long_1);
            typesView.addView(t);
        }
        for (String elem: pokemon.getAbilities()) {
            TextView ability = new TextView(getContext());
            ability.setText(elem);
            ability.setGravity(R.integer.material_motion_duration_long_1);
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
    }
}
