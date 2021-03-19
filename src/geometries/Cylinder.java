package geometries;

import primitives.*;

/**
 * class that represents a Cylinder in 3d space
 * @author Yosi and Eli
 */
public class Cylinder extends Tube implements Geometry {

    final private double _height;

    /**
     * constructor for cylinder that gets the axis ray, radius, and height of the cylinder. (radius and height must be positive).
     * @param axisRay axis ray of cylinder
     * @param radius  radius of cylinder
     * @param height  height of cylinder
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        if (height <= 0)
            throw new IllegalArgumentException("height must be a positive number.");
        _height = height;
    }

    /**
     * getter for _height field.
     * @return _height
     */
    public double get_height() {
        return _height;
    }

    @Override
    public Vector getNormal(Point3D point) {
        return null;
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "_axisRay=" + super.get_axisRay() +
                ", _radius=" + super.get_radius() +
                ", _height=" + _height +
                '}';
    }
}
