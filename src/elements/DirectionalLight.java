package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Class for a directional light source.
 *
 * @author Yosi and Eli.
 */
public class DirectionalLight extends Light implements LightSource{

    private Vector direction;

    public DirectionalLight(Color intensity, Vector direction_){
        super(intensity);
        direction = direction_;
    }


    /**
     * Gets the intensity colour at a given point with respect to this light source.
     *
     * @param p Said point.
     * @return Intensity colour at said point.
     */
    @Override
    public Color getIntensity(Point3D p) {
        return intensity;
    }

    /**
     * Calculates vector from light source to given point.
     *
     * @param p Said point.
     * @return Vector from light source to given point.
     */
    @Override
    public Vector getL(Point3D p) {
        return direction.normalized();
    }

    @Override
    public double getDistance(Point3D point) {
        return Double.POSITIVE_INFINITY;
    }
}
