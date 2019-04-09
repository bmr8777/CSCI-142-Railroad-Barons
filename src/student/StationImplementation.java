package student;

import model.Space;
import model.Station;

/**
 * Implementation of the Station interface
 * Represents a train station on the RailroadBaron Map
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class StationImplementation extends SpaceImplementation implements Station {

    /**
     * The name of the station
     */
    private String name;

    /**
     * The stations number in the map file
     */
    private int stationNumber;

    /**
     * Creates an instance of Station
     *
     * @param stationNumber the stations number in the map file
     * @param row The row of the space's location in the map
     * @param col The column of the space's location in the map
     * @param name The name of the station
     */

    public StationImplementation(int stationNumber, int row, int col, String name){
        super(row,col);
        this.stationNumber = stationNumber;
        this.name = name;
    }

    /**
     * The name of the station
     *
     * @return The name of the station.
     */

    @Override
    public String getName() { return name; }
}