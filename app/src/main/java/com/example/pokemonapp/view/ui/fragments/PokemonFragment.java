package com.example.pokemonapp.view.ui.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.example.pokemonapp.R;
import com.example.pokemonapp.businessLogic.pokeDetails.DetailPresenter;
import com.example.pokemonapp.businessLogic.pokeDetails.DetailProvider;
import com.example.pokemonapp.businessLogic.pokeDetails.IDetail;
import com.example.pokemonapp.databinding.FragmentPokemonBinding;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;
import com.example.pokemonapp.utils.constants.Helpers;

import java.util.ArrayList;


public class PokemonFragment extends Fragment implements IDetail.ViewPresenter, OnClickItemListener, View.OnClickListener {
    private FragmentPokemonBinding binding;
    private ImageButton goBack;
    private TextView titleBar;
    private LinearLayout goBackContainer;
    private DetailProvider model;
    private DetailPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        binding = FragmentPokemonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        binding.scrollViewContainer.setVisibility(View.GONE);
        binding.loader.setVisibility(View.VISIBLE);

        titleBar.setText(R.string.pokeInfo);

        Bundle data = getArguments();
        String id = data.get("idPoke").toString();
        presenter.callPoke(id);

        //event to go back
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if (getActivity() != null) getActivity().onBackPressed();
            }
        });

    }

    private void init(View view) {
        goBack = view.findViewById(R.id.goBack);
        titleBar = view.findViewById(R.id.titleBar);
        goBackContainer = view.findViewById(R.id.goBackContainer);
        model = new DetailProvider();
        presenter = new DetailPresenter(this, model);
    }

    private void callAdapter(Context context, ArrayList<Pokemon> listPokemon) {
        if (getActivity() != null){
            PokemonFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    evolutionAdapter(listPokemon);
                }
            });
        }
    }

    private void evolutionAdapter(ArrayList<Pokemon> listPokemon) {
       binding.evoContainer.setAdapter(listPokemon);
    }

    private void updateColor(int color) {
        binding.descriptionMainContainer.setCardBackgroundColor(color);
        goBackContainer.setBackgroundColor(color);

        if (getActivity() != null) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void setViewData(Pokemon pokemon) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContent(pokemon);
                }
            });
        }
    }

    @Override
    public void setEvolutions(ArrayList<Pokemon> list) {
        callAdapter(getContext(), list);
        binding.evoContainer.setListener(this.presenter);
    }

    private void setContent(Pokemon pokemon) {
        binding.loader.setVisibility(View.GONE);
        binding.scrollViewContainer.setVisibility(View.VISIBLE);
        binding.scrollViewContainer.setSmoothScrollingEnabled(true);
        binding.scrollViewContainer.setScrollY(0);
        binding.pokeImage.setTranslationY(-1000f);
        //load the image
        Helpers.loadImage(pokemon.getSpriteFront(), binding.pokeImage);
        settingColor();
        //animation
        ObjectAnimator animatorImg = ObjectAnimator.ofFloat(binding.pokeImage, "translationY", 0f);
        animatorImg.setDuration(600);
        animatorImg.start();

        double num = (pokemon.getWeight() / 4.54);
        String pound = String.format("%.02f", num);

        String n = pokemon.getName().replaceAll("-", " ");
        binding.pokeDescription.setText(pokemon.getDescription());
        binding.namePoke.setText(Helpers.ToUpperName(n));

        binding.habitat.setViewContent(pokemon.getHabitat());
        binding.weight.setViewContent(pound+" pounds");
        binding.height.setViewContent((pokemon.getHeight() * 10) + " cm");
        binding.baseExp.setViewContent(String.valueOf(pokemon.getBaseExperience()));
        binding.abilities.setElements(pokemon.getAbilities());
        binding.type.setElements(pokemon.getTypes());

        setAbilities(pokemon.getTypes());

    }

    private void setAbilities(ArrayList<String> list) {
        binding.abilitiesContainer.removeAllViewsInLayout();
        for ( String value: list) {
            binding.abilitiesContainer.addView(createT(Helpers.ToUpperName(value)));
        }
    }

    private TextView createT(String text) {

        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        margin.rightMargin = 25;

        TextView t = new TextView(getActivity());
        t.setBackgroundResource(R.drawable.platform);
        t.setPadding(20, 20, 20,20);
        t.setText(text);
        t.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        t.setTextSize(16f);
        t.setLayoutParams(margin);
        t.setOnClickListener(this);
        return t;
    }

    private void settingColor() {
        if (binding.pokeImage.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) binding.pokeImage.getDrawable()).getBitmap();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch vibrant = palette.getLightVibrantSwatch();
                    if (vibrant != null) {
                        updateColor(vibrant.getRgb());
                    } else settingColor();
                }
            });
        }
    }

    @Override
    public void onClickItem(int post) {
        binding.scrollViewContainer.setVisibility(View.GONE);
        binding.loader.setVisibility(View.VISIBLE);
        presenter.callPoke(String.valueOf(post));
    }

    @Override
    public void onClickItem(String name) { }

    @Override
    public void onClick(View v) {
        TextView t = (TextView) v;

        if (getActivity() != null ) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,R.anim.slide_out ,R.anim.slide_in, R.anim.fade_out)
                    .addToBackStack(null)
                    .add(R.id.fragment_container_view, new HomeFragment())
                    .commit();
        }
    }
}
