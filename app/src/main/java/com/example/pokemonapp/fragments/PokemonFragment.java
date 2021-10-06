package com.example.pokemonapp.fragments;

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
import com.example.pokemonapp.services.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PokemonFragment extends Fragment {
    private ImageView imageView;
    private ImageButton goBack;
    private TextView namePoke;
    private LinearLayout linearLayout, expPoke;
    public String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);

        linearLayout = view.findViewById(R.id.typePokemon);
        expPoke = view.findViewById(R.id.expPoke);

        namePoke = view.findViewById(R.id.namePoke);
        imageView = view.findViewById(R.id.image);
        goBack = view.findViewById(R.id.goBack);

        OkHttpClient client = new OkHttpClient();

        Bundle data = getArguments();
        String input = data.getString("data");

        Services services = new Services();

        try {
            Request request = services.getPokemon(input);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        String jsonString = response.body().string();
                        try {
                            JSONObject json = new JSONObject(jsonString);
                            JSONObject obj = json.getJSONObject("pokemon");

                            PokemonFragment.this.getActivity().runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    try {
                                        namePoke.setText(obj.getString("name"));
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

        }
        //Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();

        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
