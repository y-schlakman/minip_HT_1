package pictureImprovements;

import elements.AmbientLight;
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

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.PINK), 0.25));

        scene.setDiffuseEnabled(true);
        scene.setGlossyEnabled(true);
        scene.setNumGlossyDiffuseRays(15);


        /*
        Walls and floor.
         */
        double initialDepth = 2000;
        double wallHeight = 1000;
        double floorWidth = 300, floorDepth = 500;
        scene.geometries.add(
                /*
                    B       C

                    A,W       D,X


                          Z         Y
                 */
        new Polygon(new Point3D(-floorWidth/2, 0, initialDepth + floorDepth), new Point3D(-floorWidth/2, wallHeight, initialDepth + floorDepth),
                new Point3D(floorWidth/2, wallHeight, initialDepth + floorDepth), new Point3D(floorWidth/2, 0, initialDepth + floorDepth))
                .setEmission(new Color(java.awt.Color.BLUE))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),

        new Polygon(new Point3D(-floorWidth/2, 0, initialDepth), new Point3D(floorWidth/2, 0, initialDepth),
                new Point3D(floorWidth/2, 0, initialDepth + floorDepth), new Point3D(-floorWidth/2, 0, initialDepth + floorDepth))
                .setEmission(new Color(java.awt.Color.GRAY))
                .setMaterial(new Material().setkD(0.75).setkS(0.75).setnShininess(20).setkR(0.2).setGlossyRadius(2))
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

        double scopeDepth = 1000;
        double scopeScale = 7;
        double scopeDx = -17, scopeDy = 150;
        Point3D pa = new Point3D(-3.8*scopeScale + scopeDx, -1.8*scopeScale + scopeDy, scopeDepth);
        Point3D pb = new Point3D(-1.5*scopeScale + scopeDx, -1.8*scopeScale + scopeDy, scopeDepth);
        Point3D pc = new Point3D(0.2 *scopeScale + scopeDx, -0.1*scopeScale + scopeDy, scopeDepth);
        Point3D pd = new Point3D(0.2 *scopeScale + scopeDx, 2.2 *scopeScale + scopeDy, scopeDepth);
        Point3D pe = new Point3D(-1.5*scopeScale + scopeDx, 3.9 *scopeScale + scopeDy, scopeDepth);
        Point3D pf = new Point3D(-3.8*scopeScale + scopeDx, 3.9 *scopeScale + scopeDy, scopeDepth);
        Point3D pg = new Point3D(-5.5*scopeScale + scopeDx, 2.2 *scopeScale + scopeDy, scopeDepth);
        Point3D ph = new Point3D(-5.5*scopeScale + scopeDx, -0.1*scopeScale + scopeDy, scopeDepth);
        Point3D pq = new Point3D(-7.1*scopeScale + scopeDx, 5.5 *scopeScale + scopeDy, scopeDepth);
        Point3D pr = new Point3D(1.8 *scopeScale + scopeDx, 5.5 *scopeScale + scopeDy, scopeDepth);
        Point3D ps = new Point3D(1.8 *scopeScale + scopeDx, -3.5*scopeScale + scopeDy, scopeDepth);
        Point3D pt = new Point3D(-7.1*scopeScale + scopeDx, -3.5*scopeScale + scopeDy, scopeDepth);

        List<Polygon> scopeBoarder = new ArrayList<Polygon>();
        scopeBoarder.add( new Polygon(pq, pr, pe, pf));
        scopeBoarder.add( new Polygon(pr, pd, pe));
        scopeBoarder.add( new Polygon(pr, ps, pc, pd));
        scopeBoarder.add( new Polygon(pc, ps, pb));
        scopeBoarder.add( new Polygon(pb, ps, pt, pa));
        scopeBoarder.add( new Polygon(ph, pa, pt));
        scopeBoarder.add( new Polygon(pq, pg, ph, pt));
        scopeBoarder.add( new Polygon(pq, pf, pg));
        Polygon polyScope = new Polygon(pa, pb, pc, pd, pe, pf, pg, ph);

        for(Polygon p : scopeBoarder)
            p.setEmission(new Color(java.awt.Color.GREEN))
                    .setMaterial(new Material().setkD(0.4).setkS(0.4).setnShininess(30));

        scene.geometries.add(scopeBoarder.toArray(new Polygon[scopeBoarder.size()]));
        scene.geometries.add(polyScope.setEmission(new Color(java.awt.Color.WHITE).reduce(3))
        .setMaterial(new Material().setkT(0.8).setkD(0.1).setkS(0.1).setDiffuseRadius(1.5))
                );

        double crosshairWidth = 0.2, crosshairDelta = 0.5;
        Point3D pab = new Point3D((pa.getX().getCoord() + pb.getX().getCoord())/2, pa.getY().getCoord(), pa.getZ().getCoord() - crosshairDelta);
        Point3D pfe = new Point3D((pf.getX().getCoord() + pe.getX().getCoord())/2, pf.getY().getCoord(), pf.getZ().getCoord() - crosshairDelta);
        Point3D pgh = new Point3D(pg.getX().getCoord(), (pg.getY().getCoord() + ph.getY().getCoord())/2, pg.getZ().getCoord() - crosshairDelta);
        Point3D pdc = new Point3D(pd.getX().getCoord(), (pd.getY().getCoord() + pc.getY().getCoord())/2, pd.getZ().getCoord() - crosshairDelta);
        scene.geometries.add(
                //Vertical crosshair line.
                new Polygon(
                        pab.add(new Vector(1,0,0).scale(scopeScale*crosshairWidth/2)),
                        pfe.add(new Vector(1,0,0).scale(scopeScale*crosshairWidth/2)),
                        pfe.add(new Vector(-1,0,0).scale(scopeScale*crosshairWidth/2)),
                        pab.add(new Vector(-1,0,0).scale(scopeScale*crosshairWidth/2))
                ).setEmission(new Color(java.awt.Color.BLACK))
                .setMaterial(new Material()),

                //Horizontal crosshair line.
                new Polygon(
                        pgh.add(new Vector(0,1,0).scale(scopeScale*crosshairWidth/2)),
                        pdc.add(new Vector(0,1,0).scale(scopeScale*crosshairWidth/2)),
                        pdc.add(new Vector(0,-1,0).scale(scopeScale*crosshairWidth/2)),
                        pgh.add(new Vector(0,-1,0).scale(scopeScale*crosshairWidth/2))
                ).setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material())
        );

        double gunLength = 100, gunHeight = 2;
        scene.geometries.add(
                //Top part of gun
                new Polygon(
                        pt, ps, ps.add(new Vector(0,0,-1).scale(scopeScale*gunLength)),
                        pt.add(new Vector(0,0,-1).scale(scopeScale*gunLength))
                ).setEmission(new Color(java.awt.Color.GREEN).reduce(3))
                .setMaterial(new Material().setkD(0.4).setkS(0.4).setnShininess(10)),

                //Bottom left part of gun.
                new Polygon(
                        ps, ps.add(new Vector(0,0,-1).scale(scopeScale*gunLength)),
                        ps.add(new Vector(0,0,-1).scale(scopeScale*gunLength)).add(new Vector(0,-1,0).scale(scopeScale*gunHeight)),
                        ps.add(new Vector(0,-1,0).scale(scopeScale*gunHeight))
                ).setEmission(new Color(java.awt.Color.GREEN).reduce(3))
                        .setMaterial(new Material().setkD(0.4).setkS(0.4).setnShininess(10)),

                //In-front part of gun.
                new Polygon(
                        ps.add(new Vector(0,0,-1).scale(scopeScale*gunLength)),
                        pt.add(new Vector(0,0,-1).scale(scopeScale*gunLength)),
                        pt.add(new Vector(0,0,-1).scale(scopeScale*gunLength)).add(new Vector(0,-1,0).scale(scopeScale*gunHeight)),
                        ps.add(new Vector(0,0,-1).scale(scopeScale*gunLength)).add(new Vector(0,-1,0).scale(scopeScale*gunHeight))
                ).setEmission(new Color(java.awt.Color.GREEN).reduce(3))
                        .setMaterial(new Material().setkD(0.4).setkS(0.4).setnShininess(10))

                );

        double lightAngleX1 = Math.toRadians(0); //Up\down.
        double lightAngleY1 = Math.toRadians(20); //Left\right.
        double lightAngleX2 = Math.toRadians(25); //Up\down.
        double lightAngleY2 = Math.toRadians(-15); //Left\right.

        scene.lights.add( //
                new DirectionalLight(new Color(java.awt.Color.WHITE).reduce(5),
                        new Vector(0, 0, 1).RotateX(lightAngleX1).RotateY(lightAngleY1)) //

        );
        scene.lights.add( //
                new DirectionalLight(new Color(255, 200, 150).reduce(1),
                        new Vector(0, -1, 0).RotateX(lightAngleX2).RotateY(lightAngleY2)) //

        );



        Render render = new Render() //
                .setImageWriter(new ImageWriter("TargetPractice", 600, 600)) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene));
        render.renderImage();
        render.writeToImage();
    }


}
