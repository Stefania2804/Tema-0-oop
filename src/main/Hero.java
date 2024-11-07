package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Hero {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private int health;

    public Hero() {

    }
    public Hero(CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.description = cardInput.getDescription();
        this.colors = new ArrayList<>(cardInput.getColors());
        this.name = cardInput.getName();
        this.health = 30;
    }


    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "{"
                +  "mana="
                + mana
                + ", health="
                + health
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}';
    }
}
