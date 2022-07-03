package com.example.pokemonapp.view.ui.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.pokemonapp.R;
import com.example.pokemonapp.view.ui.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private GestureDetectorCompat gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set up Home View
        callFragment();

    }
    private void callFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, HomeFragment.class, null);
        fragmentTransaction.commit();
    }
}