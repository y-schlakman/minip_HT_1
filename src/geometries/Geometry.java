package geometries;

import primitives.*;

/**
 * interface to be implemented by all geometries.
 *
 * @author Yosi and Eli.
 */
public interface Geometry extends Intersectable {
    /**
     * gets the normal vector of a geometry at a given point on the geometry.
     *
     * @param point a point on the geometry
     * @return normalized normal vector to the geometry at point.
     */
    Vector getNormal(Point3D point);
}
