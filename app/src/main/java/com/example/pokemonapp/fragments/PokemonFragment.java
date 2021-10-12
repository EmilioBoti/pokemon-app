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

import androidx.annotation.LongDef;
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
    private LinearLayout typesView, weight, baseExp, evole, abilities;
    private Pokemon pokemon = new Pokemon();


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

        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                getActivity().onBackPressed();
            }
        });

        return view;
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
