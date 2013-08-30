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

import s2r2.geometry.Line;
import s2r2.geometry.Point;
import s2r2.geometry.Vector;

/**
 * This class implements a ray as processed by the ray tracer
 * @author Fabian Prasser
 */
public class Ray {

    /** The geometric representation*/
    public final Line line;
    /** The number of remaining bounces*/
    public final int  bounce;

    /**
     * Creates a new ray
     * @param position
     * @param direction
     * @param bounce
     */
    public Ray(Point position, Vector direction, int bounce) {
        this.line = new Line(position, direction);
        this.bounce = bounce;
    }

}
