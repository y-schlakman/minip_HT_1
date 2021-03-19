package geometries;

import primitives.*;

/**
 * class that represents a Sphere in 3d space
 * @author Yosi and Eli
 */
public class Sphere implements Geometry{

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
        return null;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "_center=" + _center +
                ", _radius=" + _radius +
                '}';
    }
}
