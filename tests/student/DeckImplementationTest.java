package student;

import model.Deck;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;


/**
 * tests for DeckImplementation
 * @author Shannon & Brennan
 */
class DeckImplementationTest {

    /**
     * tests if the amount of cards when created is correct & after card is drawn
     */
    @Test
    public void create() {
        Deck deck = new DeckImplementation();
        assertEquals("Wrong initial deck size!", 180,
                deck.numberOfCardsRemaining());
        deck.drawACard();
        assertEquals("Wrong size deck after draw!", 179,
                deck.numberOfCardsRemaining());
    }

    /**
     * tests if the decks are randomly shuffling
     */
    @Test
    public void randomTest() {
        Deck deck1 = new DeckImplementation();
        Deck deck2 = new DeckImplementation();

        assertEquals("Wrong size!", 180, deck1.numberOfCardsRemaining());
        assertEquals("Wrong size!", 180, deck2.numberOfCardsRemaining());

        assertEquals("Decks are equal!", false, deck1.equals(deck2));
    }

    /**
     * tests to see if the deck gets reset to original size and reshuffled
     */
    @Test
    public void resetTest() {
        Deck deck = new DeckImplementation();
        for (int i = 0; i < 50; i++) {
            deck.drawACard();
        }
        deck.reset();

        assertEquals("Deck did not reset to correct size", 180,
                deck.numberOfCardsRemaining());
    }

}

