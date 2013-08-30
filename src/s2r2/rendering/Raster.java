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
import s2r2.geometry.VectorMath;

/**
 * This class implements a raster for shooting rays through the scene
 * @author Fabian Prasser
 */
public class Raster {

    /** The origin of the raster*/
    public Point  origin = null;
    /** Move right to the next position*/
    public Vector deltaX = null;
    /** Move down to the next position*/
    public Vector deltaY = null;
    /** Reset the horizontal component*/
    public Vector resetX = null;
    /** Reset the vertical component*/
    public Vector resetY = null;

    /**
     * Updates the raster for the new camera position
     * @param camera
     */
    public void update(Camera camera) {
        Viewport viewport = Viewport.get();
        Vector right = VectorMath.crossProductAndNormalize(camera.direction,
                                                           camera.up);

        // Delta
        deltaX = VectorMath.multiply(right, viewport.resolutionX);
        deltaY = VectorMath.multiply(camera.up, -viewport.resolutionY);
        resetY = VectorMath.multiply(deltaY, viewport.height);
        resetX = VectorMath.multiply(deltaX, viewport.width);

        // Origin
        Vector forward = VectorMath.multiply(camera.direction,
                                             viewport.nearClip);
        Point start = VectorMath.add(camera.position, forward);
        double oX = start.x - this.deltaX.x
                    * viewport.width
                    / 2
                    - this.deltaY.x
                    * viewport.height
                    / 2;
        double oY = start.y - this.deltaX.y
                    * viewport.width
                    / 2
                    - this.deltaY.y
                    * viewport.height
                    / 2;
        double oZ = start.z - this.deltaX.z
                    * viewport.width
                    / 2
                    - this.deltaY.z
                    * viewport.height
                    / 2;
        this.origin = new Point(oX, oY, oZ);
    }
}
