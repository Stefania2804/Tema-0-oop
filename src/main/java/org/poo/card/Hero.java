package org.poo.card;

import org.poo.fileio.CardInput;
import org.poo.main.Constants;

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
