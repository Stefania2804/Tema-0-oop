package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Player {
    private int index;
    private ArrayList<CardInput> deck;
    private ArrayList<CardInput> cards_in_hand;
    private CardInput hero;
    private int mana;
    private int turn;
    private ArrayList<CardInput> cardsUsedThisTour;
    private ArrayList<CardInput> frozenCards;

    public Player(int index, ArrayList<CardInput> deck, CardInput hero) {
        this.index = index;
        this.deck = deck;
        this.hero = hero;
        this.cards_in_hand = new ArrayList<>();
        this.cardsUsedThisTour = new ArrayList<>();
        this.frozenCards = new ArrayList<>();
    }
    public void setPlayerIndex(int index) {
        this.index = index;
    }
    public int getPlayerIndex() {
        return index;
    }
    public void setDeck(ArrayList<CardInput> deck) {
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
    public void setMana(int mana) {
        this.mana = mana;
    }
    public int getTurn() {
        return turn;
    }
    public void setTurn (int turn) {
        this.turn = turn;
    }
    public ArrayList<CardInput> getCardsInHand() {
        return cards_in_hand;
    }
    public void addCardInHand(CardInput card) {
        cards_in_hand.add(card);
    }
    public void removeCardDeck(CardInput card) {
        deck.remove(card);
    }
    public void removeCardInHand(CardInput card) {
        cards_in_hand.remove(card);
    }
    public void addCardUsed(CardInput card) {
        cardsUsedThisTour.add(card);
    }
    public void removeCardUsed(CardInput card) {
        cardsUsedThisTour.remove(card);
    }
    public ArrayList<CardInput> getCardsUsed() {
        return cardsUsedThisTour;
    }
    public void addCardFrozen(CardInput card) {
        frozenCards.add(card);
    }
    public void removeCardFrozen(CardInput card) {
        frozenCards.remove(card);
    }
    public ArrayList<CardInput> getFrozenCards() {
        return frozenCards;
    }
}
