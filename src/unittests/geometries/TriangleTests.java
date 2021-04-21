package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

/**
 * Unit tests for geometries.Triangle class
 *
 * @author Yosi And Eli
 */
class TriangleTests {

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point3D)}
     */
    @Test
    void getNormal() {
        Point3D p1 = new Point3D(1,0,0);
        Point3D p2 = new Point3D(0,1,0);
        Point3D p3 = new Point3D(0,0,1);
        Triangle tr = new Triangle(p1,p2,p3);
        Vector normal = new Vector(-1,-1,-1).normalize();

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that getNormal() returns the correct result
        assertTrue(normal.equals(tr.getNormal(p1))||normal.equals(tr.getNormal(p1).scale(-1)),"ERROR: getNormal() wrong result");
    }


}