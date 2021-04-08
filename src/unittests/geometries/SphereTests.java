package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
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
}