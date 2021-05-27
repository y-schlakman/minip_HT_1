package geometries;

import primitives.*;
import static primitives.Util.*;
import java.util.List;
import java.util.ArrayList;

/**
 * class that represents a Sphere in 3d space
 * @author Yosi and Eli
 */
public class Sphere extends Geometry{

    final private Point3D _center;
    final private double _radius;

    /**
     * constructor for sphere that gets the center and the radius of the sphere.
     * @param c center of sphere
     * @param r radius of sphere (must be positive).
     */
    public Sphere(Point3D c, double r) {
        if(r<=0)
            throw new IllegalArgumentException("radius must have a positive value");
        _center = c;
        _radius = r;
    }

    /**
     * getter for the _center field
     * @return _center
     */
    public Point3D get_center() {
        return _center;
    }

    /**
     * getter of the _radius field
     * @return _radius
     */
    public double get_radius() {
        return _radius;
    }

    @Override
    public Vector getNormal(Point3D point) {
        return point.subtract(_center).normalize();
    }

    /**
     * Finds all intersection points between this sphere and a given ray (should there be any),
     *      with respect to this sphere as the intersections's geometry.
     *
     * @param ray the ray intersecting the geometry.
     * @return a list of the intersection points with respect to this sphere as their geometry.
     *      if there are no intersections null will be returned.
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {

        Point3D p0 = ray.get_p0();
        Vector v = ray.get_dir();
        Point3D o = _center;

        if(p0.equals(o)){
            return  List.of(new GeoPoint(this, ray.getPoint(_radius)));
        }

        Vector u = o.subtract(p0); //vector from ray origin to center of sphere.
        double tm = v.dotProduct(u); //u`s projection along the direction of v,
        // also the distance from the rays origin to the midpoint between the two intersections
        double distSquared = u.lengthSquared()-(tm*tm); //distance from sphere center to ray.

        if(distSquared >= _radius*_radius) //ray is further than a radius away from the center - no intersections.
            return null;

        double th = Math.sqrt(_radius*_radius - distSquared); //Half the distance between the intersections.
        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm +- th);

        if(t1<=0 && t2<=0)
            return null;

        List<GeoPoint> res = new ArrayList<>();
        if(t1>0)
            res.add(new GeoPoint(this, ray.getPoint(t1)));
        if(t2>0)
            res.add(new GeoPoint(this, ray.getPoint(t2)));
        return res;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "_center=" + _center +
                ", _radius=" + _radius +
                '}';
    }
}
