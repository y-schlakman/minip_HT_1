package elements;

import primitives.Point3D;
import primitives.Vector;
import primitives.Ray;

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

    //Getters for camera info.

    /**
     * Getter function fo camera position.
     * @return Camera's position in 3dD space in the form of a 'Point3D'.
     */
    public Point3D getP0() {
        return _p0;
    }

    /**
     * Getter function for the 'to' vector pointing in the direction the camera is looking at.
     * @return Camera's 'to' vector.
     */
    public Vector getvTo() {
        return _vTo;
    }

    /**
     * Getter function for the 'up' vector pointing relatively 'upwards' and perpendicular to the 'to' vector.
     * @return Camera's 'up' vector.
     */
    public Vector getvUp() {
        return _vUp;
    }

    /**
     * Getter function for the 'right' vector pointing relatively 'to the right' of and perpendicular to the 'to' vector.
     * @return Camera's 'right' vector.
     */
    public Vector getvRight() {
        return _vRight;
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

    public Ray constructRayThroughPixel(int nX, int nY, int j, int i){

        if((j<0) || (j>=nX) || (i<0) || (i>=nY))
            throw new IllegalArgumentException("Error: Pixel's position (in screen - space) exceeds/preceeds screen definition.");

        if((nX <= 0 )|| (nY <= 0))
            throw new IllegalArgumentException("Error: Screen is degenerate in one or more dimensions.");

        //The position of the centre of the view plane in 3D space.
        Point3D imageCentre =  _p0.add(_vTo.scale(_distance));

        //Ratio of screen-to-pixel along the height and width dimensions respectively.
        double heightRatio = _height/(double)nY;
        double widthRatio = _width/(double)nX;

        //The distance in units from the centre of the view plane to the pixel in 3D space
        // along the width and height dimensions respectively.
        double widthDistance = (j-(nX-1)/(double)2) * widthRatio;
        double heightDistance = -(i-(nY-1)/(double)2) * heightRatio;


        //The centre of this specific pixel in 3D space.
        Point3D pixelCentre = imageCentre;
        if(widthDistance!=0)
            pixelCentre.add(_vRight.scale(widthDistance));
        if(heightDistance!=0)
            pixelCentre.add(_vUp.scale(heightDistance));

        //Ray originates at the eye of the camera(p0) and points in the direction of the centre of the pixel.
        return new Ray(_p0, pixelCentre.subtract(_p0));
    }



}
