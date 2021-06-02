package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Interface for underlying functionality of a light source.
 *
 * @author Yosi and Eli.
 */
public interface LightSource {

    /**
     * Gets the intensity colour at a given point with respect to this light source.
     * @param p Said point.
     * @return Intensity colour at said point.
     */
    public Color getIntensity(Point3D p);

    /**
     * Calculates vector from light source to given point.
     * @param p Said point.
     * @return Vector from light source to given point.
     */
    public Vector getL(Point3D p);

    /**
     * calculates the distance between a given point and the light source
     * @param point the point from which to calculate the distance to the light source
     * @return the distance between the point and the light source
     */
    double getDistance(Point3D point);
}
