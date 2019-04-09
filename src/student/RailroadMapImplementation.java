package student;

import model.*;

import java.util.*;

/**
 * Implementation of the RailroadMap interface
 * Represents the map used in the RailroadBarons game
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class RailroadMapImplementation implements RailroadMap{

    /**
     * Set of RailroadMapObservers
     */
    private Set<RailroadMapObserver> observers;

    /**
     * Set of all unclaimed routes in the map
     */
    private Set<Route> unclaimedRoutes;


    /**
     * Set of all routes located in the map
     */
    private Set<Route> routes;

    /**
     * Set of all claimed routes in the map
     */
    private Set<Route> claimedRoutes;

    /**
     * HashMap of all tracks contained in the map
     */
    private Route[][] tracks;

    /**
     * HashMap of all spaces contained in the map
     */
    private Space[][] spaces;

    /**
     * List of all stations contained in the map
     */
    private List<Station> stations;

    /**
     * Number of rows in the map
     */
    private int rows;

    /**
     * Number of columns in the map
     */
    private int cols;

    /**
     * Starting row for station placement
     */
    private int min_Row;

    /**
     * Starting column for station placement
     */
    private int min_Col;

    /**
     * Map containing all stations located on the boundary's of the RailroadBarons Map
     */
    private Map<String, ArrayList<Station>> boundaryStations;

    /**
     * Creates an instance of RailroadMap
     *
     * @param routes Set of all routes contained in the map
     * @param stations List of all stations contained in the map
     */

    public RailroadMapImplementation(Set<Route> routes, List<Station> stations){
        this.routes = routes;
        this.stations = stations;
        rows = stations.stream().mapToInt(Station::getRow).max().getAsInt()+1;
        cols = stations.stream().mapToInt(Station::getCol).max().getAsInt()+1;
        min_Row = stations.stream().mapToInt(Station::getRow).min().getAsInt()+1;
        min_Col = stations.stream().mapToInt(Station::getCol).min().getAsInt()+1;
        spaces = new Space[rows][cols];
        tracks = new Route[rows][cols];
        unclaimedRoutes = new HashSet<>();
        claimedRoutes = new HashSet<>();
        observers = new HashSet<>();
        boundaryStations = new HashMap<>();
        boundaryStations.put("North", new ArrayList<>());
        boundaryStations.put("South", new ArrayList<>());
        boundaryStations.put("East", new ArrayList<>());
        boundaryStations.put("West", new ArrayList<>());

        for (Route route: routes) {
            if(route.getBaron().equals(Baron.UNCLAIMED)){
                unclaimedRoutes.add(route);
            } else{
                claimedRoutes.add(route);
            }
        }
        for(Route route: routes){
            List<Track> trackList = route.getTracks();
            for (Track track: trackList){
                tracks[track.getRow()][track.getCol()] = track.getRoute();
                spaces[track.getRow()][track.getCol()] = track;
            }
        }
        for(Station station: this.stations){
            int row = station.getRow();
            int col = station.getCol();
            spaces[row][col] = station;
            if (row == min_Row-1){
                boundaryStations.get("North").add(station);
            }
            if (row == rows-1){
                boundaryStations.get("South").add(station);
            }
            if (col == min_Col-1){
                boundaryStations.get("West").add(station);
            }
            if (col == cols-1){
                boundaryStations.get("East").add(station);
            }
        }

    }

    /**
     * Adds the specified {@linkplain RailroadMapObserver observer} to the
     * map. The observer will be notified of significant events involving this
     * map such as when a {@linkplain RouteImplementation route} has been claimed by a
     * {@linkplain model.Baron}.
     *
     * @param observer The {@link RailroadMapObserver} being added to the map.
     */

    @Override
    public void addObserver(RailroadMapObserver observer) { observers.add(observer); }

    /**
     * Removes the specified {@linkplain RailroadMapObserver observer} from
     * the map. The observer will no longer be notified of significant events
     * involving this map.
     *
     * @param observer The observer to remove from the collection of
     *                 registered observers that will be notified of
     *                 significant events involving this map.
     */

    @Override
    public void removeObserver(RailroadMapObserver observer) { observers.remove(observer); }

    /**
     * Returns the number of rows in the map. This is determined by the
     * location of the southernmost {@linkplain StationImplementation station} on the map.
     *
     * @return The number of rows in the map.
     */

    @Override
    public int getRows() { return rows; }

    /**
     * Returns the number of columns in the map. This is determined by the
     * location of the easternmost {@linkplain StationImplementation station} on the map.
     *
     * @return The number of columns in the map.
     */

    @Override
    public int getCols() { return cols; }

    /**
     * Returns the {@linkplain SpaceImplementation space} located at the specified
     * coordinates.
     *
     * @param row The row of the desired {@link SpaceImplementation}.
     * @param col The column of the desired {@link SpaceImplementation}.
     * @return The {@link SpaceImplementation} at the specified location, or null if the
     * location doesn't exist on this map.
     */

    @Override
    public Space getSpace(int row, int col) { return spaces[row][col]; }

    /**
     * Returns the {@linkplain RouteImplementation route} that contains the
     * {@link TrackImplementation track} at the specified location (if such a route exists}.
     *
     * @param row The row of the location of one of the {@link TrackImplementation tracks}
     *            in the route.
     * @param col The column of the location of one of the
     *            {@link TrackImplementation tracks} in the route.
     * @return The {@link RouteImplementation} that contains the {@link TrackImplementation} at the
     * specified location, or null if there is no such {@link RouteImplementation}.
     */

    @Override
    public Route getRoute(int row, int col) { return tracks[row][col]; }

    /**
     * Called to update the {@linkplain RailroadMapImplementation map} when a
     * {@linkplain model.Baron} has claimed a {@linkplain RouteImplementation route}.
     *
     * @param route The {@link RouteImplementation} that has been claimed.
     */

    @Override
    public void routeClaimed(Route route) {
        unclaimedRoutes.remove(route);
        claimedRoutes.add(route);
        for (RailroadMapObserver observer: observers) {
            observer.routeClaimed(this, route);
        }
    }

    /**
     * Returns the length of the shortest unclaimed {@linkplain RouteImplementation route}
     * in the map.
     *
     * @return The length of the shortest unclaimed {@link RouteImplementation}.
     */

    @Override
    public int getLengthOfShortestUnclaimedRoute() {
        if (unclaimedRoutes.size() == 0){
            return 0;
        }else {
            return unclaimedRoutes.stream().mapToInt(Route::getLength).min().getAsInt();
        }
    }

    /**
     * Returns all of the {@link RouteImplementation Routes} in this map.
     *
     * @return A {@link Collection} of all of the {@link RouteImplementation Routes} in this
     * RailroadMap.
     */

    @Override
    public Collection<Route> getRoutes() { return routes; }

    /**
     * Returns Map containing all Boundary Stations located on the RailroadBarons Map
     *
     * @return boundaryStations
     */

    public Map<String, ArrayList<Station>> getBoundaryStations(){
        return boundaryStations;
    }

    /**
     * Returns List containing all stations located on the RailroadBarons Map
     *
     * @return stations
     */

    public List<Station> getStations(){
        return stations;
    }

    /**
     * get the set of unclaimed routes in this map
     * @return unclaimedRoutes
     */
    public Set<Route> getUnclaimedRoutes(){ return unclaimedRoutes; }
}