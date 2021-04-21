package primitives;

import static primitives.Util.isZero;
/**
 * The Ray class is used to represent a mathematical ray stemming at a point p0 in 3 dimensional cartesian space and
 * pointing in a direction 'dir'.
 * The class is used in many calculations such as those regarding rays of light or vision.
 *
 * @author Eli and Yosi.
 */
public class Ray {

    /**The point at which the ray begins */
    final Point3D _p0;

    /** The direction in which the ray is pointing */
    final Vector _dir;

    /**
     * ctor
     * @param p0 beginning point.
     * @param dir direction.
     */
    public Ray(Point3D p0, Vector dir){
        this._p0 = p0;
        this._dir = dir;
        this._dir.normalize();
    }

    /**
     * getter for p0 field
     * @return value of p0
     */
    public Point3D get_p0()
    {
        return _p0;
    }

    /**
     * getter for dir field
     * @return value of dir
     */
    public Vector get_dir()
    {
        return _dir;
    }

    public Point3D getPoint(double t) {
        if (isZero(t)) {
            return _p0;
        }
        return _p0.add(_dir.scale(t));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Ray)) return false;
        Ray other = (Ray)obj;
        return this._p0.equals(other._p0) && this._dir.equals(other._dir);
    }

    @Override
    public String toString() {
        return "start: " + _p0.toString() + ", direction: " + _dir.toString();
    }
}
