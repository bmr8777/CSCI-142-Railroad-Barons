package student;

import model.*;

import java.util.*;

/**
 * Play Railroad Barons against 3 computer players
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class LonelyRailroadBarons implements RailroadBarons{

    /**
     * collection of RailroadBaronsObservers
     */
    private Collection<RailroadBaronsObserver> observers;

    /**
     * collection of players in the game
     */
    private List<Player> players;

    /**
     * deck of cards
     */
    private Deck deck;

    /**
     * map of the game
     */
    private RailroadMap map;

    /**
     * index the the current player is at in  list of players
     */
    private int currentPlayerIndex;

    /**
     * current player of game
     */
    private Player currentPlayer;

    /**
     * Collection of all Routes
     */
    private Collection<Route> routes;

    /**
     * Default, parameter-less constructor
     */

    public LonelyRailroadBarons(){
        observers = new HashSet<>();
        currentPlayerIndex = 0;
        players = new ArrayList<>();
        players.add(new PlayerImplementation(Baron.RED));
        players.add(new ComputerPlayer(Baron.BLUE));
        players.add(new ComputerPlayer(Baron.GREEN));
        players.add(new ComputerPlayer(Baron.YELLOW));
    }

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
     * get the railroadMap used for play
     * if the game is not progress this could be null
     *
     * @return RailroadMap being used for play
     */

    @Override
    public RailroadMap getRailroadMap() { return map; }

    /**
     * adds an observer to be notified when the state of the game changes
     *
     * @param observer The {@link RailroadBaronsObserver} to add
     */

    @Override
    public void addRailroadBaronsObserver(RailroadBaronsObserver observer) { observers.add(observer); }

    /**
     * removes an observer from the collection of observer
     *
     * @param observer The {@link RailroadBaronsObserver} to remove.
     */

    @Override
    public void removeRailroadBaronsObserver(RailroadBaronsObserver observer) { observers.remove(observer); }

    /**
     * Start a Lonely version of Railroad Barons with a specific map
     *
     * @param map The {@link RailroadMap} on which the game will be played.
     */

    @Override
    public void startAGameWith(RailroadMap map) {
        this.map = map;
        deck = new DeckImplementation();
        routes = map.getRoutes();
        RailroadMapImplementation castedMap = (RailroadMapImplementation) map;


        for(int i=0; i<4; i++){
            PlayerImplementation p= (PlayerImplementation)players.get(i);
            p.reset(createHand());
            p.addBoardSize(map.getRows(), map.getCols());
            p.addStations(castedMap.getStations());
            p.addBoundaryStations(castedMap.getBoundaryStations());

            if(i>0){
                ComputerPlayer cp = (ComputerPlayer) p;
                cp.addUnclaimedRoutes(map);
            }
        }

        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.startTurn(new PairImplementation(deck.drawACard(), deck.drawACard()));
        for (RailroadBaronsObserver observer : observers) {
            observer.turnStarted(this, currentPlayer);
        }

    }

    /**
     * Start a Lonely version of Railroad Barons with a specific map and deck
     *
     * @param map The {@link RailroadMap} on which the game will be played.
     * @param deck The {@link Deck} of cards used to play the game. This may
     *             be ANY implementation of the {@link Deck} interface,
     *             meaning that a valid implementation of the
     *             {@link RailroadBarons} interface should use only the
     */

    @Override
    public void startAGameWith(RailroadMap map, Deck deck) {
        this.deck = deck;
        startAGameWith(map);
    }

    /**
     * ends turn of current player & starts turn of the next player
     */

    @Override
    public void endTurn() {

        for (RailroadBaronsObserver observer: observers) {
            observer.turnEnded(this, currentPlayer);
        }

        if (currentPlayerIndex +1 >= 4){
            currentPlayerIndex = 0;
        } else{
            currentPlayerIndex++;
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
            return;
        }

        currentPlayer = players.get(currentPlayerIndex);
        for (RailroadBaronsObserver observer: observers) {
            observer.turnStarted(this, currentPlayer);
        }
        currentPlayer.startTurn(new PairImplementation(deck.drawACard(), deck.drawACard()));

        if(currentPlayer instanceof ComputerPlayer){
            endTurn();
        }
    }

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
     * Attempts to claim the {@linkplain RouteImplementation route} at the specified
     * location on behalf of the current {@linkplain PlayerImplementation player}.
     *
     * @param row The row of a {@link TrackImplementation} in the {@link RouteImplementation} to claim.
     * @param col The column of a {@link TrackImplementation} in the {@link RouteImplementation} to claim.
     * @throws RailroadBaronsException If the {@link RouteImplementation} cannot be claimed
     *                                 by the current player.
     */

    @Override
    public void claimRoute(int row, int col) throws RailroadBaronsException{

        if(canCurrentPlayerClaimRoute(row,col)){
            Route route = map.getRoute(row,col);
            currentPlayer.claimRoute(route);
            map.routeClaimed(route);
        }
        else{
            throw new RailroadBaronsException("The route chosen can not be claimed!");
        }
    }

    /**
     * checks if player can claim the route
     *
     * @param row The row of a {@link Track} in the {@link Route} to check.
     * @param col The column of a {@link Track} in the {@link Route} to check.
     * @return true if player can claim the route
     */

    @Override
    public boolean canCurrentPlayerClaimRoute(int row, int col) {
        Route route = map.getRoute(row,col);
        return currentPlayer.canClaimRoute(route);
    }

    /**
     * Creates and returns a hand containing 4 cards drawn from the deck
     *
     * @return hand of drawn cards
     */

    private Card[] createHand(){
        Card[] hand = new Card[4];

        for (int i = 0; i < 4; i++){
            hand[i] = deck.drawACard();
        }
        return hand;
    }
}