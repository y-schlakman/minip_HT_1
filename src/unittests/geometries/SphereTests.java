package geometries;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import java.util.List;
import primitives.*;

/**
 * Unit tests for geometries.Sphere class
 *
 * @author Yosi And Eli
 */
class SphereTests {

    /**
     * Test method for {@link geometries.Sphere#Sphere(Point3D, double)}
     */
    @Test
    void constructorTest() {
        // =============== Boundary Values Tests ==================
        //TC11: Test if exception thrown for negative radius
        assertThrows(IllegalArgumentException.class, ()->new Sphere(new Point3D(0,0,0), -1), "ERROR: no exception thrown for negative radius");

        //TC12: Test if exception thrown for zero radius
        assertThrows(IllegalArgumentException.class, ()->new Sphere(new Point3D(0,0,0), 0), "ERROR: no exception thrown for zero radius");
    }

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point3D)}
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: Test if getNormal() returns the correct result
        Sphere s = new Sphere(new Point3D(0,0,0), 10);
        Point3D pOnS = new Point3D(0, 10, 0);

        assertEquals(pOnS.subtract(s.get_center()).normalize(), new Vector(0, 10, 0).normalize(), "ERROR: getNormal() wrong result");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(new Point3D(1, 0, 0), 1d);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point3D(-1, 0, 0), new Vector(1, 1, 0))),
                "ERROR: non-existing intersections found");

        // TC02: Ray starts before and crosses the sphere (2 points)
        Point3D p1 = new Point3D(0.0651530771650466, 0.355051025721682, 0);
        Point3D p2 = new Point3D(1.53484692283495, 0.844948974278318, 0);
        List<Point3D> result = sphere.findIntersections(new Ray(new Point3D(-1, 0, 0),
                new Vector(3, 1, 0)));

        assertEquals(2, result.size(), "ERROR: incorrect number of intersections");

        if (result.get(0).getX().getCoord() > result.get(1).getX().getCoord())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "ERROR: incorrect intersections");

        // TC03: Ray starts inside the sphere (1 point)
        result = sphere.findIntersections(new Ray(new Point3D(1.1d,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "ERROR: incorrect number of points");
        assertEquals(result.get(0), new Point3D(2,0,0), "ERROR: incorrect intersection point");

        // TC04: Ray starts after the sphere (0 points)
        result = sphere.findIntersections(new Ray(new Point3D(5,0,0), new Vector(1,0,0)));
        assertNull(result, "ERROR: non-existing intersections found");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        result = sphere.findIntersections(new Ray(new Point3D(1.6d,-0.8d,0), new Vector(0,1,0)));
        assertEquals(1, result.size(), "ERROR: incorrect number of points");
        assertEquals(result.get(0), new Point3D(1.6d,0.8d,0), "ERROR: incorrect intersection point");

        // TC12: Ray starts at sphere and goes outside (0 points)
        result = sphere.findIntersections(new Ray(new Point3D(1.6d,-0.8d,0), new Vector(0,-1,0)));
        assertNull(result, "ERROR: non-existing intersections found");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        result = sphere.findIntersections(new Ray(new Point3D(-1d,0,0), new Vector(1,0,0)));
        p1 = new Point3D(0, 0, 0);
        p2 = new Point3D(2, 0, 0);
        assertEquals(2, result.size(), "ERROR: incorrect number of intersections");

        if (result.get(0).getX().getCoord() > result.get(1).getX().getCoord())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "ERROR: incorrect intersections");

        // TC14: Ray starts at sphere and goes inside (1 points)
        result = sphere.findIntersections(new Ray(new Point3D(0d,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "ERROR: incorrect number of points");
        assertEquals(result.get(0), new Point3D(2d,0,0), "ERROR: incorrect intersection point");

        // TC15: Ray starts inside (1 points)
        result = sphere.findIntersections(new Ray(new Point3D(1.3d,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "ERROR: incorrect number of points");
        assertEquals(result.get(0), new Point3D(2,0,0), "ERROR: incorrect intersection point");

        // TC16: Ray starts at the center (1 points)
        result = sphere.findIntersections(new Ray(new Point3D(1,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "ERROR: incorrect number of points");
        assertEquals(result.get(0), new Point3D(2,0,0), "ERROR: incorrect intersection point");


        // TC17: Ray starts at sphere and goes outside (0 points)
        result = sphere.findIntersections(new Ray(new Point3D(0,0,0), new Vector(-1,0,0)));
        assertNull(result, "ERROR: non-existing intersections found");

        // TC18: Ray starts after sphere (0 points)
        result = sphere.findIntersections(new Ray(new Point3D(-1,0,0), new Vector(-1,0,0)));
        assertNull(result, "ERROR: non-existing intersections found");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        result = sphere.findIntersections(new Ray(new Point3D(0,0,-1), new Vector(0,0,1)));
        assertNull(result, "ERROR: non-existing intersections found");

        // TC20: Ray starts at the tangent point
        result = sphere.findIntersections(new Ray(new Point3D(0,0,0), new Vector(0,0,1)));
        assertNull(result, "ERROR: non-existing intersections found");

        // TC21: Ray starts after the tangent point
        result = sphere.findIntersections(new Ray(new Point3D(0,0,1), new Vector(0,0,1)));
        assertNull(result, "ERROR: non-existing intersections found");

        // **** Group: Special cases
        // TC19: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        result = sphere.findIntersections(new Ray(new Point3D(5,0,1), new Vector(0,0,1)));
        assertNull(result, "ERROR: non-existing intersections found");

    }

}