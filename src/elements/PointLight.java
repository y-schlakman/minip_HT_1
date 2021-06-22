package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Class representing a point light.
 *
 * @author Yosi and Eli.
 */
public class PointLight extends Light implements LightSource{
    //The origin of the spot-light light source.
    private Point3D position;

    //Diffusive coefficients.
    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    /**
     * Setter method for 'kC' diffusion coefficient.
     *
     * @param kC desired coefficient value.
     * @return The updated instance of the point light.
     */
    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Setter method for 'kL' diffusion coefficient.
     *
     * @param kL desired coefficient value.
     * @return The updated instance of the point light.
     */
    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Setter method for 'kQ' diffusion coefficient.
     *
     * @param kQ desired coefficient value.
     * @return The updated instance of the point light.
     */
    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Constructor for point-light light source.
     *
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
        return intensity.reduce(kC + d*kL + d*d*kQ);
    }

    /**
     * Calculates vector from light source to given point.
     *
     * @param p Said point.
     * @return Vector from light source to given point.
     */
    @Override
    public Vector getL(Point3D p) {
        return p.subtract(position).normalized();
    }

    /**
     * Calculates the distance between this point light and a given point.
     *
     * @param point the point from which to calculate the distance to the light source.
     * @return Resulting distance.
     */
    @Override
    public double getDistance(Point3D point) {
        return point.distance(position);
    }
}
