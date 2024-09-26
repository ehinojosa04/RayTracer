package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.lights.PointLight;
import edu.up.isgc.cg.raytracer.objects.*;
import edu.up.isgc.cg.raytracer.tools.OBJReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Raytracer {
    private static final double AMBIENT_LIGHT_INTENSITY = 0.07;
    static final double EPSILON = 1e-4;  // Small offset value to prevent self-intersection

    public static void main(String[] args) {

        System.out.println(new Date());
        Scene scene06 = new Scene();
        scene06.setCamera(new Camera(new Vector3D(0, 0, -4), 60, 60, 800, 800, 0.6, 50.0));

        //scene06.addLight(new PointLight(new Vector3D(-1.5, 4, -3), Color.WHITE, 0.7));
        scene06.addLight(new PointLight(new Vector3D(0, 4, 0), Color.WHITE, 0.3));

        scene06.addObject(new Sphere(new Vector3D(0, 0, 0),1, Color.WHITE,10,1.9, 1));
        scene06.addObject(OBJReader.getModel3D("objfiles\\Plane.obj", new Vector3D(0f, -4, 0), Color.ORANGE, 5, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\Wall.obj", new Vector3D(0f, -3, 30), Color.WHITE, 10, 0, 0));

        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(-4,3, 8), Color.WHITE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(-2,3, 8), Color.ORANGE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(0,3, 8), Color.CYAN, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(2,3, 8), Color.MAGENTA, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(4,3, 8), Color.BLUE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(-4,1, 8), Color.BLUE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(-2,1, 8), Color.WHITE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(0,1, 8), Color.ORANGE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(2,1, 8), Color.CYAN, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(4,1, 8), Color.MAGENTA, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(-4,-1, 8), Color.MAGENTA, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(-2,-1, 8), Color.BLUE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(0,-1, 8), Color.WHITE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(2,-1, 8), Color.ORANGE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(4,-1, 8), Color.CYAN, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(-4,-3, 8), Color.CYAN, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(-2,-3, 8), Color.MAGENTA, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(0,-3, 8), Color.BLUE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(2,-3, 8), Color.WHITE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\CubeQuad.obj", new Vector3D(4,-3, 8), Color.ORANGE, 10, 0, 0));
        scene06.addObject(OBJReader.getModel3D("objfiles\\SmallTeapot.obj", new Vector3D(0,-0.75, 7), Color.RED, 10, 0, 0));

        //scene06.addObject(OBJReader.getModel3D("skull2.obj", new Vector3D(0,0, 8), Color.RED, 10, 0, 0));
        BufferedImage image = raytrace(scene06);

        File outputImage = new File("image.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        List<ObjectWithMaterial> objects = scene.getObjects();
        List<Light> lights = scene.getLights();
        Vector3D[][] posRaytrace = mainCamera.calculatePositionsToRay();
        Vector3D pos = mainCamera.getPosition();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < posRaytrace.length; i++) {
            final int row = i;
            Runnable rowTask = () -> {
                for (int j = 0; j < posRaytrace[row].length; j++) {
                    Color pixelColor = calculatePixelColor(row, j, nearFarPlanes, objects, lights, posRaytrace, pos);
                    synchronized (image) {
                        image.setRGB(row, j, pixelColor.getRGB());
                    }
                }
            };
            executorService.execute(rowTask);
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(20, TimeUnit.HOURS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!executorService.isTerminated()) {
                System.out.println("Cancel non-finished");
            }
        }
        return image;
    }

    public static Color calculatePixelColor(int i, int j, double[] nearFarPlanes, List<ObjectWithMaterial> objects, List<Light> lights, Vector3D[][] posRaytrace, Vector3D pos) {
        double x = posRaytrace[i][j].getX() + pos.getX();
        double y = posRaytrace[i][j].getY() + pos.getY();
        double z = posRaytrace[i][j].getZ() + pos.getZ();
        double cameraZ = pos.getZ();

        Ray ray = new Ray(pos, new Vector3D(x, y, z));
        return calculateRays(nearFarPlanes, objects, lights, pos, ray, cameraZ, 0);
    }

    public static Color calculateRays(double[] nearFarPlanes, List<ObjectWithMaterial> objects, List<Light> lights, Vector3D pos, Ray ray, double cameraZ, int depth) {
        Intersection closestIntersection = raycast(ray, objects, null, new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});
        Color pixelColor = Color.BLACK;

        if (closestIntersection != null) {
            Color objColor = closestIntersection.getObject().getColor();
            double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
            for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                objColors[colorIndex] *= AMBIENT_LIGHT_INTENSITY;
            }

            Color ambient = new Color((float) clamp(objColors[0], 0.0, 1.0), (float) clamp(objColors[1], 0.0, 1.0), (float) clamp(objColors[2], 0.0, 1.0));
            pixelColor = addColor(pixelColor, ambient);

            for (Light light : lights) {
                Vector3D vectorToLight = Vector3D.normalize(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()));
                Ray shadowRay = new Ray(closestIntersection.getPosition(), vectorToLight);
                Intersection shadowIntersection = raycast(shadowRay, objects, closestIntersection.getObject(), new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});
                if (shadowIntersection == null) {
                    double nDotL = light.getNDotL(closestIntersection);
                    Color lightColor = light.getColor();
                    double intensity = light.getIntensity() * nDotL;
                    if (light.getClass().getSimpleName().equals("PointLight")) {
                        intensity /= Math.pow(closestIntersection.getDistance(), 2);
                    }

                    double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                    objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
                    for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                        objColors[colorIndex] *= intensity * lightColors[colorIndex];
                    }

                    Color diffuse = new Color((float) clamp(objColors[0], 0.0, 1.0), (float) clamp(objColors[1], 0.0, 1.0), (float) clamp(objColors[2], 0.0, 1.0));
                    pixelColor = addColor(pixelColor, diffuse);

                    Vector3D vectorToCamera = Vector3D.normalize(Vector3D.substract(pos, closestIntersection.getPosition()));
                    Vector3D halfVector = Vector3D.normalize(Vector3D.add(vectorToLight, vectorToCamera));

                    double specularIndex = closestIntersection.getObject().getSpecularIndex();
                    if (specularIndex > 0) {
                        double constant = Math.pow(Vector3D.dotProduct(halfVector, closestIntersection.getNormal()), specularIndex);

                        objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
                        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                            objColors[colorIndex] *= constant;
                        }
                        Color specular = new Color((float) clamp(objColors[0], 0.0, 1.0), (float) clamp(objColors[1], 0.0, 1.0), (float) clamp(objColors[2], 0.0, 1.0));
                        pixelColor = addColor(pixelColor, specular);
                    }
                }


            }

            ObjectWithMaterial object = closestIntersection.getObject();
            if (depth < 4) {

                if (object.getReflectiveness() > 0) {
                    Vector3D normal = closestIntersection.getNormal();
                    Vector3D reflectedDir = Vector3D.substract(ray.getDirection(), Vector3D.scalarMultiplication(normal, 2 * Vector3D.dotProduct(ray.getDirection(), normal)));;
                    Ray reflectedRay = new Ray(Vector3D.add(closestIntersection.getPosition(), Vector3D.scalarMultiplication(normal, EPSILON)), reflectedDir);
                    Color reflectedColor = calculateRays(nearFarPlanes, objects, lights, pos, reflectedRay, cameraZ, depth + 1);

                    if (object.getRefractionIndex() > 0) {
                        double cosi = clamp(Vector3D.dotProduct(ray.getDirection(), normal), -1, 1);
                        double etai = 1, etat = object.getRefractionIndex();
                        Vector3D n = normal;
                        if (cosi < 0) {
                            cosi = -cosi;
                        } else {
                            n = Vector3D.scalarMultiplication(normal, -1);
                            double temp = etai;
                            etai = etat;
                            etat = temp;
                        }
                        double eta = etai / etat;
                        double k = 1 - eta * eta * (1 - cosi * cosi);
                        if (k < 0) {
                            return pixelColor;
                        }

                        Vector3D refractedDir = Vector3D.add(Vector3D.scalarMultiplication(ray.getDirection(), eta), Vector3D.scalarMultiplication(n, (eta * cosi - Math.sqrt(k))));
                        Color refractedColor = Color.BLACK;
                        Ray refractedRay = new Ray(Vector3D.substract(closestIntersection.getPosition(), Vector3D.scalarMultiplication(normal, EPSILON)), refractedDir);
                        refractedColor = calculateRays(nearFarPlanes, objects, lights, pos, refractedRay, cameraZ, depth + 1);
                        double reflectance = fresnel(ray.getDirection(), normal, 1.0, object.getRefractionIndex());

                        pixelColor = addColor(pixelColor, addWeightedColor(reflectance, reflectedColor, 1 - reflectance, refractedColor));
                    }
                    else {
                        pixelColor = addWeightedColor(1 - object.getReflectiveness(), pixelColor, object.getReflectiveness(), reflectedColor);
                    }
                }

            }
        }
        return pixelColor;
    }
    public static double fresnel(Vector3D incident, Vector3D normal, double n1, double n2) {
        double cosi = clamp(Vector3D.dotProduct(incident, normal), -1, 1);
        double etai = n1, etat = n2;
        if (cosi > 0) {
            double temp = etai;
            etai = etat;
            etat = temp;
        }
        double sint = etai / etat * Math.sqrt(Math.max(0.0, 1 - cosi * cosi));
        if (sint >= 1) {
            return 1.0; // Total internal reflection
        } else {
            double cost = Math.sqrt(Math.max(0.0, 1 - sint * sint));
            cosi = Math.abs(cosi);
            double Rs = ((etat * cosi) - (etai * cost)) / ((etat * cosi) + (etai * cost));
            double Rp = ((etai * cosi) - (etat * cost)) / ((etai * cosi) + (etat * cost));
            return (Rs * Rs + Rp * Rp) / 2.0;
        }
    }

    public static synchronized void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.setRGB(x, y, rgb);
    }

    public static Color addColor(Color original, Color otherColor) {
        float red = (float) clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static Color addWeightedColor(double weight1, Color original, double weight2, Color otherColor) {
        float red = (float) clamp((weight1 * original.getRed() / 255.0) + (weight2 * otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) clamp((weight1 * original.getGreen() / 255.0) + (weight2 * otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) clamp((weight1 * original.getBlue() / 255.0) + (weight2 * otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static Intersection raycast(Ray ray, List<ObjectWithMaterial> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (Object3D currObj : objects) {
            if (caster == null || !currObj.equals(caster)) {
                Intersection intersection = currObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().getZ();

                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }
        return closestIntersection;
    }

    public static double clamp(double n, double min, double max) {
        return Math.max(min, Math.min(max, n));
    }
}