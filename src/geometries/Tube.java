package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.isZero;

/**
 * class that represents a Tube in 3d space
 * @author Yosi and Eli
 */
public class Tube extends Geometry {

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
        //if point is in line with head of the ray treat like a sphere
        if(isZero(_axisRay.get_p0().subtract(point).dotProduct(_axisRay.get_dir())))
        {
            return point.subtract(_axisRay.get_p0()).normalize();
        }
        //otherwise treat like tube - names of variables were inspired by the matzeget
        double t = _axisRay.get_dir().normalized().dotProduct(point.subtract(_axisRay.get_p0()));
        Point3D o = _axisRay.get_p0().add(_axisRay.get_dir().scale(t));
        return point.subtract(o).normalize();
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        //For now no implementation.
        return null;
    }

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
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
