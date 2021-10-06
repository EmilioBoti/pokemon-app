package com.example.pokemonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pokemonapp.fragments.HomeFragment;

import java.io.IOException;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;

public class MainActivity extends AppCompatActivity {
    //public static final MediaType JSON = MediaType.get("application/json; charset=utf-8 ");
    //public OkHttpClient client = new OkHttpClient();
    public TextView jsonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up Home View
        callFragment();

        /*OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon-form/?offset=20&limit=10")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(json);
                            JSONArray collec  = obj.getJSONArray("results");
                            //jsonView.setText(collec.getJSONObject(0).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });*/

    }
    private void callFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, HomeFragment.class, null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        this.finish();
    }
}