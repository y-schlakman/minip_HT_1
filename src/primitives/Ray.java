package primitives;

import geometries.Intersectable.*;

import java.util.List;

import static primitives.Util.isZero;

/**
 * The Ray class is used to represent a mathematical ray stemming at a point p0 in 3 dimensional cartesian space and
 * pointing in a direction 'dir'.
 * The class is used in many calculations such as those regarding rays of light or vision.
 *
 * @author Eli and Yosi.
 */
public class Ray {

    /**
     * The point at which the ray begins
     */
    final Point3D _p0;

    /**
     * The direction in which the ray is pointing
     */
    final Vector _dir;

    /**
     * delta value to move head of rays by, so that we don't cast
     * them from inside objects
     */
    private static final double DELTA = 0.1;

    /**
     * ctor
     *
     * @param p0  beginning point.
     * @param dir direction.
     */
    public Ray(Point3D p0, Vector dir) {
        this._p0 = p0;
        this._dir = dir;
        this._dir.normalize();
    }

    /**
     * constructor that gets a head point, direction vector and normal vector, and builds a ray whose head is offset by DELTA in the given direction
     * @param p0 rays head
     * @param dir rays direction
     * @param n normal vector to surface
     */
    public Ray(Point3D p0, Vector dir, Vector n) {
        Vector delta = n.normalized().scale(n.normalized().dotProduct(dir.normalized()) > 0 ? DELTA : - DELTA);
        this._p0 = p0.add(delta);
        this._dir = dir.normalized();
    }

    /**
     * getter for p0 field
     *
     * @return value of p0
     */
    public Point3D get_p0() {
        return _p0;
    }

    /**
     * getter for dir field
     *
     * @return value of dir
     */
    public Vector get_dir() {
        return _dir;
    }

    /**
     * get the point at head of ray when scaled by a given number
     * @param t the number to scale the ray by
     * @return the point at the head of the scaled ray
     */
    public Point3D getPoint(double t) {
        if (isZero(t)) {
            return _p0;
        }
        return _p0.add(_dir.scale(t));
    }

    /**
     * finds the point which is closest to ray's head from list of points
     *
     * @param points list of points to check
     * @return closest point from points to ray's head
     */
    public Point3D findClosestPoint(List<Point3D> points) {
        //if list is empty return null
        if (points == null)
            return null;
        if (points.size() == 0)
            return null;

        //loop through the list and find the index of closest point
        int closestIndex = 0;
        double closestDistance = points.get(0).distanceSquared(_p0), currDistance;
        for (int i = 1; i < points.size(); ++i) {
            currDistance = points.get(i).distanceSquared(_p0);
            if (currDistance < closestDistance) {
                closestIndex = i;
                closestDistance = currDistance;
            }
        }

        //return the closest point
        return points.get(closestIndex);
    }

    /**
     * Finds the nearest point (with respect to its corresponding geometry) given a list.
     *
     * @param points the list of points to search through.
     * @return The nearest point to the ray's origin.
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
        //if list is empty return null
        if (points == null)
            return null;
        if (points.size() == 0)
            return null;

        //loop through the list and find the index of closest point
        int closestIndex = 0;
        double closestDistance = points.get(0).point.distanceSquared(_p0), currDistance;
        for (int i = 1; i < points.size(); ++i) {
            currDistance = points.get(i).point.distanceSquared(_p0);
            if (currDistance < closestDistance) {
                closestIndex = i;
                closestDistance = currDistance;
            }
        }

        //return the closest point
        return points.get(closestIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Ray)) return false;
        Ray other = (Ray) obj;
        return this._p0.equals(other._p0) && this._dir.equals(other._dir);
    }

    @Override
    public String toString() {
        return "start: " + _p0.toString() + ", direction: " + _dir.toString();
    }
}
