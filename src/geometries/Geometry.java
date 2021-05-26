package geometries;

import primitives.*;

/**
 * interface to be implemented by all geometries.
 *
 * @author Yosi and Eli.
 */
public abstract class Geometry implements Intersectable {

    //Variable for the emission light of a geometry.
    protected Color emission = Color.BLACK;

    /**
     * Setter method for emission colour.
     * @param emission the new colour.
     * @return This 'Geometry' object.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Getter method for emission colour.
     * @return The current emission colour.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * gets the normal vector of a geometry at a given point on the geometry.
     *
     * @param point a point on the geometry
     * @return normalized normal vector to the geometry at point.
     */
    public abstract Vector getNormal(Point3D point);
}
