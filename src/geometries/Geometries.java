package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Geometries implements Intersectable {

    private List<Intersectable> geometries;

    public Geometries() {
        geometries = new ArrayList<>(); //ArrayList is superior to LinkedList since its ease of access and to objects in the middle of the list.
    }

    public Geometries(Intersectable... geometries) {
        this.geometries = List.of(geometries);
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        if (geometries.isEmpty())
            return null;

        List<Point3D> res;
        int i = 0;
        do {
            res = geometries.get(i).findIntersections(ray);
            i++;
        } while (res == null && i < geometries.size());

        if (res == null)
            return null;

        List<Point3D> intersections;
        while (i < geometries.size()) {
            intersections = geometries.get(i).findIntersections(ray);
            if (intersections != null)
                res.addAll(intersections);
            i++;
        }
        return res;
    }
}
