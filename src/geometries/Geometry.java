package geometries;

import primitives.*;

/**
 * interface to be implemented by all geometries.
 *
 * @author Yosi and Eli.
 */
public abstract class Geometry implements Intersectable {

    //Variable for the emission light of a geometry.
    protected Color emission = Color.BLACK;

    //Material properties of this geometry object.
    protected Material material = new Material();

    //the geometry's bounding box
    protected AABB aabb;

    /**
     * Getter methode for material field.
     * @return The 'material' field.
     */
    public Material getMaterial(){
        return material;
    }

    /**
     * Setter method for material properties.
     * @param material Desired new material values.
     * @return Instance of current geometry.
     */
    public Geometry setMaterial(Material material){
        this.material = material;
        return this;
    }

    /**
     * Setter method for emission colour.
     * @param emission the new colour.
     * @return This 'Geometry' object.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Getter method for emission colour.
     * @return The current emission colour.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * gets the normal vector of a geometry at a given point on the geometry.
     *
     * @param point a point on the geometry
     * @return normalized normal vector to the geometry at point.
     */
    public abstract Vector getNormal(Point3D point);

    @Override
    public AABB getAABB() {
        return aabb;
    }
}
