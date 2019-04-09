package student;

import model.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * The implementation of the RailroadBarons interface. Main class for the
 * RailRoadBarons game
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class RailroadBaronsImplementation implements RailroadBarons{

    /**
     * Collection of all RailroadBaronsObservers
     */
    private Collection<RailroadBaronsObserver> observers;

    /**
     * Queue of all player
     */
    private LinkedList<Player> players;

    /**
     * Collection of all Routes
     */
    private Collection<Route> routes;

    /**
     * RailroadMap game is being played on
     */
    private RailroadMap map;

    /**
     * The deck of all remaining cards
     */
    private Deck deck;

    /**
     * The player whose turn it is
     */
    private Player currentPlayer;

    /**
     * The index of the current player
     */
    private int currentPlayerIndex;

    /**
     * Default, parameter-less constructor
     */

    public RailroadBaronsImplementation(){
        observers = new HashSet<>();
        players = new LinkedList<>();
        players.add(new PlayerImplementation(Baron.RED));
        players.add(new PlayerImplementation(Baron.GREEN));
        players.add(new PlayerImplementation(Baron.BLUE));
        players.add(new PlayerImplementation(Baron.YELLOW));
    }

    /**
     * Adds a new {@linkplain RailroadBaronsObserver observer} to the
     * {@linkplain Collection collection} of observers that will be notified
     * when the state of the game changes. Game state changes include:
     * <ul>
     * <li>A player's turn begins.</li>
     * <li>A player's turn ends.</li>
     * <li>The game is over.</li>
     * </ul>
     *
     * @param observer The {@link RailroadBaronsObserver} to add to the
     *                 {@link Collection} of observers.
     */

    @Override
    public void addRailroadBaronsObserver(RailroadBaronsObserver observer) {observers.add(observer);}

    /**
     * Removes the {@linkplain RailroadBaronsObserver observer} from the
     * collection of observers that will be notified when the state of the
     * game changes.
     *
     * @param observer The {@link RailroadBaronsObserver} to remove.
     */

    @Override
    public void removeRailroadBaronsObserver(RailroadBaronsObserver observer) {observers.remove(observer);}

    /**
     * Starts a new {@linkplain RailroadBaronsImplementation Railroad Barons} game with the
     * specified {@linkplain RailroadMapImplementation map} and a default {@linkplain Deck
     * deck of cards}. If a game is currently in progress, the progress is
     * lost. There is no warning!
     * <p>
     * By default, a new game begins with:
     * <ul>
     * <li>A default deck that contains 20 of each color of card and 20
     * wild cards.</li>
     * <li>4 players, each of which has 45 train pieces.</li>
     * <li>An initial hand of 4 cards dealt from the deck to each
     * player</li>
     * </ul>
     *
     * @param map The {@link RailroadMapImplementation} on which the game will be played.
     */

    @Override
    public void startAGameWith(RailroadMap map) {

        this.map = map;
        RailroadMapImplementation castedMap = (RailroadMapImplementation) map;
        deck = new DeckImplementation();
        routes = map.getRoutes();
        currentPlayerIndex = new Random().nextInt( 4);
        int rows = map.getRows();
        int cols = map.getCols();

        for (Player player : players) {
            player.reset(createHand());
            PlayerImplementation p = (PlayerImplementation) player;
            p.addBoardSize(rows, cols);
            p.addStations(castedMap.getStations());
            p.addBoundaryStations(castedMap.getBoundaryStations());
        }

        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.startTurn(new PairImplementation(deck.drawACard(), deck.drawACard()));
        for (RailroadBaronsObserver observer: observers) {
            observer.turnStarted(this, currentPlayer);
        }
    }

    /**
     * Starts a new {@linkplain RailroadBaronsImplementation Railroad Barons} game with the
     * specified {@linkplain RailroadMapImplementation map} and {@linkplain Deck deck of
     * cards}. This means that the game should work with any implementation of
     * the {@link Deck} interface (not just a specific implementation)!
     * Otherwise, the starting state of the game is the same as a
     * {@linkplain //startAGameWith(RailroadMap) normal game}.
     *
     * @param map  The {@link RailroadMapImplementation} on which the game will be played.
     * @param deck The {@link Deck} of cards used to play the game. This may
     *             be ANY implementation of the {@link Deck} interface,
     *             meaning that a valid implementation of the
     *             {@link RailroadBaronsImplementation} interface should use only the
     *             {@link Deck} interface and not a specific implementation.
     */

    @Override
    public void startAGameWith(RailroadMap map, Deck deck) {
        this.map = map;
        this.deck = deck;
        routes = map.getRoutes();
        currentPlayerIndex = new Random().nextInt( 4);
        RailroadMapImplementation castedMap = (RailroadMapImplementation) map;
        int rows = map.getRows();
        int cols = map.getCols();

        for (Player player : players) {
            player.reset(createHand());
            PlayerImplementation p = (PlayerImplementation) player;
            p.addBoardSize(rows, cols);
            p.addStations(castedMap.getStations());
            p.addBoundaryStations(castedMap.getBoundaryStations());
        }

        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.startTurn(new PairImplementation(deck.drawACard(), deck.drawACard()));
        for (RailroadBaronsObserver observer : observers) {
            observer.turnStarted(this, currentPlayer);
        }
    }

    /**
     * Returns the {@linkplain RailroadMapImplementation map} currently being used for play.
     * If a game is not in progress, this may be null!
     *
     * @return The {@link RailroadMapImplementation} being used for play.
     */

    @Override
    public RailroadMap getRailroadMap() { return map; }

    /**
     * Returns the number of {@linkplain model.Card cards} that remain to be dealt
     * in the current game's {@linkplain Deck deck}.
     *
     * @return The number of cards that have not yet been dealt in the game's
     * {@link Deck}.
     */

    @Override
    public int numberOfCardsRemaining() { return deck.numberOfCardsRemaining(); }

    /**
     * Returns true iff the current {@linkplain PlayerImplementation player} can claim the
     * {@linkplain RouteImplementation route} at the specified location, i.e. the player has
     * enough cards and pieces, and the route is not currently claimed by
     * another player. Should delegate to the
     * {@link PlayerImplementation canClaimRoute(Route)} method on the current player.
     *
     * @param row The row of a {@link TrackImplementation} in the {@link RouteImplementation} to check.
     * @param col The column of a {@link TrackImplementation} in the {@link RouteImplementation} to check.
     * @return True iff the {@link RouteImplementation} can be claimed by the current
     * player.
     */

    @Override
    public boolean canCurrentPlayerClaimRoute(int row, int col) {
        Route route = map.getRoute(row, col);
        return currentPlayer.canClaimRoute(route);
    }

    /**
     * Attempts to claim the {@linkplain RouteImplementation route} at the specified
     * location on behalf of the current {@linkplain PlayerImplementation player}.
     *
     * @param row The row of a {@link TrackImplementation} in the {@link RouteImplementation} to claim.
     * @param col The column of a {@link TrackImplementation} in the {@link RouteImplementation} to claim.
     * @throws RailroadBaronsException If the {@link RouteImplementation} cannot be claimed
     *                                 by the current player.
     */

    @Override
    public void claimRoute(int row, int col) throws RailroadBaronsException {
        if (canCurrentPlayerClaimRoute(row, col)){
            Route route = map.getRoute(row, col);
            currentPlayer.claimRoute(route);
            map.routeClaimed(route);
        } else{
            throw new RailroadBaronsException("The route cannot be claimed by the current player.");
        }
    }

    /**
     * Called when the current {@linkplain PlayerImplementation player} ends their turn.
     */

    @Override
    public void endTurn() {

        if (currentPlayerIndex +1 >= 4){
            currentPlayerIndex = 0;
        } else{
            currentPlayerIndex++;
        }

        for (RailroadBaronsObserver observer: observers) {
            observer.turnEnded(this, currentPlayer);
        }

        if (gameIsOver()){
            Player winner;
            int max = players.stream().mapToInt(Player::getScore).max().getAsInt();
            for (Player player: players) {
                if (player.getScore() == max){
                    winner = player;
                    for (RailroadBaronsObserver observer: observers) {
                        observer.gameOver(this, winner);
                    }
                    break;
                }
            }
        } else {
            currentPlayer = players.get(currentPlayerIndex);
            for (RailroadBaronsObserver observer: observers) {
                observer.turnStarted(this, currentPlayer);
            }
            currentPlayer.startTurn(new PairImplementation(deck.drawACard(), deck.drawACard()));
        }
    }

    /**
     * Returns the {@linkplain PlayerImplementation player} whose turn it is.
     *
     * @return The {@link PlayerImplementation} that is currently taking a turn.
     */

    @Override
    public Player getCurrentPlayer() { return currentPlayer; }

    /**
     * Returns all of the {@linkplain PlayerImplementation players} currently playing the
     * game.
     *
     * @return The {@link PlayerImplementation Players} currently playing the game.
     */

    @Override
    public Collection<Player> getPlayers() { return players; }

    /**
     * Indicates whether or not the game is over. This occurs when no more
     * plays can be made. Reasons include:
     * <ul>
     * <li>No one player has enough pieces to claim a route.</li>
     * <li>No one player has enough cards to claim a route.</li>
     * <li>All routes have been claimed.</li>
     * </ul>
     *
     * @return True if the game is over, false otherwise.
     */

    @Override
    public boolean gameIsOver() {
        boolean playerContinue = true;
        boolean routeContinue = true;

        for (Player player: players){
            if(player.canContinuePlaying(map.getLengthOfShortestUnclaimedRoute())){
                playerContinue = false;
            }
        }
        for (Route route: routes){
            if (route.getBaron().equals(Baron.UNCLAIMED)){
                routeContinue = false;
            }
        }
        return playerContinue || routeContinue;
    }

    /**
     * Creates and returns a hand containing 4 cards drawn from the deck
     *
     * @return hand of drawn cards
     */

    public Card[] createHand(){
        Card[] hand = new Card[4];

        for (int i = 0; i < 4; i++){
            hand[i] = deck.drawACard();
        }
        return hand;
    }
}