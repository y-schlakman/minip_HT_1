package geometries;

import java.util.List;
import java.util.ArrayList;

import primitives.*;

import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry {

    //List of polygon's vertices.
    protected List<Point3D> vertices;

    //Associated plane in which the polygon lays.
    protected Plane plane;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point3D... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3) {// no need for more tests for a Triangle
            createAABB();
            return;
        }

        //Vector n = plane.getNormal();
        Vector n = plane.getNormal(null);

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
        createAABB();
    }

    /**
     * Calculates normal vector to this polygon
     * at a given point (affectively gets normal to underlying plane).
     *
     * @param point a point on the geometry.
     * @return Normal vector to this polygon.
     */
    @Override
    public Vector getNormal(Point3D point) {
        // plane.getNormal();
        return plane.getNormal(null);
    }

    /**
     * Finds the intersection point between this polygon and a given ray (should there be one),
     * with respect to this polygon as the intersections's geometry.
     *
     * @param ray the ray intersecting the geometry.
     * @return a list including the intersection point with respect to this polygon as its geometry.
     * 'null' if there is none.
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {

        Point3D p0 = ray.get_p0();
        Vector v = ray.get_dir();

        int s = vertices.size(); //we use this value mubltiple times so we save it locally to avoid many function calls.

        List<Point3D> intersection = plane.findIntersections(ray);
        if (intersection == null) //Does not intersect with plane including polygon therefore does not intersect polygon.
            return null;

        List<Vector> toVertex = new ArrayList<>(s); //Vectors used to create a 'box-ey cone' around the polygon which
        // allows us to check whether the intersection is within or beyond each face of said polyhedra and therfore compare respective answers across each plane to find out if
        //the point is inside them all.

        for (int i = 0; i < s; ++i)
            toVertex.add(i, vertices.get(i).subtract(p0));

        List<Vector> normals = new ArrayList<>(s);
        for (int i = 0; i < s; ++i)
            normals.add(i, toVertex.get(i).crossProduct(toVertex.get((i + 1) % s)));

        double currProduct = alignZero(v.dotProduct(normals.get(0)));
        if (currProduct == 0)
            return null;

        boolean sign = currProduct > 0, currSign;

        for (int i = 1; i < s; ++i) {
            currProduct = alignZero(v.dotProduct(normals.get(i)));
            if (currProduct == 0)
                return null;
            currSign = currProduct > 0;
            if (currSign != sign)
                return null;
        }

        List<GeoPoint> res = new ArrayList();
        res.add(new GeoPoint(this, intersection.get(0)));
        return res;
    }

    /**
     * creates the aabb for the polygon
     */
    private void createAABB() {
        Point3D max = new Point3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Point3D min = new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        for (Point3D vertex : vertices) {
            //find min and max x coordinate
            if (vertex.getX().getCoord() < min.getX().getCoord())
                min = new Point3D(vertex.getX().getCoord(), min.getY().getCoord(), min.getZ().getCoord());
            if (vertex.getX().getCoord() > max.getX().getCoord())
                max = new Point3D(vertex.getX().getCoord(), max.getY().getCoord(), max.getZ().getCoord());

            //find min and max y coordinate
            if (vertex.getY().getCoord() < min.getY().getCoord())
                min = new Point3D(min.getX().getCoord(), vertex.getY().getCoord(), min.getZ().getCoord());
            if (vertex.getY().getCoord() > max.getY().getCoord())
                max = new Point3D(max.getX().getCoord(), vertex.getY().getCoord(), max.getZ().getCoord());

            //find min and max z coordinate
            if (vertex.getZ().getCoord() < min.getZ().getCoord())
                min = new Point3D(min.getX().getCoord(), min.getY().getCoord(), vertex.getZ().getCoord());
            if (vertex.getZ().getCoord() > max.getZ().getCoord())
                max = new Point3D(max.getX().getCoord(), max.getY().getCoord(), vertex.getZ().getCoord());
        }

        aabb = new AABB(max, min);
    }
}
