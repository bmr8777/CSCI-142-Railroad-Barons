package student;

import java.util.*;

/**
 * Representation of a Graph Structure with DFS and BFS included
 *
 * @param <T> the type of data to be stored in a graph
 *
 * @author Bobby
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class Graph<T> {

    /**
     * Map of vertices contained in this Graph
     */
    private final Map<T, Vertex<T>> vertices;

    /**
     * creates instance of class
     */

    public Graph() { vertices = new HashMap<>(); }

    /**
     * Adds a Vertex to the graph
     * @param data
     */

    public void addVertex(T data) {
        Vertex<T> v = new Vertex<>(data);
        vertices.put(data, v);
    }

    public Vertex<T> getVertex(T data) { return vertices.get(data); }

    /**
     * creates a bi-directional graph
     * @param data1 data to be connected
     * @param data2 data to be connected
     */

    public void connect(T data1, T data2) {
        Vertex<T> v1 = getVertex(data1);
        Vertex<T> v2 = getVertex(data2);

        v1.connectNeighbor(v2);
        v2.connectNeighbor(v1);
    }

    /**
     * breadth first search algorithm
     * @param data1 start vertex data
     * @param data2 end vertex data
     * @return true if a path exists
     *         false if no path exists
     */

    public boolean breadthFirstSearch(T data1, T data2) {
        Vertex<T> start = vertices.get(data1);
        Vertex<T> end = vertices.get(data2);

        Set<Vertex<T>> visited = new HashSet<>();
        Queue<Vertex<T>> queue = new LinkedList<>();

        visited.add(start);
        queue.add(start);

        while(!queue.isEmpty()) {
            Vertex<T> vertex = queue.poll();

            if(vertex == end) {
                return true;
            }

            for(Vertex<T> neighbor : vertex.getNeighbors()) {
                if(!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return false;
    }

    /**
     * Create a shortest path from a start Vertex to a finish Vertex if such
     * a path exists.
     * Uses breadth-first search to determine if a path exists.
     * To accomplish this, a predecessor gets associated with each Vertex.
     *
     * @rit.pre the arguments correspond to nodes in the graph
     * @param start  The object associated with the starting Vertex
     * @param end The object associated with the end Vertex
     * @return an List containing the Vertices from start to finish, or null if none exists
     */

    public List<Vertex<T>> buildPathBFS(T start, T end ) {

        // Every node we visit will have a predecessor.
        // A node has been assigned a predecessor iff it has been visited,
        // so the keys of our predecessor map create a visited set!
        Map< Vertex<T>, Vertex<T> > predecessor = new HashMap<>();

        // assumes input check occurs previously
        Vertex<T> startVertex, endVertex;
        startVertex = vertices.get( start );
        endVertex = vertices.get(end);

        predecessor.put( startVertex, null );
        // null predecessor indicates this is the starting point.
        Queue< Vertex<T> > toVisit = new LinkedList<>();
        toVisit.offer( startVertex );

        while ( !toVisit.isEmpty() && !toVisit.peek().equals( endVertex ) ) {
            Vertex<T> thisNode = toVisit.remove();
            thisNode.getNeighbors().forEach( nbr -> {
                if ( !predecessor.containsKey( nbr ) ) {
                    predecessor.put( nbr, thisNode );
                    toVisit.offer( nbr );
                }
            } );
        }

        if ( toVisit.isEmpty() ) {
            return null; // We never found the finish node.
        }
        else {
            List<Vertex<T> > path = new LinkedList<>();
            path.add( 0, endVertex );
            Vertex<T> vertex = predecessor.get( endVertex );
            while( vertex != null ) {
                path.add( 0, vertex ); // Reverse the direction to start->end
                vertex = predecessor.get( vertex );
            }
            return path;
        }
    }
}