package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class AABBTests {
    /**
     * Test method for {@link geometries.AABB#AABB(Point3D, Point3D)}
     */
    @Test
    void constructorTest() {
        // ============ Equivalence Partitions Tests ==============
        //TC00: tests if AABB calculates the right min and max point.
        assertEquals(new AABB(new Point3D(3, 3, 1), new Point3D(3, 3, 1)).getMin(), new Point3D(2.9, 2.9, 0.9), "unexpected result for min point");
        assertEquals(new AABB(new Point3D(3, 3, 1), new Point3D(3, 3, 1)).getMax(), new Point3D(3.1, 3.1, 1.1), "unexpected result for max point");
        // =============== Boundary Values Tests ==================
        //TC10: tests if throws exception when min isn't minimum and max isn't maximum.
        assertThrows(IllegalArgumentException.class, () -> new AABB(new Point3D(3, 3, 1), new Point3D(4, 2, 0)), "didn't throw exception when max bigger than min");
    }

    /**
     * Test method for {@link geometries.AABB#hasIntersection(Ray)}
     */
    @Test
    void hasIntersectionTest() {
        // ============ Equivalence Partitions Tests ==============
        //TC00: tests if hasIntersections returns true when there is an intersection with a ray.
        AABB aabb = new AABB(new Point3D(3, 3, 1), new Point3D(3, 3, 1));
        assertTrue(aabb.hasIntersection(new Ray(new Point3D(0, 0, 0), new Vector(3, 3, 1))));
        //TC00: tests if hasIntersections returns false when there is no intersection with a ray.
        assertFalse(aabb.hasIntersection(new Ray(new Point3D(1000, 1000, 1000), new Vector(3, 3, 1))));
    }
}
