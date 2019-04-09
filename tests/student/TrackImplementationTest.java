package student;

import model.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * tests for TrackImplementation
 * @author Shannon & Brennan
 */
class TrackImplementationTest {

    /**
     * test for horizontal track
     */
    @Test
    public void horizontalTrack(){
        Station start = new StationImplementation(2,0,2, "start");
        Station end = new StationImplementation(4,0,11, "end");
        Route route = new RouteImplementation(start, end, Baron.UNCLAIMED);
        List<Track> tracks = route.getTracks();
        Track t = tracks.get(0);

        assertEquals("Incorrect number of tracks", 8, tracks.size());
        assertEquals("Incorrect orientation", Orientation.HORIZONTAL,
                t.getOrientation());

    }

    /**
     * test for vertical track
     */
    @Test
    public void verticalTrack(){
        Station start = new StationImplementation(2,6,2, "start");
        Station end = new StationImplementation(4,20,2, "end");
        Route route = new RouteImplementation(start, end, Baron.UNCLAIMED);
        List<Track> tracks = route.getTracks();
        Track t = tracks.get(6);

        assertEquals("Incorrect number of tracks", 13, tracks.size());
        assertEquals("Incorrect orientation", Orientation.VERTICAL,
                t.getOrientation());

    }
}