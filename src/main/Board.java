package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Board {
    private final ArrayList<ArrayList<CardInput>> board;

    public Board(int rows, int columns) {
        // Creează structura bidimensională și inițializează fiecare poziție cu `null`
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
}
