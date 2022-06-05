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
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;
import com.example.pokemonapp.utils.constants.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PokemonFragment extends Fragment implements IDetail.ViewPresenter, OnClickItemListener {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_pokemon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        scrollViewContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

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

    private void init(View view){
        typesView = view.findViewById(R.id.typePokemon);
        abilities = view.findViewById(R.id.abilitiesPoke);
        weight = view.findViewById(R.id.weight);
        height = view.findViewById(R.id.height);
        baseExp = view.findViewById(R.id.expPoke);
        namePoke = view.findViewById(R.id.namePoke);
        pokeImage = view.findViewById(R.id.pokeImage);
        evolutions = view.findViewById(R.id.evolutions);
        goBack = view.findViewById(R.id.goBack);
        titleBar = view.findViewById(R.id.titleBar);
        pokeDescription = view.findViewById(R.id.pokeDescription);
        habitatLayout = view.findViewById(R.id.habitat);
        progressBar = view.findViewById(R.id.loader);
        scrollViewContainer = view.findViewById(R.id.scrollViewContainerr);
        descriptionMainContainer = view.findViewById(R.id.descriptionMainContainer);
        goBackContainer = view.findViewById(R.id.goBackContainer);
        evolutionContainer = view.findViewById(R.id.evoContainer);
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
        evolutionContainer.setAdapter(listPokemon);
    }

    private void updateColor(int color) {
        descriptionMainContainer.setCardBackgroundColor(color);
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
        evolutionContainer.setListener(this.presenter);
    }

    private void setContent(Pokemon pokemon) {
        progressBar.setVisibility(View.GONE);
        scrollViewContainer.setVisibility(View.VISIBLE);

        final float DIMEN = 16f;
        pokeImage.setTranslationY(-1000f);
        //load the image
        Helpers.loadImage(pokemon.getSpriteFront(), pokeImage);
        //animation
        ObjectAnimator animatorImg = ObjectAnimator.ofFloat(pokeImage, "translationY", 0f);
        animatorImg.setDuration(600);
        animatorImg.start();

        weight.removeAllViewsInLayout();
        baseExp.removeAllViewsInLayout();
        typesView.removeAllViewsInLayout();
        abilities.removeAllViewsInLayout();
        habitatLayout.removeAllViewsInLayout();
        height.removeAllViewsInLayout();

        setColor();

        double num = (pokemon.getWeight() / 4.54);
        String pound = String.format("%.02f", num);

        String n = pokemon.getName().replaceAll("-", " ");
        pokeDescription.setText(pokemon.getDescription());
        namePoke.setText(Helpers.ToUpperName(n));
        habitatLayout.addView(createTextView(pokemon.getHabitat(), DIMEN));
        weight.addView(createTextView(pound+" pounds", DIMEN));
        height.addView(createTextView((pokemon.getHeight() * 10) + " cm", DIMEN));
        baseExp.addView(createTextView(String.valueOf(pokemon.getBaseExperience()), DIMEN));

        getElem(pokemon.getTypes(), typesView, DIMEN);
        getElem(pokemon.getAbilities(), abilities, DIMEN);
    }

    private void setColor() {
        Bitmap bitmap = ((BitmapDrawable) pokeImage.getDrawable()).getBitmap();
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
        scrollViewContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        presenter.callPoke(String.valueOf(post));
    }

    @Override
    public void onClickItem(String name) {

    }
}
