/*
 *  Simple Software Realtime Raytracer S2R2 - (c) 2012 Fabian Prasser
 *  
 *  This file is part of S2R2.
 * 
 *  S2R2 is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  S2R2 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with S2R2.  If not, see <http://www.gnu.org/licenses/>.
 */
package s2r2.rendering;

import s2r2.geometry.AAB;
import s2r2.geometry.Point;
import s2r2.geometry.Vector;
import s2r2.geometry.VectorMath;
import s2r2.scene.Scene;
import s2r2.scene.SceneLight;
import s2r2.scene.SceneObject;
import s2r2.scene.SceneObject.Type;
import s2r2.ui.Settings;

/**
 * A basic ray tracer
 * 
 * @author Fabian Prasser
 */
public class Raytracer {

    private static final int MAX_OBJECTS = 10000;
    
    /** The scene */
    private final Scene   scene;
    /** The last object processed */
    public SceneObject    last;
    /** The stack of objects */
    private SceneObject[] mainObject      = new SceneObject[MAX_OBJECTS];
    /** The stack pointer */
    private int           mainPointer     = 0;
    /** The stack of intersections */
    private SceneObject[] reflectObject   = new SceneObject[MAX_OBJECTS];
    /** The stack of intersections */
    private Point[]       relectPoint     = new Point[MAX_OBJECTS];
    /** The stack of intersections */
    private double[]      reflectDistance = new double[MAX_OBJECTS];
    /** The stack pointer */
    private int           reflectPointer  = 0;
    /** The renderer */
    private Renderer      renderer        = null;

    /**
     * The core ray tracer
     * 
     * @param renderer
     * @param scene
     */
    public Raytracer(Renderer renderer, Scene scene) {
        this.scene = scene;
        this.renderer = renderer;
    }

    /**
     * Traces a ray
     * 
     * @param ray
     * @return
     */
    public Color trace(Ray ray) {

        // When no more bounces, return black
        if (ray.bounce == 0) return Color.BLACK;

        // Find next intersecting object
        intersect(ray, false);
        SceneObject object = reflectObject[--reflectPointer];
        Point point = relectPoint[reflectPointer];

        // No intersection
        if (object == null) return scene.color;

        // Remember last object
        last = object;

        // Attach distance to lights
        if (object.type == Type.LIGHT_AAB) {
            if (ray.bounce == Settings.RT_MAX_BOUNCE) {
                ((SceneLight) object).distance = reflectDistance[reflectPointer];
            }
            return object.texture.getColorAt(point);
        }

        // Perform lighting
        Color color = lighting(object, point);

        // If not reflection, return color
        if ((object.reflection == 0d && object.refraction == 1d) || ray.bounce == 1) { 
            return color; 
        }

        // Perform reflection
        if ((object.reflection != 0d)) {

            // Create ray
            Ray ray2 = reflectRay(ray, object, point);

            // Trace ray
            SceneObject tempLast = last;
            Color color2 = trace(ray2);
            last = tempLast;

            // Mix colors
            double param1 = (1.0d - object.reflection);
            double param2 = object.reflection;
            color.r = (param1 * color.r + param2 * color2.r);
            color.g = (param1 * color.g + param2 * color2.g);
            color.b = (param1 * color.b + param2 * color2.b);
        }

        // Apply transparency
        if (object.transparency != 1d) {

            // Create ray
            Ray ray2 = new Ray(point, ray.line.direction, ray.bounce);

            // Trace ray
            SceneObject tempLast = last;
            Color refColor = trace(ray2);
            last = tempLast;

            // Mix colors
            double param1 = object.transparency;
            double param2 = 1.0d - object.transparency;
            color.r = (param1 * color.r + param2 * refColor.r);
            color.g = (param1 * color.g + param2 * refColor.g);
            color.b = (param1 * color.b + param2 * refColor.b);

        }
        
        // Return color
        return color;
    }

    /**
     * Traces a ray through the scene and finds the intersecting object Values
     * are put into the variables
     */
    private void intersect(Ray ray, boolean lighting) {

        // Compute parameters for fast AAB intersection
        double invX = 1d / ray.line.direction.x;
        double invY = 1d / ray.line.direction.y;
        double invZ = 1d / ray.line.direction.z;
        int[] sign = new int[3];
        sign[0] = (invX < 0 ? 1 : 0);
        sign[1] = (invY < 0 ? 1 : 0);
        sign[2] = (invZ < 0 ? 1 : 0);

        // The currently nearest intersected object
        double distance = Double.MAX_VALUE;
        SceneObject object = null;
        Point point = null;

        // Remember current stack pointer, we are done when we reach it again
        int basePointer = mainPointer;
        AAB b = null;
        double tmin, tmax, tymin, tymax, tzmin, tzmax;

        // Push all root objects onto the stack
        for (int i = 0; i < scene.all.length; i++) {
            mainObject[mainPointer++] = scene.all[i];
        }

        // Find nearest object
        while (mainPointer != basePointer) {

            // Pop
            SceneObject currentObject = mainObject[--mainPointer];

            // If intersect with AAB push all contained objects onto the stack 
            if (currentObject.type == Type.AAB) {

                // Intersect with AAB
                b = (AAB) currentObject;
                tmin = (b.bounds[sign[0]].x - ray.line.position.x) * invX;
                tmax = (b.bounds[1 - sign[0]].x - ray.line.position.x) * invX;
                tymin = (b.bounds[sign[1]].y - ray.line.position.y) * invY;
                tymax = (b.bounds[1 - sign[1]].y - ray.line.position.y) * invY;
                if ((tmin > tymax) || (tymin > tmax)) continue; 
                
                if (tymin > tmin) tmin = tymin;
                if (tymax < tmax) tmax = tymax;
                tzmin = (b.bounds[sign[2]].z - ray.line.position.z) * invZ;
                tzmax = (b.bounds[1 - sign[2]].z - ray.line.position.z) * invZ;
                if ((tmin > tzmax) || (tzmin > tmax)) continue; 
                
                if (tzmin > tmin) tmin = tzmin;
                if (tzmax < tmax) tmax = tzmax;

                // If intersects push all onto stack
                // TODO: This is some sort of clipping here...
                if ((tmin < 500) && (tmax > 0)) {
                    for (int i = 0; i < b.objects.length; i++) {
                        mainObject[mainPointer++] = b.objects[i];
                    }
                }

                // Continue
                continue;
            }

            // No self shadows and objects that do not cast a shadow
            if (currentObject != last && (!lighting || (lighting && currentObject.canCastShadow))) {
                
                // Check intersection
                Point currentPoint = null;
                if (currentObject.type == Type.LIGHT_AAB && ((SceneLight) currentObject).intersect(ray.line,
                                                                                                   invX,
                                                                                                   invY,
                                                                                                   invZ,
                                                                                                   sign)) {
                    currentPoint = ((SceneLight) currentObject).position;
                } else {
                    currentPoint = currentObject.intersect(ray);
                }

                // If intersects, check if nearest object
                if (currentPoint != null) {
                    double currentDistance = VectorMath.distance(ray.line.position, currentPoint);
                    if (currentDistance < distance) {
                        object = currentObject;
                        point = currentPoint;
                        distance = currentDistance;
                    }
                }
            }
        }

        if (object == null) {
            reflectObject[reflectPointer++] = null;
        } else {
            reflectObject[reflectPointer] = object;
            relectPoint[reflectPointer] = point;
            reflectDistance[reflectPointer++] = distance;
        }
    }

    /**
     * Performs lighting at the given point
     * @param lObject
     * @param lPoint
     * @return
     */
    private Color lighting(SceneObject lObject, Point lPoint) {

        // Basic shading
        Color color = lObject.texture.getColorAt(lPoint);
        Color ambient = scene.getAmbient(lObject);
        if (!lObject.canBeInLighting) {
            ambient.r += color.r;
            ambient.g += color.g;
            ambient.b += color.b;
            return ambient;
        }

        // Continue with phong shading
        Color diffuse = new Color(0, 0, 0);
        Color specular = new Color(0, 0, 0);

        // TODO: This is bogus!
        double threshold = color.r;
        threshold = Math.max(threshold, color.g);
        threshold = Math.max(threshold, color.b);
        threshold = 1f / (255f * 3f);

        for (int j = 0; j < scene.lights.length; j++) {

            // Get current light
            SceneLight light = scene.lights[j];

            // Check distance to light
            Vector direction = new Vector(light.position.x - lPoint.x,
                                          light.position.y - lPoint.y,
                                          light.position.z - lPoint.z);

            // Compute distance
            double distance = direction.getLength();

            // Compute brightness
            double brightness = (light.brightness * (1d / (distance * distance)));

            // Light is not blocked yet
            boolean blocked = selfShadow(lObject, lPoint, light);

            Ray ray = new Ray(lPoint, direction, 1);
            if (!blocked && lObject.canBeInShadow) {

                // Find intersection object
                intersect(ray, true);
                SceneObject iObj = reflectObject[--reflectPointer];

                // Check if blocked
                if (iObj != null && reflectDistance[reflectPointer] < distance) {
                    blocked = true;
                }
            }

            // Is the light blocked?
            if (!blocked) {

                Vector normal = lObject.getNormalAt(lPoint);
                double diffuseFactor = lObject.diffus * VectorMath.scalarProduct(normal, ray.line.direction);

                // Apply diffuse component
                if (diffuseFactor > 0) {
                    diffuse.r += light.color.r * diffuseFactor * brightness;
                    diffuse.g += light.color.g * diffuseFactor * brightness;
                    diffuse.b += light.color.b * diffuseFactor * brightness;
                }

                double lengthR = ray.line.direction.getLength();
                double lengthN = normal.getLength();

                Vector toViewport = VectorMath.vectorFromPoints(lPoint, renderer.camera.position);
                double lengthV = toViewport.getLength();

                double nX = normal.x / lengthN;
                double nY = normal.y / lengthN;
                double nZ = normal.z / lengthN;

                double vX = toViewport.x / lengthV + ray.line.direction.x / lengthR;
                double vY = toViewport.y / lengthV + ray.line.direction.y / lengthR;
                double vZ = toViewport.z / lengthV + ray.line.direction.z / lengthR;

                lengthV = Math.sqrt(vX * vX + vY * vY + vZ * vZ);

                double specularFactor = lObject.specular * Math.pow((vX * nX + vY * nY + vZ * nZ) / lengthV,
                                                                      lObject.shininess);
                if (specularFactor > 0) {
                    specular.r += light.color.r * brightness * specularFactor;
                    specular.g += light.color.g * brightness * specularFactor;
                    specular.b += light.color.b * brightness * specularFactor;
                }
            }
        }
        ambient.r += color.r + specular.r + diffuse.r;
        ambient.g += color.g + specular.g + diffuse.g;
        ambient.b += color.b + specular.b + diffuse.b;
        return ambient;
    }

    /**
     * Computes the refleced ray
     * 
     * @param r
     * @param o
     * @param p
     * @return
     */
    private Ray reflectRay(Ray r, SceneObject iObject, Point iPoint) {

        // Get needed values
        Vector normal = iObject.getNormalAt(iPoint);

        // Compute whatever needed
        double equation_d = -(normal.x * iPoint.x + normal.y * iPoint.y + normal.z * iPoint.z);
        double distance = 2d * (normal.x * r.line.position.x
                                + normal.y
                                * r.line.position.y
                                + normal.z
                                * r.line.position.z + equation_d);
        double dx = iPoint.x - (r.line.position.x - normal.x * distance);
        double dy = iPoint.y - (r.line.position.y - normal.y * distance);
        double dz = iPoint.z - (r.line.position.z - normal.z * distance);

        // Return
        return new Ray(iPoint, new Vector(dx, dy, dz), r.bounce - 1);
    }

    /**
     * Determines whether an object casts a shadow on itself
     * @param lObject
     * @param lPoint
     * @param light
     * @return
     */
    private boolean selfShadow(SceneObject lObject, Point lPoint, SceneLight light) {
        Vector normal = lObject.getNormalAt(lPoint);
        return VectorMath.selfShadow(lPoint, normal, light.position);
    }
}
