package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a collection of geometric objects to be stored together.
 *
 * @author Yosi and Eli.
 */
public class Geometries implements Intersectable {

    private List<Intersectable> geometries; //List of geometries we store.

    /**
     * Default constructor for this class, initiates the list as an empty one.
     */
    public Geometries() {
        geometries = new ArrayList<>(); //ArrayList is superior to LinkedList since its ease of access and to objects in the middle of the list.
    }

    /**
     * Constructor for this class given an initial list of geometries to store.
     *
     * @param geometries The initial list of geometries to store.
     */
    public Geometries(Intersectable... geometries) {
        this.geometries = List.of(geometries);
    }

    /**
     * Adds a geometry to the stored list.
     *
     * @param geometries The geometry to add.
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds intersections of all the geometries in the scene with given ray,
     * with respect to the intersection's geometry.
     *
     * @param ray the ray intersecting the geometry.
     * @return list of intersection points with respect to their corresponding geometry.
     * (null if none exist).
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        if (geometries == null || geometries.isEmpty())
            return null;

        List<GeoPoint> res, tmp; //res - resultant list,
                                //tmp - used to store each batch of points found for each geometry..
        //Loop until a geometry has an intersection.
        int i = 0;
        do {
            res = geometries.get(i).findGeoIntersections(ray);
            i++;
        } while (res == null && i < geometries.size());
        if (res == null)
            return null;

        //Loop through any additional geometries and add thier intersections should they exist.
        while (i < geometries.size()) {
            tmp = geometries.get(i).findGeoIntersections(ray);
            if (tmp != null)
                res.addAll(tmp);
            i++;
        }
        return res;
    }
}
