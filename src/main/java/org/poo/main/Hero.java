package org.poo.main;

import org.poo.fileio.CardInput;

public final class Hero  extends Card {
    public Hero(final CardInput cardInput) {
        super(cardInput);
        super.setHealth(Constants.HEALTHHERO.getValue());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
