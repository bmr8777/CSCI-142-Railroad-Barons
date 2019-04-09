package student;

import model.Space;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

/**
 * tests for SpaceImplementation
 * @author Shannon & Brennan
 */
class SpaceImplementationTest {

    /**
     * tests to see if collocated returns correctly
     */
    @Test
    void collocated() {

        Space space1 = new SpaceImplementation(5,8);
        Space space2 = new SpaceImplementation(7,8);
        assertEquals("These spaces shouldn't be equal!", false,
                space1.collocated(space2));

        Space space3 = new SpaceImplementation(7,7);
        Space space4 = new SpaceImplementation(7,7);
        assertEquals("These spaces should be equal!", true,
                space3.collocated(space4));

    }
}