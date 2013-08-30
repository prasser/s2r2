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

import s2r2.geometry.Point;
import s2r2.geometry.Vector;

/**
 * This class implements a camera
 * @author Fabian Prasser
 */
public class Camera {

    /** Viewup-Vector */
    public Vector up        = new Vector(0, 0, 0);
    /** Looking direction */
    public Vector direction = new Vector(0, 0, 0);
    /** Position */
    public Point  position  = new Point(0, 0, 0);

    /**
     * Creates a new camera with the given attributes
     * 
     * @param position
     * @param direction
     * @param up
     * @param angle
     */
    public Camera(Point position, Vector direction, Vector up) {
        this.position = position;
        this.direction = direction;
        this.up = up;
    }
    
    
}
