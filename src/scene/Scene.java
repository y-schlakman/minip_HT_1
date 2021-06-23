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
     * numGlossyDiffuseRays - number of gloss and diffuse rays to be cast
     */
    public String name;
    public Color background;
    public AmbientLight ambientLight;
    public Geometries geometries;
    public List<LightSource> lights =  new LinkedList<LightSource>();
    public int numGlossyDiffuseRays;
    public boolean glossyEnabled;
    public boolean diffuseEnabled;

    /**
     * constructor for Scene that gets the scene name and sets all fields to default
     * @param name name of scene
     */
    public Scene(String name) {
        this.name = name;
        background = Color.BLACK;
        ambientLight = new AmbientLight();
        geometries = new Geometries();
        numGlossyDiffuseRays = 10;
        glossyEnabled = true;
        diffuseEnabled = true;
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

    /**
     * setter for the number of glossy and defuse rays to be cast in the scene's color calculations.
     *
     * @param numRays the desired number of rays.
     * @return instance of current scene.
     */
    public Scene setNumGlossyDiffuseRays(int numRays){
        if(numRays < 0) {
            throw new IllegalArgumentException("number of rays must be zero or larger");
        }
        numGlossyDiffuseRays = numRays;
        return this;
    }

    /**
     * setter for glossyEnabled value.
     *
     * @param glossyEnabled the desired glossyEnabled value - if true then gloss is enabled in the scene, if false then it is disabled.
     * @return returns an instance of the current scene
     */
    public Scene setGlossyEnabled(boolean glossyEnabled) {
        this.glossyEnabled = glossyEnabled;
        return this;
    }

    /**
     * setter for diffuseEnabled value.
     *
     * @param diffuseEnabled the desired diffuseEnabled value - if true then diffuse is enabled in the scene, if false then it is disabled.
     * @return returns an instance of the current scene
     */
    public Scene setDiffuseEnabled(boolean diffuseEnabled) {
        this.diffuseEnabled = diffuseEnabled;
        return this;
    }
}
