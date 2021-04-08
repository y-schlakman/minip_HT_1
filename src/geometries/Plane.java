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
        //check if first two points are the same
        if(p1.equals(p2))
            throw new IllegalArgumentException("first two points are the same");

        //check if all have the same slope (then they are on the same line)
        if((p1.subtract(p2).equals(p1.subtract(p3)) || p1.subtract(p2).equals(p1.subtract(p3).scale(-1)))
                && (p2.subtract(p3).equals(p1.subtract(p2))||p2.subtract(p3).equals(p1.subtract(p2).scale(-1))))
            throw new IllegalArgumentException("points are on the same line");

        q0 = p1;
        _normal = p3.subtract(p1).crossProduct(p2.subtract(p1)).normalize();
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
        return _normal;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "_point=" + q0 +
                ", _normal=" + _normal +
                '}';
    }
}
