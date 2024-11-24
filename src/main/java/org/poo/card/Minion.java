package org.poo.card;

import org.poo.fileio.CardInput;

public final class Minion extends Card {
    private int attackDamage;

    public Minion(final CardInput cardInput) {
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
        return super.toString().replace("}", "") + ", attackDamage=" + attackDamage + "}";
    }
}
