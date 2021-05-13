import elements.Camera;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.*;
import geometries.*;


public class IntegrationTest {
    @Test
    void sphereTest() {
        Camera c = new Camera(
                new Point3D(0, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0)
        ).setViewPlaneSize(3,3).setDistance(1);

        //TC01: Intersection with sphere when it is in front of view plane, and its radius doesn't reach it.
        Sphere s = new Sphere(new Point3D(0, 0, -3), 1);

    }
    void intersectionCounter() {

    }
}
