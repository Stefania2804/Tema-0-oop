package org.poo.main;

public final class Initials {
    private int gamesPlayed;
    private int playerOneWins;
    private int playerTwoWins;

    public Initials() {
        gamesPlayed = 0;
        playerOneWins = 0;
        playerTwoWins = 0;
    }
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public void setGamesPlayed(final int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    public int getPlayerOneWins() {
        return playerOneWins;
    }
    public void setPlayerOneWins(final int playerOneWins) {
        this.playerOneWins = playerOneWins;
    }
    public int getPlayerTwoWins() {
        return playerTwoWins;
    }
    public void setPlayerTwoWins(final int playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }
}
