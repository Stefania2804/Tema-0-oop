package main;

import fileio.CardInput;

import java.util.ArrayList;

public final class Player {
    private int index;
    private ArrayList<CardInput> deck;
    private ArrayList<CardInput> cards_in_hand;
    private CardInput hero;
    private int mana;
    private int turn;
    private ArrayList<CardInput> cardsUsedThisTour;
    private ArrayList<CardInput> frozenCards;
    private ArrayList<CardInput> stillFrozenCards;
    private int heroUsed;

    public Player(final int index, final ArrayList<CardInput> deck, final CardInput hero) {
        this.index = index;
        this.deck = deck;
        this.hero = hero;
        this.cards_in_hand = new ArrayList<>();
        this.cardsUsedThisTour = new ArrayList<>();
        this.frozenCards = new ArrayList<>();
        this.stillFrozenCards = new ArrayList<>();
        this.heroUsed = 0;
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

    public CardInput getHero() {
        return hero;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(final int turn) {
        this.turn = turn;
    }

    public int getHeroUsed() {
        return heroUsed;
    }

    public void setHeroUsed(final int heroUsed) {
        this.heroUsed = heroUsed;
    }

    public ArrayList<CardInput> getCardsInHand() {
        return cards_in_hand;
    }
    /**
     * Adauga o carte in mana jucatorului.
     *
     * @param card este cartea nou adaugata
     */
    public void addCardInHand(final CardInput card) {
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
    public void removeCardInHand(final CardInput card) {
        cards_in_hand.remove(card);
    }

    /**
     * Adauga o carte folosita tura respectiva.
     *
     * @param card este cartea nou folosita
     */
    public void addCardUsed(final CardInput card) {
        cardsUsedThisTour.add(card);
    }
    /**
     * Elimina o carte care a atacat.
     *
     * @param card este cartea care a atacat
     */
    public void removeCardUsed(final CardInput card) {
        cardsUsedThisTour.remove(card);
    }

    public ArrayList<CardInput> getCardsUsed() {
        return cardsUsedThisTour;
    }
    /**
     * Adauga o carte frozen.
     *
     * @param card este cartea nou inghetata
     */
    public void addCardFrozen(final CardInput card) {
        frozenCards.add(card);
    }
    /**
     * Elimina o carte frozen.
     *
     * @param card este cartea care este dezghetata
     */
    public void removeCardFrozen(final CardInput card) {
        frozenCards.remove(card);
    }

    public ArrayList<CardInput> getFrozenCards() {
        return frozenCards;
    }

    public ArrayList<CardInput> getStillFrozenCards() {
        return stillFrozenCards;
    }
    /**
     * Adauga o carte care va ramane frozen.
     *
     * @param card este cartea
     */
    public void addStillFrozenCard(final CardInput card) {
        stillFrozenCards.add(card);
    }
    /**
     * Elimina o carte inghetata de doua ori la rand.
     *
     * @param card este cartea reinghetata
     */
    public void removestillFrozenCard(final CardInput card) {
        stillFrozenCards.remove(card);
    }

    /**
     * Cauta o carte in cele inghetate.
     *
     * @param card este cartea cautata
     */
    public int findCard(final CardInput card) {
        if (!getFrozenCards().isEmpty()) {
            for (CardInput cardFrozen : getFrozenCards()) {
                if (cardFrozen == card) {
                    return 1;
                }
            }
        }
        return 0;
    }
}
