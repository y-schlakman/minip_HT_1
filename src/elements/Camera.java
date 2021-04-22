package elements;

import primitives.Point3D;
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
            throw new IllegalArgumentException("ERROR: 'vTo' and 'vUp' are not orthogonal");

        _p0 = p0;
        _vTo=vTo.normalized();
        _vUp=vUp.normalized();

        _vRight = _vTo.crossProduct(_vUp);

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
        _width = width;
        _height = height;
        return this;
    }

    public Camera setDistance(double distance) {
        _distance = distance;
        return this;
    }


}
