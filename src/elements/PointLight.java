package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    //The origin of the spot-light light source.
    private Point3D position;

    //Diffusive coefficients.
    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Constructor for point-light light source.
     * @param intensity Color of intensity.
     * @param position_ Position of light source.
     */
    public PointLight(Color intensity, Point3D position_)
    {
        super(intensity);
        position = position_;
    }

    /**
     * Gets the intensity colour at a given point with respect to this light source.
     *
     * @param p Said point.
     * @return Intensity colour at said point.
     */
    @Override
    public Color getIntensity(Point3D p) {
        double d = position.distance(p);
        return intensity.scale(kC + d*kL + d*d*kQ);
    }

    /**
     * Calculates vector from light source to given point.
     *
     * @param p Said point.
     * @return Vector from light source to given point.
     */
    @Override
    public Vector getL(Point3D p) {
        return p.subtract(position);
    }
}
