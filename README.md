# README: Ray Tracer Development Project
## Project Title: Development of a Ray Tracer
### Author: Emiliano Hinojosa Guzmán
### Date: June 6, 2024

# **Overview**
This project focuses on the development of a ray tracing engine built from scratch, with a primary goal of simulating lights, objects, and materials in 3D space. The development went through numerous stages, each version building upon the previous with enhanced complexity, improved functionality, and refined rendering capabilities.

The ray tracer starts from basic shapes and simple lighting, progressing to support advanced features like reflection, refraction, parallelization, and complex material handling, producing highly realistic 3D renders.

# **Features**
##Basic Ray Tracing (v0.1):

- Camera setup with origin, resolution, and field of view.
- Ray-object intersection (supports only spheres).
- Simple shading with background color for missed rays.

## Enhanced Intersection & Clipping Planes (v0.2):

- Expanded intersection properties (distance, normal vector, object reference).
- Implementation of clipping planes to optimize processing.

## Triangle Mesh & OBJ File Support (v0.3 - v0.4):

- Triangle intersections using the Möller-Trumbore algorithm.
- OBJ file parser to import 3D models as collections of triangles.
## Lighting & Shading (v0.5 - v0.7):
- Directional lights with Lambertian reflection.
- Smoothing groups for enhanced model shading.
- Support for spotlights and light falloff for realistic lighting effects.

## Reflection, Refraction & Parallelization (v0.8 - v0.9):

- Light reflection based on surface normals and Fresnel equations.
- Refraction for transparent materials using Snell’s Law.
- Multithreading for performance improvements using Java’s ExecutorService.

## Realistic Material Rendering (v0.10):

- Implemented Fresnel’s equations for realistic light behavior.
- Support for object rotation and scaling to enhance scene composition.

### [Link to Report](https://github.com/ehinojosa04/RayTracer/blob/main/EmilianoHinojosa_Report.pdf).