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
package s2r2.texture;

import s2r2.geometry.Point;
import s2r2.geometry.Sphere;
import s2r2.geometry.Vector;
import s2r2.geometry.VectorMath;
import s2r2.rendering.Color;
import s2r2.scene.SceneSphere;

/**
 * A texture that adds stripes to a sphere
 * @author Fabian Prasser
 */
public class TextureSphereStriped implements Texture {
    
    /** Size*/
    private final double size;
    /** Offset*/
    public double        offset = 0;
    /** Color*/
    private final Color  c1;
    /** Color*/
    private final Color  c2;
    /** Vector*/
    private final Vector v1;
    /** Object*/
    private Sphere       object;

    /**
     * Creates a new texture
     * @param stripes
     * @param normal
     * @param c1
     * @param c2
     */
    public TextureSphereStriped(int stripes, Vector normal, Color c1, Color c2) {
        this.size = 2f * Math.PI / stripes;
        this.c1 = c1;
        this.c2 = c2;
        this.v1 = normal;
    }

    @Override
    public Color getColorAt(Point intersection) {
        Vector v2 = new Vector(intersection.x - object.position.x,
                               intersection.y - object.position.y,
                               intersection.z - object.position.z);
        double angle = VectorMath.angle(v1, v2) + offset;
        int segment = (int) Math.round(angle / size);
        if (segment % 2 == 0) return c1;
        else return c2;
    }

    @Override
    public void initialize(Object object) {
        this.object = ((SceneSphere) object).sphere;
    }
}
