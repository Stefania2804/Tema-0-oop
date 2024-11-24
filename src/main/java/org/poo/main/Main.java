package org.poo.main;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.card.Hero;
import org.poo.card.Minion;
import org.poo.game.Board;
import org.poo.game.Game;
import org.poo.game.Initials;
import org.poo.game.JsonOutput;
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
        DecksInput decksInputPlayerOne = inputData.getPlayerOneDecks();
        DecksInput decksInputPlayerTwo = inputData.getPlayerTwoDecks();
        ArrayList<ArrayList<CardInput>> decksOne = decksInputPlayerOne.getDecks();
        ArrayList<ArrayList<CardInput>> decksTwo = decksInputPlayerTwo.getDecks();
        ArrayList<GameInput> games = inputData.getGames();
        Initials initials = new Initials();
        JsonOutput jsonOutput = new JsonOutput();
        for (int i = 0; i < games.size(); i++) {
            Board board = new Board(Constants.ROWS.getValue(), Constants.COLUMNS.getValue());
            StartGameInput startGame = games.get(i).getStartGame();
            int startingPlayer = startGame.getStartingPlayer();
            Game game = new Game(initials.getGamesPlayed(), startingPlayer);
            int playerOneDeckIdx = startGame.getPlayerOneDeckIdx();
            int playerTwoDeckIdx = startGame.getPlayerTwoDeckIdx();
            ArrayList<CardInput> deckOne = new ArrayList<>(decksOne.get(playerOneDeckIdx));
            ArrayList<CardInput> deckTwo = new ArrayList<>(decksTwo.get(playerTwoDeckIdx));
            int shuffleSeed = startGame.getShuffleSeed();
            CardInput playerOneHero = new CardInput();
            playerOneHero = startGame.getPlayerOneHero();
            CardInput playerTwoHero = new CardInput();
            playerTwoHero = startGame.getPlayerTwoHero();
            Hero heroOne = new Hero(playerOneHero);
            Hero heroTwo = new Hero(playerTwoHero);
            Player playerOne = new Player(1, deckOne, heroOne, initials.getPlayerOneWins());
            Player playerTwo = new Player(2, deckTwo, heroTwo, initials.getPlayerTwoWins());
            playerOne.setMana(1);
            playerTwo.setMana(1);
            Random random1 = new Random(shuffleSeed);
            Collections.shuffle(deckOne, random1);
            Random random2 = new Random(shuffleSeed);
            Collections.shuffle(deckTwo, random2);
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
            ArrayList<ActionsInput> actions = games.get(i).getActions();
            for (int j = 0; j < actions.size(); j++) {
                String command = actions.get(j).getCommand();
                int playerIdx = actions.get(j).getPlayerIdx();
                int handIdx = actions.get(j).getHandIdx();
                Coordinates cardAttacker = actions.get(j).getCardAttacker();
                Coordinates cardAttacked = actions.get(j).getCardAttacked();
                int xCard = actions.get(j).getX();
                int yCard = actions.get(j).getY();
                int affectedRow = actions.get(j).getAffectedRow();
                switch (playerIdx) {
                    case 1:
                        switch (command) {
                            case "getPlayerDeck":
                                jsonOutput.getPlayerDeck(playerOne, playerIdx, objectMapper,
                                        output);
                                break;
                            case "getPlayerHero":
                                jsonOutput.getPlayerHero(playerOne, playerIdx, objectMapper,
                                        output);
                                break;
                            case "getCardsInHand":
                                jsonOutput.getCardsInHand(playerOne, playerIdx, objectMapper,
                                        output);
                                break;
                            case "getPlayerMana":
                                jsonOutput.getPlayerMana(playerOne, playerIdx, objectMapper,
                                        output);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2:
                        switch (command) {
                            case "getPlayerDeck":
                                jsonOutput.getPlayerDeck(playerTwo, playerIdx, objectMapper,
                                        output);
                                break;
                            case "getPlayerHero":
                                jsonOutput.getPlayerHero(playerTwo, playerIdx, objectMapper,
                                        output);
                                break;
                            case "getCardsInHand":
                                jsonOutput.getCardsInHand(playerTwo, playerIdx, objectMapper,
                                        output);
                                break;
                            case "getPlayerMana":
                                jsonOutput.getPlayerMana(playerTwo, playerIdx, objectMapper,
                                        output);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                switch (command) {
                    case "getPlayerTurn":
                        jsonOutput.getPlayerTurn(game.getPlayerTurn(), objectMapper, output);
                        break;
                    case "getCardsOnTable":
                        jsonOutput.getCardsOnTable(board, objectMapper, output);
                        break;
                    case "getCardAtPosition":
                        jsonOutput.getCardAtPosition(board, xCard, yCard, objectMapper, output);
                        break;
                    case "cardUsesAttack":
                        if (game.getStatus() == 1) {
                            continue;
                        }
                        jsonOutput.cardUsesAttack(board, cardAttacker, cardAttacked,
                                playerOne, playerTwo, objectMapper, output);
                        break;
                    case "getFrozenCardsOnTable":
                        jsonOutput.getFrozenCardsOnTable(board, playerOne, playerTwo, objectMapper,
                                output);
                        break;
                    case "useHeroAbility":
                        if (game.getStatus() == 1) {
                            continue;
                        }
                        jsonOutput.useHeroAbility(board, affectedRow, game.getPlayerTurn(), heroOne,
                                heroTwo, playerOne, playerTwo, objectMapper, output);
                        break;
                    case "cardUsesAbility":
                        if (game.getStatus() == 1) {
                            continue;
                        }
                        jsonOutput.cardUsesAbility(board, cardAttacker, cardAttacked,
                                playerOne, playerTwo, objectMapper, output);
                        break;
                    case "useAttackHero":
                        if (game.getStatus() == 1) {
                            continue;
                        }
                        jsonOutput.useAttackHero(board, playerOne, playerTwo, cardAttacker,
                                initials, game, objectMapper, output);
                        break;
                    case "endPlayerTurn":
                        if (game.getStatus() == 1) {
                            continue;
                        }
                        jsonOutput.endPlayerTurn(game, playerOne, playerTwo, deckOne, deckTwo);
                        break;
                    case "getTotalGamesPlayed":
                        jsonOutput.getTotalGamesPlayed(initials, objectMapper, output);
                        break;
                    case "getPlayerOneWins":
                        jsonOutput.getPlayerOneWins(playerOne, objectMapper, output);
                        break;
                    case "getPlayerTwoWins":
                        jsonOutput.getPlayerTwoWins(playerTwo, objectMapper, output);
                        break;
                    default:
                        break;
                }
                switch (game.getPlayerTurn()) {
                    case 1:
                        switch (command) {
                            case "placeCard":
                                if (game.getStatus() == 1) {
                                    continue;
                                }
                                jsonOutput.placeCardOnTable(playerOne, handIdx, board, objectMapper,
                                        output);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2:
                        switch (command) {
                            case "placeCard":
                                if (game.getStatus() == 1) {
                                    continue;
                                }
                                jsonOutput.placeCardOnTable(playerTwo, handIdx, board, objectMapper,
                                        output);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
