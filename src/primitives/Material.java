package primitives;

public class Material {
    /**
     * kD is diffuse coefficient
     * kS is specular coefficient
     * kT is transparency coefficient
     * kR is reflectiveness coefficient
     * nShininess is shininess coefficient
     */
    public double kD = 0;
    public double kS = 0;
    public double kT = 0;
    public double kR = 0;
    public int nShininess = 0;

    /**
     * Setter for diffuse coefficient.
     * @param kD Desired coefficient value.
     * @return Instance of current material.
     */
    public Material setkD(double kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for specular coefficient.
     * @param kS Desired coefficient value.
     * @return Instance of current material.
     */
    public Material setkS(double kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for transparency coefficient.
     * @param kT Desired coefficient value.
     * @return Instance of current material.
     */
    public Material setkT(double kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Setter for reflectiveness coefficient.
     * @param kR Desired coefficient value.
     * @return Instance of current material.
     */
    public Material setkR(double kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Setter for shininess value.
     * @param nShininess Desired shininess value.
     * @return Instance of current material.
     */
    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
