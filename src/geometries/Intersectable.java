package geometries;

import primitives.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * interface to be implemented by all geometries.
 *
 * @author Yosi and Eli.
 */

public interface Intersectable {

    /**
     * Class representing a point with respect to a geometry it belongs to.
     *
     * @author Yosi and Eli.
     */
    public static class GeoPoint {
        public Geometry geometry; //The points geometry.
        public Point3D point;   //The points location.

        /**
         * Constructor for GeoPoint.
         *
         * @param geometry_ The geometry the point belongs to.
         * @param point_    The location of the point.
         */
        public GeoPoint(Geometry geometry_, Point3D point_)
        {
            geometry = geometry_;
            point = point_;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof GeoPoint)) return false;
            GeoPoint other = (GeoPoint)obj;

            return geometry.equals(other.geometry) && point.equals(other.point);
        }
    }

    /**
     * Finds all intersections of the implementing geometry with a given ray.
     *
     * @param ray the ray intersecting the geometry.
     * @return a list of points of intersection.
     */
    default List<Point3D> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(gp -> gp.point).collect(Collectors.toList());
    }


    /**
     * Finds all intersections of the implementing geometry with a given ray, with respect to said geometry.
     *
     * @param ray the ray intersecting the geometry.
     * @return a list of points of intersection, with respect to the implementing geometry.
     */
    List<GeoPoint> findGeoIntersections(Ray ray);

    /**
     * gets the AABB of the intersectable
     * @return the intersectable's AABB.
     */
    AABB getAABB();
}
