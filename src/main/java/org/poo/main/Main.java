package org.poo.main;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Collections;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         * ObjectMapper mapper = new ObjectMapper();
         *
         * ObjectNode objectNode = mapper.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = mapper.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */
        DecksInput decksInputPlayerOne = inputData.getPlayerOneDecks();
        DecksInput decksInputPlayerTwo = inputData.getPlayerTwoDecks();
        int nrCardsInDeckOne = decksInputPlayerOne.getNrCardsInDeck();
        int nrCardsInDeckTwo = decksInputPlayerTwo.getNrCardsInDeck();
        int nrDecksOne = decksInputPlayerOne.getNrDecks();
        int nrDecksTwo = decksInputPlayerTwo.getNrDecks();
        ArrayList<ArrayList<CardInput>> decksOne = decksInputPlayerOne.getDecks();
        ArrayList<ArrayList<CardInput>> decksTwo = decksInputPlayerTwo.getDecks();
        ArrayList<GameInput> games = inputData.getGames();
        Board board = new Board(Constants.ROWS.getValue(), Constants.COLUMNS.getValue());
        int gamesPlayed = 0;
        int playerOneWins = 0;
        int playerTwoWins = 0;
        for (int i = 0; i < games.size(); i++) {
            StartGameInput startGame = games.get(i).getStartGame();
            int playerOneDeckIdx = startGame.getPlayerOneDeckIdx();
            int playerTwoDeckIdx = startGame.getPlayerTwoDeckIdx();
            ArrayList<CardInput> deckOne = decksOne.get(playerOneDeckIdx);
            ArrayList<CardInput> deckTwo = decksTwo.get(playerTwoDeckIdx);
            int shuffleSeed = startGame.getShuffleSeed();
            CardInput playerOneHero = startGame.getPlayerOneHero();
            CardInput playerTwoHero = startGame.getPlayerTwoHero();
            Hero heroOne = new Hero(playerOneHero);
            Hero heroTwo = new Hero(playerTwoHero);
            Player playerOne = new Player(1, deckOne, playerOneHero, playerOneWins);
            Player playerTwo = new Player(2, deckTwo, playerTwoHero, playerTwoWins);
            playerOne.setMana(1);
            playerTwo.setMana(1);
            int round = 1;
            int startingPlayer = startGame.getStartingPlayer();
            int status = 0;
            Random random1 = new Random(shuffleSeed);
            Collections.shuffle(deckOne, random1);
            Random random2 = new Random(shuffleSeed);
            Collections.shuffle(deckTwo, random2);
            if (deckOne.size() > 0) {
                playerOne.addCardInHand((deckOne.get(0)));
                playerOne.removeCardDeck(deckOne.get(0));
            }
            if (deckTwo.size() > 0) {
                playerTwo.addCardInHand((deckTwo.get(0)));
                playerTwo.removeCardDeck(deckTwo.get(0));
            }
            ArrayList<ActionsInput> actions = games.get(i).getActions();
            int playerTurn = startingPlayer;
            int turnOne = 0;
            int turnTwo = 0;
            for (int j = 0; j < actions.size(); j++) {
                String command = actions.get(j).getCommand();
                int playerIdx = actions.get(j).getPlayerIdx();
                int handIdx = actions.get(j).getHandIdx();
                Coordinates cardAttacker = actions.get(j).getCardAttacker();
                Coordinates cardAttacked = actions.get(j).getCardAttacked();
                int xCard = actions.get(j).getX();
                int yCard = actions.get(j).getY();
                int affectedRow = actions.get(j).getAffectedRow();
                if (playerIdx == 1) {
                    if (command.equals("getPlayerDeck")) {
                        ArrayList<CardInput> deck = playerOne.getPlayerDeck();
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerDeck");
                        commandNode.put("playerIdx", playerIdx);

                        ArrayNode cardsArray = objectMapper.createArrayNode();
                        for (CardInput cardInput : deck) {
                            cardsArray.add(objectMapper.valueToTree(cardInput));
                        }

                        commandNode.set("output", cardsArray);
                        output.add(commandNode);
                    } else if (command.equals("getPlayerHero")) {
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerHero");
                        commandNode.put("playerIdx", playerIdx);
                        commandNode.set("output", objectMapper.valueToTree(heroOne));
                        output.add(commandNode);
                    }
                } else if (playerIdx == 2) {
                    if (command.equals("getPlayerDeck")) {
                        ArrayList<CardInput> deck = playerTwo.getPlayerDeck();
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerDeck");
                        commandNode.put("playerIdx", playerIdx);
                        ArrayNode cardsArray = objectMapper.createArrayNode();
                        for (CardInput cardInput : deck) {
                            cardsArray.add(objectMapper.valueToTree(cardInput));
                        }

                        commandNode.set("output", cardsArray);

                        output.add(commandNode);
                    } else if (command.equals("getPlayerHero")) {
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerHero");
                        commandNode.put("playerIdx", playerIdx);
                        commandNode.set("output", objectMapper.valueToTree(heroTwo));

                        output.add(commandNode);
                    }

                }
                if (command.equals("getPlayerTurn")) {
                    ObjectNode commandNode = objectMapper.createObjectNode();
                    commandNode.put("command", "getPlayerTurn");
                    commandNode.set("output", objectMapper.valueToTree(playerTurn));

                    output.add(commandNode);
                }
                if (command.equals("endPlayerTurn")) {
                    if (status == 1) {
                        continue;
                    }
                    if (playerTurn == 1) {
                        turnOne++;
                        playerTurn = 2;
                        playerOne.setHeroUsed(0);
                        if (!playerOne.getFrozenCards().isEmpty()) {
                            playerOne.getFrozenCards().clear();
                        }
                        if (!playerOne.getStillFrozenCards().isEmpty()) {
                            for (CardInput card : playerOne.getStillFrozenCards()) {
                                playerOne.addCardFrozen(card);
                            }
                            playerOne.getStillFrozenCards().clear();
                        }
                    } else if (playerTurn == 2) {
                        turnTwo++;
                        playerTurn = 1;
                        playerTwo.setHeroUsed(0);
                        if (!playerTwo.getFrozenCards().isEmpty()) {
                            playerTwo.getFrozenCards().clear();
                        }
                        if (!playerTwo.getStillFrozenCards().isEmpty()) {
                            for (CardInput card : playerTwo.getStillFrozenCards()) {
                                playerTwo.addCardFrozen(card);
                            }
                            playerTwo.getStillFrozenCards().clear();
                        }
                    }
                    if (turnOne == 1 && turnTwo == 1) {
                        round++;
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
                            playerOne.addCardInHand(deckOne.get(0));
                            playerOne.removeCardDeck(deckOne.get(0));
                        }
                        if (deckTwo.size() > 0) {
                            playerTwo.addCardInHand(deckTwo.get(0));
                            playerTwo.removeCardDeck(deckTwo.get(0));
                        }
                        turnOne = 0;
                        turnTwo = 0;
                        if (!playerOne.getCardsUsed().isEmpty()) {
                            playerOne.getCardsUsed().clear();
                        }
                        if (!playerTwo.getCardsUsed().isEmpty()) {
                            playerTwo.getCardsUsed().clear();
                        }
                    }
                }
                if (command.equals("getCardsInHand")) {
                    if (playerIdx == 1) {
                        ArrayList<CardInput> cards = playerOne.getCardsInHand();
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getCardsInHand");
                        commandNode.put("playerIdx", playerIdx);
                        ArrayNode cardsArray = objectMapper.createArrayNode();
                        for (CardInput cardInput : cards) {
                            cardsArray.add(objectMapper.valueToTree(cardInput));
                        }

                        commandNode.set("output", cardsArray);

                        output.add(commandNode);
                    } else if (playerIdx == 2) {
                        ArrayList<CardInput> cards = playerTwo.getCardsInHand();
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getCardsInHand");
                        commandNode.put("playerIdx", playerIdx);
                        ArrayNode cardsArray = objectMapper.createArrayNode();
                        for (CardInput card : cards) {
                            cardsArray.add(objectMapper.valueToTree(card));
                        }

                        commandNode.set("output", cardsArray);

                        output.add(commandNode);
                    }
                }
                if (command.equals("placeCard")) {
                    if (status == 1) {
                        continue;
                    }
                    if (playerTurn == 1) {
                        ArrayList<CardInput> cards = playerOne.getCardsInHand();
                        if (!cards.isEmpty() && handIdx >= 0 && handIdx < cards.size()) {
                            CardInput card = cards.get(handIdx);
                            int result = board.placeOnBoard(card, playerOne);
                            if (result == 0) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "placeCard");
                                commandNode.put("handIdx", handIdx);
                                commandNode.put("error", "Not enough mana to place card on table.");
                                output.add(commandNode);
                            } else if (result == 2) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "placeCard");
                                commandNode.put("handIdx", handIdx);
                                commandNode.put("error", "Cannot place card on table since row "
                                        + "is full.");
                                output.add(commandNode);
                            } else if (result == 1) {
                                playerOne.removeCardInHand(card);
                            }

                        }
                    } else if (playerTurn == 2) {
                        ArrayList<CardInput> cards = playerTwo.getCardsInHand();
                        if (!cards.isEmpty() && handIdx >= 0 && handIdx < cards.size()) {
                            CardInput card = cards.get(handIdx);
                            int result = board.placeOnBoard(card, playerTwo);
                            if (result == 0) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "placeCard");
                                commandNode.put("handIdx", handIdx);
                                commandNode.put("error", "Not enough mana to place card on table.");
                                output.add(commandNode);
                            } else if (result == 2) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "placeCard");
                                commandNode.put("handIdx", handIdx);
                                commandNode.put("error", "Cannot place card on "
                                        + "table since row is full.");
                                output.add(commandNode);
                            } else {
                                playerTwo.removeCardInHand(card);
                            }

                        }
                    }
                }
                if (command.equals("getPlayerMana")) {
                    if (playerIdx == 1) {
                        int mana = playerOne.getMana();
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerMana");
                        commandNode.put("playerIdx", playerIdx);
                        commandNode.set("output", objectMapper.valueToTree(mana));

                        output.add(commandNode);
                    } else if (playerIdx == 2) {
                        int mana = playerTwo.getMana();
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerMana");
                        commandNode.put("playerIdx", playerIdx);
                        commandNode.set("output", objectMapper.valueToTree(mana));

                        output.add(commandNode);
                    }
                }
                if (command.equals("getCardsOnTable")) {
                    ArrayList<ArrayList<CardInput>> cards = board.getBoard();
                    ObjectNode commandNode = objectMapper.createObjectNode();
                    commandNode.put("command", "getCardsOnTable");

                    ArrayNode rowsArray = objectMapper.createArrayNode();

                    for (ArrayList<CardInput> row : cards) {
                        ArrayNode rowArray = objectMapper.createArrayNode();

                        for (CardInput cardInput : row) {
                            if (cardInput != null) {
                                rowArray.add(objectMapper.valueToTree(cardInput));
                            }
                        }

                        rowsArray.add(rowArray);  // Adăugăm fiecare rând în `rowsArray`
                    }

                    commandNode.set("output", rowsArray);
                    output.add(commandNode);
                }
                if (command.equals("cardUsesAttack")) {
                    if (status == 1) {
                        continue;
                    }
                    ArrayList<ArrayList<CardInput>> cards = board.getBoard();
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
                            }
                            if (result == Constants.ERROR2.getValue()) {
                                commandNode.put("error", "Attacker card has already "
                                        + "attacked this turn.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR3.getValue()) {
                                commandNode.put("error", "Attacker card is frozen.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR4.getValue()) {
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
                            }
                            if (result == Constants.ERROR2.getValue()) {
                                commandNode.put("error", "Attacker card has "
                                        + "already attacked this turn.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR3.getValue()) {
                                commandNode.put("error", "Attacker card is frozen.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR4.getValue()) {
                                commandNode.put("error", "Attacked card is not of type 'Tank'.");
                                output.add(commandNode);
                            }
                        }
                    }
                }
                if (command.equals("getCardAtPosition")) {
                    CardInput card = board.getBoard().get(xCard).get(yCard);
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
                if (command.equals("cardUsesAbility")) {
                    if (status == 1) {
                        continue;
                    }
                    ArrayList<ArrayList<CardInput>> cards = board.getBoard();
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
                            }
                            if (result == Constants.ERROR2.getValue()) {
                                commandNode.put("error", "Attacker card has already "
                                        + "attacked this turn.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR3.getValue()) {
                                commandNode.put("error", "Attacked card does not "
                                        + "belong to the current player.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR4.getValue()) {
                                commandNode.put("error", "Attacked card does not "
                                        + "belong to the enemy.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR5.getValue()) {
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
                            }
                            if (result == Constants.ERROR2.getValue()) {
                                commandNode.put("error", "Attacker card has "
                                        + "already attacked this turn.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR3.getValue()) {
                                commandNode.put("error", "Attacked card does "
                                        + "not belong to the current player.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR4.getValue()) {
                                commandNode.put("error", "Attacked card "
                                        + "does not belong to the enemy.");
                                output.add(commandNode);
                            }
                            if (result == Constants.ERROR5.getValue()) {
                                commandNode.put("error", "Attacked card is not of type 'Tank'.");
                                output.add(commandNode);
                            }
                        }
                    }
                }
                if (command.equals("useAttackHero")) {
                    if (status == 1) {
                        continue;
                    }
                    ArrayList<ArrayList<CardInput>> cards = board.getBoard();
                    int xAttacker = cardAttacker.getX();
                    int yAttacker = cardAttacker.getY();
                    int resulted;
                    Player playerAttacker;
                    if (xAttacker == 0 || xAttacker == 1) {
                        playerAttacker = playerTwo;
                        resulted = board.HeroAttack(xAttacker, yAttacker, heroOne, playerTwo);
                    } else {
                        playerAttacker = playerOne;
                        resulted = board.HeroAttack(xAttacker, yAttacker, heroTwo, playerOne);
                    }
                    if (resulted == 0) {
                        playerAttacker.addCardUsed(cards.get(xAttacker).get(yAttacker));
                    } else {
                        if (resulted == -1) {
                            ObjectNode commandNode = objectMapper.createObjectNode();
                            if (playerAttacker == playerOne) {
                                commandNode.put("gameEnded", "Player one killed the enemy hero.");
                                output.add(commandNode);
                                status = 1;
                                gamesPlayed++;
                                playerOneWins++;
                                continue;
                            } else {
                                commandNode.put("gameEnded", "Player two killed the enemy hero.");
                                output.add(commandNode);
                                status = 1;
                                gamesPlayed++;
                                playerTwoWins++;
                                continue;
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
                                commandNode.put("error", "Attacked card is not of type 'Tank'.");
                                output.add(commandNode);
                            }
                        }
                    }
                }
                if (command.equals("useHeroAbility")) {
                    if (status == 1) {
                        continue;
                    }
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
                    int resulted = board.UseHeroAbility(heroAttacker,
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
                if (command.equals("getFrozenCardsOnTable")) {
                    ArrayList<ArrayList<CardInput>> cards = board.getBoard();
                    ObjectNode commandNode = objectMapper.createObjectNode();
                    commandNode.put("command", "getFrozenCardsOnTable");

                    ArrayNode outputArray = objectMapper.createArrayNode();
                    for (ArrayList<CardInput> row : cards) {
                        for (CardInput cardInput : row) {
                            if (cardInput != null && (playerOne.findCard(cardInput) == 1
                                    || playerTwo.findCard(cardInput) == 1)) {
                                ObjectNode cardNode = objectMapper.createObjectNode();
                                cardNode.put("attackDamage", cardInput.getAttackDamage());
                                cardNode.put("health", cardInput.getHealth());
                                cardNode.put("mana", cardInput.getMana());
                                cardNode.put("name", cardInput.getName());
                                cardNode.put("description", cardInput.getDescription());

                                ArrayNode colorsArray = objectMapper.createArrayNode();
                                for (String color : cardInput.getColors()) {
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
                if (command.equals("getTotalGamesPlayed")) {
                    ObjectNode commandNode = objectMapper.createObjectNode();
                    commandNode.put("command", "getTotalGamesPlayed");
                    commandNode.put("output", gamesPlayed);
                    output.add(commandNode);
                }
                if (command.equals("getPlayerOneWins")) {
                    ObjectNode commandNode = objectMapper.createObjectNode();
                    commandNode.put("command", "getPlayerOneWins");
                    commandNode.put("output", playerOneWins);
                    output.add(commandNode);
                }
                if (command.equals("getPlayerTwoWins")) {
                    ObjectNode commandNode = objectMapper.createObjectNode();
                    commandNode.put("command", "getPlayerTwoWins");
                    commandNode.put("output", playerTwoWins);
                    output.add(commandNode);
                }

            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
