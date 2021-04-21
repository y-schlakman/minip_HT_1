package primitives;

import java.lang.*;
/**
 * Point3D class is used to represent a point in 3 dimensional cartesian space.
 * This class is used in many geometries and as a component of Vector.
 *
 * @author Eli and Yosi.
 */

public class Point3D {

    public final static Point3D ZERO = new Point3D(0,0,0);

    //**The three values describing position along the respective axis.*/

    public Coordinate getX() {
        return x;
    }

    public Coordinate getY() {
        return y;
    }

    public Coordinate getZ() {
        return z;
    }

    /**Value along the x axis.*/
    final Coordinate x;

    /**Value along the y axis.*/
    final Coordinate y;

    /**Value along the z axis.*/
    final Coordinate z;

    /**
     * ctor using double values representing respective positions along the axis
     * in the 3D cartesian space.
     *
     * @param x value along the x axis.
     * @param y value along the y axis.
     * @param z value along the z axis.
     */
    public Point3D(double x, double y, double z){
        this.x = new Coordinate(x);
        this.y = new Coordinate(y);
        this.z = new Coordinate(z);

    }

    /**
     * ctor using Coordinate objects containing values representing respective positions along the axis
     * in the 3D cartesian space.
     *
     * @param x value along the x axis.
     * @param y value along the y axis.
     * @param z value along the z axis.
     */
    public Point3D(Coordinate x, Coordinate y, Coordinate z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds a vector to this point.
     * @param vec the other vector.
     * @return resulting point in space.
     */
    public Point3D add(Vector vec){
        return new Point3D(x.coord + vec.head.x.coord,
                       y.coord + vec.head.y.coord,
                       z.coord + vec.head.z.coord);
    }

    /**
     * Calculates vector from given point to this one.
     * @param from the base of the resulting vector.
     * @return the resulting vector.
     */
    public Vector subtract(Point3D from){
        return new Vector(x.coord - from.x.coord,
                y.coord - from.y.coord,
                z.coord - from.z.coord);
    }

    /**
     * Calculates the squared distance between two points in space.
     *
     * @param p the other point.
     * @return the distance squared.
     */
    //Using Pythagoras`s theorem (extended to 3d space [distance^2 = dx^2 + dy^2 + dz^2]).
    public double distanceSquared(Point3D p){
        return (x.coord-p.x.coord)*(x.coord-p.x.coord)
                + (y.coord-p.y.coord)*(y.coord-p.y.coord)
                + (z.coord-p.z.coord)*(z.coord-p.z.coord);
    }

    /**
     * Calculates the distance between two points in space.
     *
     * @param p the other point.
     * @return the distance.
     */
    public double distance(Point3D p){
        return Math.sqrt(distanceSquared(p));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Point3D)) return false;
        Point3D other = (Point3D)obj;
        return this.x.equals(other.x) &&
                this.y.equals(other.y) &&
                this.z.equals(other.z);
    }

    @Override
    public String toString() {
        return '(' + x.toString() + ", " + y.toString() + ", " + z.toString() + ')';
    }
}

