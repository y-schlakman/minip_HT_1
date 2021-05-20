package primitives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for primitives.Ray class
 *
 * @author Yosi and Eli
 */
class RayTest {

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}
     */
    @Test
    void findClosestPoint() {
        Ray r = new Ray(new Point3D(0,0,0), new Vector(1,2,3));
        List<Point3D> points = new ArrayList<>();

        // ============ Equivalence Partitions Tests ==============
        // TC01: test when point in middle of the list is closest
        points.add(new Point3D(0,2,0));
        points.add(new Point3D(1,0,0));
        points.add(new Point3D(0,0,4));
        assertEquals(new Point3D(1,0,0), r.findClosestPoint(points), "closest point is incorrect");

        // =============== Boundary Values Tests ==================
        // TC11: test when list is empty
        points.clear();
        assertNull(r.findClosestPoint(points), "closest point didn't return null when list of points is empty");

        // TC12: test when first point is closest
        points.clear();
        points.add(new Point3D(1,0,0));
        points.add(new Point3D(0,2,0));
        points.add(new Point3D(0,0,4));
        assertEquals(new Point3D(1,0,0), r.findClosestPoint(points), "closest point is incorrect");

        // TC13: test when first point is closest
        points.clear();
        points.add(new Point3D(0,2,0));
        points.add(new Point3D(0,0,4));
        points.add(new Point3D(1,0,0));
        assertEquals(new Point3D(1,0,0), r.findClosestPoint(points), "closest point is incorrect");
    }
}