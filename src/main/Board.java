package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Board {
    private final ArrayList<ArrayList<CardInput>> board;

    public Board(int rows, int columns) {
        board = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ArrayList<CardInput> row = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                row.add(null);
            }
            board.add(row);
        }
    }

    public int placeOnBoard(CardInput card, Player player) {
        if (player.getMana() >= card.getMana()) {
            String name = card.getName();
            if (name.equals("The Ripper") || name.equals("Miraj") || name.equals("Goliath") || name.equals("Warden")) {
                if (player.getPlayerIndex() == 2) {
                    ArrayList<CardInput> row = board.get(1);
                    for (int i = 0; i < row.size(); i++) {
                        if (row.get(i) == null) { // Verificăm dacă poziția este goală
                            row.set(i, card); // Plasăm cartea în această poziție
                            player.setMana(player.getMana() - card.getMana()); // Scădem mana jucătorului
                            return 1; // Returnăm 1 pentru a indica plasarea reușită
                        }
                    }
                    return 2;
                } else if (player.getPlayerIndex() == 1) {
                    ArrayList<CardInput> row = board.get(2);
                    for (int i = 0; i < row.size(); i++) {
                        if (row.get(i) == null) { // Verificăm dacă poziția este goală
                            row.set(i, card); // Plasăm cartea în această poziție
                            player.setMana(player.getMana() - card.getMana());
                            return 1; // Returnăm 1 pentru a indica plasarea reușit
                        }
                    }
                    return 2;
                }
            } else {
                if (player.getPlayerIndex() == 2) {
                    ArrayList<CardInput> row = board.get(0);
                    for (int i = 0; i < row.size(); i++) {
                        if (row.get(i) == null) { // Verificăm dacă poziția este goală
                            row.set(i, card); // Plasăm cartea în această poziție
                            player.setMana(player.getMana() - card.getMana()); // Scădem mana jucătorului
                            return 1; // Returnăm 1 pentru a indica plasarea reușită
                        }
                    }
                    return 2;
                } else if (player.getPlayerIndex() == 1) {
                    ArrayList<CardInput> row = board.get(3);
                    for (int i = 0; i < row.size(); i++) {
                        if (row.get(i) == null) { // Verificăm dacă poziția este goală
                            row.set(i, card); // Plasăm cartea în această poziție
                            player.setMana(player.getMana() - card.getMana()); // Scădem mana jucătorului
                            return 1; // Returnăm 1 pentru a indica plasarea reușit
                        }
                    }
                    return 2;
                }
            }
        }
        return 0;
    }

    public ArrayList<ArrayList<CardInput>> getBoard() {
        return board;
    }

    public int cardAttack(int xAttacker, int yAttacker, int xAttacked, int yAttacked, Player playerAttacker) {
        int row;
        if (((xAttacker == 0 || xAttacker == 1) && (xAttacked == 0 || xAttacked == 1)) || ((xAttacker == 2 || xAttacker == 3) && (xAttacked == 2 || xAttacked == 3))) {
            return 1;
        }
        for (CardInput card : playerAttacker.getCardsUsed()) {
            if (card == board.get(xAttacker).get(yAttacker)) {
                return 2;
            }
        }
        for (CardInput card : playerAttacker.getFrozenCards()) {
            if (card == board.get(xAttacker).get(yAttacker)) {
                return 3;
            }
        }
        if (xAttacker == 0 || xAttacker == 1) {
            row = 2;
        } else {
            row = 1;
        }
        for (CardInput card : board.get(row)) {
            if (card != null && board.get(xAttacked).get(yAttacked) != null) {
                if ((card.getName().equals("Goliath") || card.getName().equals("Warden")) && !(board.get(xAttacked).get(yAttacked).getName().equals("Goliath")
                        || board.get(xAttacked).get(yAttacked).getName().equals("Warden"))) {
                    return 4;
                }
            }
        }
        if (board.get(xAttacked).get(yAttacked) != null && board.get(xAttacker).get(yAttacker) != null) {
            int health = board.get(xAttacked).get(yAttacked).getHealth();
            health = health - board.get(xAttacker).get(yAttacker).getAttackDamage();
            board.get(xAttacked).get(yAttacked).setHealth(health);
            if (health <= 0) {
                int i = yAttacked;
                while (i < board.get(xAttacked).size() - 1 && board.get(xAttacked).get(i + 1) != null) {
                    board.get(xAttacked).set(i, board.get(xAttacked).get(i + 1));
                    i++;
                }
                board.get(xAttacked).set(i, null);
            }
        }
        return 0;
    }

    public int cardUseAbility(int xAttacker, int yAttacker, int xAttacked, int yAttacked, Player playerAttacker) {
        int row;
        if (playerAttacker.getFrozenCards().size() > 0) {
            for (CardInput card : playerAttacker.getFrozenCards()) {
                if (card != null) {
                    if (card == board.get(xAttacker).get(yAttacker)) {
                        return 1;
                    }
                }
            }
        }
        if (playerAttacker.getCardsUsed().size() > 0) {
            for (CardInput card : playerAttacker.getCardsUsed()) {
                if (card == board.get(xAttacker).get(yAttacker)) {
                    return 2;
                }
            }
        }
        if (board.get(xAttacker).get(yAttacker) != null) {
            if (board.get(xAttacker).get(yAttacker).getName().equals("Disciple")) {
                if (((xAttacker == 0 || xAttacker == 1) && (xAttacked == 0 || xAttacked == 1)) || ((xAttacker == 2 || xAttacker == 3) && (xAttacked == 2 || xAttacked == 3))) {
                    int health = board.get(xAttacked).get(yAttacked).getHealth();
                    board.get(xAttacked).get(yAttacked).setHealth(health + 2);
                } else {
                    return 3;
                }
            }
        }
        if (board.get(xAttacker).get(yAttacker) != null && board.get(xAttacked).get(yAttacked) != null) {
            if (board.get(xAttacker).get(yAttacker).getName().equals("The Ripper") || board.get(xAttacker).get(yAttacker).getName().equals("Miraj")
                    || board.get(xAttacker).get(yAttacker).getName().equals("The Cursed One")) {
                if (((xAttacker == 0 || xAttacker == 1) && (xAttacked == 0 || xAttacked == 1)) || ((xAttacker == 2 || xAttacker == 3) && (xAttacked == 2 || xAttacked == 3))) {
                    return 4;
                }
                if (xAttacker == 0 || xAttacker == 1) {
                    row = 2;
                } else {
                    row = 1;
                }
                for (CardInput card : board.get(row)) {
                    if (card != null && board.get(xAttacked).get(yAttacked) != null) {
                        if ((card.getName().equals("Goliath") || card.getName().equals("Warden")) && !(board.get(xAttacked).get(yAttacked).getName().equals("Goliath")
                                || board.get(xAttacked).get(yAttacked).getName().equals("Warden"))) {
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
                        int i = yAttacked;
                        while (i < board.get(xAttacked).size() - 1 && board.get(xAttacked).get(i + 1) != null) {
                            board.get(xAttacked).set(i, board.get(xAttacked).get(i + 1));
                            i++;
                        }
                        board.get(xAttacked).set(i, null);
                    }
                }
            }
        }
        return 0;
    }

    public int HeroAttack(int xAttacker, int yAttacker, Hero heroAttacked, Player playerAttacker) {
        if (playerAttacker.getFrozenCards().size() > 0) {
            for (CardInput card : playerAttacker.getFrozenCards()) {
                if (card != null) {
                    if (card == board.get(xAttacker).get(yAttacker)) {
                        return 1;
                    }
                }
            }
        }
        if (playerAttacker.getCardsUsed().size() > 0) {
            for (CardInput card : playerAttacker.getCardsUsed()) {
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
        for (CardInput card : board.get(row)) {
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
            if (health < 0) {
                return -1;
            }
            return 0;
        }
        return 0;
    }
}