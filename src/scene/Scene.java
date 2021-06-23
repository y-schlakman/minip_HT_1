package scene;

import elements.AmbientLight;
import geometries.Geometries;
import primitives.Color;
import elements.LightSource;

import java.util.LinkedList;
import java.util.List;

/**
 * class that represents a 3D scene
 *
 * @author Yosi and Eli
 */
public class Scene {
    /**
     * name - name of scene
     * background - color of scene's background
     * ambientLight - color of scene's ambient light
     * geometries - list of geometries in the scene
     * lights - list of lights in the scene
     */
    public String name;
    public Color background;
    public AmbientLight ambientLight;
    public Geometries geometries;
    public List<LightSource> lights =  new LinkedList<LightSource>();

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

    /**
     * Setter for lights in scene.
     * @param lights new list of lights.
     * @return The current scene instance.
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
