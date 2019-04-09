
package student;

import model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;


/**
 * Extends PlayerImplementation to represent a Computer Player
 * Also observes changes to game board to be able to make valid moves
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */
public class ComputerPlayer extends PlayerImplementation{

    private Collection<Route> unclaimedRoutes;

    private RailroadMap map;

    /**
     * creates instance of ComputerPlayer
     * @param baron the Baron the player is playing the game as
     */
    public ComputerPlayer(Baron baron){
        super(baron);
        unclaimedRoutes = new HashSet<>();
    }

    @Override
    public void startTurn(Pair dealt){
        super.startTurn(dealt);
        try {
            autoClaimRoute();
        }
        catch(RailroadBaronsException e){
            e.printStackTrace();
        }
    }

    /**
     * claims the first route available with the cards in hand
     * @throws RailroadBaronsException
     */
    public void autoClaimRoute() throws RailroadBaronsException{
        for(Route r: unclaimedRoutes){
            if(canClaimRoute(r)){
                claimRoute(r);
                map.routeClaimed(r);
                return;
            }
        }
    }


//    /**
//     * updates the collection of unclaimed routes
//     */
//    @Override
//    public void routeClaimed(RailroadMap map, Route route){
//        RailroadMapImplementation m = (RailroadMapImplementation) map;
//        unclaimedRoutes = m.getUnclaimedRoutes();
//
//    }

    /**
     * add the unclaimed routes from the map
     * @param map Railroad map for game play
     */
    public void addUnclaimedRoutes(RailroadMap map){
        RailroadMapImplementation m = (RailroadMapImplementation) map;
        unclaimedRoutes = m.getUnclaimedRoutes();
        this.map = map;
    }


}