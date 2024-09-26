package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

public abstract class ObjectWithMaterial extends Object3D {

    private double specularIndex;
    private double reflectiveness;
    private double refractionIndex;
    private double transparency;
    public ObjectWithMaterial(Vector3D position, Color color, double specularIndex, double refractionIndex, double reflectiveness) {
        super(position, color);
        setSpecularIndex(specularIndex);
        setRefractionIndex(refractionIndex);
        setReflectiveness(reflectiveness);
    }

    public double getSpecularIndex() { return specularIndex; }

    public void setSpecularIndex(double specularIndex) { this.specularIndex = specularIndex; }

    public double getReflectiveness() { return reflectiveness; }

    public void setReflectiveness(double reflectiveness) { this.reflectiveness = reflectiveness; }

    public double getRefractionIndex() { return refractionIndex; }

    public void setRefractionIndex(double refractionIndex) { this.refractionIndex = refractionIndex; }

    public void setTransparency(double transparency) { this.transparency = transparency; }

    public double getTransparency() { return transparency; }
}