package elements;

import primitives.Color;

/**
 * class that represents the ambient light of a scene
 *
 * @author Yosi and Eli
 */
public class AmbientLight extends Light {

    /**
     * default constructor for AmbientLight makes intensity black.
     */
    public AmbientLight(){
        super(Color.BLACK);
    }

    /**
     * constructor for AmbientLight that gets color and strength factor
     * @param iA color of light
     * @param kA strength factor
     */
    public AmbientLight(Color iA, double kA){ super(iA.scale(kA)); }

}
