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
     * delta value to move shade checking rays by, so that we don't cast
     * them from inside objects or behind their surface
     */
    private static final double DELTA = 0.1;

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
                if (unshaded(lightSource, l, n, intersection)) {
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
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

    /**
     * checks if a given point is shaded from a given light source by another object in the scene
     * @param l the direction of the light
     * @param n normal vector from the point
     * @param gp geo-point representing the point for which we are checking if it is shaded from the light
     * @return true if unshaded by another object and false if shaded
     */
    private boolean unshaded(LightSource light, Vector l, Vector n, GeoPoint gp){
        //cast ray from a little bit point + DELTA over the surface
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : - DELTA);
        Point3D point = gp.point.add(delta);
        Ray lightRay = new Ray(point, lightDirection);

        //get intersections between the point and the light source
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        //if there are no intersections between the object and the light return true
        if (intersections == null){
            return true;
        }

        /*
          check if intersection is actually between the light and the point
          or if it's behind the light source and therefore not shading the object from the light
         */
        double lightDistance = light.getDistance(gp.point);
        for (GeoPoint shadeIntersection : intersections) {
            //if intersection is between point and light - return false
            if (alignZero(shadeIntersection.point.distance(gp.point) - lightDistance) <= 0){
                return false;
            }
        }
        //the intersection is behind the light source - so return true
        return true;
    }
}
