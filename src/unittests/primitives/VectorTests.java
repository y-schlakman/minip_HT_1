package primitives;

import org.junit.jupiter.api.Test;
import primitives.*;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 *
 * @author Yosi
 */
class VectorTests {

    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}
     */
    @Test
    void testConstructorXYZ() {
        // =============== Boundary Values Tests ==================
        //TC10: Test to check that exception is thrown when vector constructor is passed zero vector
        assertThrows(IllegalArgumentException.class,() -> new Vector(0, 0, 0), "ERROR: zero vector does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#Vector(Point3D)}
     */
    @Test
    void testConstructorHead() {
        // =============== Boundary Values Tests ==================
        //TC10: Test to check that exception is thrown when vector constructor is passed zero vector
        assertThrows(IllegalArgumentException.class,() -> new Vector(new Point3D(0,0,0)), "ERROR: zero vector does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#add(Vector)}
     */
    @Test
    void add() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that add() returns the correct result vector
        assertEquals(new Vector(-1, -2, -3), v1.add(v2), "ERROR: add() wrong result");

        // =============== Boundary Values Tests ==================
        //TC10: Test that if add() result is zero than an exception is thrown
        assertThrows(IllegalArgumentException.class, ()->v1.add(v1.scale(-1)), "ERROR: add() doesn't throw error when result is zero");
    }

    /**
     * Test method for {@link primitives.Vector#subtract(Vector)}
     */
    @Test
    void subtract() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that subtract() returns the correct result vector
        assertEquals(new Vector(3, 6, 9), v1.subtract(v2), "ERROR: subtract() wrong result");

        // =============== Boundary Values Tests ==================
        //TC10: Test that if subtract() result is zero than an exception is thrown
        assertThrows(IllegalArgumentException.class, ()->v1.subtract(v1), "ERROR: subtract() doesn't throw error when result is zero");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}
     */
    @Test
    void scale() {
        Vector v1 = new Vector(1,2,3);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that scale() returns the correct result vector
        assertEquals(new Vector(1*-0.5,2*-0.5,3*-0.5), v1.scale(-0.5), "ERROR: scale() wrong result");

        // =============== Boundary Values Tests ==================
        //TC10: Test that scale() by scaling factor of zero throws an exception
        assertThrows(IllegalArgumentException.class, ()->v1.scale(0), "ERROR: scale() by scaling factor of zero doesn't throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}
     */
    @Test
    void dotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============
        //TC01: test if dotProduct() of orthogonal vectors returns zero as expected
        assertTrue(isZero(v1.dotProduct(v3)), "ERROR: dotProduct() for orthogonal vectors is not zero");

        //TC02: test if dotProduct() returns the right value
        assertTrue(isZero(v1.dotProduct(v2)+28), "ERROR: dotProduct() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}
     */
    @Test
    void crossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v3);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that length of cross-product is proper
        assertTrue(isZero(v1.length() * v3.length()- vr.length()), "ERROR: crossProduct() wrong result length");

        //TC02: Test to see if crossProduct() result is orthogonal to its operands as expected
        assertTrue(isZero(vr.dotProduct(v1)), "ERROR: crossProduct() result is not orthogonal to first operand");
        assertTrue(isZero(vr.dotProduct(v3)), "ERROR: crossProduct() result is not orthogonal to second operand");

        // =============== Boundary Values Tests ==================
        //TC10: Test to see if crossProduct() for parallel vectors throws an exception as expected
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v2), "ERROR: crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}
     */
    @Test
    void lengthSquared() {
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that lengthSquared() returns the actual length squared of vector
        assertTrue(isZero(v1.lengthSquared()-14), "ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link Vector#length()}
     */
    @Test
    void length() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that length() returns the actual length of the vector
        assertTrue(isZero(new Vector(0, 3, 4).length()- 5), "ERROR: length() wrong value");
    }

    /**
     * Test method for {@link Vector#normalize()}
     */
    @Test
    void normalize() {
        Vector v = new Vector(1, 2, 3);
        Vector vCopy = new Vector(v.getHead());
        Vector vCopyNormalize = vCopy.normalize();

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test if normalize() normalizes the current vector as opposed to creating a new normalized vector
        assertEquals(vCopy, vCopyNormalize, "ERROR: normalize() function creates a new vector");

        //TC02: Test if normalize() result is a normalized vector (has length of 1)
        assertTrue(isZero(1 - vCopyNormalize.length()), "ERROR: normalize() result is not a unit vector");
    }

    /**
     * Test method for {@link Vector#normalized()}
     */
    @Test
    void normalized() {
        Vector v = new Vector(1, 2, 3);
        Vector vCopy = new Vector(v.getHead());
        Vector vCopyNormalized = vCopy.normalized();

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test if normalized() creates a new normalized vector instead of normalizing current vector
        assertNotEquals(vCopy, vCopyNormalized, "ERROR: normalized() function doesn't create a new vector");

        //TC02: Test if normalized() result is a normalized vector (has length of 1)
        assertTrue(isZero(1 - vCopyNormalized.length()), "ERROR: normalized() result is not a unit vector");
    }
}