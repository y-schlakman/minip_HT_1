package geometries;

import primitives.*;

/**
 * class that represents a Triangle in 3d space
 * @author Yosi and Eli
 */
public class Triangle extends Polygon {

    /**
     * constructor for triangle. gets three points representing the triangle's vertices.
     * @param p1 first vertex
     * @param p2 second vertex
     * @param p3 third vertex
     */
    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        super(p1, p2, p3);
    }

    /**
     * Calculates the normal to this triangle
     * at a given point (affectively returns normal to underlying plane).
     *
     * @param point a point on the geometry.
     * @return The resulting normal vector.
     */
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
