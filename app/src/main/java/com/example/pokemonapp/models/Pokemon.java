package com.example.pokemonapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable, Comparable {
    private int id;
    private String name;
    private String spriteBack, spriteFront;
    private double weight;
    private double height;
    private double baseExperience;
    private ArrayList<String> abilities;
    private ArrayList<String> types;
    private String urlEvolutions;
    private String description;
    private String color;
    private String habitat;
    private ArrayList<Move> moves;
    
    public Pokemon(){ }

    public Pokemon (int id, String name){
        this.id = id;
        this.name = name;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlEvolutions() {
        return urlEvolutions;
    }

    public void setUrlEvolutions(String urlEvolutions) {
        this.urlEvolutions = urlEvolutions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpriteBack() {
        return spriteBack;
    }

    public void setSpriteBack(String spriteBack) {
        this.spriteBack = spriteBack;
    }

    public String getSpriteFront() {
        return spriteFront;
    }

    public void setSpriteFront(String spriteFront) {
        this.spriteFront = spriteFront;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBaseExperience() {
        return baseExperience;
    }

    public void setBaseExperience(double baseExperience) {
        this.baseExperience = baseExperience;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<String> abilities) {
        this.abilities = abilities;
    }



    // to sort by id: type int
    @Override
    public int compareTo(Object o) {
        //return (this.getId() < ((Pokemon)o).getId() ? -1 : (this.getId() == ((Pokemon)o).getId() ? 0: 1));
        return  Integer.compare(this.getId(), ((Pokemon)o).getId());
    }
}
