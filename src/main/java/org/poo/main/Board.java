package org.poo.main;

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

    public int placeOnBoard(final Minion card, final Player player) {
        if (player.getMana() >= card.getMana()) {
            String name = card.getName();
            if (name.equals("The Ripper") || name.equals("Miraj")
                    || name.equals("Goliath") || name.equals("Warden")) {
                if (player.getPlayerIndex() == 2) {
                    ArrayList<Minion> row = board.get(1);
                    for (int k = 0; k < row.size(); k++) {
                        if (row.get(k) == null) {
                            row.set(k, card);
                            player.setMana(player.getMana() - card.getMana());
                            return 1;
                        }
                    }
                    return 2;
                } else if (player.getPlayerIndex() == 1) {
                    ArrayList<Minion> row = board.get(2);
                    for (int k = 0; k < row.size(); k++) {
                        if (row.get(k) == null) {
                            row.set(k, card);
                            player.setMana(player.getMana() - card.getMana());
                            return 1;
                        }
                    }
                    return 2;
                }
            } else if (name.equals("The Cursed One") || name.equals("Disciple")
                    || name.equals("Sentinel") || name.equals("Berserker")) {
                if (player.getPlayerIndex() == 2) {
                    ArrayList<Minion> row = board.get(0);
                    for (int k = 0; k < row.size(); k++) {
                        if (row.get(k) == null) {
                            row.set(k, card);
                            player.setMana(player.getMana() - card.getMana());
                            return 1;
                        }
                    }
                    return 2;
                } else if (player.getPlayerIndex() == 1) {
                    ArrayList<Minion> row = board.get(3);
                    for (int k = 0; k < row.size(); k++) {
                        if (row.get(k) == null) {
                            row.set(k, card);
                            player.setMana(player.getMana() - card.getMana());
                            return 1;
                        }
                    }
                    return 2;
                }
            }
        }
        return 0;
    }

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
            return 1;
        }
        for (Minion card : playerAttacker.getCardsUsed()) {
            if (card == board.get(xAttacker).get(yAttacker)) {
                return 2;
            }
        }
        for (Minion card : playerAttacker.getFrozenCards()) {
            if (card == board.get(xAttacker).get(yAttacker)) {
                return 3;
            }
        }
        if (xAttacker == Constants.ROW0.getValue() || xAttacker == Constants.ROW1.getValue()) {
            row = 2;
        } else {
            row = 1;
        }
        for (Minion card : board.get(row)) {
            if (card != null && board.get(xAttacked).get(yAttacked) != null) {
                if ((card.getName().equals("Goliath") || card.getName().equals("Warden"))
                        && !(board.get(xAttacked).get(yAttacked).getName().equals("Goliath")
                        || board.get(xAttacked).get(yAttacked).getName().equals("Warden"))) {
                    return 4;
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
                if (((xAttacker == 0 || xAttacker == 1) && (xAttacked == 0 || xAttacked == 1))
                        || ((xAttacker == 2 || xAttacker == 3)
                        && (xAttacked == 2 || xAttacked == 3))) {
                    int health = board.get(xAttacked).get(yAttacked).getHealth();
                    board.get(xAttacked).get(yAttacked).setHealth(health + 2);
                } else {
                    return 3;
                }
            }
        }
        if (board.get(xAttacker).get(yAttacker) != null
                && board.get(xAttacked).get(yAttacked) != null) {
            if (board.get(xAttacker).get(yAttacker).getName().equals("The Ripper")
                    || board.get(xAttacker).get(yAttacker).getName().equals("Miraj")
                    || board.get(xAttacker).get(yAttacker).getName().equals("The Cursed One")) {
                if (((xAttacker == 0 || xAttacker == 1) && (xAttacked == 0 || xAttacked == 1))
                        || ((xAttacker == 2 || xAttacker == 3)
                        && (xAttacked == 2 || xAttacked == 3))) {
                    return 4;
                }
                if (xAttacker == 0 || xAttacker == 1) {
                    row = 2;
                } else {
                    row = 1;
                }
                for (Minion card : board.get(row)) {
                    if (card != null && board.get(xAttacked).get(yAttacked) != null) {
                        if ((card.getName().equals("Goliath") || card.getName().equals("Warden"))
                                && !(board.get(xAttacked).get(yAttacked).getName().equals("Goliath")
                                || board.get(xAttacked).get(yAttacked).
                                getName().equals("Warden"))) {
                            return 5;
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

    public int HeroAttack(final int xAttacker, final int yAttacker,
                          final Hero heroAttacked, final Player playerAttacker) {
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
                if (card != null) {
                    if (card == board.get(xAttacker).get(yAttacker)) {
                        return 2;
                    }
                }
            }
        }
        int row;
        if (xAttacker == 0 || xAttacker == 1) {
            row = 2;
        } else {
            row = 1;
        }
        for (Minion card : board.get(row)) {
            if (card != null) {
                if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    return 3;
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
    public int UseHeroAbility(final Hero heroAttacker, final int affectedRow,
                              final Player player, final Player playerAttacked) {
        if (player.getMana() < heroAttacker.getMana()) {
            return 1;
        }
        if (player.getHeroUsed() == 1) {
            return 2;
        }
        if (heroAttacker.getName().equals("Lord Royce")
                || heroAttacker.getName().equals("Empress Thorina")) {
            if ((player.getPlayerIndex() == 1 && (affectedRow == Constants.ROW2.getValue()
                    || affectedRow == Constants.ROW3.getValue()))
                    || (player.getPlayerIndex() == 2 && (affectedRow == Constants.ROW0.getValue()
                    || affectedRow == Constants.ROW1.getValue()))) {
                return 3;
            }
        }
        if (heroAttacker.getName().equals("General Kocioraw")
                || heroAttacker.getName().equals("King Mudface")) {
            if ((player.getPlayerIndex() == 1 && (affectedRow == 0 || affectedRow == 1))
                    || (player.getPlayerIndex() == 2 && (affectedRow == 2 || affectedRow == 3))) {
                return 4;
            }
        }
        int mana = player.getMana();
        player.setMana(mana - heroAttacker.getMana());
        if (heroAttacker.getName().equals("Lord Royce")) {
            for (Minion card : board.get(affectedRow)) {
                int ok = 1;
                if (!playerAttacked.getFrozenCards().isEmpty()) {
                    for (Minion cardfrozen : playerAttacked.getFrozenCards()) {
                        if (card == cardfrozen) {
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
