package student;

import model.Baron;
import model.Orientation;
import model.Route;
import model.Station;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertEquals;

/**
 * tests for RouteImplementation
 * @author Shannon & Brennan
 */
class RouteImplementationTest {

    /**
     * tests for a vertical route on board
     */
    @Test
    public void verticalTest(){

        Station start = new StationImplementation(2,9,9, "start");
        Station end = new StationImplementation(4,11,9, "end");
        Route route = new RouteImplementation(start, end, Baron.UNCLAIMED);

        assertEquals("This route should be vertical!", Orientation.VERTICAL,
                route.getOrientation());
        assertEquals("This should be unclaimed!", Baron.UNCLAIMED,
                route.getBaron());
        assertEquals("Wrong length",1 , route.getLength());
        assertEquals("Wrong point value", 1, route.getPointValue());

    }

    /**
     * tests for a horizontal route on board
     */
    @Test
    public void horizontalTest(){
        Station start = new StationImplementation(2,0,2, "start");
        Station end = new StationImplementation(4,0,11, "end");
        Route route = new RouteImplementation(start, end, Baron.UNCLAIMED);

        assertEquals("This route should be horizontal!", Orientation.HORIZONTAL,
                route.getOrientation());
        route.claim(Baron.RED);
        assertEquals("This should be claimed the Red baron!", Baron.RED,
                route.getBaron());
        assertEquals("Wrong length",8 , route.getLength());
        assertEquals("Wrong point value", 25, route.getPointValue());



    }

}