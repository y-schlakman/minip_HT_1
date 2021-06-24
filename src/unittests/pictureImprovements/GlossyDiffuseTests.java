package pictureImprovements;

import elements.*;
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
 * Tests for glossy and diffuse functionality.
 *
 * @author Yosi and Eli
 */

public class GlossyDiffuseTests {
    private Scene scene = new Scene("Test scene");

    /**
     * Produce a picture of a target board through the lense of a glossily refracted gun scope,
     *  Also mirrored on a glossy floor.
     */
    @Test
    public void TargetPracticeTest() {
        double camAngleX = 0.1, camAngleY = 0;

        Camera camera = new Camera(new Point3D(0, 450, 0),
                new Vector(0, 0, 1).RotateX(camAngleX).RotateY(camAngleY),
                new Vector(0, 1, 0).RotateX(camAngleX).RotateY(camAngleY)) //
                .setViewPlaneSize(150, 150).setDistance(1000);


        /*
        Walls and floor.
         */
        double initialDepth = 2000; //The starting depth of the floor.
        double wallHeight = 1000;
        double floorWidth = 300, floorDepth = 3000; //Additional depth to the floor beyond 'initialDepth'
        scene.geometries.add(
                /*
                These letters describe by alphabetical order the positions
                of the two planes (wall ABCD, floor WXYZ):
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
                .setMaterial(new Material().setkD(0.75).setkS(0.75).setnShininess(20).setkR(0.2).setGlossyRadius(7))
        );

        /*
        Target board.
         */
        //The board is split by construction from the stand its on.
        double standWidth = 20, standHeight = 75, standFromWall = 30;
        double boardDimension = 225;
        double boardDepth = initialDepth + floorDepth - standFromWall;
        scene.geometries.add(
                /*
                    X           Y

                    W   B   C   Z

                        A   D
                 */

                new Polygon(new Point3D(-standWidth/2, 0, boardDepth), new Point3D(-standWidth/2, standHeight, boardDepth),
                        new Point3D(standWidth/2, standHeight, boardDepth), new Point3D(standWidth/2, 0, boardDepth))
                        .setEmission(new Color(255, 153, 0).reduce(5))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50)),

                new Polygon(new Point3D(-boardDimension/2, standHeight, boardDepth), new Point3D(-boardDimension/2, standHeight + boardDimension, boardDepth),
                        new Point3D(boardDimension/2, standHeight + boardDimension, boardDepth), new Point3D(boardDimension/2, standHeight, boardDepth))
                        .setEmission(new Color(255, 153, 0).reduce(5))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50))
        );


        //Target polygons.
        double cbX = 0, cby = standHeight + boardDimension/2;//Center board coordinates
        int numCircles = 10, numVerticies = 20;
        double distCircleFromBoard = 0.1, minRadius = 1; // variables used to define the largest and smallest circles -
        //radius, and from there we can linearly interpolate according to the amount of circles 'numCircles'.
        double distBetweenPoly = 0.1; //distance ensuring a gap between the board and the target.
        List<Point3D> poly = new ArrayList<>();
        //Outer loop creates polygons ('circles') and adds them to the board.
        for(int i = 0; i < numCircles; ++i ) {
            poly.clear();
            Vector vDown = new Vector(0, -1, 0);
            double angle = 2 * Math.PI / (double) numVerticies;
            double radius = minRadius +
                    ((double) (numCircles-i) / (double) numCircles) * ((boardDimension / 2) - distCircleFromBoard - minRadius);
            //Inner loop creates each vertex and adds it to current polygon ('circle').
            for (int j = 0; j < numVerticies; ++j) {
                poly.add(vDown.RotateZ(angle * j).scale(radius).getHead()
                        .add(new Vector(0,0,-1).scale((i+1)*distBetweenPoly))
                        .add(new Vector(0,cby,boardDepth)));
            }
            scene.geometries.add(
                    new Polygon(poly.toArray(new Point3D[poly.size()]))
                    .setEmission(i%2 == 0? new Color(java.awt.Color.WHITE).reduce(4) : new Color(java.awt.Color.RED).reduce(4))
                    .setMaterial(new Material().setkD(0.5).setkS(0.25))
            );
        }

        //Variables defining scope features:
        double scopeDepth = 1400; //Independant of other geometries in the room and their depth.
        double scopeScale = 13;
        double scopeDx = 5, scopeDy = 350;

        //A list of hard-coded points defining the scope and its frame:
        Point3D pa = new Point3D(-3.8*scopeScale + scopeDx, -1.8*scopeScale + scopeDy, scopeDepth);
        Point3D pb = new Point3D(-1.5*scopeScale + scopeDx, -1.8*scopeScale + scopeDy, scopeDepth);
        Point3D pc = new Point3D(0.2 *scopeScale + scopeDx, -0.1*scopeScale + scopeDy, scopeDepth);
        Point3D pd = new Point3D(0.2 *scopeScale + scopeDx, 2.2 *scopeScale + scopeDy, scopeDepth);
        Point3D pe = new Point3D(-1.5*scopeScale + scopeDx, 3.9 *scopeScale + scopeDy, scopeDepth);
        Point3D pf = new Point3D(-3.8*scopeScale + scopeDx, 3.9 *scopeScale + scopeDy, scopeDepth);
        Point3D pg = new Point3D(-5.5*scopeScale + scopeDx, 2.2 *scopeScale + scopeDy, scopeDepth);
        Point3D ph = new Point3D(-5.5*scopeScale + scopeDx, -0.1*scopeScale + scopeDy, scopeDepth);
        Point3D pq = new Point3D(-4.03*scopeScale + scopeDx, 4.47 *scopeScale + scopeDy, scopeDepth);
        Point3D pr = new Point3D(-1.27 *scopeScale + scopeDx, 4.47 *scopeScale + scopeDy, scopeDepth);
        Point3D ps = new Point3D(-1.27 *scopeScale + scopeDx, -2.37*scopeScale + scopeDy, scopeDepth);
        Point3D pt = new Point3D(-6.07*scopeScale + scopeDx, -0.33*scopeScale + scopeDy, scopeDepth);
        Point3D pi = new Point3D(0.77*scopeScale + scopeDx, 2.43*scopeScale + scopeDy, scopeDepth);
        Point3D pj = new Point3D(0.77*scopeScale + scopeDx, -0.33*scopeScale + scopeDy, scopeDepth);
        Point3D pk = new Point3D(-4.03*scopeScale + scopeDx, -2.37*scopeScale + scopeDy, scopeDepth);
        Point3D pl = new Point3D(-6.07*scopeScale + scopeDx, 2.43*scopeScale + scopeDy, scopeDepth);

        //Creating the scopes frame (each polygon must be convex so one must split it into pieces).
        List<Polygon> scopeBoarder = new ArrayList<Polygon>();
        scopeBoarder.add( new Polygon(pq, pr, pe, pf));
        scopeBoarder.add( new Polygon(pr, pi, pd, pe));
        scopeBoarder.add( new Polygon(pi, pj, pc, pd));
        scopeBoarder.add( new Polygon(pj, ps, pb, pc));
        scopeBoarder.add( new Polygon(ps, pk, pa, pb));
        scopeBoarder.add( new Polygon(pk, pt, ph, pa));
        scopeBoarder.add( new Polygon(pt, pl, pg, ph));
        scopeBoarder.add( new Polygon(pl, pq, pf, pg));
        Polygon polyScope = new Polygon(pa, pb, pc, pd, pe, pf, pg, ph);

        for(Polygon p : scopeBoarder)
            p.setEmission(new Color(java.awt.Color.GREEN))
                    .setMaterial(new Material().setkD(0.4).setkS(0.4).setnShininess(30));

        scene.geometries.add(scopeBoarder.toArray(new Polygon[scopeBoarder.size()]));
        scene.geometries.add(polyScope.setEmission(new Color(java.awt.Color.BLACK))
        .setMaterial(new Material().setkT(1).setkD(0.1).setkS(0.1).setDiffuseRadius(3))
                );

        //Cross-hair features, affectively two rectangles across the scope.
        double crosshairWidth = 0.2, crosshairDelta = 0.5;
        //The following are midpoints pre-calculated to make crosshair calculations simpler.
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

        //The gun has three parts defined as follows:
        double gunLength = 100, gunHeight = 2, gunWidth = 1.3;
        Point3D rightGunVertex = pk.add(new Vector(-1,0,0).scale(scopeScale*gunWidth));
        Point3D leftGunVertex = ps.add(new Vector(1,0,0).scale(scopeScale*gunWidth));
        scene.geometries.add(
                //Top part of gun
                new Polygon(
                        ps,
                        pk,
                        rightGunVertex.add(new Vector(0,0,-1).scale(scopeScale*gunLength)),
                        leftGunVertex.add(new Vector(0,0,-1).scale(scopeScale*gunLength))
                ).setEmission(new Color(java.awt.Color.GREEN).reduce(3))
                .setMaterial(new Material().setkD(0.4).setkS(0.4).setnShininess(10)),

                //Bottom left part of gun.
                new Polygon(
                        ps, ps.add(new Vector(0,0,-1).scale(scopeScale*gunLength)),
                        leftGunVertex.add(new Vector(0,0,-1).scale(scopeScale*gunLength)).add(new Vector(0,-1,0).scale(scopeScale*gunHeight)),
                        leftGunVertex.add(new Vector(0,-1,0).scale(scopeScale*gunHeight))
                ).setEmission(new Color(java.awt.Color.GREEN).reduce(3))
                        .setMaterial(new Material().setkD(0.4).setkS(0.4).setnShininess(10)),

                //In-front part of gun.
                new Polygon(
                        leftGunVertex.add(new Vector(0,0,-1).scale(scopeScale*gunLength)),
                        rightGunVertex.add(new Vector(0,0,-1).scale(scopeScale*gunLength)),
                        rightGunVertex.add(new Vector(0,0,-1).scale(scopeScale*gunLength)).add(new Vector(0,-1,0).scale(scopeScale*gunHeight)),
                        leftGunVertex.add(new Vector(0,0,-1).scale(scopeScale*gunLength)).add(new Vector(0,-1,0).scale(scopeScale*gunHeight))
                ).setEmission(new Color(java.awt.Color.GREEN).reduce(3))
                        .setMaterial(new Material().setkD(0.4).setkS(0.4).setnShininess(10))

                );

        //Light angles.
        double lightAngleX1 = Math.toRadians(0); //Up\down.
        double lightAngleY1 = Math.toRadians(20); //Left\right.
        double lightAngleX2 = Math.toRadians(70); //Up\down.
        double lightAngleY2 = Math.toRadians(-4); //Left\right.

        scene.lights.add( //
                new DirectionalLight(new Color(java.awt.Color.WHITE).reduce(6),
                        new Vector(0, 0, 1).RotateX(lightAngleX1).RotateY(lightAngleY1))
        );
        scene.lights.add( //
                new DirectionalLight(new Color(java.awt.Color.WHITE).reduce(4),
                        new Vector(-1, 0, 0).RotateX(lightAngleX1).RotateY(lightAngleY1))
        );
        scene.lights.add( //
                new SpotLight(new Color(java.awt.Color.WHITE), new Point3D(0,standHeight + boardDimension + 100, boardDepth-((numCircles)*distBetweenPoly + 200)), new Vector(0,0,boardDepth)).setkL(0.00005).setkQ(0)
        );
        scene.lights.add(
                new SpotLight(new Color(java.awt.Color.WHITE).reduce(3), new Point3D(0,standHeight + boardDimension + 100, boardDepth-((numCircles)*distBetweenPoly + 200)), new Vector(0,0,boardDepth)).setkL(0.0005).setkQ(0)
        );

        Render render = new Render() //
                .setImageWriter(new ImageWriter("TargetPractice", 600, 600))
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene).setDiffuseEnabled(true).setGlossyEnabled(true).setNumGlossyDiffuseRays(15));
        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void BallsTest(){
        double camAngleX = Math.toRadians(29), camAngleY = Math.toRadians(0);

        Camera camera = new Camera(new Point3D(0, 1000, 0),
                new Vector(0, 0, 1).RotateX(camAngleX).RotateY(camAngleY),
                new Vector(0, 1, 0).RotateX(camAngleX).RotateY(camAngleY)) //
                .setViewPlaneSize(150, 150).setDistance(1000);

        double floorDepth = 1600;
        double tileDimension = 20;
        int numRows = 20, numColumns = 2 * 10;//NumColumns must be even.
        boolean colorFlag = true;
        for(int i=0;i<numRows;++i){
            for(int j=0;j<numColumns/2;++j){
                scene.geometries.add(
                        //Left side:
                        new Polygon(
                                new Point3D(-j*tileDimension, 0, floorDepth + i*tileDimension), new Point3D(-j*tileDimension, 0, floorDepth + (i+1)*tileDimension),
                                new Point3D(-(j+1)*tileDimension, 0, floorDepth + (i+1)*tileDimension),new Point3D(-(j+1)*tileDimension, 0, floorDepth + i*tileDimension)
                        ).setEmission( new Color(colorFlag? java.awt.Color.WHITE: java.awt.Color.BLACK))
                                .setMaterial(new Material().setkD(0.3).setkS(0.3).setkR(0.4).setGlossyRadius(0.1))
                        ,

                        //Right side:
                        new Polygon(
                                new Point3D(j*tileDimension, 0, floorDepth + i*tileDimension), new Point3D(j*tileDimension, 0, floorDepth + (i+1)*tileDimension),
                                new Point3D((j+1)*tileDimension, 0, floorDepth + (i+1)*tileDimension),new Point3D((j+1)*tileDimension, 0, floorDepth + i*tileDimension)
                        ).setEmission( new Color(colorFlag? java.awt.Color.BLACK: java.awt.Color.WHITE))
                                .setMaterial(new Material().setkD(0.3).setkS(0.3).setkR(0.4).setGlossyRadius(0.1))

                );
                colorFlag = !colorFlag;
            }
            if(numColumns%4 == 0)
                colorFlag = !colorFlag;
        }

        //The center of the floors along the z axis (x and y obviously 0). Useful as reference point for the spheres.
        double floorCenter = floorDepth + (double)numRows*tileDimension/(double)2;

        /*
        Now adding three spheres:
        On the left an opaque unreflective red sphere, called 'a'.
        In the middle an opaque reflective green sphere, called 'b'.
        On the right a transparent unreflective blue sphere, called 'c'.

        The balls are not placed in a straight line, rather in a semi-circle around the center of the floor.
         */

        double radA = 10, radB = 15, radC = 13;

        //The radius of the semi-circle the spheres are placed in.
        double placementRadius = 20;

        //The scale of all of the sphere-system.
        double sphereScale = 2.5;

        scene.geometries.add(
                new Sphere(new Point3D(placementRadius*sphereScale, radA*sphereScale, floorCenter), radA*sphereScale)
                .setEmission(new Color(java.awt.Color.RED).reduce(4.5))
                .setMaterial(new Material().setkD(0.999).setkS(0.9).setnShininess(60)),

                new Sphere(new Point3D(0, radB*sphereScale, floorCenter + placementRadius*sphereScale), radB*sphereScale)
                        .setEmission(new Color(java.awt.Color.GREEN).reduce(4))
                        .setMaterial(new Material().setkD(0.999).setkS(0.9).setnShininess(60).setkR(0.5).setGlossyRadius(0.3)),

                new Sphere(new Point3D(-placementRadius*sphereScale, radC*sphereScale, floorCenter), radC*sphereScale)
                        .setEmission(new Color(java.awt.Color.BLUE).reduce(4))
                        .setMaterial(new Material().setkD(0.999).setkS(0.9).setnShininess(60).setkT(0.15).setDiffuseRadius(0.3))
        );


        /*
        There are four street-lights, one in each corner of the floor, 1/5 floor width away from the respective edge.
        See the following diagram: (where '*' represent the boarder of the floor, ['A', 'B', 'C', 'D'] represent a street-light, and '@' a light bulb)

                *************************
                *                       *
                *   A@             @B   *
                *   @               @   *
                *                       *
                *                       *
                *   @               @   *
                *   D@             @C   *
                *                       *
                *************************

         Each light is composed of a stand - centred at 'slCenterX\Z', as wide as 'slWidth', as tall as 'slHeight';
            and two light-bulbs pointing to the axis-aligned neighboring street-light (see diagram '@'s) - whos radius is 'slBulbRadius'.

         Everything is scaled by 'slScale'.
         */

        //Calculated and stored once instead of multiple times.
        double tenthFloorX = numColumns*(double)tileDimension/(double)10;
        double tenthFloorZ = numRows*(double)tileDimension/(double)10;

        List<Double> slCenterX = new ArrayList<Double>();
        slCenterX.add(3*tenthFloorX);
        slCenterX.add(-3*tenthFloorX);
        slCenterX.add(-3*tenthFloorX);
        slCenterX.add(3*tenthFloorX);

        List<Double> slCenterZ = new ArrayList<Double>();
        slCenterZ.add(floorDepth + 8*tenthFloorZ);
        slCenterZ.add(floorDepth + 8*tenthFloorZ);
        slCenterZ.add(floorDepth + 2*tenthFloorZ);
        slCenterZ.add(floorDepth + 2*tenthFloorZ);

        double slWidth = 5, slHeight = 50;
        double slBulbRadius = 5;
        double slScale = 1;

        //Variables preventing calling 'get' method many times.
        double currX, currZ;

        for(int i=0;i<4;++i){
            currX = slCenterX.get(i);
            currZ = slCenterZ.get(i);
            scene.geometries.add(
                    //Walls of the stand:

                    //Left.
                    new Polygon(
                            new Point3D(currX + slScale*slWidth/2, 0, currZ - slScale*slWidth/2),
                            new Point3D(currX + slScale*slWidth/2, 0, currZ + slScale*slWidth/2),
                            new Point3D(currX + slScale*slWidth/2, slScale*slHeight, currZ + slScale*slWidth/2),
                            new Point3D(currX + slScale*slWidth/2, slScale*slHeight, currZ - slScale*slWidth/2)
                    ).setEmission(new Color(java.awt.Color.WHITE).reduce(8))
                            .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(20))
                    ,

                    //Back
                    new Polygon(
                            new Point3D(currX + slScale*slWidth/2, 0, currZ + slScale*slWidth/2),
                            new Point3D(currX + slScale*slWidth/2, slScale*slHeight, currZ + slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, slScale*slHeight, currZ + slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, 0, currZ + slScale*slWidth/2)
                    ).setEmission(new Color(java.awt.Color.WHITE).reduce(8))
                            .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(20))
                    ,

                    //Right.
                    new Polygon(
                            new Point3D(currX - slScale*slWidth/2, 0, currZ - slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, 0, currZ + slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, slScale*slHeight, currZ + slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, slScale*slHeight, currZ - slScale*slWidth/2)
                    ).setEmission(new Color(java.awt.Color.WHITE).reduce(8))
                            .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(20))
                    ,

                    //Front.
                    new Polygon(
                            new Point3D(currX + slScale*slWidth/2, 0, currZ - slScale*slWidth/2),
                            new Point3D(currX + slScale*slWidth/2, slScale*slHeight, currZ - slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, slScale*slHeight, currZ - slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, 0, currZ - slScale*slWidth/2)
                    ).setEmission(new Color(java.awt.Color.WHITE).reduce(8))
                            .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(20))
                    ,

                    //Top.
                    new Polygon(
                            new Point3D(currX + slScale*slWidth/2, slScale*slHeight, currZ - slScale*slWidth/2),
                            new Point3D(currX + slScale*slWidth/2, slScale*slHeight, currZ + slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, slScale*slHeight, currZ + slScale*slWidth/2),
                            new Point3D(currX - slScale*slWidth/2, slScale*slHeight, currZ - slScale*slWidth/2)
                    ).setEmission(new Color(java.awt.Color.PINK).reduce(8))
                            .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(20))
            );

            //Utility variable representing distance form stands center to any of its bulbs.
            double toBulb =  slScale*(slWidth/2 + slBulbRadius);
            double bulbeCenterLeftX = currX + toBulb, bulbeCenterLeftZ = currZ;
            double bulbeCenterBackX = currX, bulbeCenterBackZ = currZ  + toBulb;
            double bulbeCenterRightX = currX -toBulb, bulbeCenterRightZ = currZ;
            double bulbeCenterFrontX = currX, bulbeCenterFrontZ = currZ - toBulb;

            double bulbHeight = slScale*(slHeight-slBulbRadius);

            Color bulbEmission = new Color(java.awt.Color.PINK);
            Material bulbMaterial = new Material().setkD(0.1).setkS(0.1).setnShininess(3).setkT(0.5).setDiffuseRadius(0.05);

            Color lightColor = new Color(java.awt.Color.PINK).reduce(2);

            //Now adding bulbs according to current light:
            //Left bulb:
            if(i == 1 || i == 2){
                Point3D currCenter = new Point3D(bulbeCenterLeftX, bulbHeight, bulbeCenterLeftZ);
                scene.geometries.add(
                        new Sphere(currCenter, slScale*slBulbRadius)
                                .setEmission(bulbEmission)
                                .setMaterial(bulbMaterial)
                );
                scene.lights.add(new PointLight(lightColor, currCenter));
            }

            //Back bulb:
            if(i == 2 || i == 3){
                Point3D currCenter = new Point3D(bulbeCenterBackX, bulbHeight, bulbeCenterBackZ);
                scene.geometries.add(
                        new Sphere(currCenter, slScale*slBulbRadius)
                                .setEmission(bulbEmission)
                                .setMaterial(bulbMaterial)
                );
                scene.lights.add(new PointLight(lightColor, currCenter));
            }

            //Right bulb:
            if(i == 0 || i == 3){
                Point3D currCenter = new Point3D(bulbeCenterRightX, bulbHeight, bulbeCenterRightZ);
                scene.geometries.add(
                        new Sphere(currCenter, slScale*slBulbRadius)
                                .setEmission(bulbEmission)
                                .setMaterial(bulbMaterial)
                );
                scene.lights.add(new PointLight(lightColor, currCenter));
            }

            //Front bulb:
            if(i == 0 || i == 1){
                Point3D currCenter = new Point3D(bulbeCenterFrontX, bulbHeight, bulbeCenterFrontZ);
                scene.geometries.add(
                        new Sphere(currCenter, slScale*slBulbRadius)
                                .setEmission(bulbEmission)
                                .setMaterial(bulbMaterial)
                );
                scene.lights.add(new PointLight(lightColor, currCenter));
            }

        }

        Render render = new Render() //
                .setImageWriter(new ImageWriter("BallsTestWithGlossAndDiffuse", 600, 600))
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene).setGlossyEnabled(true).setDiffuseEnabled(true).setNumGlossyDiffuseRays(16))
                .setMultithreading(3);
        long begin = System.currentTimeMillis();
        render.renderImage();
        long end = System.currentTimeMillis();
        System.out.print("rendered in: " + ((int)(end - begin)/1000)/60 + " minutes ");
        System.out.print("and " + ((int)(end - begin)/1000)%60 + " seconds.");
        //render.printGrid(50, new Color(java.awt.Color.YELLOW));
        render.writeToImage();

    }


}
