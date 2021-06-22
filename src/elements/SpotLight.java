package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Class representing a spot light light-source.
 *
 * @author Yosi and Eli.
 */
public class SpotLight extends PointLight{

    private Vector direction;//The direction the spot light is pointing in.

    /**
     * Constructor method for spot light light-source.
     *
     * @param intensity The colour of the light produced.
     * @param position The origin/position of the light source.
     * @param direction_ The direction the spot light is point to.
     */
    public SpotLight(Color intensity, Point3D position, Vector direction_){
        super(intensity, position);
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
        return super.getIntensity(p).scale(Math.max(0, getL(p).dotProduct(direction.normalized())));
    }

}
