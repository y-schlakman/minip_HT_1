package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 *
 * @author Yosi And Eli
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
        //TC11: Test if exception thrown if first and second points are the same point
        assertThrows(IllegalArgumentException.class, ()->new Plane(p1,p1,new Point3D(0,1,1)), "ERROR: no exception thrown when first two points are the same");

        //TC12: Test if exception thrown if all points are on the same line
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

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Plane plane = new Plane(new Point3D(1,0,1), new Point3D(0,1,1), new Point3D(1,1,1));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects plane.
        List<Point3D> result = plane.findIntersections(new Ray(new Point3D(3,3,3), new Vector(-1,-1,-1)));
        assertEquals(1, result.size(), "ERROR: incorrect amount of intersections");
        assertEquals(result.get(0), new Point3D(1,1,1), "ERROR: incorrect intersection point");

        // TC02: Ray does not intersect plane.
        result = plane.findIntersections(new Ray(new Point3D(3,3,3), new Vector(1,1,1)));
        assertNull(result, "ERROR: non-existing intersections found");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray is parallel to plane.

        // TC03: Ray parallel and included inside plane.
        result = plane.findIntersections(new Ray(new Point3D(3,3,1), new Vector(62,490,0)));
        assertNull(result, "ERROR: infinite intersections should not be included");

        // TC04: Ray parallel to but not included inside plane.
        result = plane.findIntersections(new Ray(new Point3D(3,3,3), new Vector(62,490,0)));
        assertNull(result, "ERROR: non-existing intersections found");

        // **** Group: Ray is orthogonal to plane.

        // TC05: Ray starts before plane.
        result = plane.findIntersections(new Ray(new Point3D(3,3,0), new Vector(0,0,1)));
        assertEquals(1, result.size(), "ERROR: incorrect amount of intersections");
        assertEquals(result.get(0), new Point3D(3,3,1), "ERROR: incorrect intersection point");

        // TC06: Ray starts inside plane.
        result = plane.findIntersections(new Ray(new Point3D(3,3,1), new Vector(0,0,1)));
        assertNull(result, "ERROR: boundary intersections should not be included");

        // TC07: Ray starts after plane.
        result = plane.findIntersections(new Ray(new Point3D(3,3,2), new Vector(0,0,1)));
        assertNull(result, "ERROR: non-existing intersections found");

        // **** Group: Ray is neither parallel nor orthogonal to plane.

        // TC08: Ray begins inside plane (and 'goes' out).
        result = plane.findIntersections(new Ray(new Point3D(5,5,1), new Vector(3,6,9)));
        assertNull(result, "ERROR: non-existing intersections found");

        // TC09: Ray begins in plane's origin (and 'goes' out).
        result = plane.findIntersections(new Ray(new Point3D(1,0,1), new Vector(3,6,9)));
        assertNull(result, "ERROR: non-existing intersections found");

    }
}