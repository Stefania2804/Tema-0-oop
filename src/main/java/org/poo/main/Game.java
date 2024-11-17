package org.poo.main;

public final class Game {

    private int gamesPlayed;
    private int round;
    private int status;
    private int turnOne;
    private int turnTwo;
    private int playerTurn;

    public Game(final int gamesPlayed, final int startingPlayer) {
        this.gamesPlayed = gamesPlayed;
        round = 1;
        status = 0;
        turnOne = 0;
        turnTwo = 0;
        playerTurn = startingPlayer;
    }
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public void setGamesPlayed(final int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(final int status) {
        this.status = status;
    }
    public int getRound() {
        return round;
    }
    public void setRound(final int round) {
        this.round = round;
    }
    public int getTurnOne() {
        return  turnOne;
    }
    public void setTurnOne(final int turnOne) {
        this.turnOne = turnOne;
    }
    public int getTurnTwo() {
        return turnTwo;
    }
    public void setTurnTwo(final int turnTwo) {
        this.turnTwo = turnTwo;
    }
    public int getPlayerTurn() {
        return playerTurn;
    }
    public void setPlayerTurn(final int playerTurn) {
        this.playerTurn = playerTurn;
    }
}
