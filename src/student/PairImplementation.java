package student;

import model.Card;
import model.Pair;

/**
 * Implementation of the Pair interface
 * Holds one pair of Cards
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class PairImplementation implements Pair{

    /**
     * The first card in the pair
     */
    private Card firstCard;

    /**
     * The second card in the pair
     */
    private Card secondCard;

    /**
     * Creates an instance of Pair
     *
     * @param firstCard first card in the pair
     * @param secondCard second card in the pair
     */

    public PairImplementation(Card firstCard, Card secondCard){
        this.firstCard = firstCard;
        this.secondCard = secondCard;
    }

    /**
     * Returns the first {@linkplain Card card} in the pair. Note that, if the
     * game deck is empty, the value of this card may be {@link Card#NONE}.
     *
     * @return The first {@link Card} in the pair.
     */

    @Override
    public Card getFirstCard() { return firstCard; }

    /**
     * Returns the second {@linkplain Card card} in the pair. if the
     * game deck is empty, the value of this card may be {@link Card#NONE}.
     *
     * @return The second {@link Card} in the pair.
     */

    @Override
    public Card getSecondCard() { return secondCard; }
}