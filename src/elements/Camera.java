package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * Class encapsulating and implementing the functions of a camera in a graphic scene.
 *
 * @author Yosi and Eli.
 */
public class Camera {

    private final Point3D _p0; // Camera location
    private final Vector _vTo; // Vector pointing 'forwards' relative to camera.
    private final Vector _vUp; // Vector pointing upwards relative to camera.
    private final Vector _vRight; // Vector pointing right relative to camera.

    //View plane information.
    private double _width; // Width of view plane.
    private double _height; // Height of view plane.
    private double _distance; // Distance between view plane and camera eye.

    /**
     * Constructor for camera element.
     *
     * @param p0 camera location.
     * @param vTo Vector pointing 'forwards' relative to camera.
     * @param vUp vector pointing upwards relative to camera.
     */
    public Camera(Point3D p0, Vector vTo, Vector vUp){
        if(!isZero(vTo.dotProduct(vUp)))
            throw new IllegalArgumentException("ERROR: vTo and vUp are not orthogonal");

        _p0 = p0;
        _vTo=vTo.normalized();
        _vUp=vUp.normalized();
        _vRight = _vTo.crossProduct(_vUp).normalize();

    }

    //Setters using method chaining.

    /**
     * setter for view plane dimensions.
     *
     * @param width width of view plane.
     * @param height height of view plane.
     * @return this instance of 'Camera' object.
     */
    public Camera setViewPlaneSize(double width, double height){
        if(width <= 0)
            throw new IllegalArgumentException("width must be positive");
        if(height <= 0)
            throw new IllegalArgumentException("height must be positive");

        _width = width;
        _height = height;

        return this;
    }

    /**
     * setter for distance property.
     * @param distance distance of view plane from the camera.
     * @return this instance of 'Camera' object.
     */
    public Camera setDistance(double distance) {
        if(distance <= 0)
            throw new IllegalArgumentException("distance must be positive");

        _distance = distance;

        return this;
    }

    /**
     * constructs a ray through a pixel
     * @param nX width of row
     * @param nY height of column
     * @param j column index of pixel
     * @param i row index of pixel
     * @return null for the moment as per instructions
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
        //temporarily return null as per instructions
        return null;
    }
}
