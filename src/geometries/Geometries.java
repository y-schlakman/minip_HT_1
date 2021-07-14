package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.*;

import static primitives.Util.alignZero;

/**
 * Class representing a collection of geometric objects to be stored together.
 *
 * @author Yosi and Eli.
 */

public class Geometries implements Intersectable {

    private List<Intersectable> geometries; //List of geometries we store.

    //geometries' bounding box
    protected AABB aabb;

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
        //this.geometries = List.of(geometries);
        this.geometries = new ArrayList<>();
        Collections.addAll(this.geometries, geometries);
        createAABB();
    }

    /**
     * Adds a geometry to the stored list.
     *
     * @param geometries The geometry to add.
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
        createAABB();
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
        //if aabb is null or doesn't intersect with the ray then return null - because there are no intersections.
        if(aabb != null && !aabb.hasIntersection(ray))
            return null;

        List<GeoPoint> res = null, tmp = null; //res - resultant list,
        //tmp - used to store each batch of points found for each geometry..
        //Loop until a geometry has an intersection.
        int i = 0;
        do {
            //if aabb has no intersections so the geometry doesn't either so continue to next geometry intersection check
            if(!geometries.get(i).getAABB().hasIntersection(ray)){
                i++;
                continue;
            }
            res = geometries.get(i).findGeoIntersections(ray);
            i++;
        } while (res == null && i < geometries.size());
        if (res == null)
            return null;

        //Loop through any additional geometries and add their intersections should they exist.
        while (i < geometries.size()) {
            //if aabb has no intersections so the geometry doesn't either so continue to next geometry intersection check
            if(!geometries.get(i).getAABB().hasIntersection(ray)){
                i++;
                continue;
            }
            tmp = geometries.get(i).findGeoIntersections(ray);
            if (tmp != null)
                res.addAll(tmp);
            i++;
        }
        return res;
    }

    /**
     * creates the aabb for the geometries
     */
    private void createAABB() {
        Point3D max = new Point3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Point3D min = new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        for (Intersectable intersectable : geometries) {
            if(intersectable.getAABB() == null)
                continue;
            //find min and max x coordinate
            if (intersectable.getAABB().getMax().getX().getCoord() > max.getX().getCoord())
                max = new Point3D(intersectable.getAABB().getMax().getX().getCoord(), max.getY().getCoord(), max.getZ().getCoord());
            if (intersectable.getAABB().getMin().getX().getCoord() < min.getX().getCoord())
                min = new Point3D(intersectable.getAABB().getMin().getX().getCoord(), min.getY().getCoord(), min.getZ().getCoord());

            //find min and max y coordinate
            if (intersectable.getAABB().getMax().getY().getCoord() > max.getY().getCoord())
                max = new Point3D(max.getX().getCoord(), intersectable.getAABB().getMax().getY().getCoord(), max.getZ().getCoord());
            if (intersectable.getAABB().getMin().getY().getCoord() < min.getY().getCoord())
                min = new Point3D(min.getX().getCoord(), intersectable.getAABB().getMin().getY().getCoord(), min.getZ().getCoord());

            //find min and max z coordinate
            if (intersectable.getAABB().getMax().getZ().getCoord() > max.getZ().getCoord())
                max = new Point3D(max.getX().getCoord(), max.getY().getCoord(), intersectable.getAABB().getMax().getZ().getCoord());
            if (intersectable.getAABB().getMin().getZ().getCoord() < min.getZ().getCoord())
                min = new Point3D(min.getX().getCoord(), min.getY().getCoord(), intersectable.getAABB().getMin().getZ().getCoord());
        }
        aabb = new AABB(max, min);
    }

    @Override
    public AABB getAABB() {
        return aabb;
    }

    //Enum to elegantly represent different axis.
    public enum Axis{X_AXIS, Y_AXIS, Z_AXIS}

    /**
     * Utility methode to return a value of a point along an axis elegantly.
     *
     * @param p the point
     * @param axis the axis enum
     * @return the value along the given axis.
     */
    private double axisValue(Point3D p, Axis axis){
        if(axis == Axis.X_AXIS)
            return alignZero(p.getX().getCoord());
        if(axis == Axis.Y_AXIS)
            return alignZero(p.getY().getCoord());
        return alignZero(p.getZ().getCoord());
    }

    /**
     * Utility class for passing a comparison function to sorting call in sorting intersectables.
     */
    public class SortingGeometries implements Comparator<Intersectable> {
        private Axis _axis;
        public SortingGeometries(Axis axis){
            _axis = axis;
        }
        @Override
        public int compare(Intersectable o1, Intersectable o2) {
            double res = alignZero(axisValue(o1.getAABB().getMax(), _axis) -
                    axisValue(o2.getAABB().getMax(), _axis));

            return res>0?1:res<0?-1:0;
        }
    }

    /**
     * Sorts the geometries along a certian axis.
     * @param axis said axis.
     */
    public void sortGeometries(Axis axis){
        Collections.sort(geometries, new SortingGeometries(axis));
    }

    public List<Intersectable> getIntersections(){return geometries;}
}
