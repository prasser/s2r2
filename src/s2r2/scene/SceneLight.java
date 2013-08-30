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

import s2r2.geometry.AAB;
import s2r2.geometry.Line;
import s2r2.geometry.Point;
import s2r2.geometry.Vector;
import s2r2.rendering.Color;
import s2r2.rendering.Ray;
import s2r2.texture.TextureUnicolored;

/**
 * This class implements a light
 * @author Fabian Prasser
 */
public class SceneLight extends SceneObject {

    /** Internal*/
    private static int         ID      = 0;
    /** Internal*/
    public final int           lightid = ID++;
    
    /** Geometrically its an AAB*/
    public final AAB           box;
    /** The size*/
    public final double        size;
    /** The position*/
    public Point               position;
    /** The brightness*/
    public double              brightness;
    /** The distance from the camera*/
    public double              distance;
    /** The color*/
    public Color               color;
    
    /**
     * Creates a new light
     * @param position
     * @param brightness
     * @param size
     * @param color
     */
    public SceneLight(Point position, double brightness, double size, Color color) {
        super(new TextureUnicolored(color), Type.LIGHT_AAB, 0f, 0f, 0f, 0f, 0f, 0f, 0f, false, false, false);
        this.color = color;
        this.size = size;
        this.position = position;
        this.box = new AAB(position.x - size,
                           position.x + size,
                           position.y - size,
                           position.y + size,
                           position.z - size,
                           position.z + size,
                           null);
        this.brightness = brightness;
        this.texture.initialize(this);
    }

    @Override
    public Vector getNormalAt(Point p) {
        throw new UnsupportedOperationException();
    }

    /**
     * Intersects the light with the given ray. Requires some
     * special parameters for efficient AAB intersection
     * @param line
     * @param invX
     * @param invY
     * @param invZ
     * @param sign
     * @return
     */
    public boolean intersect(Line line,
                             double invX,
                             double invY,
                             double invZ,
                             int[] sign) {
        double tmin = (box.bounds[sign[0]].x - line.position.x) * invX;
        double tmax = (box.bounds[1 - sign[0]].x - line.position.x) * invX;
        double tymin = (box.bounds[sign[1]].y - line.position.y) * invY;
        double tymax = (box.bounds[1 - sign[1]].y - line.position.y) * invY;
        if ((tmin > tymax) || (tymin > tmax)) return false; // No intersection
        if (tymin > tmin) tmin = tymin;
        if (tymax < tmax) tmax = tymax;
        double tzmin = (box.bounds[sign[2]].z - line.position.z) * invZ;
        double tzmax = (box.bounds[1 - sign[2]].z - line.position.z) * invZ;
        if ((tmin > tzmax) || (tzmin > tmax)) return false; // No intersection
        if (tzmax < tmax) tmax = tzmax;
        if (tmax > 0) return true;
        else return false;
    }

    @Override
    public Point intersect(Ray r) {
        return null;
    }
}
