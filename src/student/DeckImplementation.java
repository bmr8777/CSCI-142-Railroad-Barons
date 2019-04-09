package student;
import model.Card;
import model.Deck;


import java.util.Arrays;
import java.util.Stack;

/**
 * Implementation of the Deck interface
 * Represents a deck of cards used in the RailroadBarons game
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class DeckImplementation implements Deck {

    /**
     * Stack that contains the remaining Cards
     */
    private Stack<Card> cards;

    /**
     * Creates an instance of Deck
     */

    public DeckImplementation(){ cards = createDeck(); }

    /**
     * Resets the {@linkplain model.Deck deck} to its starting state. Restores any
     * {@linkplain Card cards} that were drawn and shuffles the deck.
     */

    @Override
    public void reset() { cards = createDeck(); }

    /**
     * Draws the next {@linkplain Card card} from the "top" of the deck.
     *
     * @return The next {@link Card}, unless the deck is empty, in which case
     * this should return {@link Card#NONE}.
     */

    @Override
    public Card drawACard() {
        if (cards.empty()){
            return Card.NONE;
        }
        return cards.pop();
    }

    /**
     * Returns the number of {@link Card cards} that have yet to be drawn.
     *
     * @return The number of {@link Card cards} that have yet to be drawn.
     */

    @Override
    public int numberOfCardsRemaining() { return cards.size(); }

    /**
     * Creates and returns a stack containing Cards
     *
     * @return default stack used for the game
     */

    private Stack<Card> createDeck(){

        Card[] deck = new Card [180];

        Arrays.fill(deck, 0, 20, Card.WILD);
        Arrays.fill(deck, 20, 40, Card.BLACK);
        Arrays.fill(deck, 40, 60, Card.BLUE);
        Arrays.fill(deck, 60, 80, Card.GREEN);
        Arrays.fill(deck, 80, 100, Card.ORANGE);
        Arrays.fill(deck, 100, 120, Card.PINK);
        Arrays.fill(deck, 120, 140, Card.RED);
        Arrays.fill(deck, 140, 160, Card.WHITE);
        Arrays.fill(deck, 160, 180, Card.YELLOW);


        Stack <Card> shuffledDeck = shuffle(deck);

        return shuffledDeck;
    }

    /**
     * Shuffles the deck of cards using Fisher-Yates Shuffle
     *
     * @param deck deck of cards to shuffle
     * @return stack of cards
     */

    private Stack<Card> shuffle(Card [] deck){

        int currentIndex = deck.length;

        Stack<Card> shuffledDeck = new Stack<>();

        while(currentIndex > 0){
            int i = (int)(Math.floor(Math.random() * currentIndex--));
            Card c = deck[currentIndex];
            deck[currentIndex] = deck[i];
            deck[i] = c;
        }

        for(Card c : deck){
            shuffledDeck.push(c);
        }

        return shuffledDeck;
    }

    /**
     * Equals method for testing purposes
     *
     * @param o object to compare
     * @return true if equal
     */

    @Override
    public boolean equals(Object o){
        if(! (o instanceof DeckImplementation)){
            return false;
        }

        DeckImplementation d = (DeckImplementation) o;

        while(cards.size() > 0){
            Card c = cards.pop();
            Card c1 = d.cards.pop();
            if(!c.equals(c1)){
                return false;
            }
        }
        return true;
    }
}