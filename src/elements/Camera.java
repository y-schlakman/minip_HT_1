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
     * @return Ray originating at eye of camera and pointing towards the centre of the pixel in the j`th column and i`th row.
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i){

        if((nX <= 0 )|| (nY <= 0))
            throw new IllegalArgumentException("Error: Screen is degenerate in one or more dimensions.");

        if((j<0) || (j>=nX) || (i<0) || (i>=nY))
            throw new IllegalArgumentException("Error: Pixel's position (in screen - space) exceeds/preceeds screen definition.");

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
