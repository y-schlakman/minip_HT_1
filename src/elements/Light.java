package elements;

import primitives.Color;

/**
 * Abstract class as a base for all types of lights in a scene.
 *
 * @author Yosi and Eli.
 */
abstract class Light {

    protected Color intensity;

    /**
     * Getter function for the light's intensity.
     * @return The intensity.
     */
    public Color getIntensity() {
        return intensity;
    }

    /**
     * Constructor for light source, initializes intensity with given parameter.
     * @param intensity_ the desired intensity for the light.
     */
    protected Light(Color intensity_)
    {
        intensity  = intensity_;
    }


}
