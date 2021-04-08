package primitives;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for primitives.Point3D class
 *
 * @author Yosi
 */
class Point3DTests {

    /**
     * Test method for {@link primitives.Point3D#add(Vector)}
     */
    @Test
    void add() {
        Point3D p1 = new Point3D(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test if add() result is correct
        assertEquals(Point3D.ZERO, p1.add(new Vector(-1, -2, -3)), "ERROR: add() wrong result");
    }

    /**
     * Test method for {@link primitives.Point3D#subtract(Point3D)}
     */
    @Test
    void subtract() {
        Point3D p1 = new Point3D(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test if subtract() result is correct
        assertEquals(new Vector(1, 1, 1), new Point3D(2, 3, 4).subtract(p1), "ERROR: subtract() wrong result");
    }

    /**
     * Test method for {@link primitives.Point3D#distanceSquared(Point3D)}
     */
    @Test
    void distanceSquared() {
        Point3D p1 = new Point3D(1, 2, 3);
        Point3D p2 = new Point3D(2, 3, 4);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test if distanceSquared() result is correct
        assertTrue(isZero(p1.distanceSquared(p2) - 3), "ERROR: distanceSquared() wrong result");
    }

    /**
     * Test method for {@link primitives.Point3D#distance(Point3D)}
     */
    @Test
    void distance() {
        Point3D p1 = new Point3D(1, 2, 3);
        Point3D p2 = new Point3D(2, 3, 4);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test if distance() result is correct
        assertTrue(isZero(p1.distance(p2) - Math.sqrt(3)), "ERROR: distance() wrong result");
    }
}