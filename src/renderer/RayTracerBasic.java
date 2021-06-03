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
     * TODO: explain what these are
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double INITIAL_K = 1.0;

    /**
     * constructor that gets a scene
     *
     * @param scene the scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closest = findClosestIntersection(ray);
        if (closest == null)
            return scene.background;
        return calcColor(closest, ray);
    }

    /**
     * TODO:make javadoc
     *
     * @param gp
     * @param ray
     * @return
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }

    /**
     * find the color of a intersection.
     *
     * @param intersection the intersection to find the color of
     * @return the color of the intersection
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, double k) {
        Color color = intersection.geometry.getEmission();
        color = color.add(calcLocalEffects(intersection, ray, k));
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray.get_dir(), level, k));
    }

    /**
     * TODO: make javadoc
     *
     * @param gp
     * @param v
     * @param level
     * @param k
     * @return
     */
    private Color calcGlobalEffects(GeoPoint gp, Vector v, int level, double k) {
        Color color = Color.BLACK;
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        double kkr = k * material.kR;
        if (kkr > MIN_CALC_COLOR_K)
            color = calcGlobalEffect(constructReflectedRay(gp.point, v, n), level, material.kR, kkr);
        double kkt = k * material.kT;
        if (kkt > MIN_CALC_COLOR_K)
            color = color.add(
                    calcGlobalEffect(constructRefractedRay(gp.point, v, n), level, material.kT, kkt));
        return color;
    }

    /**
     * TODO:make javadoc
     *
     * @param ray
     * @param level
     * @param kx
     * @param kkx
     * @return
     */
    private Color calcGlobalEffect(Ray ray, int level, double kx, double kkx) {
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background : calcColor(gp, ray, level - 1, kkx)
        ).scale(kx);
    }

    /**
     * calculates the local light effects color at an intersection point
     *
     * @param intersection the point for which we want the local light effects color
     * @param ray          the ray from camera through view plane that intersects our point
     * @return the local light effects color at the given point
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {
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
                double ktr = transparency(lightSource, l, n, intersection);
                if (ktr * k > MIN_CALC_COLOR_K) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
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
     *
     * @param kd             diffuse coefficient
     * @param l              direction of the light
     * @param n              normal vector from the point
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
     *
     * @param ks             specular coefficient
     * @param l              direction of the light
     * @param n              normal vector from the point
     * @param v              direction from the camera to the point
     * @param nShininess     shininess of the point
     * @param lightIntensity light intensity color
     * @return the specular light color at the point
     */
    private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector reflectance = l.subtract(n.scale(l.dotProduct(n)).scale(2)).normalize();//according to the presentation: r = l - 2 * (l * n) * n
        return lightIntensity.scale(ks * Math.pow(Math.max(0, v.scale(-1).dotProduct(reflectance)), nShininess));
    }

    /**
     * checks if a given point is shaded from a given light source by another object in the scene
     *
     * @param l  the direction of the light
     * @param n  normal vector from the point
     * @param gp geo-point representing the point for which we are checking if it is shaded from the light
     * @return true if unshaded by another object and false if shaded
     */
    private boolean unshaded(LightSource light, Vector l, Vector n, GeoPoint gp) {
        //cast ray from a little bit point + DELTA over the surface
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(gp.point, lightDirection, n);

        //get intersections between the point and the light source
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        //if there are no intersections between the object and the light return true
        if (intersections == null) {
            return true;
        }

        /*
          check if intersection is actually between the light and the point
          or if it's behind the light source and therefore not shading the object from the light
         */
        double lightDistance = light.getDistance(gp.point);
        for (GeoPoint shadeIntersection : intersections) {
            //if intersection is between point and light - return false
            if (alignZero(shadeIntersection.point.distance(gp.point) - lightDistance) <= 0 && shadeIntersection.geometry.getMaterial().kT == 0) {
                return false;
            }
        }

        //the intersection is behind the light source - so return true
        return true;
    }

    /**
     * checks if a given point is shaded from a given light source by another object in the scene
     * and measures how much light (if any) passes through the obstructing object and reaches the point.
     *
     * @param ls       the light source for which we are testing if its light gets to the point
     * @param l        the direction of the light source
     * @param n        normal vector from the point
     * @param geoPoint geo-point representing the point for which we are checking if and how much it is shaded from the light source
     * @return a number between 0 and 1 representing how much light reaches the point. 1 is all of the light, and 0 is none of it.
     */
    private double transparency(LightSource ls, Vector l, Vector n, GeoPoint geoPoint) {
        //cast shadow ray from a bit over the surface to the light source
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);

        //get distance from the point to the light and find intersections between them
        double lightDistance = ls.getDistance(geoPoint.point);
        var intersections = scene.geometries.findGeoIntersections(lightRay);

        //if no intersections found - so light is unobstructed and return 1.0
        if (intersections == null) {
            return 1.0;
        }

        double ktr = 1.0;
        for (GeoPoint gp : intersections) {
            //check that the intersection is actually between the light and the point and not behind the light
            if (alignZero(gp.point.distance(geoPoint.point) - lightDistance) <= 0) {
                ktr *= gp.geometry.getMaterial().kT;

                //if the ktr is becoming insignificant - so there is a shadow return 0.0
                if (ktr < MIN_CALC_COLOR_K) {
                    return 0.0;
                }
            }
        }
        return ktr;
    }

    /**
     * TODO: add javadoc
     *
     * @param point
     * @param v
     * @param n
     * @return
     */
    private Ray constructReflectedRay(Point3D point, Vector v, Vector n) {
        Vector direction = v.subtract(n.scale(v.dotProduct(n)).scale(2)).normalize();
        return new Ray(point, direction, n);
    }

    /**
     * TODO: add javadoc
     *
     * @param point
     * @param v
     * @param n
     * @return
     */
    private Ray constructRefractedRay(Point3D point, Vector v, Vector n) {
        return new Ray(point, v, n);
    }

    /**
     * finds the closest intersection with the ray that's closest to the base of the ray
     *
     * @param ray the ray to intersect and find closest intersection to its base
     * @return the closest intersection to base of the given ray
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        return ray.findClosestGeoPoint(scene.geometries.findGeoIntersections(ray));
    }
}
