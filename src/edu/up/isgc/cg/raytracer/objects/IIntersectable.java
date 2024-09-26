package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;

public interface IIntersectable {
    public abstract Intersection getIntersection(Ray ray);
}
