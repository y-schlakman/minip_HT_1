package renderer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Color;

/**
 * Unit tests for renderer.ImageWriter class
 *
 * @author Yosi and Eli
 */
class ImageWriterTest {
    /**
     * renders an image from an empty scene, draws a grid on the image, and saves ut as a png file
     * this is the first stage of assignment 5 and is the test described in the assignment's instructions
     */
    @Test
    void writeEmptySceneWithGrid() {
        int nX = 800, nY = 500;
        int xInterval = nX / 16, yInterval = nY / 10;
        ImageWriter imageWriter = new ImageWriter("empty scene with grid", nX, nY);

        //draw grid and background
        for (int j = 0; j < nX; ++j) {
            for (int i = 0; i < nY; ++i) {
                if ((j % xInterval == 0 && j != 0) || (i % yInterval == 0 && i != 0))
                    imageWriter.writePixel(j, i, new Color(new java.awt.Color(0, 255, 0)));
                else
                    imageWriter.writePixel(j, i, new Color(new java.awt.Color(0, 0, 255)));
            }
        }
        //save as an image
        imageWriter.writeToImage();
    }
}