package geometries;

import primitives.*;

import java.util.*;

/**
 * interface to be implemented by all geometries.
 *
 * @author Yosi and Eli.
 */

public interface Intersectable {

    /**
     * Finds all intersections of the implementing geometry with a given ray.
     *
     * @param ray the ray intersecting the geometry.
     * @return a list of points of intersection.
     */
    List<Point3D> findIntersections(Ray ray);
}
