package pictureImprovements;

import elements.Camera;
import elements.DirectionalLight;
import elements.SpotLight;
import geometries.Polygon;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import renderer.Render;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for glossy reflection and refraction functionality.
 *
 * @author Yosi and Eli
 */

public class GlossyTests {
    private Scene scene = new Scene("Test scene");

    /**
     * Produce a picture of a target board through the lense of a glossily refracted gun scope,
     *  Also mirrored on a glossy floor.
     */
    @Test
    public void TargetPracticeTest() {
        Camera camera = new Camera(new Point3D(0, 150, 0), new Vector(0, 0, 1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(150, 150).setDistance(1000);

        /*
        Walls and floor.
         */
        double initialDepth = 2000;
        //double wallHeight = 100, wallWidth = 100, wallDepth = 5000;
        double floorWidth = 300, floorDepth = 500;
        scene.geometries.add(
                /*
                    B       C

                    A,W       D,X


                          Z         Y
                 */
        /*new Polygon(new Point3D(-wallWidth/2, 0, wallDepth), new Point3D(-wallWidth/2, wallHeight, wallDepth),
                new Point3D(wallWidth/2, wallHeight, wallDepth), new Point3D(wallWidth/2, 0, wallDepth))
                .setEmission(new Color(java.awt.Color.BLUE))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),
        */
        new Polygon(new Point3D(-floorWidth/2, 0, initialDepth), new Point3D(floorWidth/2, 0, initialDepth),
                new Point3D(floorWidth/2, 0, initialDepth + floorDepth), new Point3D(-floorWidth/2, 0, initialDepth + floorDepth))
                .setEmission(new Color(java.awt.Color.LIGHT_GRAY))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50).setkR(0.5))
        );

        /*
        Target board.
         */
        double standWidth = 20, standHeight = 100;
        double boardDimention = 150;
        scene.geometries.add(
                /*
                    X           Y

                    W   B   C   Z

                        A   D
                 */

                new Polygon(new Point3D(-standWidth/2, 0, initialDepth + floorDepth/2), new Point3D(-standWidth/2, standHeight, initialDepth + floorDepth/2),
                        new Point3D(standWidth/2, standHeight, initialDepth + floorDepth/2), new Point3D(standWidth/2, 0, initialDepth + floorDepth/2))
                        .setEmission(new Color(255, 153, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),

                new Polygon(new Point3D(-boardDimention/2, standHeight, initialDepth + floorDepth/2), new Point3D(-boardDimention/2, standHeight + boardDimention, initialDepth + floorDepth/2),
                        new Point3D(boardDimention/2, standHeight + boardDimention, initialDepth + floorDepth/2), new Point3D(boardDimention/2, standHeight, initialDepth + floorDepth/2))
                        .setEmission(new Color(255, 153, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50))

                /*
                new Polygon(
                        new Point3D(-standWidth/2, 0, initialDepth + floorDepth/2), new Point3D(-standWidth/2, standHeight, initialDepth + floorDepth/2),
                        new Point3D(-boardDimention/2, standHeight, initialDepth + floorDepth/2), new Point3D(-boardDimention/2, standHeight + boardDimention, initialDepth + floorDepth/2),
                        new Point3D(boardDimention/2, standHeight + boardDimention, initialDepth + floorDepth/2), new Point3D(boardDimention/2, standHeight, initialDepth + floorDepth/2),
                        new Point3D(standWidth/2, standHeight, initialDepth + floorDepth/2), new Point3D(standWidth/2, 0, initialDepth + floorDepth/2)
                )
                */
        );


        //Target polygons.
        double cbX = 0, cby = standHeight + boardDimention/2;//Center board coordinates
        int numCircles = 10, numVerticies = 20;
        double distCircleFromBoard = 10, minRadius = 1;
        double distBetweenPoly = 1;
        List<Point3D> poly = new ArrayList<>();
        for(int i = 0; i < numCircles; ++i ) {
            poly.clear();
            Vector vDown = new Vector(0, -1, 0);
            double angle = 2 * Math.PI / (double) numVerticies;
            double radius = minRadius +
                    ((double) (numCircles-i) / (double) numCircles) * ((boardDimention / 2) - distCircleFromBoard - minRadius);
            for (int j = 0; j < numVerticies; ++j) {
                poly.add(vDown.RotateZ(angle * j).scale(radius).getHead()
                        .add(new Vector(0,0,-1).scale((i+1)*distBetweenPoly))
                        .add(new Vector(0,cby,initialDepth + floorDepth/2)));
            }
            scene.geometries.add(
                    new Polygon(poly.toArray(new Point3D[poly.size()]))
                    .setEmission(i%2 == 0? new Color(java.awt.Color.WHITE) : new Color(java.awt.Color.RED))
                    .setMaterial(new Material().setkD(0.5).setkS(0.25))
            );
        }



        double lightAngleX = Math.toRadians(-20); //Up\down.
        double lightAngleY = Math.toRadians(-10); //Left\right.
        //double lightAnglez = Math.toRadians(20); //dunno.

        scene.lights.add( //
                new DirectionalLight(new Color(java.awt.Color.WHITE).reduce(3),
                        new Vector(0, 0, 1).RotateX(lightAngleX).RotateY(lightAngleY)) //

        );




        Render render = new Render() //
                .setImageWriter(new ImageWriter("TargetPractice", 500, 500)) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene));
        render.renderImage();
        render.writeToImage();
    }


}
