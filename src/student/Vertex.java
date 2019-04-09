package student;

import java.util.*;

/**
 * Representation of a vertex in a graph
 * @param <T> any data to be stored in the vertex
 *
 * @ Bobby
 */

public class Vertex<T> {

    /**
     * type of data stored in this class
     */
    private T data;

    /**
     * list of neighbors in the graph
     */
    private Set<Vertex<T>> neighbors;


    /**
     * creates a Vertex
     * @param data what to store in this vertex
     */

    public Vertex(T data) {
        this.data = data;
        neighbors = new HashSet<>();
    }

    public Collection<Vertex<T>> getNeighbors() { return neighbors; }

    /**
     * get the data in this vertex
     * @return T
     */
    public T getData() { return data; }

    /**
     * adds a vertex to list of neighbors
     * @param neighbor vertex that is a neighbor to this vertex
     */
    public void connectNeighbor(Vertex<T> neighbor) { neighbors.add(neighbor); }
}