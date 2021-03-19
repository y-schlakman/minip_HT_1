package geometries;

import primitives.*;

/**
 * class that represents a Plane in 3d space
 * @author Yosi and Eli
 */
public class Plane implements Geometry {

    final private Point3D q0;
    final private Vector _normal;

    /**
     * constructor for plane that gets point on plane and normal vector.
     * @param p point on plane
     * @param n normal vector
     */
    public Plane(Point3D p, Vector n) {
        q0 = p;
        _normal = n;
    }

    /**
     * constructor for plane that gets three points and calculates the normal from them.
     * temporarily doesn't calculate normal, and puts null instead.
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point3D p1, Point3D p2, Point3D p3) {
        q0 = p1;
        _normal = null;
    }

    /**
     * getter for _q0
     * @return _q0 field
     */
    public Point3D get_point() {
        return q0;
    }

    /**
     * getter for _normal
     * @return _normal field
     */
    public Vector get_normal() {
        return _normal;
    }

    /**
     * @deprecated use {@link Plane#getNormal(Point3D)} with null as param.
     * @return normal to plane
     */
    public Vector getNormal() {
        return null;
    }

    @Override
    public Vector getNormal(Point3D point) {
        return null;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "_point=" + q0 +
                ", _normal=" + _normal +
                '}';
    }
}
