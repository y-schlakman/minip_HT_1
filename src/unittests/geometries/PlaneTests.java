package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 *
 * @author Yosi
 */
class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#Plane(Point3D, Point3D, Point3D)}
     */
    @Test
    void constructorTest() {
        Point3D p1 = new Point3D(0,0,0);
        Point3D p2 = new Point3D(1,0,0);
        Point3D p3 = new Point3D(2,0,0);

        // =============== Boundary Values Tests ==================
        //TC10: Test if exception thrown if first and second points are the same point
        assertThrows(IllegalArgumentException.class, ()->new Plane(p1,p1,new Point3D(0,1,1)), "ERROR: no exception thrown when first two points are the same");

        //TC11: Test if exception thrown if all points are on the same line
        assertThrows(IllegalArgumentException.class, ()->new Plane(p1,p2,p3), "ERROR: no exception thrown when points are on same line");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point3D)}
     */
    @Test
    void getNormal() {
        Point3D p1 = new Point3D(1,0,0);
        Point3D p2 = new Point3D(0,1,0);
        Point3D p3 = new Point3D(0,0,1);
        Plane pl = new Plane(p1,p2,p3);
        Vector normal = new Vector(-1,-1,-1).normalize();

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that getNormal() returns the correct result
        assertTrue(normal.equals(pl.getNormal(p1))||normal.equals(pl.getNormal(p1).scale(-1)),"ERROR: getNormal() wrong result");
    }
}