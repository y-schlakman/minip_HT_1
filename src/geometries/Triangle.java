package geometries;

import primitives.*;

/**
 * class that represents a Triangle in 3d space
 * @author Yosi and Eli
 */
public class Triangle extends Polygon implements Geometry {

    /**
     * constructor for triangle. gets three points representing the triangle's vertices.
     * @param p1 first vertex
     * @param p2 second vertex
     * @param p3 third vertex
     */
    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        super(p1, p2, p3);
    }

    @Override
    public Vector getNormal(Point3D point) {
        return plane.getNormal(point);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vertices=" + vertices.toString() +
                ", plane=" + plane.toString() +
                '}';
    }
}
