package renderer;

import elements.LightSource;
import geometries.Intersectable;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

import geometries.Intersectable.GeoPoint;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * child class of RayTracerBase, that is used to determine color of a view plane pixel from which we casted a ray.
 *
 * @author Yosi and Eli
 */
public class RayTracerBasic extends RayTracerBase {
    /**
     * MAX_CALC_COLOR_LEVEL is the amount of levels we allow the recursive calculation of light to run for.
     * MIN_CALC_COLOR_K the k below which we consider a coefficient insignificant and consider it as zero.
     * INITIAL_K is the constant that scales the coefficients in our calculations by zero to one (zero - use none its value, one - use all of its value).
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double INITIAL_K = 1.0;

//TODO:remove
    /*//Constants for testing purposes:
    private int GLOSSY_NUM_RAYS = 10;
    private double GLOSSY_RADIUS = 0.5;*/

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
     * calculates the color at a given point given the ray that intersects the point.
     *
     * @param gp  the geo-point representing the point for which we want the color at.
     * @param ray the ray that intersected the point.
     * @return the color at the given point.
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }

    /**
     * recursive function to find the color of an intersection.
     *
     * @param intersection the intersection to find the color of.
     * @param ray          the ray that intersected the intersection point.
     * @param level        the level of the recursion.
     * @param k            the current k coefficient of the color.
     * @return the color of the intersection
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, double k) {
        Color color = intersection.geometry.getEmission();
        color = color.add(calcLocalEffects(intersection, ray, k));
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray.get_dir(), level, k));
    }

    /**
     * calculates the color to add from global effects in the scene like reflections/refractions of light rays
     *
     * @param gp    the point for which to calculate the color for.
     * @param v     the direction vector from the camera to the point.
     * @param level the current level of the recursion.
     * @param k     the current k coefficient of the color.
     * @return the color added by the global effects in the scene.
     */
    private Color calcGlobalEffects(GeoPoint gp, Vector v, int level, double k) {
        Color color = Color.BLACK;
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();

        double kkr = k * material.kR;
        if (kkr > MIN_CALC_COLOR_K) {
            if (!scene.glossyEnabled || isZero(material.glossyRadius))//No glossy affect.
                color = calcGlobalEffect(constructReflectedRay(gp.point, v, n), level, material.kR, kkr);
            else {
                color = calcGlossiness(constructReflectedRay(gp.point, v, n), material.glossyRadius, level, material.kR, kkr);
            }
        }

        /*
        //Reflection light.
        double kkr = k * material.kR;
        if (kkr > MIN_CALC_COLOR_K)
            color = calcGlobalEffect(constructReflectedRay(gp.point, v, n), level, material.kR, kkr);

        */
/*
        //Refraction \\ transparency light.
        double kkt = k * material.kT;
        if (kkt > MIN_CALC_COLOR_K)
            color = color.add(
                    calcGlobalEffect(constructRefractedRay(gp.point, v, n), level, material.kT, kkt));
        return color;*/

        //Refraction \\ transparency light.
        double kkt = k * material.kT;
        if (kkt > MIN_CALC_COLOR_K)
            if (!scene.diffuseEnabled || isZero(material.diffuseRadius))//No diffuse affect.
                color = color.add(calcGlobalEffect(constructRefractedRay(gp.point, v, n), level, material.kT, kkt));
            else {
                color = color.add(calcGlossiness(constructRefractedRay(gp.point, v, n), material.diffuseRadius, level, material.kT, kkt));
            }
        return color;
    }

    /**
     * gets a reflected or refracted ray, and calculates color added from that ray.
     *
     * @param ray   the reflected or refracted ray.
     * @param level the current level of the recursion.
     * @param kx    kx coefficient value.
     * @param kkx   kkx coefficient value.
     * @return the color added from the given ray.
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
     * constructs a reflected ray given the point of reflection, the original rays direction, and the normal vector at the point.
     *
     * @param point the reflection point
     * @param v     the direction of the original ray.
     * @param n     the normal vector at the point of reflection.
     * @return a ray that is the original ray reflected off of the reflection point and elevated by a small delta.
     */
    private Ray constructReflectedRay(Point3D point, Vector v, Vector n) {
        Vector direction = v.subtract(n.scale(v.dotProduct(n)).scale(2)).normalize();
        return new Ray(point, direction, n);
    }

    /**
     * constructs a refracted ray given a refraction point, the original rays direction and the normal vector at the point.
     *
     * @param point the refraction point.
     * @param v     the original rays direction.
     * @param n     the normal vector at the refraction point.
     * @return a ray that is the original ray refracted off of the refraction point and lowered by a small delta.
     */
    private Ray constructRefractedRay(Point3D point, Vector v, Vector n) {
        return new Ray(point, v, n);
    }

    /**
     * Finds some vector that's perpendicular to the given.
     *
     * @param n Said given vector to find a perpendicular vector for.
     * @return The resulting vector.
     */
    private Vector findPerpendicular(Vector n) {
        double x = n.getHead().getX().getCoord();
        double y = n.getHead().getY().getCoord();
        double z = n.getHead().getZ().getCoord();

        double a, b, c;
        if (x == 0) {
            a = 1;
            b = z;
            c = -y;
        } else if (y == 0) {
            b = 1;
            a = z;
            c = -x;
        } else if (z == 0) {
            c = 1;
            a = y;
            b = -x;
        } else {
            a = 1;
            b = 1;
            c = -x * y / z;
        }
        return new Vector(a, b, c);
    }

    /**
     * calculates the average color around a given ray's intersection.
     *
     * @param ray the ray to calculate average color around its intersection.
     * @param radius the radius the cone of rays around the original ray
     * @param level level parameter to send to calcGlobalEffect
     * @param k k parameter to send to calcGlobalEffect
     * @param kk kk parameter to send to calcGlobalEffect
     * @return the average color around the given ray's intersection.
     */
    private Color calcGlossiness(Ray ray, double radius, int level, double k, double kk) {
        //calculate the original color coming from the original ray.
        Color color = calcGlobalEffect(ray, level, k, kk);

        //find the closest intersection of the original ray with another object in the scene
        GeoPoint intersection = findClosestIntersection(ray);

        //if the ray has no intersections with other objects in the scene, return the color from the original ray.
        if (intersection == null)
            return color;

        //the points through which the additional rays should pass through
        ArrayList<Point3D> vertexPoints = getRegularPolygonVertices(intersection.point, radius, scene.numGlossyDiffuseRays, ray.get_dir().normalized());

        //add supplemental rays color to the final color
        for (Point3D vertexPoint:vertexPoints) {
            color = color.add(calcGlobalEffect(new Ray(ray.get_p0(), vertexPoint.subtract(ray.get_p0())), level, k, kk));
        }

        //return the average of all the rays colors
        return color.reduce(scene.numGlossyDiffuseRays + 1);
    }

    private ArrayList<Point3D> getRegularPolygonVertices(Point3D center, double radius, int numVertices, Vector axis) {
        //find the perpendicular vector to the axis
        Vector toRotate = findPerpendicular(axis).normalize();

        //Cross product vector used each time to find next vertex.
        Vector crossProduct = axis.crossProduct(toRotate);

        Vector vertexVector;
        ArrayList<Point3D> vertexPoints = new ArrayList<Point3D>();
        double angle = 2 * Math.PI / numVertices;//calculate the angle between each new vertex.
        double cos, sin;

        for (int i = 0; i < numVertices; ++i) {
            //spin the vector along the given axis
            cos = alignZero(Math.cos(angle * i));
            sin = alignZero(Math.sin(angle * i));

            if (cos == 0)
                vertexVector = crossProduct.scale(sin);
            else if (sin == 0)
                vertexVector = toRotate.scale(cos);
            else
                vertexVector = toRotate.scale(cos).add(crossProduct.scale(sin));

            //find vertex that is on the rotated vector and "radius" away from center point
            vertexPoints.add(center.add(vertexVector.scale(radius)));
        }
        return vertexPoints;
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
