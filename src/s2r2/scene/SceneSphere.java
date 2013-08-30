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
import s2r2.geometry.Sphere;
import s2r2.geometry.Vector;
import s2r2.geometry.VectorMath;
import s2r2.rendering.Ray;
import s2r2.texture.Texture;

/**
 * This class implements a sphere
 * @author Fabian Prasser
 */
public class SceneSphere extends SceneObject {

    /**
     * Geometric representation
     */
    public final Sphere sphere;

    /**
     * Creates a new sphere
     * @param position
     * @param radius
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
    public SceneSphere(Point position,
                       double radius,
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
              Type.SPHERE,
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
        this.sphere = new Sphere(position, radius);
        this.texture.initialize(this);
    }

    @Override
    public Vector getNormalAt(Point p) {
        return VectorMath.getNormalAt(this.sphere, p);
    }

    @Override
    public Point intersect(Ray r) {
        return VectorMath.getIntersectionPoint(r.line, this.sphere);
    }
}
