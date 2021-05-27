package primitives;

public class Material {
    public double kD = 0, kS = 0;
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
     * Setter for shininess value.
     * @param nShininess Desired shininess value.
     * @return Instance of current material.
     */
    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
