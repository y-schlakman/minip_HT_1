package renderer;

import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * child class of RayTracerBase, that is used to determine color of a view plane pixel from which we casted a ray.
 *
 * @author Yosi and Eli
 */
public class RayTracerBasic extends RayTracerBase {
    /**
     * constructor that gets a scene
     * @param scene the scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point3D> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null)
            return scene.background;
        Point3D closest = ray.findClosestPoint(intersections);
        return calcColor(closest);
    }

    /**
     * find the color of a point
     * @param point the point to find the color of
     * @return the color of the point
     */
    private Color calcColor(Point3D point) {
        return scene.ambientLight.getIntensity();
    }
}
