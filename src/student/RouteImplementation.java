package student;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the Route interface
 * Represents a route consisting of tracks located between two stations
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class RouteImplementation implements model.Route{

    /**
     * The owner of the route
     */
    private Baron baron;

    /**
     * The routes station of origin
     */
    private Station origin;

    /**
     * The routes destination
     */
    private Station destination;

    /**
     * The routes orientation
     */
    private Orientation orientation;

    /**
     * List of tracks contained in the route
     */
    private List<Track> tracks;

    /**
     * The number of tracks in the route
     */
    private int length;

    /**
     * Creates an instance of route
     *
     * @param origin The routes station of origin
     * @param destination The routes destination
     * @param baron The route's current owner
     */

    public RouteImplementation(Station origin, Station destination, Baron baron){
        this.origin = origin;
        this.destination = destination;
        this.baron = baron;
        tracks = new ArrayList<>();
        TrackImplementation track;

        if (origin.getRow() < destination.getRow() && origin.getCol() == destination.getCol()){
            orientation = Orientation.VERTICAL;
        }
        else if (origin.getRow() == destination.getRow() && origin.getCol() < destination.getCol()){
            orientation = Orientation.HORIZONTAL;
        } else {
            System.out.println("Invalid station locations");
        }

        if (orientation.equals(Orientation.HORIZONTAL)) {

            for (int i = origin.getCol()+1; i < destination.getCol(); i++) {
                track = new TrackImplementation(this, origin.getRow(), i);
                tracks.add(track);
            }
        } else {
            for (int i = origin.getRow()+1; i < destination.getRow(); i++) {
                track = new TrackImplementation(this, i, origin.getCol());
                tracks.add(track);
            }
        }
        this.length = tracks.size();
    }

    /**
     * Returns the {@linkplain Baron} that has claimed this route. Note that
     * this route may be {@linkplain Baron#UNCLAIMED unclaimed}.
     *
     * @return The {@link Baron} that has claimed this route.
     */

    @Override
    public Baron getBaron() { return baron; }

    /**
     * Returns the {@linkplain StationImplementation station} at the beginning of this
     * route. The origin must be directly north of or to the west of the
     * destination.
     *
     * @return The {@link StationImplementation} at the beginning of this route.
     */

    @Override
    public Station getOrigin() { return origin; }

    /**
     * Returns the {@linkplain StationImplementation station} at the end of this route. The
     * destination must be directly south of or to the east of the origin.
     *
     * @return The {@link StationImplementation} at the end of this route.
     */

    @Override
    public Station getDestination() { return destination; }

    /**
     * Returns the {@linkplain Orientation orientation} of this route; either
     * {@linkplain Orientation#HORIZONTAL horizontal} or
     * {@linkplain Orientation#VERTICAL vertical}.
     *
     * @return The {@link Orientation} of this route.
     */

    @Override
    public Orientation getOrientation() { return orientation; }

    /**
     * The set of {@linkplain TrackImplementation tracks} that make up this route.
     *
     * @return The {@link List} of {@link TrackImplementation tracks} that make up this
     * route.
     */

    @Override
    public List<Track> getTracks() { return tracks; }

    /**
     * Returns the length of the route, not including the {@linkplain StationImplementation
     * stations} at the end points.
     *
     * @return The number of {@link TrackImplementation Tracks} comprising this route.
     */

    @Override
    public int getLength() { return length; }

    /**
     * Returns the number of points that this {@linkplain RouteImplementation route} is
     * worth according to the following algorithm:
     * <ul>
     * <li>1 - 1 point</li>
     * <li>2 - 2 points</li>
     * <li>3 - 4 points</li>
     * <li>4 - 7 points</li>
     * <li>5 - 10 points</li>
     * <li>6 - 15 points</li>
     * <li>7 (or more) - 5 * (length - 3) points</li>
     * </ul>
     *
     * @return The number of points that this route is worth.
     */

    @Override
    public int getPointValue() {
        switch (length) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 7;
            case 5:
                return 10;
            case 6:
                return 15;
            default:
                return (5 * (length - 3));
        }
    }

    /**
     * Returns true if the route covers the ground at the location of the
     * specified {@linkplain SpaceImplementation space} and false otherwise.
     *
     * @param space The {@link SpaceImplementation} that may be in this route.
     * @return True if the {@link SpaceImplementation Space's} coordinates are a part of
     * this route, and false otherwise.
     */

    @Override
    public boolean includesCoordinate(Space space) {
        for (Track track:tracks) {
            if (track.collocated(space)){
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to claim the route on behalf of the specified
     * {@linkplain Baron}. Only unclaimed routes may be claimed.
     *
     * @param claimant The {@link Baron} attempting to claim the route. Must
     *                 not be null or {@link Baron#UNCLAIMED}.
     * @return True if the route was successfully claimed. False otherwise.
     */

    @Override
    public boolean claim(Baron claimant) {
        if (baron.equals(Baron.UNCLAIMED)) {
            baron = claimant;
            return true;
        }
        return false;
    }
}