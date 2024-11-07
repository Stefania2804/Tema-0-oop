package main;

import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import checker.CheckerConstants;
import fileio.*;

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
        DecksInput decksInput_player_one = inputData.getPlayerOneDecks();
        DecksInput decksInput_player_two = inputData.getPlayerTwoDecks();
        int nrCardsInDeck_one = decksInput_player_one.getNrCardsInDeck();
        int nrCardsInDeck_two = decksInput_player_two.getNrCardsInDeck();
        int nrDecks_one = decksInput_player_one.getNrDecks();
        int nrDecks_two = decksInput_player_two.getNrDecks();
        ArrayList<ArrayList<CardInput>> decks_one = decksInput_player_one.getDecks();
        ArrayList<ArrayList<CardInput>> decks_two = decksInput_player_two.getDecks();
        ArrayList<GameInput> games = inputData.getGames();
        for (int i = 0; i < games.size(); i++) {
            StartGameInput startGame = games.get(i).getStartGame();
            int playerOneDeckIdx = startGame.getPlayerOneDeckIdx();
            int playerTwoDeckIdx = startGame.getPlayerTwoDeckIdx();
            ArrayList<CardInput> deckOne = decks_one.get(playerOneDeckIdx);
            ArrayList<CardInput> deckTwo = decks_two.get(playerTwoDeckIdx);
            int shuffleSeed = startGame.getShuffleSeed();
            CardInput playerOneHero = startGame.getPlayerOneHero();
            CardInput playerTwoHero = startGame.getPlayerTwoHero();
            Player player_one = new Player(1, deckOne, playerOneHero);
            Player player_two = new Player(2, deckTwo, playerTwoHero);
            player_one.setMana(1);
            player_two.setMana(1);
            int round = 1;
            int startingPlayer = startGame.getStartingPlayer();
            Random random = new Random(shuffleSeed);
            Collections.shuffle(deckOne, random);
            random = new Random(shuffleSeed);
            Collections.shuffle(deckTwo, random);
            if (deckOne.size() > 0) {
                player_one.addCardInHand((deckOne.get(0)));
                player_one.removeCardDeck(deckOne.get(0));
            }
            if (deckTwo.size() > 0) {
                player_two.addCardInHand((deckTwo.get(0)));
                player_two.removeCardDeck(deckTwo.get(0));
            }
            ArrayList<ActionsInput> actions = games.get(i).getActions();
            int player_turn = startingPlayer;
            Board board = new Board(4 ,5);
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
                if (playerIdx == 1) {
                    if (command.equals("getPlayerDeck")) {
                        ArrayList<CardInput> deck = player_one.getPlayerDeck();
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
                        CardInput heroInput = player_one.getHero();
                        Hero hero = new Hero(heroInput);
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerHero");
                        commandNode.put("playerIdx", playerIdx);
                        commandNode.set("output", objectMapper.valueToTree(hero));
                        output.add(commandNode);
                    }
                } else if (playerIdx == 2) {
                    if (command.equals("getPlayerDeck")) {
                        ArrayList<CardInput> deck = player_two.getPlayerDeck();
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
                        CardInput heroInput = player_two.getHero();
                        Hero hero = new Hero(heroInput);
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerHero");
                        commandNode.put("playerIdx", playerIdx);
                        commandNode.set("output", objectMapper.valueToTree(hero));

                        output.add(commandNode);
                    }

                }
                if (command.equals("getPlayerTurn")) {
                    ObjectNode commandNode = objectMapper.createObjectNode();
                    commandNode.put("command", "getPlayerTurn");
                    commandNode.set("output", objectMapper.valueToTree(player_turn));

                    output.add(commandNode);
                }
                if (command.equals("endPlayerTurn")) {
                    if (player_turn == 1) {
                        turnOne++;
                        player_turn = 2;
                    } else if (player_turn == 2) {
                        turnTwo++;
                        player_turn = 1;
                    }
                    if (turnOne == 1 && turnTwo == 1) {
                        round++;
                        int manaOne = player_one.getMana();
                        int manaTwo = player_two.getMana();
                        if (round >= 10) {
                            manaOne = manaOne + 10;
                            manaTwo = manaTwo + 10;
                        } else {
                            manaOne = manaOne + round;
                            manaTwo = manaTwo + round;
                        }
                        player_one.setMana(manaOne);
                        player_two.setMana(manaTwo);
                        if (deckOne.size() > 0) {
                            player_one.addCardInHand(deckOne.get(0));
                            player_one.removeCardDeck(deckOne.get(0));
                        }
                        if (deckTwo.size() > 0) {
                            player_two.addCardInHand(deckTwo.get(0));
                            player_two.removeCardDeck(deckTwo.get(0));
                        }
                        turnOne = 0;
                        turnTwo = 0;
                        if (!player_one.getCardsUsed().isEmpty()) {
                            player_one.getCardsUsed().clear();
                        }
                        if (!player_two.getCardsUsed().isEmpty()) {
                            player_two.getCardsUsed().clear();
                        }
                    }
                }
                if (command.equals("getCardsInHand")) {
                    if (playerIdx == 1) {
                        ArrayList<CardInput> cards = player_one.getCardsInHand();
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
                        ArrayList<CardInput> cards = player_two.getCardsInHand();
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
                    if (player_turn == 1) {
                        ArrayList<CardInput> cards = player_one.getCardsInHand();
                        if (!cards.isEmpty() && handIdx >= 0 && handIdx < cards.size()) {
                            CardInput card = cards.get(handIdx);
                            int result = board.placeOnBoard(card, player_one);
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
                                commandNode.put("error", "Cannot place card on table since row is full.");
                                output.add(commandNode);
                            } else if (result == 1){
                                player_one.removeCardInHand(card);
                            }

                        }
                    } else if (player_turn == 2) {
                        ArrayList<CardInput> cards = player_two.getCardsInHand();
                        if (!cards.isEmpty() && handIdx >= 0 && handIdx < cards.size()) {
                            CardInput card = cards.get(handIdx);
                            int result = board.placeOnBoard(card, player_two);
                            if (result == 0) {
                                handIdx = 0;
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "placeCard");
                                commandNode.put("handIdx", handIdx);
                                commandNode.put("error", "Not enough mana to place card on table.");
                                output.add(commandNode);
                            } else if (result == 2) {
                                handIdx = 0;
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "placeCard");
                                commandNode.put("handIdx", handIdx);
                                commandNode.put("error", "Cannot place card on table since row is full.");
                                output.add(commandNode);
                            } else {
                                player_two.removeCardInHand(card);
                            }

                        }
                    }
                }
                if (command.equals("getPlayerMana")) {
                    if (playerIdx == 1) {
                        int mana = player_one.getMana();
                        ObjectNode commandNode = objectMapper.createObjectNode();
                        commandNode.put("command", "getPlayerMana");
                        commandNode.put("playerIdx", playerIdx);
                        commandNode.set("output", objectMapper.valueToTree(mana));

                        output.add(commandNode);
                    } else if (playerIdx == 2) {
                        int mana = player_two.getMana();
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

                    ArrayNode rowsArray = objectMapper.createArrayNode();  // ArrayNode pentru rânduri

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
                    ArrayList<ArrayList<CardInput>> cards = board.getBoard();
                    int xAttacker = cardAttacker.getX();
                    int yAttacker = cardAttacker.getY();
                    int xAttacked = cardAttacked.getX();
                    int yAttacked = cardAttacked.getY();
                    if (xAttacker == 3 || xAttacker == 2) {
                        if (board.cardAttack(xAttacker, yAttacker, xAttacked, yAttacked, player_one) == 0) {
                            player_one.addCardUsed(cards.get(xAttacker).get(yAttacker));
                        } else {
                            int result = board.cardAttack(xAttacker, yAttacker, xAttacked, yAttacked, player_one);
                            if (result == 1) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "cardUsesAttack");
                                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                                commandNode.put("error", "Attacked card does not belong to the enemy.");
                                output.add(commandNode);
                            }
                            if (result == 2) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "cardUsesAttack");
                                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                                commandNode.put("error", "Attacker card has already attacked this turn.");
                                output.add(commandNode);
                            }
                            if (result == 3) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "cardUsesAttack");
                                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                                commandNode.put("error", "Attacker card is frozen.");
                                output.add(commandNode);
                            }
                            if (result == 4) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "cardUsesAttack");
                                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                                commandNode.put("error", "Attacked card is not of type 'Tank'.");
                                output.add(commandNode);
                            }
                        }
                    } else if (xAttacker == 0 || xAttacker == 1) {
                        if (board.cardAttack(xAttacker, yAttacker, xAttacked, yAttacked, player_two) == 0) {
                            player_two.addCardUsed(cards.get(xAttacker).get(yAttacker));
                        } else {
                            int result = board.cardAttack(xAttacker, yAttacker, xAttacked, yAttacked, player_two);
                            if (result == 1) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "cardUsesAttack");
                                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                                commandNode.put("error", "Attacked card does not belong to the enemy.");
                                output.add(commandNode);
                            }
                            if (result == 2) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "cardUsesAttack");
                                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                                commandNode.put("error", "Attacker card has already attacked this turn.");
                                output.add(commandNode);
                            }
                            if (result == 3) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "cardUsesAttack");
                                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
                                commandNode.put("error", "Attacker card is frozen.");
                                output.add(commandNode);
                            }
                            if (result == 4) {
                                ObjectNode commandNode = objectMapper.createObjectNode();
                                commandNode.put("command", "cardUsesAttack");
                                commandNode.set("cardAttacker", objectMapper.valueToTree(cardAttacker));
                                commandNode.set("cardAttacked", objectMapper.valueToTree(cardAttacked));
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
            }
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            objectWriter.writeValue(new File(filePath2), output);
        }
    }
}
