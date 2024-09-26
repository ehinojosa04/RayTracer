package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.objects.ObjectWithMaterial;

public class Intersection {

    private double distance;
    private Vector3D position;
    private Vector3D normal;
    private ObjectWithMaterial object;

    public Intersection(Vector3D position, double distance, Vector3D normal, ObjectWithMaterial object) {
        setPosition(position);
        setDistance(distance);
        setNormal(normal);
        setObject(object);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }

    public ObjectWithMaterial getObject() {
        return object;
    }

    public void setObject(ObjectWithMaterial object) {
        this.object = object;
    }
}
