package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;

import java.util.MissingResourceException;

/**
 * class for rendering an image of a 3d scene
 * has functions to render, draw a grid and save as an image
 *
 * @author Yosi and Eli
 */
public class Render {
    private ImageWriter imageWriter;
    private Camera camera;
    private RayTracerBase rayTracer;

    /**
     * setter for imageWriter
     * @param imageWriter new imageWriter
     * @return the Render object
     */
    public Render setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    /**
     * setter for camera
     * @param camera new camera
     * @return the Render object
     */
    public Render setCamera(Camera camera) {
        this.camera = camera;
        return this;
    }

    /**
     * setter for rayTracer
     * @param rayTracer new rayTracer
     * @return the Render object
     */
    public Render setRayTracer(RayTracerBase rayTracer) {
        this.rayTracer = rayTracer;
        return this;
    }

    /**
     * renders the image from the 3d scene
     */
    public void renderImage() {
        if (imageWriter == null)
            throw new MissingResourceException("render fields cannot be null", "Render", "imageWriter");
        if (camera == null)
            throw new MissingResourceException("render fields cannot be null", "Render", "camera");
        if (rayTracer == null)
            throw new MissingResourceException("render fields cannot be null", "Render", "rayTracer");

        Ray ray;
        Color color;
        for (int j = 0; j < imageWriter.getNx(); ++j) {
            for (int i = 0; i < imageWriter.getNy(); ++i) {
                ray = camera.constructRayThroughPixel(imageWriter.getNx(), imageWriter.getNy(), j, i);
                color = rayTracer.traceRay(ray);
                imageWriter.writePixel(j, i, color);
            }
        }
    }

    /**
     * prints a grid onto the rendered image
     * @param interval distance between lines of grid
     * @param color color of grid lines
     */
    public void printGrid(int interval, Color color) {
        //check that image
        if (imageWriter == null)
            throw new MissingResourceException("render fields cannot be null", "Render", "imageWriter");

        //draw grid
        for (int j = 0; j < imageWriter.getNx(); ++j) {
            for (int i = 0; i < imageWriter.getNy(); ++i) {
                if ((j % interval == 0 && j != 0) || (i % interval == 0 && i != 0))
                    imageWriter.writePixel(j, i, color);
            }
        }
    }

    /**
     * calls imageWriter's writeToImage method that saves the rendered image as a png file
     */
    public void writeToImage() {
        if (imageWriter == null)
            throw new MissingResourceException("render fields cannot be null", "Render", "imageWriter");
        imageWriter.writeToImage();
    }
}
