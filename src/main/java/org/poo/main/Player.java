package org.poo.main;


import org.poo.fileio.CardInput;

import java.util.ArrayList;

public final class Player {
    private int index;
    private ArrayList<CardInput> deck;
    private ArrayList<Minion> cards_in_hand = new ArrayList<>();
    private Hero hero;
    private int mana;
    private ArrayList<Minion> cardsUsedThisTour = new ArrayList<>();
    private ArrayList<Minion> frozenCards = new ArrayList<>();
    private ArrayList<Minion> stillFrozenCards = new ArrayList<>();
    private int heroUsed;
    private int wonGames;

    public Player(final int index, ArrayList<CardInput> deck, final Hero hero,
                  final int wins) {
        this.index = index;
        this.deck = deck;
        this.hero = hero;
        this.heroUsed = 0;
        this.wonGames = wins;
        this.cardsUsedThisTour = new ArrayList<>();
        this.frozenCards = new ArrayList<>();
        this.stillFrozenCards = new ArrayList<>();

    }

    public void setPlayerIndex(final int index) {
        this.index = index;
    }

    public int getPlayerIndex() {
        return index;
    }

    public void setDeck(final ArrayList<CardInput> deck) {
        this.deck = deck;
    }

    public ArrayList<CardInput> getPlayerDeck() {
        return deck;
    }

    public Hero getHero() {
        return hero;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getHeroUsed() {
        return heroUsed;
    }

    public void setHeroUsed(final int heroUsed) {
        this.heroUsed = heroUsed;
    }

    public int getWonGames() {
        return wonGames;
    }
    public void setWonGames(final int wonGames) {
        this.wonGames = wonGames;
    }

    public ArrayList<Minion> getCardsInHand() {
        return cards_in_hand;
    }
    /**
     * Adauga o carte in mana jucatorului.
     *
     * @param card este cartea nou adaugata
     */
    public void addCardInHand(final Minion card) {
        cards_in_hand.add(card);
    }
    /**
     * Elimina o carte din pachet.
     *
     * @param card este cartea eliminata
     */
    public void removeCardDeck(final CardInput card) {
        deck.remove(card);
    }
    /**
     * Elimina o carte din mana.
     *
     * @param card este cartea eliminata
     */
    public void removeCardInHand(final Minion card) {
        cards_in_hand.remove(card);
    }

    /**
     * Adauga o carte folosita tura respectiva.
     *
     * @param card este cartea nou folosita
     */
    public void addCardUsed(final Minion card) {
        cardsUsedThisTour.add(card);
    }
    /**
     * Elimina o carte care a atacat.
     *
     * @param card este cartea care a atacat
     */
    public void removeCardUsed(final Minion card) {
        cardsUsedThisTour.remove(card);
    }

    public ArrayList<Minion> getCardsUsed() {
        return cardsUsedThisTour;
    }
    /**
     * Adauga o carte frozen.
     *
     * @param card este cartea nou inghetata
     */
    public void addCardFrozen(final Minion card) {
        frozenCards.add(card);
    }
    /**
     * Elimina o carte frozen.
     *
     * @param card este cartea care este dezghetata
     */
    public void removeCardFrozen(final Minion card) {
        frozenCards.remove(card);
    }

    public ArrayList<Minion> getFrozenCards() {
        return frozenCards;
    }

    public ArrayList<Minion> getStillFrozenCards() {
        return stillFrozenCards;
    }
    /**
     * Adauga o carte care va ramane frozen.
     *
     * @param card este cartea
     */
    public void addStillFrozenCard(final Minion card) {
        stillFrozenCards.add(card);
    }
    /**
     * Elimina o carte inghetata de doua ori la rand.
     *
     * @param card este cartea reinghetata
     */
    public void removestillFrozenCard(final Minion card) {
        stillFrozenCards.remove(card);
    }

    /**
     * Cauta o carte in cele inghetate.
     *
     * @param card este cartea cautata
     */
    public int findCard(final Minion card) {
        if (!getFrozenCards().isEmpty()) {
            for (Minion cardFrozen : getFrozenCards()) {
                if (cardFrozen == card) {
                    return 1;
                }
            }
        }
        return 0;
    }
}
