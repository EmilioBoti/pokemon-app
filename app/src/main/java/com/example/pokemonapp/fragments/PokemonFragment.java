package com.example.pokemonapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;
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
    private TextView namePoke;
    private LinearLayout typesView, weight, evole, abilities;
    private Pokemon pokemon = new Pokemon();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);

        //get all views
        getViews(view);

        //get datas from home's input or searcher
        Bundle data = getArguments();
        String input = data.getString("data");
        getNamePoke(input);

        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                getActivity().onBackPressed();
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

                            PokemonFragment.this.getActivity().runOnUiThread(new Runnable(){
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

                                        //Set datas in viewer
                                        setViewData();

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
                JSONObject type = (JSONObject) object.getJSONObject(objName);
                listElements.add(type.getString("name"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return listElements;
    }
    private void setEvolePoke(JSONObject json) {

    }

    private void setViewData() throws JSONException {
        //load the image
        loadImage(pokemon.getSpriteFront(), pokeImage);

        TextView we = new TextView(getContext());
        we.setGravity(R.integer.material_motion_duration_long_1);
        we.setText(String.valueOf(pokemon.getWeight()));
        namePoke.setText(pokemon.getName());

        weight.addView(we);

        int dimen = 400;

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
        /*for (int i = 0; i < types.length(); i++) {
            JSONObject obj = (JSONObject)types.get(i);
            JSONObject type = obj.getJSONObject("type");

            TextView t = new TextView(getContext());
            t.setText(type.getString("name"));
            t.setGravity(R.integer.material_motion_duration_long_1);

            ImageView img = new ImageView(getContext());
            img.setLayoutParams(new LinearLayout.LayoutParams(dimen, dimen));

            TextView to = new TextView(getContext());
            //to.setGravity(100);
            to.setText("Evole to");

            //testing horizontal scroll
            //loadImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+json.getString("id")+".png", img);

            //evole.addView(img);
            //evole.addView(to);

            linearLayout.addView(t);
        }*/
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
        namePoke = view.findViewById(R.id.namePoke);
        pokeImage = view.findViewById(R.id.pokeImage);
        evole = view.findViewById(R.id.evole);
        goBack = view.findViewById(R.id.goBack);
    }

    //not in use
    private class LoadImagePoke extends AsyncTask<String, Void, Bitmap>{
        ImageView pokeImage;

        public LoadImagePoke(ImageView pokeImage){
            this.pokeImage = pokeImage;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream in = new URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            pokeImage.setImageBitmap(bitmap);
        }
    }
}
