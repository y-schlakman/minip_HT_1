package primitives;

/**
 * The vector class is used to represent a mathematical vector in the 3 dimentional cartesian space.
 * This class is used in many calculations such as lighting, collision etc.
 *
 * @author Eli and Yosi.
 */
public class Vector {
    /**The point containing the values associated with
     *  the vectors direction and space in cartesian representation */
    Point3D head;

    /**
     * ctor using double values representing the necessary information about
     * the vectors magnitude and direction in the 3 dimensional cartesian space.
     *
     * @param x value along the x axis.
     * @param y value along the y axis.
     * @param z value along the z axis.
     * @exception IllegalArgumentException if the given values form a degenerate vector (the zero vector).
     */
    public Vector(double x, double y, double z)
    {
        head = new Point3D(x, y, z);
        if(head.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Degenerate vector parameters (zero vector).");
    }

    /**
     * ctor using a Point3D object representing the necessary information about
     * the vectors magnitude and direction in the 3 dimensional cartesian space.
     *
     * @exception IllegalArgumentException if the given point forms a degenerate vector (the zero vector).
     */
    public Vector(Point3D head){
        this.head = head;
        if(head.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Degenerate vector parameters (zero vector).");
    }

    /**
     * getter for the head of a vector.
     * @return the head of the vector.
     */
    public Point3D getHead() {
        return head;
    }

    /**
     * Calculates vector addition.
     *
     * @param v the other vector.
     * @return the sum of this vector and the given vector.
     */
    public Vector add(Vector v){
        return new Vector(head.add((v)));
    }

    /**
     * Calculates vector subtraction.
     *
     * @param v the other vector.
     * @return the difference between this vector and the given vector.
     */
    public Vector subtract(Vector v){
        return this.add(v.scale(-1));
    }

    /**
     * Calculates scaling this vector by a constant.
     *
     * @param c the scaling constant.
     * @return the resulting scaled vector.
     */
    public Vector scale(double c){
        return new Vector(head.x.coord * c,
                head.y.coord * c,
                head.z.coord * c);
    }

    /**
     * Calculates the dot product between two vectors geometrically .
     *
     * @param v the other vector.
     * @return resulting value.
     */
    //This is done geometrically (x1*x2 + y1*y2 + z1*z2) and not trigonometricaly (|v1|*|v2| cos(alpha)) to save time.
    public double dotProduct(Vector v){
        return head.x.coord * v.head.x.coord +
                head.y.coord * v.head.y.coord +
                head.z.coord * v.head.z.coord;
    }

    /**
     * Calculates the cross product between two vectors.
     *
     * @param v the other vector.
     * @return the resulting vector.
     */
    /*
    The calculations were done as follows:
    (a1,b2,c1) X (a2,b2,c2) =
          | i  j  k  |
        = | a1 b1 c1 | = i(b1*c2 - c1*b2) - j(a1*c2 - c1*a2) + k(a1*b2 - b1*a2)
          | a2 b2 c2 |

     */
    public Vector crossProduct(Vector v){
        return new Vector(head.y.coord*v.head.z.coord - head.z.coord*v.head.y.coord,
                head.z.coord*v.head.x.coord - head.x.coord*v.head.z.coord,
                head.x.coord*v.head.y.coord - head.y.coord*v.head.x.coord);
    }

    /**
     * Calculates the length of the vector squared.
     * @return the length squared.
     */
    public double lengthSquared(){
        return head.distanceSquared(Point3D.ZERO);
    }

    /**
     * Calculates the length of the vector.
     * @return the length of the vector.
     */
    public double length(){
        return head.distance(Point3D.ZERO);
    }

    /**
     * Normalizes this vector, IE setting its magnitude to be 1 whilst keeping its direction the same.
     * @return the resulting vector.
     */
    public Vector normalize(){
        head = scale(1/length()).head;
        return this;
    }

    /**
     * Calcultes the normal vector pointing in this vector`s direction.
     * @return the resulting vector.
     */
    public Vector normalized(){
        Vector v = new Vector(head);
        return v.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Vector)) return false;
        Vector other = (Vector)obj;
        return this.head.equals(other.head);
    }

    @Override
    public String toString() {
        return head.toString();
    }
}
