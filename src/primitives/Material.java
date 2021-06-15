package primitives;

public class Material {
    /**
     * kD is diffuse coefficient
     * kS is specular coefficient
     * kT is transparency coefficient
     * kR is reflectiveness coefficient
     * nShininess is shininess coefficient
     * glossyRadius is the radius of the cone of reflection rays sent in color calculations
     * diffuseRadius is the radius of the cone of refraction rays in color calculations
     */

    public double kD = 0;
    public double kS = 0;
    public double kT = 0;
    public double kR = 0;
    public int nShininess = 0;
    public double glossyRadius = 0.5;
    public double diffuseRadius = 0.5;

    /**
     * Setter for diffuse coefficient.
     *
     * @param kD Desired coefficient value.
     * @return Instance of current material.
     */
    public Material setkD(double kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for specular coefficient.
     *
     * @param kS Desired coefficient value.
     * @return Instance of current material.
     */
    public Material setkS(double kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for transparency coefficient.
     *
     * @param kT Desired coefficient value.
     * @return Instance of current material.
     */
    public Material setkT(double kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Setter for reflectiveness coefficient.
     *
     * @param kR Desired coefficient value.
     * @return Instance of current material.
     */
    public Material setkR(double kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Setter for shininess value.
     *
     * @param nShininess Desired shininess value.
     * @return Instance of current material.
     */
    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * setter for glossy radius value.
     *
     * @param radius the desired glossy radius value.
     * @return instance of current material.
     */
    public Material setGlossyRadius(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("glossy radius must be zero or higher");
        }
        glossyRadius = radius;
        return this;
    }

    /**
     * setter for diffuse radius value.
     *
     * @param radius the desired diffuse radius value.
     * @return instance of current material.
     */
    public Material setDiffuseRadius(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("diffuse radius must be zero or higher");
        }
        diffuseRadius = radius;
        return this;
    }
}
