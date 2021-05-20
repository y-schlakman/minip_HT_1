package elements;

import primitives.Color;

/**
 * class that represents the ambient light of a scene
 *
 * @author Yosi and Eli
 */
public class AmbientLight {
    private Color intensity;

    /**
     * default constructor for AmbientLight makes intensity black
     */
    public AmbientLight(){
        intensity = Color.BLACK;
    }

    /**
     * constructor for AmbientLight that gets color and strength factor
     * @param iA color of light
     * @param kA strength factor
     */
    public AmbientLight(Color iA, double kA){
        intensity = iA.scale(kA);
    }

    /**
     * getter for intensity
     * @return intensity value
     */
    public Color getIntensity() {
        return intensity;
    }
}
