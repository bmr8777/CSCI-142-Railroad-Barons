package student;

import model.*;

import java.util.*;

/**
 * Implementation of the Player interface
 * Represents a player in the RailroadBarons game
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class PlayerImplementation implements Player {

    /**
     * Collection of all PlayerObservers
     */
    protected Collection<PlayerObserver> observers;

    /**
     * The cards in the players hand
     */
    protected Map<Card, Integer> cards;

    /**
     * The collection of all routes claimed by the user
     */
    protected Collection<Route> claimedRoutes;

    /**
     * The last pair of cards the user was dealt
     */
    protected Pair lastPair;

    /**
     * The number of game pieces the player has remaining
     */
    protected int piecesRemaining;

    /**
     * The Baron the player is playing the game as
     */
    protected Baron baron;

    /**
     * Whether the player has claimed a route this turn
     */
    protected boolean routeClaimed;

    /**
     * Graph of all stations
     * Used for northSouth Bonus
     */
    protected Graph<Station> northSouthGraph;

    /**
     * Graph of all stations
     * Used for eastWest Bonus
     */
    protected Graph<Station> eastWestGraph;

    /**
     * flag to determine if player already received bonus
     * points for a west to east route
     */
    protected boolean westEastBonus;

    /**
     * flag to determine if player already received bonus
     * points for a north to south route
     */
    protected boolean northSouthBonus;

    /**
     * flag to determine whether the score algorithm should search for a path
     */
    protected boolean gameStarted;

    /**
     * number of rows in board for bonus points
     */
    protected int rows;

    /**
     * number of columns in board for bonus points
     */
    protected int cols;

    /**
     * The current score of the player
     */
    protected int score;

    /**
     * Station connected to all stations located at the
     * North-Most row on the RailroadBarons Map
     */
    protected Station northMost;

    /**
     * Station connected to all stations located at the
     * South-Most row on the RailroadBarons Map
     */
    protected Station southMost;

    /**
     * Station connected to all stations located at the
     * East-Most column on the RailroadBarons Map
     */
    protected Station eastMost;

    /**
     * Station connected to all stations located at the
     * West-Most column on the RailroadBarons Map
     */
    protected Station westMost;

    /**
     * Creates an instance of Player
     *
     * @param baron the Baron the player is playing the game as
     */

    public PlayerImplementation(Baron baron){
        northSouthGraph = new Graph<>();
        eastWestGraph = new Graph<>();
        routeClaimed = true;
        this.baron = baron;
        observers = new HashSet<>();
        cards = new HashMap<>();
        claimedRoutes = new HashSet<>();
        piecesRemaining = 45;
        score = 0;
        gameStarted = false;
    }

    /**
     * This is called at the start of every game to reset the player to its
     * initial state:
     * <ul>
     * <li>Number of train pieces reset to the starting number of 45.</li>
     * <li>All remaining {@link Card cards} cleared from hand.</li>
     * <li>Score reset to 0.</li>
     * <li>Claimed {@link RouteImplementation routes} cleared.</li>
     * <li>Sets the most recently dealt {@link PairImplementation} of cards to two
     * {@link Card#NONE} values.</li>
     * </ul>
     *
     * @param dealt The hand of {@link Card cards} dealt to the player at the
     *              start of the game. By default this will be 4 cards.
     */

    @Override
    public void reset(Card... dealt) {
        score = 0;
        routeClaimed = true;
        piecesRemaining = 45;
        northSouthGraph = new Graph<>();
        eastWestGraph = new Graph<>();
        cards = new HashMap<>();
        westEastBonus = false;
        northSouthBonus = false;
        northMost = new StationImplementation(0, 0, 0, "North_Most");
        southMost = new StationImplementation(0, 0, 0, "South_Most");
        eastMost = new StationImplementation(0, 0, 0, "East_Most");
        westMost = new StationImplementation(0, 0, 0, "West_Most");
        northSouthGraph.addVertex(northMost);
        northSouthGraph.addVertex(southMost);
        eastWestGraph.addVertex(eastMost);
        eastWestGraph.addVertex(westMost);
        for (Card card: model.Card.values()){
            cards.put(card, 0);
        }
        claimedRoutes = new HashSet<>();
        lastPair = new PairImplementation(Card.NONE, Card.NONE);
        for (Card card: dealt) {
            if (card != null){
                addCard(card);
            }
        }
        for (PlayerObserver observer: observers) {
            observer.playerChanged(this);
        }
    }

    /**
     * Adds an {@linkplain PlayerObserver observer} that will be notified when
     * the player changes in some way.
     *
     * @param observer The new {@link PlayerObserver}.
     */

    @Override
    public void addPlayerObserver(PlayerObserver observer) { observers.add(observer); }

    /**
     * Removes an {@linkplain PlayerObserver observer} so that it is no longer
     * notified when the player changes in some way.
     *
     * @param observer The {@link PlayerObserver} to remove.
     */

    @Override
    public void removePlayerObserver(PlayerObserver observer) { observers.remove(observer); }

    /**
     * The {@linkplain Baron baron} as which this player is playing the game.
     *
     * @return The {@link Baron} as which this player is playing.
     */

    @Override
    public Baron getBaron() { return baron; }

    /**
     * Used to start the player's next turn. A {@linkplain PairImplementation pair of cards}
     * is dealt to the player, and the player is once again able to claim a
     * {@linkplain RouteImplementation route} on the {@linkplain RailroadMapImplementation map}.
     *
     * @param dealt a {@linkplain PairImplementation pair of cards} to the player. Note that
     *              one or both of these cards may have a value of {@link Card#NONE}.
     */

    @Override
    public void startTurn(Pair dealt) {
        gameStarted = true;
        routeClaimed = false;
        lastPair = dealt;
        Card first = dealt.getFirstCard();
        Card second = dealt.getSecondCard();
        addCard(first);
        addCard(second);
        for (PlayerObserver observer: observers) {
            observer.playerChanged(this);
        }
    }

    /**
     * Returns the most recently dealt {@linkplain PairImplementation pair of cards}. Note
     * that one or both of the {@linkplain Card cards} may have a value of
     * {@link Card#NONE}.
     *
     * @return The most recently dealt {@link PairImplementation} of {@link Card Cards}.
     */

    @Override
    public Pair getLastTwoCards() { return lastPair; }

    /**
     * Returns the number of the specific kind of {@linkplain Card card} that
     * the player currently has in hand. Note that the number may be 0.
     *
     * @param card The {@link Card} of interest.
     * @return The number of the specified type of {@link Card} that the
     * player currently has in hand.
     */

    @Override
    public int countCardsInHand(Card card) { return cards.get(card); }

    /**
     * Returns the number of game pieces that the player has remaining. Note
     * that the number may be 0.
     *
     * @return The number of game pieces that the player has remaining.
     */

    @Override
    public int getNumberOfPieces() { return piecesRemaining; }

    /**
     * Returns true iff the following conditions are true:
     * <p>
     * <ul>
     * <li>The {@linkplain RouteImplementation route} is not already claimed by this or
     * some other {@linkplain Baron baron}.</li>
     * <li>The player has not already claimed a route this turn (players
     * are limited to one claim per turn).</li>
     * <li>The player has enough {@linkplain Card cards} (including ONE
     * {@linkplain Card#WILD wild card, if necessary}) to claim the
     * route.</li>
     * <li>The player has enough train pieces to claim the route.</li>
     * </ul>
     *
     * @param route The {@link RouteImplementation} being tested to determine whether or not
     *              the player is able to claim it.
     * @return True if the player is able to claim the specified
     * {@link RouteImplementation}, and false otherwise.
     */

    @Override
    public boolean canClaimRoute(Route route) {
        int length = route.getLength();
        return (route.getBaron().equals(Baron.UNCLAIMED) && !routeClaimed
                && sufficientCards(length) && length <= piecesRemaining);
    }

    /**
     * Claims the given {@linkplain RouteImplementation route} on behalf of this player's
     * {@linkplain Baron Railroad Baron}. It is possible that the player has
     * enough cards in hand to claim the route by using different
     * combinations of {@linkplain Card card}. It is up to the implementor to
     * employ an algorithm that determines which cards to use, but here are
     * some suggestions:
     * <ul>
     * <li>Use the color with the lowest number of cards necessary to
     * match the length of the route.</li>
     * <li>Do not use a wild card unless absolutely necessary (i.e. the
     * player has length-1 cards of some color in hand and it is the most
     * numerous card that the player holds).</li>
     * </ul>
     *
     * @param route The {@link RouteImplementation} to claim.
     * @throws RailroadBaronsException If the {@link RouteImplementation} cannot be claimed,
     *                                 i.e. if the {@link //canClaimRoute(RouteImplementation)} method returns false.
     */

    @Override
    public void claimRoute(Route route) throws RailroadBaronsException {
        int num, length, count;
        length = route.getLength();

        /*
        Uses the card with the same amount as the length of the route
         */

        for (Card card: cards.keySet()){
            if (!card.equals(Card.WILD) && cards.get(card) == length) {
                if (!route.claim(baron)) {
                    throw new RailroadBaronsException("The route has already been claimed!");
                } else{
                    cards.put(card, 0);
                    routeClaimed = true;
                    claimedRoutes.add(route);
                    piecesRemaining -= route.getLength();
                    connectStations(route.getOrigin(), route.getDestination());
                    score += route.getPointValue();
                    for (PlayerObserver observer: observers) {
                        observer.playerChanged(this);
                    }
                    return;
                }
            }
        }

        /*
        Uses the card with the lowest number necessary to match the length of the route
         */

        for (int i = length; i < piecesRemaining; i++){
            for (Card card: cards.keySet()){
                if (!card.equals(Card.WILD) && cards.get(card) == i) {
                    if (!route.claim(baron)) {
                        throw new RailroadBaronsException("The route has already been claimed!");
                    } else{
                        cards.put(card, i - length);
                        piecesRemaining -= length;
                        routeClaimed = true;
                        claimedRoutes.add(route);
                        connectStations(route.getOrigin(), route.getDestination());
                        score += route.getPointValue();
                        for (PlayerObserver observer: observers) {
                            observer.playerChanged(this);
                        }
                        return;
                    }
                }
            }
        }

        /*
        Last resort, Wild Card is required to claim route
         */

        for (Card card: cards.keySet()){
            if (card.equals(Card.WILD)){
                continue;
            }
            num = cards.get(Card.WILD);
            count = cards.get(card);
            if (count > 0 && count + 1 == length){
                if (!route.claim(baron)) {
                    throw new RailroadBaronsException("The route has already been claimed!");
                } else {
                    cards.put(card, 0);
                    cards.put(Card.WILD, num-1);
                    routeClaimed = true;
                    claimedRoutes.add(route);
                    connectStations(route.getOrigin(), route.getDestination());
                    piecesRemaining -= length;
                    score += route.getPointValue();
                    for (PlayerObserver observer: observers) {
                        observer.playerChanged(this);
                    }
                    return;
                }
            }
        }
    }

    /**
     * Returns the {@linkplain Collection collection} of {@linkplain RouteImplementation
     * routes} claimed by this player.
     *
     * @return The {@link Collection} of {@linkplain RouteImplementation Routes} claimed by
     * this player.
     */

    @Override
    public Collection<Route> getClaimedRoutes() { return claimedRoutes; }

    /**
     * Returns the players current score based on the
     * {@linkplain RouteImplementation#getPointValue() point value} of each
     * {@linkplain RouteImplementation route} that the player has currently claimed.
     *
     * @return The player's current score.
     */

    @Override
    public int getScore() {
        if (!westEastBonus && gameStarted){
            if (eastWestGraph.breadthFirstSearch(westMost, eastMost)){
                if (eastWestGraph.buildPathBFS(westMost, eastMost).size() >= 5){
                    score += cols*5;
                    westEastBonus = true;
                }
            }
        }
        if (!northSouthBonus && gameStarted){
            if (northSouthGraph.breadthFirstSearch(northMost, southMost)){
                if (northSouthGraph.buildPathBFS(northMost, southMost).size() >= 5){
                    score += 5*rows;
                    northSouthBonus = true;
                }
            }
        }
        return score;
    }

    /**
     * Returns true iff the following conditions are true:
     * <p>
     * <ul>
     * <li>The player has enough {@linkplain Card cards} (including
     * {@linkplain Card#WILD wild cards}) to claim a
     * {@linkplain RouteImplementation route} of the specified length.</li>
     * <li>The player has enough train pieces to claim a
     * {@linkplain RouteImplementation route} of the specified length.</li>
     * </ul>
     *
     * @param shortestUnclaimedRoute The length of the shortest unclaimed
     *                               {@link RouteImplementation} in the current game.
     * @return True if the player can claim such a {@link RouteImplementation route}, and
     * false otherwise.
     */

    @Override
    public boolean canContinuePlaying(int shortestUnclaimedRoute) {
        /*
        Checks to see if there are any cards left in the deck
         */
        boolean deckEmpty = (lastPair.getFirstCard().equals(Card.NONE)
                && lastPair.getSecondCard().equals(Card.NONE));
        return (piecesRemaining >= shortestUnclaimedRoute)
                && (sufficientCards(shortestUnclaimedRoute) || !deckEmpty);
    }

    /**
     * String representation of the RailroadBarons Player
     *
     * @return String representation of the player
     */

    @Override
    public String toString(){ return baron + " Baron"; }

    /**
     * Compares two Player objects to determine whether they are equal: Have the same Baron
     *
     * @param object the object being compared to the Player
     * @return whether the two objects are equal
     */

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Player)){
            return false;
        }
        PlayerImplementation player = (PlayerImplementation) object;
        return player.getBaron().equals(baron);
    }

    /**
     * Adds dealt cards to the player's hand
     *
     * @param card the card being added
     */

    public void addCard(Card card){
        if (!card.equals(Card.NONE)){
            int count = cards.get(card);
            count++;
            cards.put(card, count);
        }
    }

    /**
     * Determines whether the player has a sufficient number of cards to claim a
     * route of a specified length
     *
     * @param length the length of the specified route
     * @return whether the player has enough cards to claim a route
     */

    public boolean sufficientCards(int length){
        int wild = cards.get(Card.WILD);
        if (wild > 0){
            wild = 1;
        }
        for (Card card: cards.keySet()){
            if (card.equals(Card.WILD)){
                continue;
            }
            int num = cards.get(card);
            if (num > 0  && num + wild >= length) {
                return true;
            }
        }
        return false;
    }

    /**
     * tell the player the size of the map
     *
     * @param rows number of rows in the map
     * @param cols number of columns in map
     */

    public void addBoardSize(int rows, int cols){
        this.cols = cols;
        this.rows = rows;
    }

    /**
     * Connects the stations in the graph
     *
     * @param origin the station of origin for a route
     * @param destination the destination station for a route
     */

    public void connectStations(Station origin, Station destination){
        eastWestGraph.connect(origin, destination);
        northSouthGraph.connect(origin, destination);
    }

    /**
     * Adds and connects all boundary stations to the Station Graph
     *
     * @param map map containing all stations located on the map boundary's
     */

    public void addBoundaryStations(Map<String, ArrayList<Station>> map){
        for (String key: map.keySet()) {
            switch (key){
                case "North":
                    for (Station station: map.get(key)) {
                        northSouthGraph.connect(station, northMost);
                    }
                    break;
                case "South":
                    for (Station station: map.get(key)) {
                       northSouthGraph.connect(station, southMost);
                    }
                    break;
                case "East":
                    for (Station station: map.get(key)) {
                        eastWestGraph.connect(station, eastMost);
                    }
                    break;
                case "West":
                    for (Station station: map.get(key)) {
                        eastWestGraph.connect(station, westMost);
                    }
                    break;
            }
        }
    }

    /**
     * Adds all RailroadBarons Stations to the graph
     *
     * @param stations List of all Stations
     */

    public void addStations(List<Station> stations){
        for (Station station: stations) {
            northSouthGraph.addVertex(station);
            eastWestGraph.addVertex(station);
        }
    }
}