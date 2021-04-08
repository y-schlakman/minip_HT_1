package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.*;

/**
 * Unit tests for geometries.Tube class
 *
 * @author Yosi
 */
class TubeTests {

    /**
     * Test method for {@link geometries.Tube#getNormal(Point3D)}
     */
    @Test
    void constructorTest() {
        Ray r = new Ray(new Point3D(0,0,0), new Vector(1,2,3));

        // =============== Boundary Values Tests ==================
        //TC10: Test if exception thrown when radius is negative
        assertThrows(IllegalArgumentException.class, ()->new Tube(r, -1), "ERROR: no exception thrown when radius negative");

        //TC11: Test if exception thrown when radius is zero
        assertThrows(IllegalArgumentException.class, ()->new Tube(r, 0), "ERROR: no exception thrown when radius zero");
    }

    /**
     * Test method for {@link geometries.Tube#getNormal(Point3D)}
     */
    @Test
    void getNormal() {
        Point3D head = new Point3D(0,0,0);
        Vector dir = new Vector(0,1,0);
        Ray r = new Ray(head, dir);
        Tube t = new Tube(r, 5);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test if getNormal() returns the correct answer for points that don't form 90 degree angle with axis ray when connected to ray's head
        Point3D pOnTEasy = new Point3D(5, 5, 0);
        assertEquals(new Vector(5,0,0).normalize(), t.getNormal(pOnTEasy), "ERROR: getNormal() regular case gave wrong result");

        // =============== Boundary Values Tests ==================
        //TC10: Test if getNormal() returns the correct answer for points that form 90 degree angle with axis ray when connected to ray's head
        Point3D pOnTHard = new Point3D(5, 0, 0);
        assertEquals(new Vector(5,0,0).normalize(),t.getNormal(pOnTHard),"ERROR: getNormal() boundary case gave wrong result");
    }
}