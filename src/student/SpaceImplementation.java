package student;

import model.Space;

/**
 * Implementation of the Space interface
 * Represents a space on the RailroadBarons Map
 *
 * @author Shannon Quinn
 * @author Brennan Reed
 */

public class SpaceImplementation implements Space {

    /**
     * Row value of the space
     */
    private int row;

    /**
     * Column value of the space
     */
    private int col;

    /**
     * Creates an instance of space
     *
     * @param row The row in which the space is located on the map
     * @param col The column in which the space is located on the map
     */

    public SpaceImplementation(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row of the space's location in the map.
     *
     * @return The row of the space's location in the map.
     */

    @Override
    public int getRow() { return row; }

    /**
     * Returns the column of the space's location in the map.
     *
     * @return The column of the space's location in the map.
     */

    @Override
    public int getCol() { return col; }

    /**
     * Returns true if the other space is occupying the same physical location
     * in the map as this space.
     *
     * @param other The other space to which this space is being compared for
     *              collocation.
     * @return True if the two spaces are in the same physical location (row
     * and column) in the map; false otherwise.
     */

    @Override
    public boolean collocated(Space other) { return (col == other.getCol() && row == other.getRow()); }
}