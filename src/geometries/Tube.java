package geometries;

import primitives.*;

/**
 * class that represents a Tube in 3d space
 * @author Yosi and Eli
 */
public class Tube implements Geometry {

    final private Ray _axisRay;
    final private double _radius;

    /**
     * constructor for tube that gets the axis ray and the radius of the tube. (radius must be positive).
     *
     * @param axisRay axis ray of tube
     * @param radius  radius of tube
     */
    public Tube(Ray axisRay, double radius) {
        if (radius <= 0)
            throw new IllegalArgumentException("radius must have a positive value");
        _axisRay = axisRay;
        _radius = radius;
    }

    /**
     * getter for _axisRay field
     *
     * @return _axisRay
     */
    public Ray get_axisRay() {
        return _axisRay;
    }

    /**
     * getter for _radius field
     *
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
        return "Tube{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }
}
