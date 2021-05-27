package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class SpotLight extends PointLight{
    private Vector direction;

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
