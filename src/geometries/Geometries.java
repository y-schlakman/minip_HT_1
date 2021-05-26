package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Geometries implements Intersectable {

    private List<Intersectable> geometries;

    public Geometries() {
        geometries = new ArrayList<>(); //ArrayList is superior to LinkedList since its ease of access and to objects in the middle of the list.
    }

    public Geometries(Intersectable... geometries) {
        this.geometries = List.of(geometries);
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds intersections of all the geometries in the scene with given ray.
     * @param ray the ray intersecting the geometry.
     * @return list of intersection points. (null if none exist).
     */
    @Override
    public List<Point3D> findIntersections(Ray ray) {
        if (geometries.isEmpty())
            return null;

        List<Point3D> res;
        int i = 0;
        do {
            res = geometries.get(i).findIntersections(ray);
            i++;
        } while (res == null && i < geometries.size());

        if (res == null)
            return null;

        List<Point3D> intersections;
        while (i < geometries.size()) {
            intersections = geometries.get(i).findIntersections(ray);
            if (intersections != null)
                res.addAll(intersections);
            i++;
        }
        return res;
    }

    /**
     * Finds intersections of all the geometries in the scene with given ray,
     *      with respect to the intersection's geometry.
     *
     * @param ray the ray intersecting the geometry.
     * @return list of intersection points with respect to their corresponding geometry.
     *      (null if none exist).
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        List<GeoPoint> res, tmp;
        int i=0;
        do {
            res=geometries.get(i).findGeoIntersections(ray);
            i++;
        }while(res==null && i<geometries.size());
        if(res==null)
            return null;

        while(i<geometries.size())
        {
            tmp = geometries.get(i).findGeoIntersections(ray);
            if(tmp!=null)
                res.addAll(tmp);
            i++;
        }
        return res;
    }
}
