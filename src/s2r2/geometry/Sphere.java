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
 * This class implements a sphere
 * @author Fabian Prasser
 */
public class Sphere {

    /** The position*/
    public Point  position = new Point(0.0f, 0.0f, 0.0f);
    
    /** The radius*/
    public double radius   = 0.0f;

    /**
     * Creates a new sphere
     * @param position
     * @param radius
     */
    public Sphere(Point position, double radius) {
        this.position = position;
        this.radius = radius;
    }
}
