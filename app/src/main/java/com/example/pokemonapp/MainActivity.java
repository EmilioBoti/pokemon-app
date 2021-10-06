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