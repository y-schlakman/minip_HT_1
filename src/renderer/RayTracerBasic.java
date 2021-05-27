package renderer;

import elements.LightSource;
import geometries.Intersectable;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable.GeoPoint;

import static primitives.Util.alignZero;

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
        return calcColor(closest, ray);
    }

    /**
     * find the color of a point.
     * @param point the point to find the color of
     * @return the color of the point
     */
    private Color calcColor(GeoPoint point, Ray ray) {
        return scene.ambientLight.getIntensity().add(point.geometry.getEmission())
                // add calculated light contribution from all light sources)
                .add(calcLocalEffects(point, ray));
    }

    /**
     * calculates the local light effects color at an intersection point
     * @param intersection the point for which we want the local light effects color
     * @param ray the ray from camera through view plane that intersects our point
     * @return the local light effects color at the given point
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Vector v = ray.get_dir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;
        Material material = intersection.geometry.getMaterial();
        int nShininess = material.nShininess;
        double kd = material.kD, ks = material.kS;
        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sing(nv)
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));
            }
        }
        return color;
    }

    /**
     * calculates the diffusive light color at a point
     * given the diffuse coefficient, direction of the light, normal vector from the point, and the light intensity color.
     * @param kd diffuse coefficient
     * @param l direction of the light
     * @param n normal vector from the point
     * @param lightIntensity light intensity color
     * @return the diffusive light color at the point
     */
    private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity) {
        return lightIntensity.scale(kd * Math.abs(l.dotProduct(n)));
    }

    /**
     * calculates the specular light color at a point
     * given the specular coefficient, direction of the light, normal vector from the point,
     * direction from the camera to the point, shininess of the point, and the light intensity color.
     * @param ks specular coefficient
     * @param l direction of the light
     * @param n normal vector from the point
     * @param v direction from the camera to the point
     * @param nShininess shininess of the point
     * @param lightIntensity light intensity color
     * @return the specular light color at the point
     */
    private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector reflectance = l.subtract(n.scale(l.dotProduct(n)).scale(2)).normalize();//according to the presentation: r = l - 2 * (l * n) * n
        return lightIntensity.scale(ks * Math.pow(Math.max(0, v.scale(-1).dotProduct(reflectance)), nShininess));
    }
}
