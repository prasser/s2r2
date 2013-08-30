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
 * X-axis and Z-axis aligned plane
 * @author Fabian Prasser
 */
public class SceneRectangleXZ extends SceneObject {

    /** Boundary*/
    public double        minX;
    /** Boundary*/
    public double        maxX;
    /** Boundary*/
    public double        minZ;
    /** Boundary*/
    public double        maxZ;

    /** Normal*/
    private final Vector normal = new Vector(0, 1, 0);
    /** Position for textures*/
    public final double  position;

    /**
     * Creates a new rectangle
     * @param position
     * @param minX
     * @param maxX
     * @param minZ
     * @param maxZ
     * @param texture
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
    public SceneRectangleXZ(double position,
                            double minX,
                            double maxX,
                            double minZ,
                            double maxZ,
                            Texture texture,
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
        super(texture,
              Type.RECTANGLE_AA,
              transparency,
              reflection,
              refraction,
              ambient,
              diffus,
              specular,
              shininess,
              lighting,
              haveShadow,
              castShadow);
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.position = position;
        this.texture.initialize(this);
    }

    @Override
    public Vector getNormalAt(Point p) {
        return normal;
    }

    @Override
    public Point intersect(Ray r) {
        double var = (position - r.line.position.y) / r.line.direction.y;
        if (var < 0) return null;
        double pX = r.line.position.x + var * r.line.direction.x;
        double pZ = r.line.position.z + var * r.line.direction.z;
        if (pX >= minX && pX <= maxX && pZ >= minZ && pZ <= maxZ) {
            double pY = r.line.position.y + var * r.line.direction.y;
            return new Point(pX, pY, pZ);
        } else {
            return null;
        }
    }
}
