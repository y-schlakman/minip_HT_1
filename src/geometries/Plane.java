package geometries;

import primitives.*;
import static primitives.Util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * class that represents a Plane in 3d space
 *
 * @author Yosi and Eli
 */
public class Plane implements Geometry {

    final private Point3D q0;
    final private Vector _normal;

    /**
     * constructor for plane that gets point on plane and normal vector.
     *
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
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point3D p1, Point3D p2, Point3D p3) {
        //check if first two points are the same
        if (p1.equals(p2))
            throw new IllegalArgumentException("first two points are the same");

        //check if all have the same slope (then they are on the same line)
        Vector p2p1 = p1.subtract(p2).normalize();
        Vector p3p1 = p1.subtract(p3).normalize();
        Vector p3p2 = p2.subtract(p3).normalize();
        if ((p2p1.equals(p3p1) || p2p1.equals(p3p1.scale(-1)))
                && (p3p2.equals(p2p1) || p3p2.equals(p2p1.scale(-1))))
            throw new IllegalArgumentException("points are on the same line");

        q0 = p1;
        _normal = p3.subtract(p1).crossProduct(p2.subtract(p1)).normalize();
    }

    /**
     * getter for _q0
     *
     * @return _q0 field
     */
    public Point3D get_point() {
        return q0;
    }

    /**
     * getter for _normal
     *
     * @return _normal field
     */
    public Vector get_normal() {
        return _normal;
    }

    /**
     * @return normal to plane
     * @deprecated use {@link Plane#getNormal(Point3D)} with null as param.
     */
    @Deprecated
    public Vector getNormal() {
        return null;
    }

    @Override
    public Vector getNormal(Point3D point) {
        return _normal;
    }

    /**
     * Finds the intersection point (should one exist) between the plane and a given ray.
     *
     * @param ray the ray intersecting the geometry.
     * @return a list containing the intersection point. (if there is no intersection null will be returned).
     */
    @Override
    public List<Point3D> findIntersections(Ray ray) {
        Point3D p0 = ray.get_p0();
        Vector v = ray.get_dir();

        if(q0.equals(p0))//Ray begins inside plane so no intersections regardless of direction.
            return null;

        double nv = _normal.dotProduct(v);

        if(isZero(nv)) // ray is included inside plane (infinite intersections are not counted).
            return null;

        Vector toPlane = q0.subtract(p0);

        double t = alignZero(_normal.dotProduct(toPlane) / nv);
        if(t<=0) //Ray begins after plane.
            return null;

        Point3D intersection = ray.getPoint(t);

        List<Point3D> res = new ArrayList<>();
        res.add(intersection);
        return res;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "_point=" + q0 +
                ", _normal=" + _normal +
                '}';
    }
}
