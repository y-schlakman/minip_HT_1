import elements.Camera;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import primitives.*;
import geometries.*;
import java.util.List;

/**
 * Integration tests for testing camera to view plane ray creation and their intersection with intersectable objects.
 *
 * @author Yosi And Eli
 */
public class IntegrationTest {
    /**
     * test method to check that camera to view plane ray creation and sphere intersections are working correctly together.
     */
    @Test
    void sphereTest() {
        Camera c;
        Sphere s;

        //TC01: first test from slides - Intersection with sphere with two intersections
        c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        s = new Sphere(new Point3D(0, 0, -3), 1);
        assertEquals(2, intersectionCounter(c, s, 3, 3), "ERROR: wrong amount of intersections found");

        //TC02: second test from slides - intersection with big sphere with 18 intersections
        c = new Camera(
                new Point3D(0, 0, 0.5),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        s = new Sphere(new Point3D(0, 0, -2.5), 2.5);
        assertEquals(18, intersectionCounter(c, s, 3, 3), "ERROR: wrong amount of intersections found");

        //TC03: third test from slides - intersection with medium sphere with 10 intersections
        c = new Camera(
                new Point3D(0, 0, 0.5),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        s = new Sphere(new Point3D(0, 0, -2), 2);
        assertEquals(10, intersectionCounter(c, s, 3, 3), "ERROR: wrong amount of intersections found");

        //TC04: fourth test from slides - intersection with huge sphere with 9 intersections
        c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        s = new Sphere(new Point3D(0, 0, 0), 250);
        assertEquals(9, intersectionCounter(c, s, 3, 3), "ERROR: wrong amount of intersections found");

        //TC05: fifth test from slides - intersection with sphere behind camera with 0 intersections
        c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        s = new Sphere(new Point3D(0, 0, 1), 0.5);
        assertEquals(0, intersectionCounter(c, s, 3, 3), "ERROR: wrong amount of intersections found");
    }

    /**
     * test method to check that camera to view plane ray creation and plane intersections are working correctly together.
     */
    @Test
    void planeTest() {
        Camera c;
        Plane p;

        //TC01: first test from slides - parallel plane with 9 intersections
        c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        p = new Plane(new Point3D(0, 0, -100), new Vector(0, 0, 1));
        assertEquals(9, intersectionCounter(c, p, 3, 3), "ERROR: wrong amount of intersections found");

        //TC02: second test from slides - non-parallel plane with 9 intersections
        c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        p = new Plane(new Point3D(0, 0, -10), new Vector(0, 0.2, 1));
        assertEquals(9, intersectionCounter(c, p, 3, 3), "ERROR: wrong amount of intersections found");

        //TC03: second test from slides - non-parallel plane with 6 intersections
        c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        p = new Plane(new Point3D(0, 0, -10), new Vector(0, 1, 1));
        assertEquals(6, intersectionCounter(c, p, 3, 3), "ERROR: wrong amount of intersections found");
    }

    /**
     * test method to check that camera to view plane ray creation and triangle intersections are working correctly together.
     */
    @Test
    void triangleTest(){
        Camera c;
        Triangle t;

        //TC01: first test from slides - triangle with one intersection point
        c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        t = new Triangle(new Point3D(0,1,-2), new Point3D(1,-1,-2), new Point3D(-1,-1,-2));
        assertEquals(1, intersectionCounter(c, t, 3, 3), "ERROR: wrong amount of intersections found");

        //TC02: second test from slides - triangle with two intersection points
        c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3, 3).setDistance(1);
        t = new Triangle(new Point3D(0,20,-2), new Point3D(1,-1,-2), new Point3D(-1,-1,-2));
        assertEquals(2, intersectionCounter(c, t, 3, 3), "ERROR: wrong amount of intersections found");
    }

    /**
     * helper function to find amount of intersections a geometry has with rays from camera to view plane
     * @param c camera
     * @param intersectable intersectable geometry
     * @param nX width of row
     * @param nY width of column
     * @return number of intersections
     */
    int intersectionCounter(Camera c, Intersectable intersectable, int nX, int nY) {
        int interCount = 0;
        Ray r;
        List<Point3D> intersections;
        for (int i = 0; i < nX; ++i) {
            for (int j = 0; j < nY; ++j) {
                r = c.constructRayThroughPixel(nX, nY, j, i);
                intersections = intersectable.findIntersections(r);
                if (intersections != null)
                    interCount += intersectable.findIntersections(r).size();
            }
        }
        return interCount;
    }
}
