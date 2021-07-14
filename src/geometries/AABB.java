package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * This class represents a 3 dimensional axis aligned bounding box. it consists of two points (max and min) that represent the most maximal point
 * of the box and the most minimal point of the box (accordingly)
 *
 * @author Yosi and Eli
 */
public class AABB {
    /**
     * max is the point of the AABB which is highest on the y axis, deepest on the z axis, and furthest on the positive x axis.
     */
    private Point3D max;
    /**
     * min is the point of the AABB which is lowest on the y axis, shallowest on the z axis, and furthest on the negative x axis.
     */
    private Point3D min;

    /**
     * DELTA_VECTOR sets the space between the geometry and the aabb.cit is needed in order to make the box 3D even for 2D geometries.
     */
    private static final double DELTA = 0.1;

    /**
     * constructor for AABB that gets min and max points of AABB.
     * @param max desired max point of AABB.
     * @param min desired min point of AABB.
     * @throws IllegalArgumentException if min is not minimum and max is not maximum.
     */
    public AABB(Point3D max, Point3D min){
        if(max.getX().getCoord() < min.getX().getCoord()||
                max.getY().getCoord() < min.getY().getCoord()||
                max.getZ().getCoord() < min.getZ().getCoord()){
            throw new IllegalArgumentException("min point must be minimal and max must be maximal");
        }
        this.max = max.add(new Vector(1, 1, 1).scale(DELTA));
        this.min = min.add(new Vector(-1,-1,-1).scale(DELTA));
    }

    /**
     * getter for the max point of aabb
     * @return the max point of the aabb
     */
    public Point3D getMax() {
        return max;
    }

    /**
     * getter for the min point of aabb
     * @return the min point of the aabb
     */
    public Point3D getMin() {
        return min;
    }

    /**
     * check if a given ray intersects the AABB. based off of a 2D solution in an article <a href="https://tavianator.com/2011/ray_box.html">The article</a>
     *
     * @param ray the ray to check intersection with.
     * @return a boolean value representing if there is an intersection.
     */
    public boolean hasIntersection(Ray ray){
        Point3D p0 = ray.get_p0();
        Point3D dirHead = ray.get_dir().getHead();

        //rays p0's, direction's head, max's and min's coordinates - saved as variables for DRY
        double p0X = p0.getX().getCoord(), p0Y = p0.getY().getCoord(), p0Z = p0.getZ().getCoord();
        double dirX = dirHead.getX().getCoord(), dirY = dirHead.getY().getCoord(), dirZ = dirHead.getZ().getCoord();
        double minX = min.getX().getCoord(), minY = min.getY().getCoord(), minZ = min.getZ().getCoord();
        double maxX = max.getX().getCoord(),  maxY = max.getY().getCoord(), maxZ = max.getZ().getCoord();

        //tMin is the minimum t value to scale ray by for the ray to intersect the AABB
        double tMin = Double.NEGATIVE_INFINITY;
        //tMax is the maximum t value to scale ray by for the ray to intersect the AABB
        double tMax = Double.POSITIVE_INFINITY;

        //find minimum and maximum t value to scale the ray by to hit the box's x coordinate range.
        if(!isZero(dirX)){
            double tToMinX = (minX - p0X)/dirX;
            double tToMaxX = (maxX - p0X)/dirX;
            tMin = Math.max(tMin, Math.min(tToMinX, tToMaxX));
            tMax = Math.min(tMax, Math.max(tToMinX, tToMaxX));
        }

        //check if minimum and maximum t value to scale the ray by to hit the box's y coordinate range is smaller or larger than the x range.
        if(!isZero(dirY)){
            double tToMinY = (minY - p0Y)/dirY;
            double tToMaxY = (maxY - p0Y)/dirY;
            tMin = Math.max(tMin, Math.min(tToMinY, tToMaxY));
            tMax = Math.min(tMax, Math.max(tToMinY, tToMaxY));
        }

        //check if minimum and maximum t value to scale the ray by to hit the box's z coordinate range is smaller or larger than the x and y range.
        if(!isZero(dirZ)){
            double tToMinZ = (minZ - p0Z)/dirZ;
            double tToMaxZ = (maxZ - p0Z)/dirZ;
            tMin = Math.max(tMin, Math.min(tToMinZ, tToMaxZ));
            tMax = Math.min(tMax, Math.max(tToMinZ, tToMaxZ));
        }

        //if the minimum is bigger than the maximum so there is no intersection.
        return tMax >= tMin;
    }

    public Geometries.Axis getLongestAxis(){
        double x = max.getX().getCoord() - min.getX().getCoord();
        double y = max.getY().getCoord() - min.getY().getCoord();
        double z = max.getZ().getCoord() - min.getZ().getCoord();
        double m = Math.max(x, Math.max(y, z));
        if(m==x)
            return Geometries.Axis.X_AXIS;
        if(m==y)
            return Geometries.Axis.Y_AXIS;
        return Geometries.Axis.Z_AXIS;
    }
}
