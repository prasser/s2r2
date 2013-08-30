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
package s2r2.geometry;

/**
 * This class implements a plane in 3D space
 * @author Fabian Prasser
 */
public class Plane {

    /** The position */
    public Point  position   = null;
    /** The normal */
    public Vector normal     = null;
    /** Coefficient in the hesse-normal-form representation */
    public double equation_a = 0.0f;
    /** Coefficient in the hesse-normal-form representation */
    public double equation_b = 0.0f;
    /** Coefficient in the hesse-normal-form representation */
    public double equation_c = 0.0f;
    /** Coefficient in the hesse-normal-form representation */
    public double equation_d = 0.0f;

    /**
     * Creates a new plane
     * @param position
     * @param normal
     */
    public Plane(Point position, Vector normal) {
        this.position = position;
        this.normal = normal;
        equation_d = -(normal.x * position.x + normal.y * position.y + normal.z * position.z);
        equation_a = normal.x;
        equation_b = normal.y;
        equation_c = normal.z;
    }
}
