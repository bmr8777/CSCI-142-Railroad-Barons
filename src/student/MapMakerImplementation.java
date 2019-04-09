package student;

import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * Implementation of the MapMaker interface
 * Can load and save RailroadBarons Maps
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class MapMakerImplementation implements MapMaker {

    /**
     * Default parameter-less constructor
     */

    public MapMakerImplementation(){

    }

    /**
     * Loads a {@linkplain RailroadMapImplementation map} using the data in the given
     * {@linkplain InputStream input stream}.
     *
     * @param in The {@link InputStream} used to read the {@link RailroadMapImplementation
     *           map} data.
     * @return The {@link RailroadMapImplementation map} read from the given
     * {@link InputStream}.
     * @throws RailroadBaronsException If there are any problems reading the
     *                                 data from the {@link InputStream}.
     */

    @Override
    public RailroadMap readMap(InputStream in) throws RailroadBaronsException {
        try {
            Scanner scanner = new Scanner(in);
            Set<Route> routes = new HashSet<>();
            Map<Integer, Station> stations = new HashMap<>();
            ArrayList<Station> stationList = new ArrayList<>();
            Station station, origin, destination;
            RouteImplementation route;
            Baron owner;
            String line, name, ownerName;
            String[] splitLine;
            int row, col, stationNumber;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.equals("##ROUTES##")){
                    break;
                }
                splitLine = line.split("\\s+", 4);
                stationNumber = Integer.parseInt(splitLine[0]);
                row = Integer.parseInt(splitLine[1]);
                col = Integer.parseInt(splitLine[2]);
                name = splitLine[3];
                station = new StationImplementation(stationNumber, row, col, name);
                stations.put(stationNumber, station);
                stationList.add(station);
            }

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                splitLine = line.split("\\s+");
                origin = stations.get(Integer.parseInt(splitLine[0]));
                destination = stations.get(Integer.parseInt(splitLine[1]));
                ownerName = splitLine[2];
                owner = Baron.valueOf(ownerName);
                route = new RouteImplementation(origin, destination, owner);
                routes.add(route);
            }
            scanner.close();
            return new RailroadMapImplementation(routes, stationList);
        }
        catch (Exception e){
            throw new RailroadBaronsException("There were problems reading the data from the input stream.");
        }
    }

    /**
     * Writes the specified {@linkplain RailroadMapImplementation map} in the Railroad
     * Barons map file format to the given {@linkplain OutputStream output
     * stream}. The written map should include an accurate record of any
     * routes that have been claimed, and by which {@linkplain Baron}.
     *
     * @param map The {@link RailroadMapImplementation map} to write out to the
     *            {@link OutputStream}.
     * @param out The {@link OutputStream} to which the
     *            {@link RailroadMapImplementation map} data should be written.
     * @throws RailroadBaronsException If there are any problems writing the
     *                                 data to the {@link OutputStream}.
     */

    @Override
    public void writeMap(RailroadMap map, OutputStream out) throws RailroadBaronsException {
        PrintStream printStream = new PrintStream(out);
        Collection<Route> routes = map.getRoutes();
        Map<Integer, Station> stations = new HashMap<>();
        Collection<Station> stationList  = new HashSet<>();
        int row, col, stationNumber, originNumber, destinationNumber;
        String name, line;
        Station origin, destination, station;
        stationNumber = 0;
        for (Route route: routes) {
            origin = route.getOrigin();
            destination = route.getDestination();
            if (!stationList.contains(origin)){
                stationList.add(origin);
                stations.put(stationNumber, origin);
                stationNumber++;
            }
            if (!stationList.contains(destination)){
                stationList.add(destination);
                stations.put(stationNumber, destination);
                stationNumber++;
            }
        }

        for (int i = 0; i < stations.size(); i++){
            station = stations.get(i);
            name = station.getName();
            row = station.getRow();
            col = station.getCol();
            line = i + " " + row + " " + col + " " + name;
            printStream.println(line);
        }
        printStream.println("##ROUTES##");
        originNumber = 0;
        destinationNumber = 0;
        for (Route route: routes) {
            origin = route.getOrigin();
            destination = route.getDestination();
            for (Integer key: stations.keySet()) {
                if (stations.get(key).collocated(origin)){
                    originNumber = key;
                }
                if (stations.get(key).collocated(destination)){
                    destinationNumber = key;
                }
            }
            printStream.println(originNumber + " " + destinationNumber + " " + route.getBaron());
        }
        printStream.close();
    }


}