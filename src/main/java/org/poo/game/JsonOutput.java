package org.poo.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.card.Hero;
import org.poo.card.Minion;
import org.poo.fileio.CardInput;
import org.poo.fileio.Coordinates;
import org.poo.main.Constants;
import org.poo.main.Player;

import java.util.ArrayList;

public final class JsonOutput {
    public JsonOutput() {

    }
    /**
     * Afiseaza pachetul de carti ales de jucator.
     *
     */
    public static void getPlayerDeck(final Player player, final int playerIdx,
                                     final ObjectMapper objectMapper, final ArrayNode output) {
        ArrayList<CardInput> deck = player.getPlayerDeck();
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getPlayerDeck");
        commandNode.put("playerIdx", playerIdx);

        ArrayNode cardsArray = objectMapper.createArrayNode();
        for (CardInput cardInput : deck) {
            cardsArray.add(objectMapper.valueToTree(cardInput));
        }

        commandNode.set("output", cardsArray);
        output.add(commandNode);
    }
    /**
     * Afiseaza eroul primit de jucatorul cu indexul primit ca parametru.
     *
     */
    public static void getPlayerHero(final Player player, final int playerIdx,
                                     final ObjectMapper objectMapper, final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getPlayerHero");
        commandNode.put("playerIdx", playerIdx);
        commandNode.set("output", objectMapper.valueToTree(player.getHero()));

        output.add(commandNode);
    }
    /**
     * Afiseaza indexul jucatorului curent.
     *
     */
    public static void getPlayerTurn(final int playerTurn,
                                     final ObjectMapper objectMapper, final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getPlayerTurn");
        commandNode.set("output", objectMapper.valueToTree(playerTurn));

        output.add(commandNode);
    }
    /**
     * Afiseaza cartile din mana jucatorului cu indexul primit ca parametru.
     *
     */
    public static void getCardsInHand(final Player player, final int playerIdx,
                                      final ObjectMapper objectMapper, final ArrayNode output) {
        ArrayList<Minion> cards = player.getCardsInHand();
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getCardsInHand");
        commandNode.put("playerIdx", playerIdx);
        ArrayNode cardsArray = objectMapper.createArrayNode();
        for (Minion card : cards) {
            cardsArray.add(objectMapper.valueToTree(card));
        }

        commandNode.set("output", cardsArray);

        output.add(commandNode);
    }
    /**
     * Punerea unei carti pe masa de catre jucatorul curent.
     *
     */
    public static void placeCardOnTable(final Player player, final int handIdx,
                                        final Board board, final ObjectMapper objectMapper,
                                        final ArrayNode output) {
        ArrayList<Minion> cards = player.getCardsInHand();
        if (!cards.isEmpty() && handIdx >= 0 && handIdx < cards.size()) {
            Minion card = cards.get(handIdx);
            int result = board.placeOnBoard(card, player);
            if (result == Constants.ERROR1.getValue()) {
                ObjectNode commandNode = objectMapper.createObjectNode();
                commandNode.put("command", "placeCard");
                commandNode.put("handIdx", handIdx);
                commandNode.put("error", "Not enough mana to place card on table.");
                output.add(commandNode);
            } else if (result == Constants.ERROR2.getValue()) {
                ObjectNode commandNode = objectMapper.createObjectNode();
                commandNode.put("command", "placeCard");
                commandNode.put("handIdx", handIdx);
                commandNode.put("error", "Cannot place card on table since row "
                        + "is full.");
                output.add(commandNode);
            } else if (result == 0) {
                player.removeCardInHand(card);
            }

        }
    }
    /**
     * Afiseaza mana jucatorului cu indexul primit ca parametru.
     *
     */
    public static void getPlayerMana(final Player player, final int playerIdx,
                                     final ObjectMapper objectMapper, final ArrayNode output) {
        int mana = player.getMana();
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getPlayerMana");
        commandNode.put("playerIdx", playerIdx);
        commandNode.set("output", objectMapper.valueToTree(mana));

        output.add(commandNode);
    }
    /**
     * Afiseaza cartile de pe masa.
     *
     */
    public static void getCardsOnTable(final Board board, final ObjectMapper objectMapper,
                                       final ArrayNode output) {
        ArrayList<ArrayList<Minion>> cards = board.getBoard();
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getCardsOnTable");

        ArrayNode rowsArray = objectMapper.createArrayNode();

        for (ArrayList<Minion> row : cards) {
            ArrayNode rowArray = objectMapper.createArrayNode();

            for (Minion card : row) {
                if (card != null) {
                    rowArray.add(objectMapper.valueToTree(card));
                }
            }

            rowsArray.add(rowArray);
        }

        commandNode.set("output", rowsArray);
        output.add(commandNode);
    }
    /**
     * Afisarea cartii de la pozitia x, y primita ca parametru.
     *
     */
    public static void getCardAtPosition(final Board board, final int xCard, final int yCard,
                                         final ObjectMapper objectMapper, final ArrayNode output) {
        Minion card = board.getBoard().get(xCard).get(yCard);
        if (card == null) {
            ObjectNode commandNode = objectMapper.createObjectNode();
            commandNode.put("command", "getCardAtPosition");
            commandNode.set("x", objectMapper.valueToTree(xCard));
            commandNode.set("y", objectMapper.valueToTree(yCard));
            commandNode.put("output", "No card available at that position.");
            output.add(commandNode);
        } else {
            ObjectNode commandNode = objectMapper.createObjectNode();
            commandNode.put("command", "getCardAtPosition");
            commandNode.put("x", xCard);
            commandNode.put("y", yCard);
            commandNode.put("output", objectMapper.valueToTree(card));
            output.add(commandNode);
        }
    }
    /**
     * Atacul unei carti asupra altei carti.
     *
     */
    public static void cardUsesAttack(final Board board, final Coordinates cardAttacker,
                                      final Coordinates cardAttacked,
                                      final Player playerOne, final Player playerTwo,
                                      final ObjectMapper objectMapper, final ArrayNode output) {
        ArrayList<ArrayList<Minion>> cards = board.getBoard();
        int xAttacker = cardAttacker.getX();
        int yAttacker = cardAttacker.getY();
        int xAttacked = cardAttacked.getX();
        int yAttacked = cardAttacked.getY();
        if (xAttacker == Constants.ROW3.getValue()
                || xAttacker == Constants.ROW2.getValue()) {
            if (board.cardAttack(xAttacker, yAttacker,
                    xAttacked, yAttacked, playerOne) == 0) {
                playerOne.addCardUsed(cards.get(xAttacker).get(yAttacker));
            } else {
                int result = board.cardAttack(xAttacker, yAttacker,
                        xAttacked, yAttacked, playerOne);
                ObjectNode commandNode = objectMapper.createObjectNode();
                commandNode.put("command", "cardUsesAttack");
                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                if (result == Constants.ERROR1.getValue()) {
                    commandNode.put("error", "Attacked card does not "
                            + "belong to the enemy.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR2.getValue()) {
                    commandNode.put("error", "Attacker card has already "
                            + "attacked this turn.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR3.getValue()) {
                    commandNode.put("error", "Attacker card is frozen.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR4.getValue()) {
                    commandNode.put("error", "Attacked card is not of type 'Tank'.");
                    output.add(commandNode);
                }
            }
        } else if (xAttacker == 0 || xAttacker == 1) {
            if (board.cardAttack(xAttacker, yAttacker,
                    xAttacked, yAttacked, playerTwo) == 0) {
                playerTwo.addCardUsed(cards.get(xAttacker).get(yAttacker));
            } else {
                int result = board.cardAttack(xAttacker, yAttacker,
                        xAttacked, yAttacked, playerTwo);
                ObjectNode commandNode = objectMapper.createObjectNode();
                commandNode.put("command", "cardUsesAttack");
                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                if (result == Constants.ERROR1.getValue()) {
                    commandNode.put("error", "Attacked card does "
                            + "not belong to the enemy.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR2.getValue()) {
                    commandNode.put("error", "Attacker card has "
                            + "already attacked this turn.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR3.getValue()) {
                    commandNode.put("error", "Attacker card is frozen.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR4.getValue()) {
                    commandNode.put("error", "Attacked card is not of type 'Tank'.");
                    output.add(commandNode);
                }
            }
        }
    }
    /**
     * Afiseaza cartile inghetate de pe masa.
     *
     */
    public static void getFrozenCardsOnTable(final Board board,
                                             final Player playerOne,
                                             final Player playerTwo,
                                             final ObjectMapper objectMapper,
                                             final ArrayNode output) {
        ArrayList<ArrayList<Minion>> cards = board.getBoard();
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getFrozenCardsOnTable");

        ArrayNode outputArray = objectMapper.createArrayNode();
        for (ArrayList<Minion> row : cards) {
            for (Minion card : row) {
                if (card != null && (playerOne.findCard(card) == 1
                        || playerTwo.findCard(card) == 1)) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("attackDamage", card.getAttackDamage());
                    cardNode.put("health", card.getHealth());
                    cardNode.put("mana", card.getMana());
                    cardNode.put("name", card.getName());
                    cardNode.put("description", card.getDescription());

                    ArrayNode colorsArray = objectMapper.createArrayNode();
                    for (String color : card.getColors()) {
                        colorsArray.add(color);
                    }
                    cardNode.set("colors", colorsArray);

                    outputArray.add(cardNode);
                }
            }
        }
        commandNode.set("output", outputArray);
        output.add(commandNode);
    }
    /**
     * Utilizarea abilitatii unui erou asupra unui rand de carti de pe masa.
     *
     */
    public static void useHeroAbility(final Board board, final int affectedRow,
                                      final int playerTurn,
                                      final Hero heroOne, final Hero heroTwo,
                                      final Player playerOne,
                                      final Player playerTwo, final ObjectMapper objectMapper,
                                      final ArrayNode output) {
        Player player;
        Player playerAttacked;
        Hero heroAttacker;
        if (playerTurn == 1) {
            heroAttacker = heroOne;
            player = playerOne;
            playerAttacked = playerTwo;
        } else {
            player = playerTwo;
            heroAttacker = heroTwo;
            playerAttacked = playerOne;
        }
        int resulted = board.useHeroAbility(heroAttacker,
                affectedRow, player, playerAttacked);
        if (resulted == 0) {
            player.setHeroUsed(1);
        } else {
            ObjectNode commandNode = objectMapper.createObjectNode();
            commandNode.put("command", "useHeroAbility");
            commandNode.put("affectedRow", affectedRow);
            if (resulted == Constants.ERROR1.getValue()) {
                commandNode.put("error", "Not enough mana to use hero's ability.");
                output.add(commandNode);
            } else if (resulted == Constants.ERROR2.getValue()) {
                commandNode.put("error", "Hero has already attacked this turn.");
                output.add(commandNode);
            } else if (resulted == Constants.ERROR3.getValue()) {
                commandNode.put("error", "Selected row does not belong to the enemy.");
                output.add(commandNode);
            } else if (resulted == Constants.ERROR4.getValue()) {
                commandNode.put("error", "Selected row does not "
                        + "belong to the current player.");
                output.add(commandNode);
            }
        }
    }
    /**
     * Utilizarea abilitatii unei carti asupra altei carti de pe masa.
     *
     */
    public static void cardUsesAbility(final Board board, final Coordinates cardAttacker,
                                       final Coordinates cardAttacked,
                                       final Player playerOne, final Player playerTwo,
                                       final ObjectMapper objectMapper, final ArrayNode output) {
        ArrayList<ArrayList<Minion>> cards = board.getBoard();
        int xAttacker = cardAttacker.getX();
        int yAttacker = cardAttacker.getY();
        int xAttacked = cardAttacked.getX();
        int yAttacked = cardAttacked.getY();
        if (xAttacker == Constants.ROW3.getValue()
                || xAttacker == Constants.ROW2.getValue()) {
            if (board.cardUseAbility(xAttacker, yAttacker,
                    xAttacked, yAttacked, playerOne) == 0) {
                playerOne.addCardUsed(cards.get(xAttacker).get(yAttacker));
            } else {
                ObjectNode commandNode = objectMapper.createObjectNode();
                commandNode.put("command", "cardUsesAbility");
                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                int result = board.cardUseAbility(xAttacker, yAttacker,
                        xAttacked, yAttacked, playerOne);
                if (result == Constants.ERROR1.getValue()) {
                    commandNode.put("error", "Attacker card is frozen.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR2.getValue()) {
                    commandNode.put("error", "Attacker card has already "
                            + "attacked this turn.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR3.getValue()) {
                    commandNode.put("error", "Attacked card does not "
                            + "belong to the current player.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR4.getValue()) {
                    commandNode.put("error", "Attacked card does not "
                            + "belong to the enemy.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR5.getValue()) {
                    commandNode.put("error", "Attacked card is not of type 'Tank'.");
                    output.add(commandNode);
                }
            }
        } else if (xAttacker == 0 || xAttacker == 1) {
            if (board.cardUseAbility(xAttacker, yAttacker,
                    xAttacked, yAttacked, playerTwo) == 0) {
                playerTwo.addCardUsed(cards.get(xAttacker).get(yAttacker));
            } else {
                ObjectNode commandNode = objectMapper.createObjectNode();
                commandNode.put("command", "cardUsesAbility");
                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                int result = board.cardUseAbility(xAttacker, yAttacker,
                        xAttacked, yAttacked, playerTwo);
                if (result == Constants.ERROR1.getValue()) {
                    commandNode.put("error", "Attacker card is frozen.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR2.getValue()) {
                    commandNode.put("error", "Attacker card has "
                            + "already attacked this turn.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR3.getValue()) {
                    commandNode.put("error", "Attacked card does "
                            + "not belong to the current player.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR4.getValue()) {
                    commandNode.put("error", "Attacked card "
                            + "does not belong to the enemy.");
                    output.add(commandNode);
                } else if (result == Constants.ERROR5.getValue()) {
                    commandNode.put("error", "Attacked card is not of type 'Tank'.");
                    output.add(commandNode);
                }
            }
        }
    }
    /**
     * Se executa un atac asupra eroului adversar.
     *
     */
    public static void useAttackHero(final Board board, final Player playerOne,
                                     final Player playerTwo, final Coordinates cardAttacker,
                                     final Initials initials, final Game game,
                                     final ObjectMapper objectMapper, final ArrayNode output) {
        ArrayList<ArrayList<Minion>> cards = board.getBoard();
        int xAttacker = cardAttacker.getX();
        int yAttacker = cardAttacker.getY();
        int resulted;
        Player playerAttacker;
        if (xAttacker == 0 || xAttacker == 1) {
            playerAttacker = playerTwo;
            resulted = board.heroAttack(xAttacker, yAttacker, playerOne.getHero(), playerTwo);
        } else {
            playerAttacker = playerOne;
            resulted = board.heroAttack(xAttacker, yAttacker, playerTwo.getHero(), playerOne);
        }
        if (resulted == 0) {
            playerAttacker.addCardUsed(cards.get(xAttacker).get(yAttacker));
        } else {
            if (resulted == -1) {
                ObjectNode commandNode = objectMapper.createObjectNode();
                if (playerAttacker == playerOne) {
                    commandNode.put("gameEnded", "Player one "
                            + "killed the enemy hero.");
                    output.add(commandNode);
                    game.setStatus(1);
                    int gamesPlayed = initials.getGamesPlayed();
                    gamesPlayed++;
                    initials.setGamesPlayed(gamesPlayed);
                    game.setGamesPlayed(initials.getGamesPlayed());
                    int playerOneWins = initials.getPlayerOneWins();
                    playerOneWins++;
                    initials.setPlayerOneWins(playerOneWins);
                    playerOne.setPlayerWins(initials.getPlayerOneWins());
                } else {
                    commandNode.put("gameEnded", "Player two "
                            + "killed the enemy hero.");
                    output.add(commandNode);
                    game.setStatus(1);
                    int gamesPlayed = initials.getGamesPlayed();
                    gamesPlayed++;
                    initials.setGamesPlayed(gamesPlayed);
                    int playerTwoWins = initials.getPlayerTwoWins();
                    playerTwoWins++;
                    initials.setPlayerTwoWins(playerTwoWins);
                    playerTwo.setPlayerWins(initials.getPlayerTwoWins());
                }
            } else {
                ObjectNode commandNode = objectMapper.createObjectNode();
                commandNode.put("command", "useAttackHero");
                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                if (resulted == Constants.ERROR1.getValue()) {
                    commandNode.put("error", "Attacker card is frozen.");
                    output.add(commandNode);
                } else if (resulted == Constants.ERROR2.getValue()) {
                    commandNode.put("error", "Attacker card has "
                            + "already attacked this turn.");
                    output.add(commandNode);
                } else if (resulted == Constants.ERROR3.getValue()) {
                    commandNode.put("error", "Attacked card "
                            + "is not of type 'Tank'.");
                    output.add(commandNode);
                }
            }
        }
    }
    /**
     * Se termina tura jucatorului curent.
     *
     */
    public static void endPlayerTurn(final Game game, final Player playerOne,
                                     final Player playerTwo,
                                     final ArrayList<CardInput> deckOne,
                                     final ArrayList<CardInput> deckTwo) {
        if (game.getPlayerTurn() == 1) {
            int turnOne = game.getTurnOne();
            turnOne++;
            game.setTurnOne(turnOne);
            game.setPlayerTurn(2);
            playerOne.setHeroUsed(0);
            if (!playerOne.getFrozenCards().isEmpty()) {
                playerOne.getFrozenCards().clear();
            }
            if (!playerOne.getStillFrozenCards().isEmpty()) {
                for (Minion card : playerOne.getStillFrozenCards()) {
                    playerOne.addCardFrozen(card);
                }
                playerOne.getStillFrozenCards().clear();
            }
        } else if (game.getPlayerTurn() == 2) {
            int turnTwo = game.getTurnTwo();
            turnTwo++;
            game.setTurnTwo(turnTwo);
            game.setPlayerTurn(1);
            playerTwo.setHeroUsed(0);
            if (!playerTwo.getFrozenCards().isEmpty()) {
                playerTwo.getFrozenCards().clear();
            }
            if (!playerTwo.getStillFrozenCards().isEmpty()) {
                for (Minion card : playerTwo.getStillFrozenCards()) {
                    playerTwo.addCardFrozen(card);
                }
                playerTwo.getStillFrozenCards().clear();
            }
        }
        if (game.getTurnOne() == 1 && game.getTurnTwo() == 1) {
            int round = game.getRound();
            round++;
            game.setRound(round);
            int manaOne = playerOne.getMana();
            int manaTwo = playerTwo.getMana();
            if (round > Constants.MAXIMMANA.getValue()) {
                manaOne = manaOne + Constants.MAXIMMANA.getValue();
                manaTwo = manaTwo + Constants.MAXIMMANA.getValue();
            } else {
                manaOne = manaOne + round;
                manaTwo = manaTwo + round;
            }
            playerOne.setMana(manaOne);
            playerTwo.setMana(manaTwo);
            if (deckOne.size() > 0) {
                CardInput cardInput = deckOne.get(0);
                Minion card = new Minion(cardInput);
                playerOne.addCardInHand(card);
                playerOne.removeCardDeck(deckOne.get(0));
            }
            if (deckTwo.size() > 0) {
                CardInput cardInput = deckTwo.get(0);
                Minion card = new Minion(cardInput);
                playerTwo.addCardInHand(card);
                playerTwo.removeCardDeck(deckTwo.get(0));
            }
            game.setTurnOne(0);
            game.setTurnTwo(0);
            if (!playerOne.getCardsUsed().isEmpty()) {
                playerOne.getCardsUsed().clear();
            }
            if (!playerTwo.getCardsUsed().isEmpty()) {
                playerTwo.getCardsUsed().clear();
            }
        }
    }
    /**
     * Se afiseaza numarul de jocuri jucate in total.
     *
     */
    public static void getTotalGamesPlayed(final Initials initials, final ObjectMapper
            objectMapper, final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getTotalGamesPlayed");
        commandNode.put("output", initials.getGamesPlayed());
        output.add(commandNode);
    }
    /**
     * Se afiseaza numarul de castiguri ale jucatorului 1.
     *
     */
    public static void getPlayerOneWins(final Player player, final ObjectMapper
            objectMapper, final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getPlayerOneWins");
        commandNode.put("output", player.getPlayerWins());
        output.add(commandNode);
    }
    /**
     * Se afiseaza numarul de castiguri ale jucatorului 2.
     *
     */
    public static void getPlayerTwoWins(final Player player, final ObjectMapper
            objectMapper, final ArrayNode output) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "getPlayerTwoWins");
        commandNode.put("output", player.getPlayerWins());
        output.add(commandNode);
    }
}
