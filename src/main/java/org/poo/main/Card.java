package org.poo.main;

import org.poo.fileio.CardInput;
import java.util.ArrayList;

public class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public Card(CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.health = cardInput.getHealth();
        this.description = cardInput.getDescription();
        this.colors = new ArrayList<>(cardInput.getColors());
        this.name = cardInput.getName();
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
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

class Minion extends Card {
    private int attackDamage;

    public Minion(CardInput cardInput) {
        super(cardInput);
        this.attackDamage = cardInput.getAttackDamage();
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    @Override
    public String toString() {
        return super.toString() + ", attackDamage=" + attackDamage;
    }
}

class Hero  extends Card {
    public Hero(final CardInput cardInput) {
        super(cardInput);
        super.setHealth(Constants.HEALTHHERO.getValue());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
