package student;

import model.Card;
import model.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

/**
 * test class for PairImplementation
 * @author Shannon & Brennan
 */
class PairImplementationTest {

    /**
     * Tests if the PairImplementation is created correctly
     */
    @Test
    public void create() {
        Pair pair = new PairImplementation(Card.RED, Card.BLUE);

        // params: error message, expected value, actual value
        assertEquals("Wrong first card!", Card.RED, pair.getFirstCard());
        assertEquals("Wrong second card!", Card.BLUE, pair.getSecondCard());
    }

}