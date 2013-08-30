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
package s2r2.scene;

import s2r2.geometry.Point;
import s2r2.geometry.Vector;
import s2r2.rendering.Ray;
import s2r2.texture.Texture;

/**
 * Abstract base class for objects
 * @author Fabian Prasser
 */
public abstract class SceneObject {

    /** Object type*/
    public static enum Type {
        LIGHT_AAB, PLANE, SPHERE, AAB, PLANE_AA, RECTANGLE_AA
    }

    /** Object type*/
    public final Type     type;

    /** Material*/
    public final double   ambient;
    /** Material*/
    public final double   diffus;
    /** Material*/
    public final double   specular;
    /** Material*/
    public final double   shininess;
    /** Material*/
    public final double   transparency;
    /** Material*/
    public final double   reflection;
    /** Material*/
    public final double   refraction;
    
    /** Hints*/
    public final boolean  canBeInShadow;
    /** Hints*/
    public final boolean  canCastShadow;
    /** Hints*/
    public final boolean  canBeInLighting;
    
    /** Texture*/
    public final Texture  texture;

    /**
     * Creates a new object
     * @param texture
     * @param type
     * @param transparency
     * @param reflection
     * @param refraction
     * @param ambient
     * @param diffus
     * @param specular
     * @param shininess
     * @param lighting
     * @param haveShadow
     * @param castShadow
     */
    protected SceneObject(Texture texture,
                          Type type,
                          double transparency,
                          double reflection,
                          double refraction,
                          double ambient,
                          double diffus,
                          double specular,
                          double shininess,
                          boolean lighting,
                          boolean haveShadow,
                          boolean castShadow) {
        this.texture = texture;
        this.type = type;
        this.transparency = transparency;
        this.reflection = reflection;
        this.refraction = refraction;
        this.ambient = ambient;
        this.diffus = diffus;
        this.specular = specular;
        this.shininess = shininess;
        this.canCastShadow = castShadow;
        this.canBeInShadow = haveShadow;
        this.canBeInLighting = lighting;
    }

    /** Implement*/
    public abstract Vector getNormalAt(Point p);

    /** Implement*/
    public abstract Point intersect(Ray r);
}
