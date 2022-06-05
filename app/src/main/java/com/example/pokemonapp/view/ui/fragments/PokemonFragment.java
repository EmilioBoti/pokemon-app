package com.example.pokemonapp.view.ui.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.EvolutionsAdapter;
import com.example.pokemonapp.businessLogic.pokeDetails.DetailPresenter;
import com.example.pokemonapp.businessLogic.pokeDetails.DetailProvider;
import com.example.pokemonapp.businessLogic.pokeDetails.IDetail;
import com.example.pokemonapp.components.EvolutionContainer;
import com.example.pokemonapp.databinding.FragmentPokemonBinding;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;
import com.example.pokemonapp.utils.constants.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PokemonFragment extends Fragment implements IDetail.ViewPresenter, OnClickItemListener {
    private FragmentPokemonBinding binding;
    private ImageView pokeImage;
    private ImageButton goBack;
    private TextView namePoke, pokeDescription, titleBar;
    private LinearLayout typesView, weight,height, baseExp, abilities, habitatLayout, goBackContainer;
    private RecyclerView evolutions;
    private ProgressBar progressBar;
    private ScrollView scrollViewContainer;
    private CardView descriptionMainContainer;
    private DetailProvider model;
    private DetailPresenter presenter;
    private EvolutionContainer evolutionContainer;

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

        //get datas from home's input(searcher)
        Bundle data = getArguments();
        String id = data.get("idPoke").toString();
        presenter.callPoke(id);
        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
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
                    evolutionAdapter(context, listPokemon);
                }
            });
        }
    }

    private void evolutionAdapter(Context context, ArrayList<Pokemon> listPokemon) {
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

        final float DIMEN = 16f;
        binding.pokeImage.setTranslationY(-1000f);
        //load the image
        Helpers.loadImage(pokemon.getSpriteFront(), binding.pokeImage);
        settingColor();
        //animation
        ObjectAnimator animatorImg = ObjectAnimator.ofFloat(binding.pokeImage, "translationY", 0f);
        animatorImg.setDuration(600);
        animatorImg.start();

        binding.weight.removeAllViewsInLayout();
        binding.expPoke.removeAllViewsInLayout();
        binding.typePokemon.removeAllViewsInLayout();
        binding.abilitiesPoke.removeAllViewsInLayout();
        binding.habitat.removeAllViewsInLayout();
        binding.height.removeAllViewsInLayout();

        double num = (pokemon.getWeight() / 4.54);
        String pound = String.format("%.02f", num);

        String n = pokemon.getName().replaceAll("-", " ");
        binding.pokeDescription.setText(pokemon.getDescription());
        binding.namePoke.setText(Helpers.ToUpperName(n));
        binding.habitat.addView(createTextView(pokemon.getHabitat(), DIMEN));
        binding.weight.addView(createTextView(pound+" pounds", DIMEN));
        binding.height.addView(createTextView((pokemon.getHeight() * 10) + " cm", DIMEN));
        binding.expPoke.addView(createTextView(String.valueOf(pokemon.getBaseExperience()), DIMEN));

        getElem(pokemon.getTypes(), binding.typePokemon, DIMEN);
        getElem(pokemon.getAbilities(), binding.abilitiesPoke, DIMEN);
    }

    private void settingColor() {
        if (binding.pokeImage.getDrawable() != null){
            Bitmap bitmap = ((BitmapDrawable) binding.pokeImage.getDrawable()).getBitmap();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch vibrant = palette.getLightVibrantSwatch();
                    if (vibrant != null) {
                        updateColor(vibrant.getRgb());
                    }
                }
            });
        }
    }

    private void getElem(ArrayList<String> type, LinearLayout layout , Float dimen) {
        for (String elem : type) layout.addView(createTextView(elem, dimen));
    }

    private TextView createTextView(String text, Float dimen) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(dimen);
        textView.setText(text);
        return textView;
    }

    @Override
    public void onClickItem(int post) {
        binding.scrollViewContainer.setVisibility(View.GONE);
        binding.loader.setVisibility(View.VISIBLE);
        presenter.callPoke(String.valueOf(post));
    }

    @Override
    public void onClickItem(String name) {

    }
}
