package renderer;

import geometries.Intersectable;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;

import java.util.List;
import geometries.Intersectable.GeoPoint;
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
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null)
            return scene.background;
        GeoPoint closest = ray.findClosestGeoPoint(intersections);
        return calcColor(closest);
    }

    /**
     * find the color of a point.
     * @param point the point to find the color of
     * @return the color of the point
     */

    private Color calcColor(GeoPoint point) {
        return scene.ambientLight.getIntensity().add(point.geometry.getEmission());
    }
}
