package org.poo.game;

import org.poo.card.Hero;
import org.poo.card.Minion;
import org.poo.main.Constants;
import org.poo.main.Player;

import java.util.ArrayList;

public final class Board {
    private final ArrayList<ArrayList<Minion>> board;

    public Board(final int rows, final int columns) {
        board = new ArrayList<>();
        for (int k = 0; k < rows; k++) {
            ArrayList<Minion> row = new ArrayList<>();
            for (int p = 0; p < columns; p++) {
                row.add(null);
            }
            board.add(row);
        }
    }
    public ArrayList<ArrayList<Minion>> getBoard() {
        return board;
    }
    /**
     * Jucatorul curent pune o carte pe masa.
     *
     * @param card este cartea nou adaugata,
     * @param player este player-ul care pune cartea pe masa
     */
    public int placeOnBoard(final Minion card, final Player player) {
        if (player.getMana() >= card.getMana()) {
            String name = card.getName();
            if (name.equals("The Ripper") || name.equals("Miraj")
                    || name.equals("Goliath") || name.equals("Warden")) {
                if (player.getPlayerIndex() == Constants.INDEXTWO.getValue()) {
                    ArrayList<Minion> row = board.get(1);
                    for (int k = 0; k < row.size(); k++) {
                        if (row.get(k) == null) {
                            row.set(k, card);
                            player.setMana(player.getMana() - card.getMana());
                            return 0;
                        }
                    }
                    return Constants.ERROR2.getValue();
                } else if (player.getPlayerIndex() == Constants.INDEXONE.getValue()) {
                    ArrayList<Minion> row = board.get(2);
                    for (int k = 0; k < row.size(); k++) {
                        if (row.get(k) == null) {
                            row.set(k, card);
                            player.setMana(player.getMana() - card.getMana());
                            return 0;
                        }
                    }
                    return Constants.ERROR2.getValue();
                }
            } else if (name.equals("The Cursed One") || name.equals("Disciple")
                    || name.equals("Sentinel") || name.equals("Berserker")) {
                if (player.getPlayerIndex() == 2) {
                    ArrayList<Minion> row = board.get(0);
                    for (int k = 0; k < row.size(); k++) {
                        if (row.get(k) == null) {
                            row.set(k, card);
                            player.setMana(player.getMana() - card.getMana());
                            return 0;
                        }
                    }
                    return Constants.ERROR2.getValue();
                } else if (player.getPlayerIndex() == 1) {
                    ArrayList<Minion> row = board.get(Constants.ROW3.getValue());
                    for (int k = 0; k < row.size(); k++) {
                        if (row.get(k) == null) {
                            row.set(k, card);
                            player.setMana(player.getMana() - card.getMana());
                            return 0;
                        }
                    }
                    return Constants.ERROR2.getValue();
                }
            }
        }
        return Constants.ERROR1.getValue();
    }
    /**
     * Jucatorul foloseste o carte de pe masa sa atace.
     *
     * @param playerAttacker este player-ul care ataca
     */
    public int cardAttack(final int xAttacker, final int yAttacker, final int xAttacked,
                          final int yAttacked, final Player playerAttacker) {
        int row;
        if (((xAttacker == Constants.ROW0.getValue() || xAttacker == Constants.ROW1.getValue())
                && (xAttacked == Constants.ROW0.getValue()
                || xAttacked  == Constants.ROW1.getValue()))
                || ((xAttacker == Constants.ROW2.getValue()
                || xAttacker == Constants.ROW3.getValue())
                && (xAttacked == Constants.ROW2.getValue()
                || xAttacked == Constants.ROW3.getValue()))) {
            return Constants.ERROR1.getValue();
        }
        for (Minion card : playerAttacker.getCardsUsed()) {
            if (card == board.get(xAttacker).get(yAttacker)) {
                return Constants.ERROR2.getValue();
            }
        }
        for (Minion card : playerAttacker.getFrozenCards()) {
            if (card == board.get(xAttacker).get(yAttacker)) {
                return Constants.ERROR3.getValue();
            }
        }
        if (xAttacker == Constants.ROW0.getValue() || xAttacker == Constants.ROW1.getValue()) {
            row = Constants.ROW2.getValue();
        } else {
            row = Constants.ROW1.getValue();
        }
        for (Minion card : board.get(row)) {
            if (card != null && board.get(xAttacked).get(yAttacked) != null) {
                if ((card.getName().equals("Goliath") || card.getName().equals("Warden"))
                        && !(board.get(xAttacked).get(yAttacked).getName().equals("Goliath")
                        || board.get(xAttacked).get(yAttacked).getName().equals("Warden"))) {
                    return Constants.ERROR4.getValue();
                }
            }
        }
        if (board.get(xAttacked).get(yAttacked) != null
                && board.get(xAttacker).get(yAttacker) != null) {
            int health = board.get(xAttacked).get(yAttacked).getHealth();
            health = health - board.get(xAttacker).get(yAttacker).getAttackDamage();
            board.get(xAttacked).get(yAttacked).setHealth(health);
            if (health <= 0) {
                int i = yAttacked;
                while (i < board.get(xAttacked).size() - 1
                        && board.get(xAttacked).get(i + 1) != null) {
                    board.get(xAttacked).set(i, board.get(xAttacked).get(i + 1));
                    i++;
                }
                board.get(xAttacked).set(i, null);
            }
        }
        return 0;
    }
    /**
     * Cartea isi foloseste abilitatea.
     *
     * @param playerAttacker este player -ul care utilizeaza abilitatea cartii.
     */
    public int cardUseAbility(final int xAttacker, final int yAttacker, final int xAttacked,
                              final int yAttacked, final Player playerAttacker) {
        int row;
        if (playerAttacker.getFrozenCards().size() > 0) {
            for (Minion card : playerAttacker.getFrozenCards()) {
                if (card != null) {
                    if (card == board.get(xAttacker).get(yAttacker)) {
                        return 1;
                    }
                }
            }
        }
        if (playerAttacker.getCardsUsed().size() > 0) {
            for (Minion card : playerAttacker.getCardsUsed()) {
                if (card == board.get(xAttacker).get(yAttacker)) {
                    return 2;
                }
            }
        }
        if (board.get(xAttacker).get(yAttacker) != null) {
            if (board.get(xAttacker).get(yAttacker).getName().equals("Disciple")) {
                if (((xAttacker == Constants.ROW0.getValue()
                        || xAttacker == Constants.ROW1.getValue())
                        && (xAttacked == Constants.ROW0.getValue()
                        || xAttacked == Constants.ROW1.getValue()))
                        || ((xAttacker == Constants.ROW2.getValue()
                        || xAttacker == Constants.ROW3.getValue())
                        && (xAttacked == Constants.ROW2.getValue()
                        || xAttacked == Constants.ROW3.getValue()))) {
                    int health = board.get(xAttacked).get(yAttacked).getHealth();
                    board.get(xAttacked).get(yAttacked).setHealth(health + 2);
                } else {
                    return Constants.ERROR3.getValue();
                }
            }
        }
        if (board.get(xAttacker).get(yAttacker) != null
                && board.get(xAttacked).get(yAttacked) != null) {
            if (board.get(xAttacker).get(yAttacker).getName().equals("The Ripper")
                    || board.get(xAttacker).get(yAttacker).getName().equals("Miraj")
                    || board.get(xAttacker).get(yAttacker).getName().equals("The Cursed One")) {
                if (((xAttacker == Constants.ROW0.getValue()
                        || xAttacker == Constants.ROW1.getValue())
                        && (xAttacked == Constants.ROW0.getValue()
                        || xAttacked == Constants.ROW1.getValue()))
                        || ((xAttacker == Constants.ROW2.getValue()
                        || xAttacker == Constants.ROW3.getValue())
                        && (xAttacked == Constants.ROW2.getValue()
                        || xAttacked == Constants.ROW3.getValue()))) {
                    return Constants.ERROR4.getValue();
                }
                if (xAttacker == Constants.ROW0.getValue()
                        || xAttacker == Constants.ROW1.getValue()) {
                    row = Constants.ROW2.getValue();
                } else {
                    row = Constants.ROW1.getValue();
                }
                for (Minion card : board.get(row)) {
                    if (card != null && board.get(xAttacked).get(yAttacked) != null) {
                        if ((card.getName().equals("Goliath") || card.getName().equals("Warden"))
                                && !(board.get(xAttacked).get(yAttacked).getName().equals("Goliath")
                                || board.get(xAttacked).get(yAttacked).
                                getName().equals("Warden"))) {
                            return Constants.ERROR5.getValue();
                        }
                    }
                }
                if (board.get(xAttacker).get(yAttacker).getName().equals("The Ripper")) {
                    int attackDamage = board.get(xAttacked).get(yAttacked).getAttackDamage();
                    attackDamage = attackDamage - 2;
                    if (attackDamage < 0) {
                        attackDamage = 0;
                    }
                    board.get(xAttacked).get(yAttacked).setAttackDamage(attackDamage);
                } else if (board.get(xAttacker).get(yAttacker).getName().equals("Miraj")) {
                    int healthAttacker = board.get(xAttacker).get(yAttacker).getHealth();
                    int healthAttacked = board.get(xAttacked).get(yAttacked).getHealth();
                    board.get(xAttacker).get(yAttacker).setHealth(healthAttacked);
                    board.get(xAttacked).get(yAttacked).setHealth(healthAttacker);
                } else if (board.get(xAttacker).get(yAttacker).getName().equals("The Cursed One")) {
                    int health = board.get(xAttacked).get(yAttacked).getHealth();
                    int attackDamage = board.get(xAttacked).get(yAttacked).getAttackDamage();
                    board.get(xAttacked).get(yAttacked).setHealth(attackDamage);
                    board.get(xAttacked).get(yAttacked).setAttackDamage(health);
                    if (attackDamage == 0) {
                        int pos = yAttacked;
                        while (pos < board.get(xAttacked).size() - 1
                                && board.get(xAttacked).get(pos + 1) != null) {
                            board.get(xAttacked).set(pos, board.get(xAttacked).get(pos + 1));
                            pos++;
                        }
                        board.get(xAttacked).set(pos, null);
                    }
                }
            }
        }
        return 0;
    }
    /**
     * Se ataca eroul folosind o carte primita ca parametru.
     *
     * @param heroAttacked este eroul atacat
     */
    public int heroAttack(final int xAttacker, final int yAttacker,
                          final Hero heroAttacked, final Player playerAttacker) {
        if (playerAttacker.getFrozenCards().size() > 0) {
            for (Minion card : playerAttacker.getFrozenCards()) {
                if (card != null) {
                    if (card == board.get(xAttacker).get(yAttacker)) {
                        return Constants.ERROR1.getValue();
                    }
                }
            }
        }
        if (playerAttacker.getCardsUsed().size() > 0) {
            for (Minion card : playerAttacker.getCardsUsed()) {
                if (card != null) {
                    if (card == board.get(xAttacker).get(yAttacker)) {
                        return Constants.ERROR2.getValue();
                    }
                }
            }
        }
        int row;
        if (xAttacker == Constants.ROW0.getValue()
                || xAttacker == Constants.ROW1.getValue()) {
            row = Constants.ROW2.getValue();
        } else {
            row = Constants.ROW1.getValue();
        }
        for (Minion card : board.get(row)) {
            if (card != null) {
                if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    return Constants.ERROR3.getValue();
                }
            }
        }
        if (heroAttacked != null && board.get(xAttacker).get(yAttacker) != null) {
            int health = heroAttacked.getHealth();
            health = health - board.get(xAttacker).get(yAttacker).getAttackDamage();
            heroAttacked.setHealth(health);
            if (health <= 0) {
                return -1;
            }
            return 0;
        }
        return 0;
    }
    /**
     * Eroul isi foloseste abilitatea speciala.
     *
     * @param heroAttacker este eroul care ataca,
     * @param affectedRow este randul atacat.
     */
    public int useHeroAbility(final Hero heroAttacker, final int affectedRow,
                              final Player player, final Player playerAttacked) {
        if (player.getMana() < heroAttacker.getMana()) {
            return Constants.ERROR1.getValue();
        }
        if (player.getHeroUsed() == 1) {
            return Constants.ERROR2.getValue();
        }
        if (heroAttacker.getName().equals("Lord Royce")
                || heroAttacker.getName().equals("Empress Thorina")) {
            if ((player.getPlayerIndex() == Constants.INDEXONE.getValue()
                    && (affectedRow == Constants.ROW2.getValue()
                    || affectedRow == Constants.ROW3.getValue()))
                    || (player.getPlayerIndex() == Constants.INDEXTWO.getValue()
                    && (affectedRow == Constants.ROW0.getValue()
                    || affectedRow == Constants.ROW1.getValue()))) {
                return Constants.ERROR3.getValue();
            }
        }
        if (heroAttacker.getName().equals("General Kocioraw")
                || heroAttacker.getName().equals("King Mudface")) {
            if ((player.getPlayerIndex() == Constants.INDEXONE.getValue()
                    && (affectedRow == Constants.ROW0.getValue()
                    || affectedRow == Constants.ROW1.getValue()))
                    || (player.getPlayerIndex() == Constants.INDEXTWO.getValue()
                    && (affectedRow == Constants.ROW2.getValue()
                    || affectedRow == Constants.ROW3.getValue()))) {
                return Constants.ERROR4.getValue();
            }
        }
        int mana = player.getMana();
        player.setMana(mana - heroAttacker.getMana());
        if (heroAttacker.getName().equals("Lord Royce")) {
            for (Minion card : board.get(affectedRow)) {
                int ok = 1;
                if (!playerAttacked.getFrozenCards().isEmpty()) {
                    for (Minion cardFrozen : playerAttacked.getFrozenCards()) {
                        if (card == cardFrozen) {
                            playerAttacked.addStillFrozenCard(card);
                            ok = 0;
                        }
                    }
                    if (ok == 1) {
                        playerAttacked.addCardFrozen(card);
                    }
                } else {
                    playerAttacked.addCardFrozen(card);
                }
            }
        }
        if (heroAttacker.getName().equals("Empress Thorina")) {
            int maxim = 0;
            for (Minion card : board.get(affectedRow)) {
                if (card != null && card.getHealth() > maxim) {
                    maxim = card.getHealth();
                }
            }
            int collumn = 0;
            for (Minion card : board.get(affectedRow)) {
                if (card != null && card.getHealth() == maxim) {
                    card.setHealth(0);
                    int pos = collumn;
                    while (pos < board.get(affectedRow).size() - 1
                            && board.get(affectedRow).get(pos + 1) != null) {
                        board.get(affectedRow).set(pos, board.get(affectedRow).get(pos + 1));
                        pos++;
                    }
                    board.get(affectedRow).set(pos, null);
                    break;
                }
                collumn++;
            }

        }
        if (heroAttacker.getName().equals("King Mudface")) {
            for (Minion card : board.get(affectedRow)) {
                if (card != null) {
                    int health = card.getHealth();
                    card.setHealth(health + 1);
                }
            }
        }
        if (heroAttacker.getName().equals("General Kocioraw")) {
            for (Minion card : board.get(affectedRow)) {
                if (card != null) {
                    int attackDamage = card.getAttackDamage();
                    card.setAttackDamage(attackDamage + 1);
                }
            }
        }

        return 0;
    }
}
