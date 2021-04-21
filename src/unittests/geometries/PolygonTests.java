package geometries;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;

import java.util.List;

/**
 * Testing Polygons
 *
 * @author Dan
 *
 */
public class PolygonTests {

    /**
     * Test method for
     * {@link geometries.Polygon##Polygon(primitives.Point3D, primitives.Point3D, primitives.Point3D, primitives.Point3D)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(0, 1, 0),
                    new Point3D(1, 0, 0), new Point3D(-1, 1, 1));
            fail("Constructed a polygon with wrong order of vertices");
        } catch (IllegalArgumentException e) {}

        // TC03: Not in the same plane
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 2, 2));
            fail("Constructed a polygon with vertices that are not in the same plane");
        } catch (IllegalArgumentException e) {}

        // TC04: Concave quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0.5, 0.25, 0.5));
            fail("Constructed a concave polygon");
        } catch (IllegalArgumentException e) {}

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0.5, 0.5));
            fail("Constructed a polygon with vertix on a side");
        } catch (IllegalArgumentException e) {}

        // TC11: Last point = first point
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0, 1));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

        // TC12: Colocated points
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 1, 0));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Polygon pl = new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0),
                new Point3D(-1, 1, 1));
        double sqrt3 = Math.sqrt(1d / 3);
        assertTrue(new Vector(sqrt3, sqrt3, sqrt3).equals(pl.getNormal(new Point3D(0, 0, 1)))||new Vector(sqrt3, sqrt3, sqrt3).equals(pl.getNormal(new Point3D(0, 0, 1)).scale(-1)), "Bad normal to triangle");
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(Ray)}
     */
    @Test
    void findIntersections(){
        Polygon polygon = new Polygon(new Point3D(5,0,1), new Point3D(0,0,1), new Point3D(0,5,1), new Point3D(5,5,1));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Intersection is inside polygon.
        List<Point3D> result = polygon.findIntersections(new Ray(new Point3D(1,1,0), new Vector(0,0,1)));
        assertEquals(1, result.size(), "ERROR: incorrect amount of intersections");
        assertEquals(new Point3D(1,1,1), result.get(0));

        // TC02: Intersection is outside against edge.
        result = polygon.findIntersections(new Ray(new Point3D(-1,1,0), new Vector(0,0,1)));
        assertNull(result, "ERROR: non-existing intersections found");

        // TC03: Intersection is outside against vertex.
        result = polygon.findIntersections(new Ray(new Point3D(-1,-1,0), new Vector(0,0,1)));
        assertNull(result, "ERROR: non-existing intersections found");

        // =============== Boundary Values Tests ==================

        // **** Group: Intersection is on an edge.

        // TC04: Intersection is on an edge.
        result = polygon.findIntersections(new Ray(new Point3D(1,0,0), new Vector(0,0,1)));
        assertNull(result, "ERROR: boundary intersections should not count");

        // **** Group: Intersection is on a vertex.

        // TC05: Intersection is on a vertex.
        result = polygon.findIntersections(new Ray(new Point3D(0,0,0), new Vector(0,0,1)));
        assertNull(result, "ERROR: boundary intersections should not count");

        // **** Group: Intersection is on an edges continuation.

        // TC05: Intersection is on an edges continuation.
        result = polygon.findIntersections(new Ray(new Point3D(-1,0,0), new Vector(0,0,1)));
        assertNull(result, "ERROR: non-existing intersections found");

    }

}
