package primitives;

/**
 * The Ray class is used to represent a mathematical ray stemming at a point p0 in 3 dimensional cartesian space and
 * pointing in a direction 'dir'.
 * The class is used in many calculations such as those regarding rays of light or vision.
 *
 * @author Eli and Yosi.
 */
public class Ray {

    /**The point at which the ray begins */
    final Point3D p0;

    /** The direction in which the ray is pointing */
    final Vector dir;

    /**
     * ctor
     * @param p0 beginning point.
     * @param dir direction.
     */
    public Ray(Point3D p0, Vector dir){
        this.p0 = p0;
        this.dir = dir;
        if(dir.lengthSquared() != 1)
            this.dir.normalized();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Ray)) return false;
        Ray other = (Ray)obj;
        return this.p0.equals(other.p0) && this.dir.equals(other.dir);
    }

    @Override
    public String toString() {
        return "start: " + p0.toString() + ", direction: " + dir.toString();
    }
}
