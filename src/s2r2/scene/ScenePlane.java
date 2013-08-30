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

import s2r2.geometry.Plane;
import s2r2.geometry.Point;
import s2r2.geometry.Vector;
import s2r2.geometry.VectorMath;
import s2r2.rendering.Ray;
import s2r2.texture.Texture;

/**
 * A plane
 * @author Fabian Prasser
 */
public class ScenePlane extends SceneObject {

    /** Geometric representation*/
    public final Plane plane;

    /** 
     * Creates a new plane
     * @param position
     * @param normal
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
    public ScenePlane(Point position,
                      Vector normal,
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
              Type.PLANE,
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
        this.plane = new Plane(position, normal);
        this.texture.initialize(this);
    }

    @Override
    public Vector getNormalAt(Point p) {
        return VectorMath.getNormalAt(this.plane, p);
    }

    @Override
    public Point intersect(Ray r) {
        return VectorMath.getIntersectionPoint(r.line, this.plane);
    }
}
