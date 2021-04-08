package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 *
 * @author Yosi And Eli
 */
class CylinderTests {

    /**
     * Test method for {@link geometries.Cylinder#Cylinder(Ray, double, double)}
     */
    @Test
    void constructorTest() {
        Ray ray = new Ray(new Point3D(0,0,0), new Vector(1,2,3));

        // =============== Boundary Values Tests ==================
        //TC11: Test if exception is thrown for radius of negative length
        assertThrows(IllegalArgumentException.class, ()->new Cylinder(ray, -1, 10), "ERROR: exception not thrown for negative radius");

        //TC12: Test if exception is thrown for radius of length zero
        assertThrows(IllegalArgumentException.class, ()->new Cylinder(ray, 0, 10), "ERROR: exception not thrown for zero radius");

        //TC13: Test if exception is thrown for height of negative length
        assertThrows(IllegalArgumentException.class, ()->new Cylinder(ray, 10, -1), "ERROR: exception not thrown for negative height");

        //TC14: Test if exception is thrown for height of length zero
        assertThrows(IllegalArgumentException.class, ()->new Cylinder(ray, 10, 0), "ERROR: exception not thrown for zero height");
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point3D)}
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // =============== Boundary Values Tests ==================
    }
}