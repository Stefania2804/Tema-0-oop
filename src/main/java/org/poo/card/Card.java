package org.poo.card;

import org.poo.fileio.CardInput;
import java.util.ArrayList;

public class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public Card(final CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.health = cardInput.getHealth();
        this.description = cardInput.getDescription();
        this.colors = new ArrayList<>(cardInput.getColors());
        this.name = cardInput.getName();
    }
    /**
     * Returneaza mana cartii.
     *
     */
    public int getMana() {
        return mana;
    }
    /**
     * Seteaza mana cartii.
     *
     * @param mana este mana pe care o atribui cartii
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }
    /**
     * Returneaza viata cartii.
     *
     */
    public int getHealth() {
        return health;
    }
    /**
     * Seteaza viata cartii.
     *
     * @param health este viata pe care o atribui cartii
     */
    public void setHealth(final int health) {
        this.health = health;
    }
    /**
     * Returneaza descriereea cartii.
     *
     */
    public String getDescription() {
        return description;
    }
    /**
     * Seteaza descrierea cartii.
     *
     * @param description este descrierea pe care vreau sa o atribui cartii
     */
    public void setDescription(final String description) {
        this.description = description;
    }
    /**
     * Returneaza culorile cartii.
     *
     */
    public ArrayList<String> getColors() {
        return colors;
    }
    /**
     * Seteaza culorile cartii.
     *
     * @param colors reprezinta culorile pe care le atribui cartii
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }
    /**
     * Returneaza numele cartii.
     *
     */
    public String getName() {
        return name;
    }
    /**
     * Seteaza numele cartii.
     *
     * @param name este numele pe care il atribui cartii
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Suprascrie metoda toString.
     *
     */
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

