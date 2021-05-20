package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * abstract class which is the base for ray tracing classes
 * it is used to find the color of a view plane pixel from which we casted a ray
 *
 * @author Yosi and Eli
 */
abstract class RayTracerBase {
    protected Scene scene;

    /**
     * constructor that gets a scene
     * @param scene the scene
     */
    public RayTracerBase(Scene scene){
        this.scene = scene;
    }

    /**
     * gets the color of pixel at closest ray intersection or background color if there are no intersections
     * @param ray the ray to trace
     * @return the color of closest ray intersection or background color if there are no intersections
     */
    public abstract Color traceRay(Ray ray);
}
