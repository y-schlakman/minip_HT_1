package scene;

import elements.AmbientLight;
import geometries.Geometries;
import primitives.Color;

/**
 * class that represents a 3D scene
 *
 * @author Yosi and Eli
 */
public class Scene {
    public String name;
    public Color background;
    public AmbientLight ambientLight;
    public Geometries geometries;

    /**
     * constructor for Scene that gets the scene name and sets all fields to default
     * @param name name of scene
     */
    public Scene(String name) {
        this.name = name;
        background = Color.BLACK;
        ambientLight = new AmbientLight();
        geometries = new Geometries();
    }

    /**
     * setter for scene background that returns the scene object
     * @param background new background value
     * @return the scene object
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * setter for scene ambient light that returns the scene object
     * @param ambientLight new ambient light value
     * @return the scene object
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * setter for scene geometries that returns the scene object
     * @param geometries new geometries value
     * @return the scene object
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}
