package lights;

import geometries.Polygon;
import org.junit.jupiter.api.Test;

import elements.*;
import geometries.Sphere;
import geometries.Triangle;
import primitives.*;
import renderer.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 *
 * @author dzilb
 */
public class ReflectionRefractionTests {
    private Scene scene = new Scene("Test scene").setNumGlossyDiffuseRays(3).setDiffuseEnabled(true).setGlossyEnabled(true);

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(150, 150).setDistance(1000);

        scene.geometries.add( //
                new Sphere(new Point3D(0, 0, -50), 50) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setnShininess(100).setkT(0.3)),
                new Sphere(new Point3D(0, 0, -50), 25) //
                        .setEmission(new Color(java.awt.Color.RED)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)));
        scene.lights.add( //
                new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500), new Vector(-1, -1, -2)) //
                        .setkL(0.0004).setkQ(0.0000006));

        Render render = new Render() //
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500)) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        Camera camera = new Camera(new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(2500, 2500).setDistance(10000); //

        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

        scene.geometries.add( //
                new Sphere(new Point3D(-950, -900, -1000), 400) //
                        .setEmission(new Color(0, 0, 100)) //
                        .setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20).setkT(0.5).setDiffuseRadius(30)),
                new Sphere(new Point3D(-950, -900, -1000), 200) //
                        .setEmission(new Color(100, 20, 20)) //
                        .setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20)),
                new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
                        new Point3D(670, 670, 3000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setkR(1).setGlossyRadius(10)),
                new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
                        new Point3D(-1500, -1500, -2000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setkR(0.5).setGlossyRadius(10)));

        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point3D(-750, -750, -150), new Vector(-1, -1, -4)) //
                .setkL(0.00001).setkQ(0.000005));

        ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirroredWithGlossy", 500, 500);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene));

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a partially
     * transparent Sphere producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000);

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.geometries.add( //
                new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135), new Point3D(75, 75, -150)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)), //
                new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140), new Point3D(75, 75, -150)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)), //
                new Sphere(new Point3D(60, 50, -50), 30) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(30).setkT(0.6)));

        scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(60, 50, 0), new Vector(0, 0, -1)) //
                .setkL(4E-5).setkQ(2E-7));

        ImageWriter imageWriter = new ImageWriter("refractionShadowWithDiffuse", 600, 600);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene));

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produces a model of the our solar system showcasing all lighting effects we have.
     */
    @Test
    public void solarSystemTest() {

        double xAngle = Math.toRadians(-13);//Looking down 4.5 degrees.
        double yAngle = Math.toRadians(-36);//Looking 8 degrees to the left.
        double zAngle = Math.toRadians(0);//Rotation around z axis is like having ones head stay in place ,
        // and spin his legs around him without turning him away from what hes looking at.

        Vector to = new Vector(0, 0, -1).RotateX(xAngle).RotateY(yAngle).RotateZ(zAngle);
        Vector up = new Vector(0, 1, 0).RotateX(xAngle).RotateY(yAngle).RotateZ(zAngle);

        double dist = 7E3;//Factor of distance of camera to scene, used to control view maintaining the desired angle.
        Camera camera = new Camera(new Point3D(-6.3 * dist, 3 * dist, 10 * dist), to, up) //
                .setViewPlaneSize(200, 200).setDistance(1000);


        //scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        //Lists of planets radii and colour.
        List<Double> radii = new ArrayList<Double>(Arrays.<Double>asList(8d, 1d, 2d, 3d, 1.5d, 4d, 3.5d, 2d, 2.5d, 1d));
        List<Color> colors = new ArrayList<Color>(Arrays.<Color>asList(
                new Color(java.awt.Color.YELLOW), //Sun
                new Color(java.awt.Color.DARK_GRAY), //Mercury
                new Color(232, 104, 137), //Venus
                new Color(125, 232, 240), //Earth
                new Color(201, 97, 48), //Mars
                new Color(212, 166, 68), //Jupiter
                new Color(240, 205, 129), //Saturn
                new Color(62, 237, 220), //Uranus
                new Color(63, 58, 189), //Neptune
                new Color(195, 199, 70) //Pluto
        ));

        //First adding the sun as it has special definitions.
        double planetScale = 1E2;
        scene.geometries.add(
                new Sphere(new Point3D(0, 0, 0), radii.get(0) * planetScale) //Sun
                        .setEmission(colors.get(0).reduce(3))
                        .setMaterial(new Material().setkD(0.3).setkS(0).setnShininess(0).setkT(0.95))
        );


        double planetDistance = 6.5;//Distance between planets.
        Point3D pos, prevPos = new Point3D(0, 0, 0);
        //Looping through planets and adding them.
        for (int i = 1; i < 10; ++i) {
            pos = prevPos.add(new Vector(0, 0, 1).scale(planetScale * (radii.get(i - 1) + planetDistance)));
            pos = new Vector(pos).RotateY(0.04 * i).getHead();
            scene.geometries.add(
                    new Sphere(pos, radii.get(i) * planetScale)
                            .setEmission(colors.get(i).reduce(1))
                            .setMaterial(new Material().setkD(2.767676544).setkS(0))
            );
            prevPos = pos;
        }

        //Depth of mirror plane.
        double zPlane = -(radii.get(0) * planetScale + 300);

        //Room dimensions.
        double roomHeight = 6E3, roomWidth = 1.5E4, roomDepth = 8E3, xPush = 5E3;

        //Room walls.
        scene.geometries.add( //
                new Polygon(new Point3D(-roomWidth / 2 + xPush, -roomHeight / 2, zPlane), new Point3D(-roomWidth / 2 + xPush, roomHeight / 2, zPlane),
                        new Point3D(roomWidth / 2 + xPush, roomHeight / 2, zPlane), new Point3D(roomWidth / 2 + xPush, -roomHeight / 2, zPlane))
                        .setEmission(new Color(java.awt.Color.BLACK).scale(0.5))
                        .setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20).setkR(0.5)),

                new Polygon(new Point3D(-roomWidth / 2 + xPush, -roomHeight / 2, zPlane), new Point3D(roomWidth / 2 + xPush, -roomHeight / 2, zPlane),
                        new Point3D(roomWidth / 2 + xPush, -roomHeight / 2, zPlane + roomDepth), new Point3D(-roomWidth / 2 + xPush, -roomHeight / 2, zPlane + roomDepth))
                        .setEmission(new Color(java.awt.Color.GREEN).scale(0.25))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50))

        );

        //An arbitrary pentagon to meet the assignment requirements of three different types of bodies in the scene.
        scene.geometries.add(
                new Polygon(new Point3D(0, -roomHeight / 2, zPlane + roomDepth / 2), new Point3D(250, -roomHeight / 2, zPlane + roomDepth / 2),
                        new Point3D(250, -roomHeight / 2 + 250, zPlane + roomDepth / 2), new Point3D(125, -roomHeight / 2 + 375, zPlane + roomDepth / 2),
                        new Point3D(0, -roomHeight / 2 + 250, zPlane + roomDepth / 2))
                        .setEmission(new Color(java.awt.Color.BLUE).scale(1))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(50))
        );

        //Light sources.
        double kl = 1E-10, kq = 1E-10;
        //The sun light source.
        scene.lights.add(
                new PointLight(new Color(java.awt.Color.WHITE), new Point3D(0, 0, 0)) //
                        .setkL(kl).setkQ(kq)
        );
        //Weak directional light to showcase shadow.
        scene.lights.add(
                new DirectionalLight(new Color(java.awt.Color.WHITE).scale(0.1), new Vector(0, -1, 0).normalize())
        );

        ImageWriter imageWriter = new ImageWriter("refractionSolarSystemWithGlossy", 600, 600);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene));

        render.renderImage();
        render.writeToImage();

    }
}