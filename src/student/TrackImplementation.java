package student;

import model.Baron;
import model.Orientation;
import model.Route;
import model.Track;

/**
 * Implementation of the Track interface
 * Represents a track segment on the RailroadBarons Map
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class TrackImplementation extends SpaceImplementation implements Track {

    /**
     * The orientation of the track
     */
    private Orientation orientation;

    /**
     * The route that the track is contained in
     */
    private Route route;

    /**
     * Creates an instance of Track
     *
     * @param route the route in which the track is contained - might have to be added in a separate method
     * @param row   the row in the map where the track is located
     * @param col   the column in the map where the track is located
     */

    public TrackImplementation(Route route, int row, int col) {
        super(row, col);
        orientation = route.getOrientation();
        this.route = route;
    }

    /**
     * Returns the orientation of the track; either
     * {@linkplain Orientation#HORIZONTAL horizontal} or
     * {@linkplain Orientation#VERTICAL vertical}. This is based on the
     * {@linkplain Orientation orientation} of the {@linkplain model.Route route}
     * that contains the track.
     *
     * @return The {@link Orientation} of the {@link TrackImplementation}; this is the same
     * as the {@link Orientation} of the {@link model.Route} that contains the
     * track.
     */

    @Override
    public Orientation getOrientation() { return orientation; }

    /**
     * Returns the current {@link Baron owner} of this track, either
     * {@linkplain Baron#UNCLAIMED unclaimed} if the track has not been
     * claimed, or the {@linkplain Baron owner} that corresponds with the
     * color of the player that successfully claimed the
     * {@linkplain model.Route route} of which this track is a part.
     *
     * @return The {@link Baron} that has claimed the route of which this
     * track is a part.
     */

    @Override
    public Baron getBaron() { return route.getBaron(); }

    /**
     * Returns the {@linkplain model.Route route} of which this
     * {@linkplain TrackImplementation track} is a part.
     *
     * @return The {@link model.Route} that contains this track.
     */

    @Override
    public Route getRoute() { return route; }
}